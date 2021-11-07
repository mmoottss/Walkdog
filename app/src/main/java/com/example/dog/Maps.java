package com.example.dog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Maps";
    private static final int TIME = 2000;
    private static final int DISTANCE = 4;
    private static int search_day = 1;
    private static final double CHECK_POINT = 0.00002;
    private static final int CHECK_TIME = 60000;// 1000당 1초
    private GoogleMap mMap;
    int start_flag = 0, load_flag = 0;
    double longitude, latitude;
    LatLng user_pos;
    Button start_button, community_button, option_button,location_button, load_button;
    LocationManager lm;

    List<List<Polyline>> ary = new ArrayList<>();
    List<List<Long>> timeline = new ArrayList<>();
    List<Polyline> cur_ary = new ArrayList<>();

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userName = intent.getStringExtra("userName");

        start_button = findViewById(R.id.start);
        location_button = findViewById(R.id.location);
        load_button = findViewById(R.id.load);
        community_button = findViewById(R.id.cummunity);
        option_button = findViewById(R.id.option);

        // 산책시작버튼
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //산책시작
                if (start_flag == 0) {
                    if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= 23 &&
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                    } else {

                        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        }
                        //권한획득시
                        else {
                            mMap.setMyLocationEnabled(true);
                            start_flag = 1;
                            start_button.setText("산책종료");
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, gpsLocationListener);
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location == null) {
                                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, gpsLocationListener);
                                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location == null) {
                                    Toast.makeText(getApplicationContext(), "프로바이더 오류", Toast.LENGTH_SHORT).show();
                                    Log.v(TAG, "프로바이더 오류");
                                    return;
                                }
                                else{
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                    user_pos = new LatLng(latitude, longitude);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
                                }
                            } else {
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                user_pos = new LatLng(latitude, longitude);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
                            }
                        }
                    }
                }
                //산책종료
                else if (start_flag == 1) {
                    start_flag = 2;
                    start_button.setText("경로 제거");
                    lm.removeUpdates(gpsLocationListener);
                    lm.removeUpdates(netLocationListener);
                }
                //산책한 경로 제거
                else if (start_flag == 2) {
                    start_flag = 0;
                    start_button.setText("산책 시작");
                    for (int i = cur_ary.size() - 1; i > -1; i--) {
                        cur_ary.get(i).remove();
                        cur_ary.remove(i);
                    }
                }
            }
        });

        // 유저좌표이동버튼
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            //권한체크
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Maps.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
                //권한획득시
                else {
                    mMap.setMyLocationEnabled(true);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, SingleListener);
                    lm.removeUpdates(SingleListener);

                    if (location == null) {
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, SingleListener);
                        lm.removeUpdates(SingleListener);

                        if (location == null) {
                            Toast.makeText(getApplicationContext(), "프로바이더 오류", Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "프로바이더 오류");
                            return;
                        } else {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            user_pos = new LatLng(latitude, longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
                        }
                    } else {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        user_pos = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
                    }
                }
            }
        });
        //이전산책경로가져오기
        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileRead();
            }
        });
        //하단바 커뮤니티로 이동
        community_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"2"+getIntent());Intent intent = new Intent(Maps.this, Community.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
        //하단바 설정창으로 이동
        option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_option);
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new option(), null).commit();
            }
        });
    }
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //단일 리스너
    final LocationListener SingleListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            user_pos = new LatLng(latitude, longitude);
        }
    };

    //gps리스너
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LatLng tmp_user_pos = user_pos;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            user_pos = new LatLng(latitude, longitude);
            PolylineOptions polylineOptions = new PolylineOptions().add(tmp_user_pos).add(user_pos);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polyline.setWidth(20f);
            polyline.setEndCap(new RoundCap());
            polyline.setStartCap(new RoundCap());
            cur_ary.add(polyline);
            FileWrite(tmp_user_pos, user_pos, location.getTime());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    //net리스너
    final LocationListener netLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LatLng tmp_user_pos = user_pos;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            user_pos = new LatLng(latitude, longitude);
            PolylineOptions polylineOptions = new PolylineOptions().add(tmp_user_pos).add(user_pos);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polyline.setWidth(20f);
            polyline.setEndCap(new RoundCap());
            polyline.setStartCap(new RoundCap());
            cur_ary.add(polyline);
            FileWrite(tmp_user_pos, user_pos, location.getTime());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    //지도시작
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37, 128), 18));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else {
            mMap.setMyLocationEnabled(true);
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, SingleListener);
            lm.removeUpdates(SingleListener);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, SingleListener);
                lm.removeUpdates(SingleListener);
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    user_pos = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37, 128), 18));
                }
            } else {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                user_pos = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
            }
        }
    }
    // 경로 저장
    void FileWrite(LatLng tmp_location,LatLng location,Long time) {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {}
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            String str = tmp_location+"->"+location+"time "+time;
            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loc_data"); // 저장 경로
            if (!saveFile.exists()) { // 폴더 없을 경우
                saveFile.mkdir(); // 폴더 생성
            }
            try {
                long now = System.currentTimeMillis(); // 현재시간 받아오기
                Date date = new Date(now); // Date 객체 생성
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String nowTime = sdf.format(date);

                BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile +"/"+nowTime+".txt", true));
                buf.append(str); // 파일 쓰기
                buf.newLine(); // 개행
                buf.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 저장경로 읽기
    void FileRead(){
        //저장경로 표시
        if(load_flag==0) {
            load_flag=1;
            Toast.makeText(this, search_day+"일전까지의 경로 표시", Toast.LENGTH_SHORT).show();
            String line; // 한줄씩 읽기
            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loc_data"); // 저장 경로
// 폴더 생성
            if (!saveFile.exists()) { // 폴더 없을 경우
                saveFile.mkdir(); // 폴더 생성
            }
            //파일검색, 경로표시
            for (int i = 0; i < search_day + 1; i++) {
                if(ary.size()<=i) {
                    ary.add(new ArrayList<>());
                    timeline.add(new ArrayList<>());
                }
                try {
                    long now = System.currentTimeMillis(); // 현재시간 받아오기
                    Date date = new Date(now); // Date 객체 생성
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String nowTime = sdf.format(date);
                    String y = nowTime.substring(0, 4);
                    String m = nowTime.substring(4, 6);
                    String d = nowTime.substring(6, 8);
                    d = Integer.toString(Integer.parseInt(d) - i);
                    if (Integer.parseInt(d) < 1) {//0일이하일때
                        switch (Integer.parseInt(nowTime.substring(4, 6))) {
                            case 1:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "12";
                                y = Integer.toString(Integer.parseInt(y) - 1);
                                break;
                            case 2:
                                d = Integer.toString(28 + Integer.parseInt(d));
                                m = "01";
                                break;
                            case 3:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "02";
                                break;
                            case 4:
                                d = Integer.toString(30 + Integer.parseInt(d));
                                m = "03";
                                break;
                            case 5:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "04";
                                break;
                            case 6:
                                d = Integer.toString(30 + Integer.parseInt(d));
                                m = "05";
                                break;
                            case 7:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "06";
                                break;
                            case 8:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "07";
                                break;
                            case 9:
                                d = Integer.toString(30 + Integer.parseInt(d));
                                m = "08";
                                break;
                            case 10:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "09";
                                break;
                            case 11:
                                d = Integer.toString(30 + Integer.parseInt(d));
                                m = "10";
                                break;
                            case 12:
                                d = Integer.toString(31 + Integer.parseInt(d));
                                m = "11";
                                break;
                        }
                    }
                    if(Integer.parseInt(d) < 10){
                        d="0"+d;
                    }
                    String searchTime = y + m + d;
                    int ia=0;
                    BufferedReader buf = new BufferedReader(new FileReader(saveFile + "/" + searchTime + ".txt"));
                    while ((line = buf.readLine()) != null) {
                        ia++;
                        String[] Loc = line.split("->");
                        String[] timeary = Loc[1].split("time ");
                        //Loc[0] start  Loc[1] end
                        double lat = Double.parseDouble(Loc[0].substring(Loc[0].indexOf("(") + 1, Loc[0].indexOf(",")));
                        double lng = Double.parseDouble(Loc[0].substring(Loc[0].indexOf(",") + 1, Loc[0].indexOf(")")));

                        LatLng start_loc = new LatLng(lat, lng);
                        lat = Double.parseDouble(Loc[1].substring(Loc[1].indexOf("(") + 1, Loc[1].indexOf(",")));
                        lng = Double.parseDouble(Loc[1].substring(Loc[1].indexOf(",") + 1, Loc[1].indexOf(")")));
                        LatLng end_loc = new LatLng(lat, lng);

                        //동적배열생성
                        PolylineOptions polylineOptions = new PolylineOptions().add(start_loc).add(end_loc);
                        Polyline polyline = mMap.addPolyline(polylineOptions);
                        polyline.setWidth(20f);

                        polyline.setColor(polyColor(polyCompare(polyline,Long.parseLong(timeary[1]))));
                        polyline.setStartCap(new RoundCap());polyline.setEndCap(new RoundCap());
                        ary.get(i).add(polyline);//ary.array.add(polyline); 2중 동적생성

                        if(timeary[1]!=null)
                            timeline.get(i).add(Long.parseLong(timeary[1]));//폴리라인 시간 생성
                    }
                    buf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        //저장경로 삭제
        else{
            load_flag=0;
            for(int j=ary.size()-1;j>-1;j--) {
                for (int i =ary.get(j).size()-1; i > -1; i--) {
                    ary.get(j).get(i).remove();
                    ary.get(j).remove(i);
                    timeline.get(j).remove(i);
                }
                ary.remove(j);
                timeline.remove(j);
            }
        }
    }
    //폴리라인 색상결정
    int polyColor(int count){
        switch(count){
            case 0: return 0xffFFCCE5;
            case 1: return 0xffFF99CC;
            case 2: return 0xffFF66B2;
            case 3: return 0xffFF3399;
            case 4: return 0xffFF007F;
            case 5: return 0xffCC0066;
            case 6: return 0xff99004C;
            case 7: return 0xff660033;
            case 8: return 0xff330019;
            default:return 0xff330020;
        }
    }
    //겹치는 경로 개수결정
    int polyCompare(Polyline poly,Long time){
        int count=0;
        ArrayList<Long> time_ary=new ArrayList<>();
        time_ary.add(time);
        int time_flag=0;
        for(int j=0;j<ary.size();j++) {
            for (int i = 0; i < ary.get(j).size(); i++) {//StartPoint or EndPoint가 겹칠때
                if (location_equal(ary.get(j).get(i).getPoints().get(0), poly.getPoints().get(0)) ||
                        location_equal(ary.get(j).get(i).getPoints().get(0), poly.getPoints().get(1)) ||
                        location_equal(ary.get(j).get(i).getPoints().get(1), poly.getPoints().get(0)) ||
                        location_equal(ary.get(j).get(i).getPoints().get(1), poly.getPoints().get(1))) {
                    for (int k = 0; k < time_ary.size(); k++) {
                        if (timeline.get(j).get(i) < time_ary.get(k) + CHECK_TIME &&
                                timeline.get(j).get(i) > time_ary.get(k) - CHECK_TIME) {
                            if(ary.get(j).get(i).getColor()>polyColor(count)) {
                                ary.get(j).get(i).setColor(polyColor(count));
                                ary.get(j).get(i).setZIndex(count);
                            }
                            time_flag = 1;
                        }
                    }
                    if (time_flag == 1) {
                        time_flag = 0;
                        continue;
                    }
                    count++;
                    time_ary.add(timeline.get(j).get(i));
                    ary.get(j).get(i).setColor(polyColor(count));
                    poly.setZIndex(count);
                }
            }
        }
        return count;
    }
    //location 정보 같은지 확인
    boolean location_equal(LatLng point1,LatLng point2){
        if(point1.longitude > point2.longitude- CHECK_POINT && point1.longitude < point2.longitude+ CHECK_POINT){
            if(point1.latitude > point2.latitude- CHECK_POINT && point1.latitude < point2.latitude+ CHECK_POINT){
                return true;
            }
        }
        return false;
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
                ActivityCompat.finishAffinity(Maps.this);
                System.exit(0);
            }
        });
        builder.create().show();
    }
}