package com.example.gay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.MerchandiseActivity;
import com.example.gay.Model.product;
import com.example.gay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class shopAdapter2 extends RecyclerView.Adapter<shopAdapter2.ViewHolder>{

    private ArrayList<product> product;
    private Context shop;
    private StorageReference storageReferance,pickReferance;

    public shopAdapter2(){}

    public shopAdapter2(ArrayList<product> productName, Context shop) {
        this.product = productName;
        this.shop = shop;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item11,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtProductName.setText(product.get(position).getName());
        holder.txtProductPrice.setText(Long.toString(product.get(position).getPrice()));
//need to set the image
        storageReferance = FirebaseStorage.getInstance().getReference();
        pickReferance = storageReferance.child(product.get(position).getUri());

        try {
            final File file = File.createTempFile("images",".png");
            pickReferance.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    holder.ProductImage.setImageURI(Uri.fromFile(file));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shop, MerchandiseActivity.class);
                intent.putExtra("Name",product.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Category",product.get(holder.getAdapterPosition()).getCategory());
                intent.putExtra("Price",String.valueOf(product.get(holder.getAdapterPosition()).getPrice()));
                intent.putExtra("Uri",product.get(holder.getAdapterPosition()).getUri());
                shop.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtProductName,txtProductPrice;
        private ImageView ProductImage;
        private View productView;
        private Button buttonAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = (TextView) itemView.findViewById(R.id.textView2);
            txtProductPrice = (TextView) itemView.findViewById(R.id.textView19);
            ProductImage = (ImageView) itemView.findViewById(R.id.imageView2);
            productView = itemView;
        }
    }
}
