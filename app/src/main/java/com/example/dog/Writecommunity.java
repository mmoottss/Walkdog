package com.example.dog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Writecommunity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communtiy_write);



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

    //뒤로가기 누를 시 글 작성을 취소하시겠습니까? 출력
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("글 작성을 취소하시겠습니까?");

        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(Writecommunity.this, Community.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }
}
