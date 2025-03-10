package com.example.gay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gay.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    private ArrayList<String> NameList;

    public MenuAdapter(ArrayList<String> nameList) {
        NameList = nameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item9,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(NameList.get(position));
        holder.txtNumber.setText(String.format("%02d",position+1));
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNumber,txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.textView33);
            txtName = (TextView) itemView.findViewById(R.id.textView34);
        }
    }
}
