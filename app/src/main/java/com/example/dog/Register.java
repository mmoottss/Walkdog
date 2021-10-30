package com.example.dog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText reg_id, reg_pass, reg_nickname;
    private Button register_confirm, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_id = findViewById(R.id.reg_id);
        reg_pass = findViewById(R.id.reg_pass);
        reg_nickname = findViewById(R.id.reg_nickcname);

        //뒤로가기 버튼을 눌렀을 때
        btn_back = findViewById(R.id.reg_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });


        // 회원가입 버튼을 눌렀을 때
        register_confirm = findViewById(R.id.register_confirm);
        register_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 기재된 값을 get으로 불러온다.
                String userID = reg_id.getText().toString();
                String userPassword = reg_pass.getText().toString();
                String userName = reg_nickname.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(),"회원가입 성공.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"회원가입에 실패.",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    class RegisterRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동)
        final static private String URL = "http://skwhdgns111.ivyro.net/Register.php";
        private Map<String, String> map;

        public RegisterRequest(String userID, String userPassword, String userName, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID",userID);
            map.put("userPassword", userPassword);
            map.put("userName", userName);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

}