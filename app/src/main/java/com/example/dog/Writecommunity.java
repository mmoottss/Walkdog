package com.example.dog;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class Writecommunity extends AppCompatActivity {

    final private String TAG = getClass().getSimpleName();

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    EditText content;
    Button btnSave;
    Bitmap uploadBitmap;

    String userid = ""; //유저 아이디



    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communtiy_write);

        userid = getIntent().getStringExtra("userID");

        imageview = (ImageView) findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                // 사진을 글쓰는 창에 올리기까지 가능. 글자취소선 이유 모르겠음...
            }
        });
        //글쓰기?
        /*content = (EditText) findViewById(R.id.content_et);
        String contents = content.getText().toString();
        contents = contents.replace("'","''");*/

        content = (EditText) findViewById(R.id.content_et);


        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //게시물 등록 함수
                RegBoard regBoard = new RegBoard();
                regBoard.execute(userid, content.getText().toString());

                // 버튼클릭 했을 때 사진이랑 텍스트 같이 저장하기.
                // 저장 성공하면 문구띄우고 글쓰기창 나가기
                // 우선순위가 저장소 호출,,,



            }
        });
    }



    class RegBoard extends AsyncTask<String,Void,String> {
        protected void onPreExcute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExcute");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG,"onPostExecute" + result);

            if(result.equals("success")) {
                // 결과값이 successd이면 토스트 메시지 뿌리고 이전 액티비티로 이동.
                // 이때 activity_community 의 onResume() 함수가 호출되면서, 데이터를 새로고침
                Toast.makeText(Writecommunity.this,"등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else
            {
                Toast.makeText(Writecommunity.this,result,Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {

        }
        // hph url 만들어지면 시도해보기.
            /*String userid = params[0];
            String content = params[1];

            String server_url = "";


            URL url;
            String response = "";
            try {
                url = new URL(server_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", userid)
                        .appendQueryParameter("content", content);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }*/


        // 아마 사진 저장하는 거....?
        /*String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
            String mi = System.currentTimeMillis() + ".png";
            String realpath = dirPath + "/" + mi;

            String str = Environment.getExternalStorageState();
            if(str.equals(Environment.MEDIA_MOUNTED)) {
                OutputStream outputStream = null;
                File file = new File(dirPath);

                if(!file.exists()) {
                    file.mkdir();
                }
                try {
                    outputStream = new FileOutputStream(dirPath + "/"+ mi);


                    uploadBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                catch (Exception e) {

                }
            } else {
                Toast.makeText(Writecommunity.this,"실패",Toast.LENGTH_SHORT).show();
            }
            return null;
        }*/

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
    // 우선은 저장해서 커뮤니티 메인에서 볼 수 있게 하기. 그 후에 서버연결 같이하기.


}
