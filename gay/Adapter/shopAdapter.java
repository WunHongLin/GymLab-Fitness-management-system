package com.example.gay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.Fragment.ShopCartFragment;
import com.example.gay.Model.product;
import com.example.gay.R;
import com.example.gay.shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.ViewHolder>{

    private ArrayList<String> itemTitle;
    private shopAdapter2 shopAdapter2;
    private Context shop;
    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private ArrayList<product> itemProductName;
    private ArrayList<product> shopDetailProducName = new ArrayList<product>();

    public shopAdapter(){}

    public shopAdapter(ArrayList<String> itemTitle, Context shop) {
        this.itemTitle = itemTitle;
        this.shop = shop;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item5,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt.setText(itemTitle.get(position));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shop, shop.class);
                intent.putExtra("Name",itemTitle.get(holder.getAdapterPosition()));
                shop.startActivity(intent);
            }
        });

        holder.recycler.setLayoutManager(new LinearLayoutManager(holder.recycler.getContext(),LinearLayoutManager.HORIZONTAL,false));

        dbCR = FirebaseFirestore.getInstance().collection("/productInformation");
        dbCR.whereEqualTo("category",itemTitle.get(position)).limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                itemProductName = new ArrayList<product>();
                //here need to get all info of the product
                for(DocumentSnapshot document:task.getResult()){ itemProductName.add(document.toObject(product.class)); }
                shopAdapter2 = new shopAdapter2(itemProductName,shop);
                holder.recycler.setAdapter(shopAdapter2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemTitle.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recycler;
        private Button button;
        private TextView txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textView6);
            button = (Button) itemView.findViewById(R.id.button2);
            recycler = (RecyclerView) itemView.findViewById(R.id.recycler2);
        }
    }
}
