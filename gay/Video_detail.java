package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.gay.databinding.ActivityVideoDetailBinding;
public class Video_detail extends AppCompatActivity {
    private ActivityVideoDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bodyBackImage.setOnClickListener(v -> onBackPressed());
        go();
    }
   private void go() {
        binding.armVideo1.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=a5D6rTGpySk";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        binding.armVideo2.setOnClickListener(v -> {
            String url ="https://www.youtube.com/watch?v=jABUkxCK4EY";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
       binding.armVideo3.setOnClickListener(v -> {
           String url ="https://www.youtube.com/watch?v=Yy58aPnIwPI";
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
           startActivity(intent);
       });
       binding.armVideo4.setOnClickListener(v -> {
           String url ="https://www.youtube.com/watch?v=bvINsj0gjOA";
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
           startActivity(intent);
       });
       binding.armVideo5.setOnClickListener(v -> {
           String url ="https://www.youtube.com/watch?v=1Q4ejuLJtvk";
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
           startActivity(intent);
       });
       binding.armVideo6.setOnClickListener(v -> {
           String url ="https://www.youtube.com/watch?v=BBymV9EFbjU";
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
           startActivity(intent);
       });
   }


}