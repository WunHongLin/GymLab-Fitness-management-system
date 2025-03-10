package com.example.gay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gay.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Auth = FirebaseAuth.getInstance();
        binding.textViewSignUp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this ,RegisterActivity.class)));
        binding.buttonGo.setOnClickListener(v -> loginCheck());
    }
        private void loginCheck() {
            String userMail = binding.MailTextView.getText().toString();
            String userPassword = binding.PasswordTextView.getText().toString();
            if(userMail.isEmpty()) {
                binding.MailTextView.setError("請輸入信箱");
                binding.MailTextView.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
                binding.MailTextView.setError("請輸入有效的密碼");
                binding.MailTextView.requestFocus();
                return;
            }
            if(userPassword.isEmpty()) {
                binding.PasswordTextView.setError("請輸入密碼");
                binding.PasswordTextView.requestFocus();
                return;
            }
            Auth.signInWithEmailAndPassword(userMail ,userPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                   // getToken();
                    Intent intent = new Intent(MainActivity.this , HomeActivity.class);
                    SharedPreferences ClearPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                    ClearPreferences.edit().clear().commit();

                    //set the data into sharedPreference
                    SharedPreferences PutDataPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                    SharedPreferences.Editor editor = PutDataPreferences.edit();
                    editor.putString("userID",binding.MailTextView.getText().toString());
                    editor.commit();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
                }
            });
    }

  /*  private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken) ;
    }
    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =database.collection("User").document(Objects.requireNonNull(Auth.getUid()));
        documentReference.update("Token" , token)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this , "Unable to update Token" ,Toast.LENGTH_SHORT ).show());
    }*/
}