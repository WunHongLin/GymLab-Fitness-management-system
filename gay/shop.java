package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gay.Adapter.shopAdapter2;
import com.example.gay.Model.product;
import com.example.gay.databinding.ActivityMainBinding;
import com.example.gay.databinding.ActivityShopBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class shop extends AppCompatActivity {

    private TextView Title;
    private Button goCar;
    private ImageView turnback;
    private RecyclerView recycler;
    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private ArrayList<product> shopDetailProducName = new ArrayList<product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        set();
    }

    private void set(){
        turnback = (ImageView) findViewById(R.id.bodyBackBackImage);
        Title = (TextView) findViewById(R.id.bodyTitleText);
        recycler = (RecyclerView) findViewById(R.id.recycler3);
        goCar = (Button) findViewById(R.id.button);

        Title.setText(getIntent().getExtras().getString("Name"));

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler.setLayoutManager(new GridLayoutManager(shop.this,2));
        dbCR = FirebaseFirestore.getInstance().collection("/productInformation");
        dbCR.whereEqualTo("category",Title.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document: task.getResult()){ shopDetailProducName.add(document.toObject(product.class)); }
                shopAdapter2 adapter = new shopAdapter2(shopDetailProducName,shop.this);
                recycler.setAdapter(adapter);
            }
        });

        goCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shop.this,ShopDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}