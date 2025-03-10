package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.gay.Adapter.healthDetailAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BodyDetailActivity extends AppCompatActivity {

    private ImageView turnback;
    private Spinner spinner1,spinner2;
    private RecyclerView recyclerView;
    private FloatingActionButton search;
    private ArrayList<String> yearList,monthList;
    private ArrayList<HashMap<String,String>> healthDetailMap = new ArrayList<HashMap<String,String>>();
    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private String userID;
    private healthDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_detail);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        turnback = (ImageView) findViewById(R.id.bodyBackBackImage);
        spinner1 = (Spinner) findViewById(R.id.spinner4);
        spinner2 = (Spinner) findViewById(R.id.spinner5);
        recyclerView = (RecyclerView) findViewById(R.id.recycler6);
        search = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();

        yearList = new ArrayList<String>();
        monthList = new ArrayList<String>();

        dbCR = FirebaseFirestore.getInstance().collection("/userHealthInfo/"+userID+"/illName/"+getIntent().getExtras().getString("Name")+"/DateManager");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document: task.getResult()){
                    //check if year exist
                    if(!(yearList.contains(document.get("year").toString()))){
                        yearList.add(document.get("year").toString());
                    }
                    //check if month exist
                    if(!(monthList.contains(document.get("month").toString()))){
                        monthList.add(document.get("month").toString());
                    }
                }

                //transfer the arraylist to array
                String[] arrayYear = yearList.toArray(new String[yearList.size()]);
                String[] arrayMonth = monthList.toArray(new String[monthList.size()]);

                //set the spinner
                ArrayAdapter<String> monthAD = new ArrayAdapter<String>(BodyDetailActivity.this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,arrayMonth);
                monthAD.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
                spinner1.setAdapter(monthAD);

                ArrayAdapter<String> yearAD = new ArrayAdapter<String>(BodyDetailActivity.this,com.google.android.material.R.layout.support_simple_spinner_dropdown_item,arrayYear);
                yearAD.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
                spinner2.setAdapter(yearAD);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(BodyDetailActivity.this));

        //download the information
        dbCR = FirebaseFirestore.getInstance().collection("/userHealthInfo/"+userID+"/illName/"+getIntent().getExtras().getString("Name")+"/DateManager");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document:task.getResult()){
                    //add the data into map
                    HashMap<String,String> Map = new HashMap<String,String>();
                    Map.put("Date", document.getId());
                    Map.put("Value",document.get("value").toString());
                    healthDetailMap.add(Map);
                }
                detailAdapter = new healthDetailAdapter(healthDetailMap);
                recyclerView.setAdapter(detailAdapter);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchYear = (String) spinner2.getSelectedItem();
                String searchMonth = (String) spinner1.getSelectedItem();

                //prevent the data reduncy so clear the data
                healthDetailMap.clear();

                //down the data
                dbCR = FirebaseFirestore.getInstance().collection("/userHealthInfo/"+userID+"/illName/"+getIntent().getExtras().getString("Name")+"/DateManager");
                dbCR.whereEqualTo("year",searchYear).whereEqualTo("month",searchMonth).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot document:task.getResult()){
                            //add the data into map
                            HashMap<String,String> Map = new HashMap<String,String>();
                            Map.put("Date", document.getId());
                            Map.put("Value",document.get("value").toString());
                            healthDetailMap.add(Map);
                        }

                        //notify the recycler to change
                        detailAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}