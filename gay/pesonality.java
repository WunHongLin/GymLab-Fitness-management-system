package com.example.gay;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.gay.databinding.ActivityPesonalityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

public class pesonality extends AppCompatActivity {
    private ActivityPesonalityBinding binding;
    private FirebaseUser user;
    private FirebaseFirestore fireStore;
    private ActivityResultLauncher<String> mGetContent;
    private StorageReference storageReference;
    private Uri imageUri;
    private Uri downloadUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPesonalityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user  = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        fireStore = FirebaseFirestore.getInstance();
        set();
        binding.saveButton.setOnClickListener(v -> update());
        binding.personalityBackImage.setOnClickListener(v -> onBackPressed());
        binding.userImagePerson.setOnClickListener(v -> showImage());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                imageUri = result;
                binding.userImagePerson.setImageURI(imageUri);
            }
        });
    }
    private void showImage() {
        mGetContent.launch("image/*");
    }

    private void set() {
        fireStore.collection("User").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = task.getResult().getString("userName");
                String height = task.getResult().getString("userHeight");
                String weight = task.getResult().getString("userWeight");
                String state = task.getResult().getString("userState");
                String image  =task.getResult().getString("userImage");
                binding.editTextUserNamePerson.setText(name);
                binding.editTextUserHeightPerson.setText(height);
                binding.editTextUserWeightPerson.setText(weight);
                binding.editTextUserStatePerson.setText(state);
                assert image != null;
                if (!image.equals("no")) {
                    Glide.with(pesonality.this).load(image).into(binding.userImagePerson);
                }
            }
        });
    }
    private void update() {
        String name = binding.editTextUserNamePerson.getText().toString();
        String height = binding.editTextUserHeightPerson.getText().toString();
        String weight =  binding.editTextUserWeightPerson.getText().toString();
        String state = binding.editTextUserStatePerson.getText().toString();
        StorageReference imgRef = storageReference.child("UserImg").child(FieldValue.serverTimestamp() + ".jpg");
        if(imageUri != null) {
            imgRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    save(name, height ,weight ,imgRef ,state);
                } else {
                    Toast.makeText(pesonality.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            saveOnlyDate(name,height,weight,state);
        }
    }

    private void save(String name, String height, String weight, StorageReference imgRef, String state) {
        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUri =uri;
            fireStore.collection("User").document(user.getUid()).update("userName" ,name ,"userState" ,state ,"userWeight" ,weight ,"userHeight" ,height ,"userImage" ,downloadUri.toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(pesonality.this ,HomeActivity.class));
                    Toast.makeText(pesonality.this, "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(pesonality.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
    private void saveOnlyDate(String name, String height, String weight, String state) {
        fireStore.collection("User").document(user.getUid()).update("userName" ,name ,"userState" ,state ,"userWeight" ,weight ,"userHeight" ,height).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(pesonality.this ,HomeActivity.class));
                Toast.makeText(pesonality.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(pesonality.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}