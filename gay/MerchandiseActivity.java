package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gay.Model.ShoppingCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MerchandiseActivity extends AppCompatActivity {

    private TextView txtName,txtCategory,txtPrice,count;
    private Button add,minus,putCar,goCar;
    private ImageView image,turnback;
    private StorageReference storageReferance,pickReferance;
    private DocumentReference dbDR;
    private CollectionReference dbCR;
    private int currentProductNum = 0;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        txtName = (TextView) findViewById(R.id.textView12);
        txtCategory = (TextView) findViewById(R.id.textView13);
        txtPrice = (TextView) findViewById(R.id.textView15);
        image = (ImageView) findViewById(R.id.imageView7);
        turnback = (ImageView) findViewById(R.id.bodyChestBackImage);

        txtName.setText(getIntent().getExtras().getString("Name"));
        txtCategory.setText(getIntent().getExtras().getString("Category"));
        txtPrice.setText(getIntent().getExtras().getString("Price"));

        //need to set the image
        storageReferance = FirebaseStorage.getInstance().getReference();
        pickReferance = storageReferance.child(getIntent().getExtras().getString("Uri"));

        try {
            final File file = File.createTempFile("images",".png");
            pickReferance.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    image.setImageURI(Uri.fromFile(file));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add = (Button) findViewById(R.id.button4);
        minus = (Button) findViewById(R.id.button3);
        count = (TextView) findViewById(R.id.textView14);

        //init the current product Num
        currentProductNum = 1;

        //set the event of button plus push
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProductNum +=1;
                count.setText(String.format("%02d",currentProductNum));
            }
        });

        //set the event of button minus push
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentProductNum-1>0){
                    currentProductNum -=1;
                    count.setText(String.format("%02d",currentProductNum));
                }
            }
        });

        putCar = (Button) findViewById(R.id.button6);
        putCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //count the total num
                int totalPrice = currentProductNum*Integer.parseInt(txtPrice.getText().toString());

                //create the new class
                ShoppingCar shoppingCar = new ShoppingCar(txtName.getText().toString(),txtCategory.getText().toString(),Long.valueOf(currentProductNum),Long.valueOf(totalPrice));

                dbDR = FirebaseFirestore.getInstance().document("/userShoppingCar/"+userID+"/totalProduct/"+txtName.getText().toString());
                dbDR.set(shoppingCar).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });

        goCar = (Button) findViewById(R.id.button);
        goCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchandiseActivity.this,ShopDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}