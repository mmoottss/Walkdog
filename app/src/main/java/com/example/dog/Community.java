package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Community extends AppCompatActivity {

    final private String TAG = getClass().getSimpleName();

    Button write_btn, map_btn, community_btn, option_btn;
    ListView listView;
    String userid = "";

    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();

    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // 로그인에서 넘긴 userid값 받기..?
        userid = getIntent().getStringExtra("userID");

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 어떤 값을 선택했는지 토스트를 뿌려줌
                Toast.makeText(Community.this, adapterView.getItemAtPosition(i) + " 클릭", Toast.LENGTH_SHORT).show();
/*
                // 게시물 크게 보기
                // 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
                Intent intent = new Intent(Community.this, Detailcommunity.class);
                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userid);
                startActivity(intent);
*/

            }
        });
        write_btn = findViewById(R.id.btnWrite);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 화면간 이동 데이터 전달
                Intent intent = new Intent(getApplicationContext(), Writecommunity.class);
                intent.putExtra("userID",userid);
                startActivity(intent);
            }
        });
        //하단바 클릭으로 맵으로 이동
        map_btn = findViewById(R.id.mapmenu);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, Maps.class);
                startActivity(intent);
            }
        });
        //하단바 클릭으로 옵션으로 이동
        option_btn = findViewById(R.id.option);
        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    protected void onResume() {
        super.onResume();
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }

    class GetBoard extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG,"onPreExcute");
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG,"onPostExecute," + result);

            titleList.clear();
            seqList.clear();
            try {
                // 결과물이 JSONArray 형태로 넘어오기 때문에 파싱
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.optString("title");
                    String seq = jsonObject.optString("seq");

                    // title, seq 값을 변수로 받아서 배열에 추가
                    titleList.add(title);
                    seqList.add(seq);
                }
                // ListView 에서 사용할 arrayAdapter를 생성하고, ListView 와 연결
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(Community.this, android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);

                // arrayAdapter의 데이터가 변경되었을때 새로고침
                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected String doInBackground(String... strings) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            return null;
        }
    }
}