package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.gay.databinding.ActivityChestBinding;

public class ChestActivity extends AppCompatActivity {
    private ActivityChestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        get();
        binding.bodyChestBackImage.setOnClickListener(v -> onBackPressed());

    }
    private void get() {
        binding.chestVideo1.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=Hdj9rkj-NIE";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.chestVideo2.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=2MsLchgeMHw";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.chestVideo3.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=Iiwaj6ECX7Q";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.chestVideo4.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=V1HIvXNrlGw";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.chestVideo5.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=5QL2Y4BR7LY";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.chestVideo6.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=t0XraX6_lRA";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}