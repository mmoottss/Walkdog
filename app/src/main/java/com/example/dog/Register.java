package com.example.dog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

public class Register extends AppCompatActivity {

    private EditText reg_id, reg_pass, reg_nickname;
    private Button register_confirm, id_check, name_check;
    public int idcheck = 0, namecheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_id = findViewById(R.id.reg_id);
        reg_pass = findViewById(R.id.reg_pass);
        reg_nickname = findViewById(R.id.reg_nickcname);
        id_check = findViewById(R.id.id_check);
        name_check = findViewById(R.id.name_check);

        //아이디 중복체크 눌렀을 때
        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = reg_id.getText().toString();

                if(reg_id.length() == 0) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(!success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setTitle("안내");
                                builder.setMessage("사용 가능한 아이디입니다.");

                                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.create().show();
                                reg_id.setInputType(InputType.TYPE_NULL);
                                idcheck += 1;
                            } if(success) {
                                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                IdRequest idRequest = new IdRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(idRequest);
            }

        });
        //닉네임 중복체크 눌렀을 때
        name_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = reg_nickname.getText().toString();

                if(reg_nickname.length() == 0) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(!success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setTitle("안내");
                                builder.setMessage("사용 가능한 닉네임입니다.");

                                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.create().show();
                                reg_nickname.setInputType(InputType.TYPE_NULL);
                                namecheck += 1;

                            } if(success) {
                                Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            }
                        }
                    };
                NameRequest nameRequest = new NameRequest(userName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(nameRequest);

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

                if(reg_id.length() == 0) {
                    Toast.makeText(getApplicationContext(), "사용할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } if(reg_pass.length() == 0) {
                    Toast.makeText(getApplicationContext(),"사용할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } if (reg_nickname.length() == 0) {
                    Toast.makeText(getApplicationContext(), "사용할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } if (idcheck == 0) {
                    Toast.makeText(getApplicationContext(), "아이디 중복체크를 완료해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } if (namecheck == 0) {
                    Toast.makeText(getApplicationContext(), "닉네임 중복체크를 완료해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                               if (success) {
                                Toast.makeText(getApplicationContext(), "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                   Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
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
    class IdRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동 )
        final static String URL = "http://skwhdgns111.ivyro.net/idcheck.php";
        private Map<String, String> map;

        public IdRequest(String userID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
    class NameRequest extends StringRequest {

        // 서버 URL 설정 ( PHP 파일 연동 )
        final static String URL = "http://skwhdgns111.ivyro.net/namecheck.php";
        private Map<String, String> map;

        public NameRequest(String userName, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userName", userName);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
    //뒤로가기 누를 시 회원가입을 하시겠습니까? 출력
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("회원가입을 취소하시겠습니까?");

        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }
}