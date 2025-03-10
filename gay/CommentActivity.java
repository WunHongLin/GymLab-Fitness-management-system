package com.example.gay;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.gay.Adapter.CommentAdapter;
import com.example.gay.Model.Comment;
import com.example.gay.Model.User;
import com.example.gay.databinding.ActivityCommentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    private String post_id;
    private FirebaseUser user;
    private String ID;
    private FirebaseFirestore database;
    private List<User> userList;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ID = user.getUid();
        post_id = getIntent().getStringExtra("PostId");
        userList = new ArrayList<>();
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(CommentActivity.this ,commentList ,userList);
        binding.userCommentRecyclerView.setHasFixedSize(true);
        binding.userCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.userCommentRecyclerView.setAdapter(commentAdapter);
        binding.commentBackImage.setOnClickListener(v -> onBackPressed());
        set();
        binding.commentButton.setOnClickListener(v -> {
            String comment = binding.editTextUserCommentText.getText().toString();
            if (!comment.isEmpty()) {
                HashMap<String , Object> commentMap = new HashMap<>();
                commentMap.put("textComment" ,comment);
                commentMap.put("timeComment" , FieldValue.serverTimestamp());
                commentMap.put("userComment" ,ID);
                database.collection("Post/" + post_id + "/Comments").add(commentMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                        binding.editTextUserCommentText.setText("");
                    } else {
                        Toast.makeText(CommentActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CommentActivity.this, "請勿空白", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        database.collection("Post/" + post_id + "/Comments").addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Comment comment =documentChange.getDocument().toObject(Comment.class);
                    String userId = documentChange.getDocument().getString("userComment");
                    assert userId != null;
                    database.collection("Users").document(userId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User users = Objects.requireNonNull(task.getResult()).toObject(User.class);
                            userList.add(users);
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void set() {
        database.collection("User").document(ID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String image = task.getResult().getString("userImage");
                Glide.with(CommentActivity.this).load(image).into(binding.userCommentImage);
            }
        });
    }
}