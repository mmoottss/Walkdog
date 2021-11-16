package com.example.dog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Writecommunity extends AppCompatActivity {

    private ImageView img_et;
    private EditText title_et,content_et;
    private TextView nickname;
    private Button btnSave;
    Uri uri;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    uri = intent.getData();  //선택한 사진의 경로 객체 얻어오기
                    img_et.setImageURI(uri);

                }
            }
        });


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communtiy_write);

        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
        img_et = (ImageView) findViewById(R.id.img_et);
        //textview 내용을 string으로 저장
        title_et.getText().toString();
        content_et.getText().toString();
//        name = new String(nickname.getText().toString());

        img_et.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });
        //글 작성하기 누를 시
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityTitle = title_et.getText().toString();
                String communityContent = content_et.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {

                                String communityTitle = jsonObject.getString("communityTitle");
                                String communityContent = jsonObject.getString("communityContent");
                                String communityimg = jsonObject.getString("communityimg");
                                Uri uri = Uri.parse(communityimg);

                                Intent intent = new Intent(Writecommunity.this, Community.class);
                                String userID = intent.getStringExtra("userID");
                                String userPassword = intent.getStringExtra("userPassword");
                                String userName = intent.getStringExtra("userName");
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                intent.putExtra("userName", userName);
                                intent.putExtra("communityTitle", communityTitle);
                                intent.putExtra("communityContent", communityContent);
                                intent.putExtra("uri", uri);

                                startActivity(intent);

                                //subcommunity에 넘길 값 list에 저장 후 동적 생성
//                                list = new ArrayList<>();
//                                SampleItem sampleItem = new SampleItem(userName, communityTitle, communityContent, "");
//                                list.add(sampleItem);

                                //동적생성
//                                subcommunity sub = new subcommunity(getApplicationContext());
//                                LinearLayout con = (LinearLayout)findViewById(R.id.LinLayout);
//                                con.addView(sub);

                                //여기까지!

                                Toast.makeText(getApplicationContext(), "글을 작성하였습니다.", Toast.LENGTH_SHORT).show();
//                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 서버로 Volley를 이용해서 요청
                WriteRequest writeRequest = new WriteRequest(communityTitle, communityContent, uri, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Writecommunity.this);
                queue.add(writeRequest);

                ExportRequest exportRequest = new ExportRequest(communityTitle, communityContent, uri, responseListener);
                Volley.newRequestQueue(Writecommunity.this); //////////////////////////////////////////////////////////
                queue.add(exportRequest);
            }
        });
    }

    //뒤로가기 누를 시 글 작성을 취소하시겠습니까? 출력
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userName = intent.getStringExtra("userName");
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
                Intent intent = new Intent(Writecommunity.this, Community.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                finish();
            }
        });
        builder.create().show();
    }

    class WriteRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동 )
        final static String URL = "http://skwhdgns111.ivyro.net/community_save.php";
        private Map<String, String> map;

        public WriteRequest(String communityTitle, String communityContent, Uri communityimg, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("communityTitle", communityTitle);
            map.put("communityContent", communityContent);
            map.put("communityimg", communityimg+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
    class ExportRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동 )
        final static String URL = "http://skwhdgns111.ivyro.net/community_export.php";
        private Map<String, String> map;

        public ExportRequest(String communityTitle, String communityContent, Uri communityimg, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("communityTitle", communityTitle);
            map.put("communityContent", communityContent);
            map.put("communityimg", communityimg+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
}