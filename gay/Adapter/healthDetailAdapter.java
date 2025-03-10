package com.example.gay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.R;

import java.util.ArrayList;
import java.util.HashMap;

public class healthDetailAdapter extends RecyclerView.Adapter<healthDetailAdapter.ViewHolder>{
    private ArrayList<HashMap<String,String>> healthMap;

    public healthDetailAdapter(ArrayList<HashMap<String,String>> healthMap) {
        this.healthMap = healthMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item8,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(healthMap.get(position).get("Date"));
        holder.value.setText(healthMap.get(position).get("Value"));
    }

    @Override
    public int getItemCount() {
        return healthMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView date,value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.textView25);
            value = (TextView) itemView.findViewById(R.id.textView29);
        }
    }
}
