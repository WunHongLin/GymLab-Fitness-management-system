package com.example.gay.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.MenuActivity;
import com.example.gay.MenuDetailActivity;
import com.example.gay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuSetAdapter extends RecyclerView.Adapter<MenuSetAdapter.ViewHolder>{
    private ArrayList<String> NameList;
    private MenuActivity Menu;
    private DocumentReference dbDR;
    private String userID;
    private MenuAdapter adapter;

    public MenuSetAdapter(ArrayList<String> nameList, MenuActivity menu,String useriD) {
        NameList = nameList;
        Menu = menu;
        userID = useriD;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item10,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(NameList.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(Menu));

        dbDR = FirebaseFirestore.getInstance().document("/userTrain/"+userID+"/"+Menu.getIntent().getExtras().getString("Name")+"/"+NameList.get(position));
        dbDR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                ArrayList<String> List = (ArrayList<String>) documentSnapshot.get("name");
                adapter = new MenuAdapter(List);
                holder.recyclerView.setAdapter(adapter);
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu, MenuDetailActivity.class);
                intent.putExtra("Name",Menu.getIntent().getExtras().getString("Name"));
                intent.putExtra("Set",holder.title.getText().toString());
                intent.putExtra("key","2");
                Menu.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private RecyclerView recyclerView;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textView35);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler10);
            view = itemView;
        }
    }
}
