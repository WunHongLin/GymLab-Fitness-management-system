package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.gay.databinding.ActivityLegBinding;

public class LegActivity extends AppCompatActivity {
    private ActivityLegBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        set();
        binding.bodyLegBackImage.setOnClickListener(v -> onBackPressed());
    }
    private void set() {
        binding.legVideo1.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=_gjkeqoUsGE";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.legVideo2.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=So8dqGeUh8U";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.legVideo3.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=qMBvQfmRQ1c";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.legVideo4.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=9OqYm6BSHxQ";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.legVideo5.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=NinrX2xbezI";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.legVideo6.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=9wdoNESYNX8";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}