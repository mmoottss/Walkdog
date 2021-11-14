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


    private static final String KEY_DATA = "KEY_DATA";
    Button write_btn, map_btn, community_btn, option_btn;
    private TextView title;
    private String TAG = getClass().getSimpleName();
    private ListView listView;

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
        Uri imageuri = getIntent().getParcelableExtra("uri");

//        Bitmap bitmap = (Bitmap) intent.getExtras().get("image");
//        ImageView communityImage = (ImageView) findViewById(R.id.cont_image);
//        communityImage.setImageBitmap(bitmap);
        // 이미지를 bitmap에 저장해서 그대로 옮기는 거.


//        byte[] arr = getIntent().getByteArrayExtra("image");
//        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//        ImageView communityImage = (ImageView) findViewById(R.id.cont_image);
//        communityImage.setImageBitmap(image);
        // 이미지를 bitmap에 저장해서 byteArray에 넣어서 옮기는 거.


//        TextView name = (TextView)findViewById(R.id.cont_name);
        TextView title = (TextView)findViewById(R.id.cont_title);
        TextView content = (TextView)findViewById(R.id.cont_text);
        ImageView image = (ImageView)findViewById(R.id.cont_image);

        //글쓰기에서 넘어오는 제목,내용 동적생성한 레이아웃에 적용하기.

        title.setText(communityTitle);
//        name.setText(userName);  //시작할 때부터 있어서 고민해봐야 함
        image.setImageURI(imageuri);
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
                // 화면간 이동 데이터 전달
                Intent intent = new Intent(getApplicationContext(), Writecommunity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
//                finish();

                // 동적생성
                // 방법을 찾는다면 글쓰기창의 저장버튼을 눌렀을 때 생성되도록 수정하기
                // 생성된 레이아웃에 데이터 적용법도 찾기!

//                subcommunity n_layout = new subcommunity(getApplicationContext());
//                LinearLayout con = (LinearLayout) findViewById(R.id.LinLayout);
//                con.addView(n_layout);

            }
        });
        //글

    }



    //뒤로가기 누를 시 로그아웃 하시겠습니까? 출력
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("앱을 종료 하시겠습니까?");

        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity(Community.this);
                System.exit(0);
            }
        });
        builder.create().show();
    }


}