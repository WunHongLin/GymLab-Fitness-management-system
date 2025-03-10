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

import com.example.gay.Adapter.MenuAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MenuDetailActivity extends AppCompatActivity {

    private ImageView turnback;
    private FloatingActionButton buttonAdd,buttonFinish;
    private EditText Name;
    private Button add,cancel;
    private TextView Title;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private View viewDialog;
    private String userID;
    private MenuAdapter adapter;
    private ArrayList<String> NameList = new ArrayList<String>();
    private ArrayList<String> ItemNameList = new ArrayList<String>();
    private DocumentReference dbDR;
    private CollectionReference dbCR;
    private String partial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        if(getIntent().getExtras().getString("key").equals("1")){
            set();
        }else{
            setRecycler();
        }
    }

    private void set(){
        turnback = (ImageView) findViewById(R.id.personalityBackImage);
        buttonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        buttonFinish = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        recyclerView = (RecyclerView) findViewById(R.id.recycler7);

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog2();
            }
        });
    }

    private void setRecycler(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler7);
        recyclerView.setLayoutManager(new LinearLayoutManager(MenuDetailActivity.this));

        String command = String.format("/userTrain/%s/%s/%s",userID,getIntent().getExtras().getString("Name"),getIntent().getExtras().getString("Set"));
        dbDR = FirebaseFirestore.getInstance().document(command);
        dbDR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                adapter = new MenuAdapter((ArrayList<String>) documentSnapshot.get("name"));
                recyclerView.setAdapter(adapter);
            }
        });

        turnback = (ImageView) findViewById(R.id.personalityBackImage);
        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDialog(){
        dialog = new Dialog(MenuDetailActivity.this);
        viewDialog = getLayoutInflater().inflate(R.layout.pop_up2,null);
        dialog.setContentView(viewDialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Name = (EditText) viewDialog.findViewById(R.id.editTextNumberDecimal);
        add = (Button) viewDialog.findViewById(R.id.button17);
        cancel = (Button) viewDialog.findViewById(R.id.button16);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty()){
                    Toast.makeText(MenuDetailActivity.this,"有欄位還沒填寫喔",Toast.LENGTH_SHORT).show();
                }else{
                    NameList.add(Name.getText().toString());
                    recyclerView.setLayoutManager(new LinearLayoutManager(MenuDetailActivity.this));
                    adapter = new MenuAdapter(NameList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog show
        dialog.show();
        dialog.getWindow().setLayout(680,920);
    }

    private void setDialog2(){
        dialog = new Dialog(MenuDetailActivity.this);
        viewDialog = getLayoutInflater().inflate(R.layout.pop_up2,null);
        dialog.setContentView(viewDialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Title = (TextView) viewDialog.findViewById(R.id.textView28);
        Name = (EditText) viewDialog.findViewById(R.id.editTextNumberDecimal);
        add = (Button) viewDialog.findViewById(R.id.button17);
        cancel = (Button) viewDialog.findViewById(R.id.button16);

        Title.setText("新訓練組合");
        Name.setHint("請輸入訓練組合名稱");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty()){
                    Toast.makeText(MenuDetailActivity.this,"有欄位還沒填寫喔",Toast.LENGTH_SHORT).show();
                }else{
                    partial = getIntent().getExtras().getString("Name");
                    String SetName = Name.getText().toString();
                    String command = String.format("/userTrain/%s/%s/%s",userID,partial,SetName);
                    dbDR = FirebaseFirestore.getInstance().document(command);
                    HashMap<String,Object> map = new HashMap<String,Object>();
                    map.put("name",NameList);
                    dbDR.set(map);
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog show
        dialog.show();
        dialog.getWindow().setLayout(680,920);
    }
}