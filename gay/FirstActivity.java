package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gay.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {
    private ActivityFirstBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        set();
    }
    private void set() {
        binding.buttonSignIn.setOnClickListener(v -> startActivity(new Intent(this ,MainActivity.class)));
        binding.buttonSignUp.setOnClickListener(v -> startActivity(new Intent(this ,RegisterActivity.class)));
    }
}