package com.example.gay.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gay.Model.Comment;
import com.example.gay.Model.User;
import com.example.gay.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private final Activity context;
    private final List<Comment> list;
    private final List<User> userList;
    private FirebaseFirestore firebaseFirestore;
    public CommentAdapter(Activity context, List<Comment> list, List<User> userList) {
        this.context = context;
        this.list = list;
        this.userList = userList;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_item2 ,parent ,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.setComment(comment.getTextComment());
        User users = userList.get(position);
        String userID = comment.getUserComment();
       firebaseFirestore.collection("User").document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())  {
                String userName = task.getResult().getString("userName");
                String userImage = task.getResult().getString("userImage");
                holder.setUserCircleImage(userImage);
                holder.setUserComment(userName);
            } else {
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
      /*   holder.setUserComment(users.getUserName());
        holder.setUserCircleImage(users.getUserImage());*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView CommentText ,userComment;
        CircleImageView userCircleImage;
        View view;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setComment(String comment) {
            CommentText = view.findViewById(R.id.userTextComment);
            CommentText.setText(comment);
        }
        public void setUserComment(String user) {
            userComment = view.findViewById(R.id.userNameComment);
            userComment.setText(user);
        }
        public void setUserCircleImage(String userImage) {
            userCircleImage = view.findViewById(R.id.userImageComment);
            Glide.with(context).load(userImage).into(userCircleImage);
        }
    }
}
