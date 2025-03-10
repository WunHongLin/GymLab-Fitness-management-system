package com.example.gay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TrainingActivity extends AppCompatActivity {

    private Button case1,case2,case3,case4,case5;
    private ImageView turnback;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        set();
    }

    private void set(){
        case1 = (Button) findViewById(R.id.button18);
        case2 = (Button) findViewById(R.id.button19);
        case3 = (Button) findViewById(R.id.button20);
        case4 = (Button) findViewById(R.id.button21);
        case5 = (Button) findViewById(R.id.button22);

        case1.setOnClickListener(openEvent);
        case2.setOnClickListener(openEvent);
        case3.setOnClickListener(openEvent);
        case4.setOnClickListener(openEvent);
        case5.setOnClickListener(openEvent);

        turnback = (ImageView) findViewById(R.id.personalityBackImage);

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener openEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button18:
                    content = "手部訓練";
                    break;
                case R.id.button19:
                    content = "腿部訓練";
                    break;
                case R.id.button20:
                    content = "背部訓練";
                    break;
                case R.id.button21:
                    content = "腹部訓練";
                    break;
                case R.id.button22:
                    content = "胸部訓練";
                    break;
            }

            Intent intent = new Intent(TrainingActivity.this,MenuActivity.class);
            intent.putExtra("Name",content);
            startActivity(intent);
        }
    };
}