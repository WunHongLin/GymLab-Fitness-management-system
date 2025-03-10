package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gay.Adapter.shoppingCarAdapter;
import com.example.gay.Model.ShoppingCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopDetailActivity extends AppCompatActivity {

    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private RecyclerView recyceler;
    private TextView Coin;
    private Button GoCash;
    private ImageView turnback;
    private ArrayList<ShoppingCar> shoppingCarList = new ArrayList<ShoppingCar>();
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        recyceler = (RecyclerView) findViewById(R.id.recycler4);
        Coin = (TextView) findViewById(R.id.textView22);
        GoCash = (Button) findViewById(R.id.button11);
        turnback = (ImageView) findViewById(R.id.bodyBackBackImage);

        recyceler.setLayoutManager(new LinearLayoutManager(ShopDetailActivity.this));

        shoppingCarList.clear();

        //download the data from database
        dbCR = FirebaseFirestore.getInstance().collection("/userShoppingCar/"+userID+"/totalProduct");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document:task.getResult()){ shoppingCarList.add(document.toObject(ShoppingCar.class)); }
                shoppingCarAdapter adapter = new shoppingCarAdapter(shoppingCarList, ShopDetailActivity.this,userID);
                recyceler.setAdapter(adapter);
            }
        });

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}