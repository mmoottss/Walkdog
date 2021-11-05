package com.example.dog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userName = intent.getStringExtra("userName");

        t.setText(userName);

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, Maps.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
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
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
            }
        });

        }

    //뒤로가기 누를 시 로그아웃 하시겠습니까? 출력
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("로그아웃 하시겠습니까?");

        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Community.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }
    }