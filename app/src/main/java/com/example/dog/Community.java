package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Community extends AppCompatActivity {

    Button write_btn,map_btn,community_btn,option_btn;
    private TextView nickname;

    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        map_btn=findViewById(R.id.mapmenu);
        write_btn=findViewById(R.id.btnWrite);
        option_btn = findViewById(R.id.option);



        /*Intent intent = getIntent();
        intent.putExtra("userName", userName);
        String userName = intent.getStringExtra("userName");

        nickname.setText(userName);*/

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
                setContentView(R.layout.activity_option);
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new option(), null).commit();
            }
        });



        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 화면간 이동 데이터 전달
                Intent intent = new Intent(getApplicationContext(), Writecommunity.class);
                startActivity(intent);

                // 동적생성
                // 방법을 찾는다면 글쓰기창의 저장버튼을 눌렀을 때 생성되도록 수정하기
                // 생성된 레이아웃에 데이터 적용법도 찾기!
                subcommunity n_layout = new subcommunity(getApplicationContext());
                LinearLayout con = (LinearLayout)findViewById(R.id.LinLayout);
                con.addView(n_layout);
            }
        });
        }



    }