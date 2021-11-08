package com.example.dog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class subcommunity2 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_community);

        TextView name = (TextView)findViewById(R.id.reg_nickcname);
        TextView title = (TextView)findViewById(R.id.title_et);
        TextView content = (TextView)findViewById(R.id.content_et);

        Intent intent = getIntent();
        String userID = intent.getExtras().getString("userID");
        String userPassword = intent.getExtras().getString("userPassword");
        String userName = intent.getExtras().getString("userName");
        String communityTitle = intent.getExtras().getString("communityTitle");
        String communityContent = intent.getExtras().getString("communityContent");

        name.setText(userName);
        title.setText(communityTitle);
        content.setText(communityContent);

    }
}
