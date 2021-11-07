package com.example.dog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class option_act extends AppCompatActivity {
    Button community_button, map_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        community_button = findViewById(R.id.community);
        map_button = findViewById(R.id.mapmenu);

        //하단바 커뮤니티로 이동
        community_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option_act.this, Community.class);
                startActivity(intent);
            }
        });

        //하단바 커뮤니티로 이동
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);
                startActivity(intent);
            }
        });
    }
}