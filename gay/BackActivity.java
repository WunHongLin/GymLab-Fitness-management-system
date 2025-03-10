package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.gay.databinding.ActivityBackBinding;

public class BackActivity extends AppCompatActivity {
    private ActivityBackBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setting();
        binding.bodyBackBackImage.setOnClickListener(v -> onBackPressed());
    }
    private void setting() {
        binding.backVideo1.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=pxzsXLjKihQ";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.backVideo2.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=7R7tuhtJudI";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.backVideo3.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=Wo3GI41l6Ok";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.backVideo4.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=xbuegWdo5s8";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.backVideo5.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=dSb8f3__ecY";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.backVideo6.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=o3YbCCes1PQ";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}