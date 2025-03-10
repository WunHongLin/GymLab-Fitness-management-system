package com.example.gay;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gay.Adapter.MenuSetAdapter;
import com.example.gay.Adapter.shopAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.NameList;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private TextView Title;
    private Button turnback;
    private RecyclerView recycler;
    private FloatingActionButton addButton;
    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private MenuSetAdapter adapter;
    private ArrayList<String> Name = new ArrayList<String>();
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        Title = (TextView) findViewById(R.id.textView32);
        turnback = (Button) findViewById(R.id.button23);
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        recycler = (RecyclerView) findViewById(R.id.recycler8);

        Title.setText(getIntent().getExtras().getString("Name"));

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,MenuDetailActivity.class);
                intent.putExtra("Name",getIntent().getExtras().getString("Name"));
                intent.putExtra("key","1");
                claerEditText.launch(intent);
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

        dbCR = FirebaseFirestore.getInstance().collection("/userTrain/"+userID+"/"+getIntent().getExtras().getString("Name"));
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Name.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){
                    Name.add(documentSnapshot.getId());
                }
                adapter = new MenuSetAdapter(Name,MenuActivity.this,userID);
                recycler.setAdapter(adapter);
            }
        });
    }

    private ActivityResultLauncher<Intent> claerEditText = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //clear the editText content
            recycler.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

            dbCR = FirebaseFirestore.getInstance().collection("/userTrain/"+userID+"/"+getIntent().getExtras().getString("Name"));
            dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Name.clear();

                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Name.add(documentSnapshot.getId());
                    }
                    adapter = new MenuSetAdapter(Name,MenuActivity.this,userID);
                    recycler.setAdapter(adapter);
                }
            });
        }
    });
}