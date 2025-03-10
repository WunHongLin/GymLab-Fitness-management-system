package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gay.Adapter.healthAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BodyActivity extends AppCompatActivity {

    private ImageView turnBack;
    private Button add;
    private RecyclerView recyclerView;
    private DocumentReference dbDR;
    private CollectionReference dbCR;
    private Dialog dialog;
    private View viewDialog;
    private TextView Title;
    private Button dialogButtonAdd,dialogButtonClose;
    private EditText dialogAddEditText;
    private healthAdapter adapter;
    private String userID;
    private ArrayList<String> illName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        turnBack = (ImageView) findViewById(R.id.bodyBackBackImage);
        add = (Button) findViewById(R.id.button7);
        recyclerView = (RecyclerView) findViewById(R.id.recycler5);

        turnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(BodyActivity.this);
                viewDialog = getLayoutInflater().inflate(R.layout.pop_up1,null);
                dialog.setContentView(viewDialog);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Title = viewDialog.findViewById(R.id.textView28);
                dialogButtonAdd = viewDialog.findViewById(R.id.button17);
                dialogButtonClose = viewDialog.findViewById(R.id.button16);
                dialogAddEditText = viewDialog.findViewById(R.id.editTextNumberDecimal);

                Title.setText("身體資訊");

                //dialog show
                dialog.show();
                dialog.getWindow().setLayout(680,920);

                dialogButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(dialogAddEditText.getText().toString().isEmpty()){
                            Toast.makeText(BodyActivity.this,"欄位尚未填寫",Toast.LENGTH_LONG).show();
                        }else{
                            dbDR = FirebaseFirestore.getInstance().document("/userHealthInfo/"+userID+"/illName/"+dialogAddEditText.getText().toString());
                            Map<String,Object> emptyMap = new HashMap<String,Object>();
                            dbDR.set(emptyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // add success close the dialog
                                    notifyTheDataChange(userID);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });

                //the close event
                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        setTheRecycler();
    }

    private void notifyTheDataChange(String ID){
        dbCR = FirebaseFirestore.getInstance().collection("userHealthInfo/"+ID+"/illName");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                illName.clear();
                adapter.notifyDataSetChanged();
                for(DocumentSnapshot documentSnapshot:task.getResult()){
                    illName.add(documentSnapshot.getId());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setTheRecycler(){
        illName.clear();

        recyclerView = (RecyclerView) findViewById(R.id.recycler5);
        recyclerView.setLayoutManager(new LinearLayoutManager(BodyActivity.this,LinearLayoutManager.HORIZONTAL,false));

        //get the data from database
        dbCR = FirebaseFirestore.getInstance().collection("userHealthInfo/"+userID+"/illName");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot:task.getResult()){
                    illName.add(documentSnapshot.getId());
                }
                adapter = new healthAdapter(illName,BodyActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}