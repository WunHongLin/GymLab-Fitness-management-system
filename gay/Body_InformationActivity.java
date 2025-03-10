package com.example.gay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gay.Model.healthInfoValue;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Body_InformationActivity extends AppCompatActivity {

    private TextView Title;
    private DocumentReference dbDR;
    private CollectionReference dbCR;
    private String userID;
    private Button buttonAdd,buttonDetail;
    private Dialog dialog;
    private View viewDialog;
    private TextView dialogTitle;
    private ImageView turnback;
    private Button dialogButtonAdd,dialogButtonClose;
    private EditText dialogAddEditText;
    private ArrayList<healthInfoValue> chartData = new ArrayList<healthInfoValue>();
    private LineChart healthIllChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_information);

        SharedPreferences preferences = getSharedPreferences("userFile",MODE_PRIVATE);
        userID = preferences.getString("userID","unknown");

        set();
    }

    private void set(){
        Title = (TextView) findViewById(R.id.textView26);
        buttonAdd = (Button) findViewById(R.id.button14);
        buttonDetail = (Button) findViewById(R.id.button15);
        turnback = (ImageView) findViewById(R.id.bodyBackBackImage);

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Title.setText(getIntent().getExtras().getString("Name"));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Body_InformationActivity.this);
                viewDialog = getLayoutInflater().inflate(R.layout.pop_up,null);
                dialog.setContentView(viewDialog);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogTitle = viewDialog.findViewById(R.id.textView28);
                dialogButtonAdd = viewDialog.findViewById(R.id.button17);
                dialogButtonClose = viewDialog.findViewById(R.id.button16);
                dialogAddEditText = viewDialog.findViewById(R.id.editTextNumberDecimal);

                dialogTitle.setText(getIntent().getExtras().getString("Name"));

                dialog.show();
                dialog.getWindow().setLayout(680,920);

                dialogButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialogAddEditText.getText().toString().isEmpty()){
                            Toast.makeText(Body_InformationActivity.this,"欄位尚未填寫",Toast.LENGTH_LONG).show();
                        }else{
                            //init the element to database
                            Date Date = new Date(System.currentTimeMillis());
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String CurrentDate = format.format(Date);

                            //set the date and value information
                            Calendar calendar = Calendar.getInstance();
                            String Year = Integer.toString(calendar.get(Calendar.YEAR));
                            String Month = Integer.toString(calendar.get(Calendar.MONTH)+1);
                            String Day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                            Long Value = Long.parseLong(dialogAddEditText.getText().toString());
                            healthInfoValue HealthValue = new healthInfoValue(Year,Month,Day,Value);

                            //start to upload the Date
                            dbDR = FirebaseFirestore.getInstance().document("/userHealthInfo/"+userID+"/illName/"+dialogTitle.getText().toString()+"/DateManager/"+CurrentDate);
                            dbDR.set(HealthValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //setChart
                                    setChart();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });

                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Body_InformationActivity.this,BodyDetailActivity.class);
                intent.putExtra("Name",Title.getText().toString());
                startActivity(intent);
            }
        });

        setChart();
    }

    private void setChart(){
        healthIllChart = (LineChart) findViewById(R.id.healthIllChart);
        //init the chart
        Calendar calendar = Calendar.getInstance();
        chartData.clear();
        healthIllChart.invalidate();
        healthIllChart.clear();

        //select the date from database
        dbCR = FirebaseFirestore.getInstance().collection("/userHealthInfo/"+userID+"/illName/"+Title.getText().toString()+"/DateManager");
        dbCR.whereEqualTo("year",Integer.toString(calendar.get(Calendar.YEAR))).whereEqualTo("month",Integer.toString(calendar.get(Calendar.MONTH)+1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document:task.getResult()){
                    healthInfoValue userHealthValue = document.toObject(healthInfoValue.class);
                    chartData.add(userHealthValue);
                }
                //set the detail data of the chart
                Log.v("123",Integer.toString(chartData.size()));
                showChart();
            }
        });
    }

    private void showChart(){
        ArrayList<Entry> Line = new ArrayList<Entry>();

        for(int index=0;index<chartData.size();index++){
            Float value = new Float((chartData.get(index).getValue()).floatValue());
            Float v_index = (float) index;
            Line.add(new Entry(v_index,value));
        }

        //Line_end.add(new Entry(Float.parseFloat(Integer.toString(Chartdata.size())),Float.parseFloat(Chartdata.get(Chartdata.size()-1).getValue())));

        LineDataSet set = new LineDataSet(Line,"");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setColor(Color.RED);
        set.setLineWidth(1.5f);//線寬
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setLabel("123");

        LineData LineData = new LineData(set);
        healthIllChart.setData(LineData);
        healthIllChart.invalidate();

        initX();
        initY();
    }

    private void initX(){
        XAxis xAxis = healthIllChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);//X軸標籤顏色
        xAxis.setTextSize(10);
        xAxis.setLabelCount(chartData.size()+1);
        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離

        ArrayList<String> xAxisList = new ArrayList<String>();

        for (int index=0;index<chartData.size();index++){ xAxisList.add(chartData.get(index).getDay()); }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisList));
    }

    private void initY(){
        YAxis rightAxis = healthIllChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = healthIllChart.getAxisLeft();//獲取左側的軸線
    }
}