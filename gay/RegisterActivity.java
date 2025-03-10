package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import com.example.gay.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        binding.cancelButton.setOnClickListener(v -> onBackPressed());
        binding.registerButton.setOnClickListener(v -> registerUser());
    }


    private void registerUser() {
        String userMail = binding.mailTextViewSignUp.getText().toString();
        String userName = binding.NameTextViewSignUp.getText().toString();
        String userPassword = binding.PasswordTextViewSignUp.getText().toString();
        String userAccount = binding.editTextAccount.getText().toString();

        if(userMail.isEmpty()) {
            binding.mailTextViewSignUp.setError("Please enter your mail !");
            binding.mailTextViewSignUp.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            binding.mailTextViewSignUp.setError("Please enter valid mail");
            binding.mailTextViewSignUp.requestFocus();
            return;
        }
        if(userName.isEmpty()) {
            binding.NameTextViewSignUp.setError("Please enter your name");
            binding.NameTextViewSignUp.requestFocus();
            return;
        }
        if(userPassword.isEmpty()) {
            binding.PasswordTextViewSignUp.setError("Please enter your password");
            binding.PasswordTextViewSignUp.requestFocus();
            return;
        }
        if(userAccount.isEmpty()) {
            binding.editTextAccount.setError("Please enter your account");
            binding.editTextAccount.requestFocus();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(userMail ,userPassword).addOnCompleteListener((task) -> {
            if(task.isSuccessful()) {
                saveAll(userName ,userMail ,userPassword ,userAccount);
                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
            }  else {
                Toast.makeText(RegisterActivity.this, "錯誤" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveAll( String userName, String userMail, String userPassword ,String userAccount) {
            HashMap<String ,Object> user = new HashMap<>();
            user.put("userName" ,userName);
            user.put("userMail" ,userMail);
            user.put("userPassword" ,userPassword);
            user.put("userHeight" ,"no");
            user.put("userWeight" ,"no");
            user.put("userImage" ,"no");
            //user.put("Token", "no");
            user.put("userState" ,"no");
            user.put("userAccount" ,userAccount);
            if (binding.radioButtonMan.isChecked()) {
                user.put("userMan", "man");
            } else if (binding.radioButtonWoman.isChecked()) {
                user.put("userWoman", "woman");
            }
            fireStore.collection("User").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).set(user).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this ,MainActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}