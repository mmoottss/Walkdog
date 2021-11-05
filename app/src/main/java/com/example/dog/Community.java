package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class Community extends AppCompatActivity {

    Button write_btn,map_btn,community_btn,option_btn;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        t = findViewById(R.id.o);
        map_btn=findViewById(R.id.mapmenu);
        write_btn=findViewById(R.id.btnWrite);
        option_btn = findViewById(R.id.option);


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        t.setText(userName);

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Community.this, Maps.class);
                startActivity(intent);
            }
        });


        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 화면간 이동 데이터 전달
                Intent intent = new Intent(getApplicationContext(), Writecommunity.class);
                finishActivity(0);
                startActivity(intent);
            }
        });
        }

    //자동 로그아웃 방지. 뒤로가기 누를 시 Maps로 이동
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Maps.class);
        startActivity(intent);
    }

    }