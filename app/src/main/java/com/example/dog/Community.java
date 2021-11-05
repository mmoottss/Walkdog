package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Community extends AppCompatActivity {

    Button write_btn,map_btn,community_btn,option_btn;
    private TextView o;

    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        o = findViewById(R.id.o);
        map_btn=findViewById(R.id.mapmenu);
        write_btn=findViewById(R.id.btnWrite);
        option_btn = findViewById(R.id.option);



        Intent intent = getIntent();
        intent.putExtra("userName", userName);
        String userName = intent.getStringExtra("userName");

        o.setText(userName);

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                startActivity(intent);
            }
        });
        }



    }