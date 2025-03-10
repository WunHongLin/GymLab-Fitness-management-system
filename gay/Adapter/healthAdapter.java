package com.example.gay.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.BodyActivity;
import com.example.gay.Body_InformationActivity;
import com.example.gay.R;

import java.util.ArrayList;

public class healthAdapter extends RecyclerView.Adapter<healthAdapter.ViewHolder>{
    private ArrayList<String> illName;
    private BodyActivity body;

    public healthAdapter(ArrayList<String> illName, BodyActivity body) {
        this.illName = illName;
        this.body = body;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item7,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt.setText(illName.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(body, Body_InformationActivity.class);
                intent.putExtra("Name",illName.get(holder.getAdapterPosition()));
                body.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return illName.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt;
        private Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textView24);
            button = (Button) itemView.findViewById(R.id.button13);
        }
    }
}
