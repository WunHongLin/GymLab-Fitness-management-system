package com.example.gay.Adapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gay.Model.Post;
import com.example.gay.R;
import java.util.List;

public class MyPostAdapter  extends RecyclerView.Adapter<MyPostAdapter.ViewHolder>{
    private final Activity context;
    private final List<Post> postList;

    public MyPostAdapter(Activity context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_item1  ,parent ,false);
        return new MyPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.setPostImage(post.getPostImage());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setPostImage(String image) {
            postImage = itemView.findViewById(R.id.myPostImage);
            Glide.with(context).load(image).into(postImage);
        }
    }
}
