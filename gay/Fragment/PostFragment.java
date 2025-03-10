package com.example.gay.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gay.Adapter.PostAdapter;
import com.example.gay.BodyActivity;
import com.example.gay.Model.Post;
import com.example.gay.Model.User;
import com.example.gay.NewPostActivity;
import com.example.gay.R;
import com.example.gay.TrainingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFragment extends Fragment {
    private RecyclerView recyclerViewPost;
    private List<Post> list ;
    private PostAdapter postAdapter;
    private FirebaseFirestore fireStore;
    private ListenerRegistration listenerRegistration;
    private List<User> userList;
    private ImageView body,train;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_post, container, false);
        //FloatingActionButton postImage = view.findViewById(R.id.goPost);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
      //  postImage.setOnClickListener(v -> startActivity(new Intent(getActivity() , NewPostActivity.class)));
        recyclerViewPost = view.findViewById(R.id.postRecyclerView);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        userList = new ArrayList<>();
        postAdapter = new PostAdapter((Activity) getContext(),list ,userList);
        recyclerViewPost.setAdapter(postAdapter);
        if(firebaseUser != null ) {
            recyclerViewPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    boolean isBottom = !recyclerViewPost.canScrollVertically(1);
                    if(isBottom) {
                        Toast.makeText(getContext() ,"到達底部" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Query query = fireStore.collection("Post").orderBy("postTime", Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener((value, error) -> {
                assert value != null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String postID = documentChange.getDocument().getId();
                        Post post = documentChange.getDocument().toObject(Post.class).withId(postID);
                        String postId = documentChange.getDocument().getString("postUser");
                        assert postId != null;
                        fireStore.collection("Users").document(postId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                userList.add(user);
                                list.add(post);
                                postAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    postAdapter.notifyDataSetChanged();
                }
                listenerRegistration.remove();
            });
        }

        body = (ImageView) view.findViewById(R.id.body);
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BodyActivity.class);
                startActivity(intent);
            }
        });

        train = (ImageView) view.findViewById(R.id.train);
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrainingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}