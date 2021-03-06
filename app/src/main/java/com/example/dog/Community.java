package com.example.dog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Community extends AppCompatActivity {

    Button write_btn, map_btn, community_btn, option_btn;
    private String TAG = getClass().getSimpleName();
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        map_btn = findViewById(R.id.map_btn);
        write_btn = findViewById(R.id.btnWrite);
        option_btn = findViewById(R.id.option);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userName = intent.getStringExtra("userName");
        String communityTitle = intent.getExtras().getString("communityTitle");
        String communityContent = intent.getExtras().getString("communityContent");
        uri = intent.getData();




//        TextView name = (TextView)findViewById(R.id.cont_name);
        TextView title = (TextView)findViewById(R.id.cont_title);
        TextView content = (TextView)findViewById(R.id.cont_text);
        ImageView image = (ImageView)findViewById(R.id.cont_image);

        title.setText(communityTitle);
//        name.setText(userName);  //????????? ????????? ????????? ??????????????? ???
        image.setImageURI(uri);
        content.setText(communityContent);



        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, Maps.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });


        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, option_act.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });


        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????? ?????? ????????? ??????
                Intent intent = new Intent(getApplicationContext(), Writecommunity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
//                finish();

                // ????????????
                // ????????? ???????????? ??????????????? ??????????????? ????????? ??? ??????????????? ????????????
                // ????????? ??????????????? ????????? ???????????? ??????!

//                subcommunity n_layout = new subcommunity(getApplicationContext());
//                LinearLayout con = (LinearLayout) findViewById(R.id.LinLayout);
//                con.addView(n_layout);

            }
        });
        //???

    }



    //???????????? ?????? ??? ???????????? ??????????????????? ??????
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("?????? ?????? ???????????????????");

        builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity(Community.this);
                System.exit(0);
            }
        });
        builder.create().show();
    }


}