package com.example.gay;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gay.databinding.ActivityNewPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewPostActivity extends AppCompatActivity {
    private ActivityNewPostBinding binding;
    private ActivityResultLauncher<String> mGetContent;
    private Uri postImgUri = null ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        UID = firebaseUser.getUid();
        binding.postImage.setOnClickListener(v -> selectImage());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                postImgUri = result;
                binding.postImage.setImageURI(postImgUri);
            }
        });
        set();
    }
    private void selectImage() {
        mGetContent.launch("image/*");
    }
    private void set() {
        binding.postBackImage.setOnClickListener(v -> onBackPressed());
        binding.postButton.setOnClickListener(v -> summit());
    }

    private void summit() {
        String content = binding.editPostText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        if(content.isEmpty()) {
            binding.editPostText.setError("Please write your content");
            binding.editPostText.requestFocus();
        }
        if(postImgUri != null) {
            StorageReference imageRef = storageReference.child("Post_Image").child(FieldValue.serverTimestamp() + ".jpg");
            imageRef.putFile(postImgUri).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        HashMap<String , Object> post = new HashMap<>();
                        post.put("postUser" ,UID);
                        post.put("PostContent" ,content);
                        post.put("postImage" ,uri.toString());
                        post.put("postDate" , date);
                        post.put("postTime" , FieldValue.serverTimestamp());
                        firebaseFirestore.collection("Post").add(post).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(NewPostActivity.this, "成功發文", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NewPostActivity.this ,HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(NewPostActivity.this, "發文失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
            });
        }
    }
}