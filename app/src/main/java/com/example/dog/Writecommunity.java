package com.example.dog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;


public class Writecommunity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    EditText title_et,content_et;
    private TextView nickname;
    Button btnSave;
    Bundle bundle;
    Bitmap sendbitmap;
    byte[] image = new byte[100];

    String userName = "";
    String title, content, name;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communtiy_write);

        /*Intent intent = getIntent();
        intent.putExtra("userName", userName);
        String userName = intent.getStringExtra("userName");

        nickname.setText(userName);*/

        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
        //textview 내용을 string으로 저장
        title_et.getText().toString();
        content_et.getText().toString();
        title = new String(title_et.getText().toString());
        content = new String(content_et.getText().toString());
//        name = new String(nickname.getText().toString());


        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 동적생성 되는건 모르겠지만 켜진다
                //보내줄 데이터 bundle에 저장
                //이미지는 비트맵으로 변환 후 저장
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendbitmap = BitmapFactory.decodeResource(getResources(), GET_GALLERY_IMAGE);
//                sendbitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                image = stream.toByteArray();
//                bundle.putByteArray("image",image);
//                bundle.putStringArray("text", new String[]{title});
//                bundle.putStringArray("content", new String[]{content});
//                bundle.putStringArray("nickname", new String[]{name});

//               Intent intent = new Intent(getApplicationContext(), subcommunity.class);
//                intent.putExtras(bundle);
            }
        });



        imageview = (ImageView) findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                // 사진을 글쓰는 창에 올리기까지 가능. 글자취소선 이유 모르겠음...
            }
        });
    }

    @Override
    // 갤러리에 있는 사진을 글쓰는 창에 올리는 코드
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);
        if (requsetCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }

}
