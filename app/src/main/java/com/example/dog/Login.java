package com.example.dog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    private long backKeyPressedTime = 0;
    String userId, userPw, userNm;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        userId = auto.getString("userId", userId);
        userPw = auto.getString("userPw", userPw);
        userNm = auto.getString("userNm", userNm);

        //자동 로그인
        if(userId == null && userPw == null) {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //EditText에 기재된 값을 get으로 불러온다.
                    String userID = et_id.getText().toString();
                    String userPassword = et_pass.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (et_id.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (et_pass.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!success) {
                                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (success) {
                                    String userID = jsonObject.getString("userID");
                                    String userPassword = jsonObject.getString("userPassword");
                                    String userName = jsonObject.getString("userName");
                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("userId", et_id.getText().toString());
                                    autoLogin.putString("userPw", et_pass.getText().toString());
                                    autoLogin.putString("userNm", userName);
                                    autoLogin.commit();
                                    Intent intent = new Intent(Login.this, Maps.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPassword", userPassword);
                                    intent.putExtra("userName", userName);
                                    Toast.makeText(getApplicationContext(), userName + "님 어서오세요!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);
                }
            });

        }
        else if (userId != null && userPw != null) {
            if (userId.equals(userId) && userPw.equals(userPw)) {
                    Toast.makeText(getApplicationContext(), userNm + "님 자동 로그인 되셨습니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Maps.class);
                    startActivity(intent);
                    finish();
            }
        }


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }
    class LoginRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동 )
        final static String URL = "http://skwhdgns111.ivyro.net/Login.php";
        private Map<String, String> map;

        public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);
            map.put("userPassword",userPassword);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 2번 누를 시 종료
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다. ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // 앱 완전 종료 코드
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }

    }

}
