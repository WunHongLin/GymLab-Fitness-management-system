package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gay.Fragment.MemberFragment;
import com.example.gay.Fragment.PostFragment;
import com.example.gay.Fragment.ShopCartFragment;
import com.example.gay.Fragment.VideoFragment;
import com.example.gay.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacementFragment(new PostFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.nav_post:
                    replacementFragment(new PostFragment());
                    break;
                case R.id.nav_shopCart:
                    replacementFragment(new ShopCartFragment());
                    break;
                case R.id.nav_video:
                    replacementFragment(new VideoFragment());
                    break;
                case R.id.nav_member:
                    replacementFragment(new MemberFragment());
                    break;
            }
            return true;
        });
        binding.goPost.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this ,NewPostActivity.class)));

    }
    private void replacementFragment(Fragment fragment) {
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottom , fragment);
        fragmentTransaction.commit();
    }
}