package com.example.gay.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gay.CommentActivity;
import com.example.gay.Model.Post;
import com.example.gay.Model.User;
import com.example.gay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private final Activity context;
    private final List<Post> postList;
    private final List<User> userList;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    public PostAdapter(Activity context, List<Post> postList, List<User> userList) {
        this.context = context;
        this.postList = postList;
        this.userList = userList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycle_item  ,parent ,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.setPostImage(post.getPostImage());
        holder.setContent(post.getPostContent());
        holder.setPostDate(post.getPostDate());
        String userID = post.getPostUser();
        String postId = post.PostId;

        //照片和名字
        firebaseFirestore.collection("User").document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())  {
                String userName = task.getResult().getString("userName");
                String userImage = task.getResult().getString("userImage");
                holder.setUserImage(userImage);
                holder.setPostUsername(userName);
            } else {
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
      /*  String username = userList.get(position).getUserName();
        String image = userList.get(position).getUserImage();
        holder.setPostUsername(username);
        holder.setUserImage(image);*/

        holder.commentImage.setOnClickListener(v -> {
            Intent commentIntent =new Intent(context , CommentActivity.class);
            commentIntent.putExtra("PostId" ,postId);
            context.startActivity(commentIntent);
        });


        holder.likeImage.setOnClickListener(v -> firebaseFirestore.collection("Post/" +postId+ "/Likes").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
            if(!Objects.requireNonNull(task.getResult()).exists()) {
                HashMap<String , Object> likes = new HashMap<>();
                likes.put("timeStamp" , FieldValue.serverTimestamp());
                firebaseFirestore.collection("Post/" + postId + "/Likes").document(firebaseUser.getUid()).set(likes);
            } else {
                firebaseFirestore.collection("Post/" + postId + "/Likes").document(firebaseUser.getUid()).delete();
            }
        }));
        firebaseFirestore.collection("Post/" + postId + "/Likes").document(firebaseUser.getUid()).addSnapshotListener((value, error) -> {
            if (error == null) {
                assert value != null;
                if (value.exists()) {
                    holder.likeImage.setImageResource(R.drawable.group30);
                } else {
                    holder.likeImage.setImageResource(R.drawable.group29);
                }
            }
        });


        firebaseFirestore.collection("Post/" + postId + "/Likes").addSnapshotListener((value, error) -> {
            if (error == null) {
                assert value != null;
                if (!value.isEmpty()) {
                    int count = value.size();
                    holder.setPostLikes(String.valueOf(count));
                } else {
                    holder.setPostLikes(String.valueOf(0));
                }
            }
        });

        firebaseFirestore.collection("Post/" + postId + "/Comments").addSnapshotListener((value, error) -> {
            if (error == null) {
                assert value != null;
                if (!value.isEmpty()) {
                    int num = value.size();
                    holder.setPostComment(String.valueOf(num));
                } else {
                    holder.setPostComment(String.valueOf(0));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage ,likeImage,commentImage;
        TextView postContent ,postUsername,postDate ,postLike ,postComment;
        CircleImageView userImage;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            likeImage = view.findViewById(R.id.beforeLike);
            commentImage = view.findViewById(R.id.commentPost);
        }
        public void setPostImage(String image) {
            postImage = view.findViewById(R.id.imagePost);
            Glide.with(context).load(image).into(postImage);
        }
        public void setUserImage(String UserImageUri) {
            userImage = view.findViewById(R.id.profilePic);
            Glide.with(context).load(UserImageUri).into(userImage);
        }
        public void setPostUsername(String username) {
            postUsername = view.findViewById(R.id.userNamePost);
            postUsername.setText(username);
        }
        public void setContent(String content) {
            postContent = view.findViewById(R.id.contentPost);
            postContent.setText(content);
        }
        public void setPostDate(String date) {
            postDate = view.findViewById(R.id.datePost);
            postDate.setText(date);
        }
        public void setPostLikes(String count) {
            postLike = view.findViewById(R.id.countLikePost);
            postLike.setText(count);
        }
        public void setPostComment(String number) {
            postComment = view.findViewById(R.id.commentCountPost);
            postComment.setText(number);
        }
    }
}
