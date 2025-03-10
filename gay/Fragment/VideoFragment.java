package com.example.gay.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gay.BackActivity;
import com.example.gay.ChestActivity;
import com.example.gay.LegActivity;
import com.example.gay.R;
import com.example.gay.Video_detail;


public class VideoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        Button armButton =view.findViewById(R.id.armButton);
        Button chestButton =view.findViewById(R.id.chestButton);
        Button legButton =view.findViewById(R.id.legButton);
        Button backButton =view.findViewById(R.id.backButton);

        armButton.setOnClickListener(v -> startActivity(new Intent(getActivity() ,Video_detail.class)));
        chestButton.setOnClickListener(v -> startActivity(new Intent(getActivity() , ChestActivity.class)));
        legButton.setOnClickListener(v -> startActivity(new Intent(getActivity() , LegActivity.class)));
        backButton.setOnClickListener(v -> startActivity(new Intent(getActivity() , BackActivity.class)));
        return view;
    }
}