package com.example.gay.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.gay.Adapter.MyPostAdapter;
import com.example.gay.Adapter.PostAdapter;
import com.example.gay.Model.Post;
import com.example.gay.Model.User;
import com.example.gay.R;
import com.example.gay.pesonality;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MemberFragment extends Fragment {
    private String UserId;
    private List<Post> userPostList;
    private MyPostAdapter myPostAdapter;
    private FirebaseFirestore fireStore;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_member, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        UserId = user.getUid();
        fireStore = FirebaseFirestore.getInstance();
        ImageView userImage = view.findViewById(R.id.UserImage);
        TextView userNameText = view.findViewById(R.id.UserName);
        TextView userStateText = view.findViewById(R.id.UserSate);
        TextView userNumText = view.findViewById(R.id.UserPostNum);
        Button updateButton = view.findViewById(R.id.updateButton);
        RecyclerView recyclerViewMyPost = view.findViewById(R.id.myPostRecyclerView);
        recyclerViewMyPost.setHasFixedSize(true);
        recyclerViewMyPost.setLayoutManager(new GridLayoutManager(getContext() ,2));
        userPostList = new ArrayList<>();
        myPostAdapter =new MyPostAdapter((Activity) getContext() ,userPostList);
        recyclerViewMyPost.setAdapter(myPostAdapter);
        updateButton.setOnClickListener(v -> startActivity(new Intent(getActivity() , pesonality.class)));
        recyclerViewMyPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottom = !recyclerView.canScrollVertically(1);
                if(isBottom) {
                    Toast.makeText(getContext() ,"到達底部" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        getUserDate(userImage ,userNameText ,userStateText);
        fireStore.collection("Post").whereEqualTo("postUser" ,UserId).addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange :value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String postId = documentChange.getDocument().getId();
                    int count  = value.size();
                    userNumText.setText(String.valueOf(count));
                    Post post = documentChange.getDocument().toObject(Post.class).withId(postId);
                    userPostList.add(post);
                    myPostAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    private void getUserDate(ImageView userImage, TextView userNameText, TextView userStateText) {
        DocumentReference documentReference = fireStore.collection("User").document(UserId);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                String name =task.getResult().getString("userName");
                String image = task.getResult().getString("userImage");
                String state = task.getResult().getString("userState");
                userNameText.setText(name);
                if (getActivity() != null) {
                    Glide.with(getActivity()).load(image).into(userImage);
                }
                assert state != null;
                if (!state.equals("no")) {
                    userStateText.setText(state);
                }
            }
        });
    }
}