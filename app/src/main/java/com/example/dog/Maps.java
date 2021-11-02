package com.example.dog;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

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
    private static final double matrix = 10000;
    private static final int time=100;
    private static final int distance = 3;
    private GoogleMap mMap;

    int start_flag = 0, load_flag = 0, search_day = 0, debug = 0;
    double longitude, latitude;
    LatLng user_pos, first_pos;

    Button start_button, community_button, location_button, load_button, option_button;
    TextView txv;

    List<Polyline> array = new ArrayList<>();
    List<Polyline> cur_ary = new ArrayList<>();
    Cap cap;
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        cap= new RoundCap();

        first_pos = new LatLng(37, 128);
        start_button = findViewById(R.id.start);
        location_button = findViewById(R.id.location);
        load_button = findViewById(R.id.load);
        community_button = findViewById(R.id.cummunity);
        option_button = findViewById(R.id.option);

        txv = findViewById(R.id.debug);
        // 산책시작버튼
        start_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                //산책시작
                if (start_flag == 0) {
                    start_flag = 1;
                    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    }
                    //권한획득시
                    else {
                        start_button.setText("산책종료");

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, gpsLocationListener);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location==null){
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance, gpsLocationListener);
                            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location == null){
                                Toast.makeText(getApplicationContext(),"프로바이더 오류", Toast.LENGTH_SHORT).show();
                                Log.v(TAG,"프로바이더 오류");
                                return ;
                            }
                        }
                        longitude = Math.round(location.getLongitude() * matrix) / matrix;
                        latitude = Math.round(location.getLatitude() * matrix) / matrix;
                        user_pos = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
                    }
                }
                //산책종료
                else if (start_flag == 1) {
                    start_flag = 2;
                    start_button.setText("산책 경로 제거");
                    lm.removeUpdates(gpsLocationListener);
                    lm.removeUpdates(netLocationListener);
                }
                //산책한 경로 제거
                else if (start_flag == 2) {
                    start_flag = 0;
                    start_button.setText("산책 시작");
                    for(int i =cur_ary.size()-1;i>-1;i--) {
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
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, SingleListener);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location==null) {
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance, SingleListener);
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location == null){
                            Toast.makeText(getApplicationContext(),"프로바이더 오류", Toast.LENGTH_SHORT).show();
                            Log.v(TAG,"프로바이더 오류");
                            return ;
                        }
                    }
                    Toast.makeText(getApplicationContext(),""+location.getTime(), Toast.LENGTH_SHORT).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
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
                Intent intent = new Intent(Maps.this, Community.class);
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
    //단일 리스너
    final LocationListener SingleListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = Math.round(location.getLongitude() * matrix) / matrix;
            latitude = Math.round(location.getLatitude() * matrix) / matrix;
            user_pos = new LatLng(latitude, longitude);
        }
    };

    //gps리스너
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
                LatLng tmp_user_pos = user_pos;
                longitude = Math.round(location.getLongitude() * matrix) / matrix;
                latitude = Math.round(location.getLatitude() * matrix) / matrix;
                user_pos = new LatLng(latitude, longitude);
                PolylineOptions polylineOptions = new PolylineOptions().add(tmp_user_pos).add(user_pos);
                Polyline polyline = mMap.addPolyline(polylineOptions);
                polyline.setWidth(20f);
                cur_ary.add(polyline);
                FileWrite(tmp_user_pos, user_pos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
                //디버그
                txv.setText("" + user_pos + debug++);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    //net리스너
    final LocationListener netLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
                LatLng tmp_user_pos = user_pos;
                longitude = Math.round(location.getLongitude() * matrix) / matrix;
                latitude = Math.round(location.getLatitude() * matrix) / matrix;
                user_pos = new LatLng(latitude, longitude);
                PolylineOptions polylineOptions = new PolylineOptions().add(tmp_user_pos).add(user_pos);
                Polyline polyline = mMap.addPolyline(polylineOptions);
                polyline.setWidth(20f);
                cur_ary.add(polyline);
                FileWrite(tmp_user_pos, user_pos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
                txv.setText(""+user_pos+debug++);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    //지도시작
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;}
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, SingleListener);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        if(location==null){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance, SingleListener);
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null) {
                longitude = Math.round(location.getLongitude() * matrix) / matrix;
                latitude = Math.round(location.getLatitude() * matrix) / matrix;
                user_pos = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos, 18));
            }
            else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first_pos, 18));
            }
        }
        else {

            longitude = Math.round(location.getLongitude()*matrix)/matrix;
            latitude = Math.round(location.getLatitude()*matrix)/matrix;
            user_pos = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos,18));
        }
    }
    // 경로 저장
    void FileWrite(LatLng tmp_location,LatLng location) {

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
            String str = tmp_location+"->"+location;
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
            Toast.makeText(this, "이전 경로 표시", Toast.LENGTH_SHORT).show();
            String line; // 한줄씩 읽기
            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loc_data"); // 저장 경로
// 폴더 생성
            if (!saveFile.exists()) { // 폴더 없을 경우
                saveFile.mkdir(); // 폴더 생성
            }
            //파일검색, 경로표시
            for (int i = 0; i < search_day + 1; i++) {
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
                        polyline.setColor(polyColor(polyCompare(polyline)));
                        polyline.setStartCap(cap);polyline.setEndCap(cap);
                        array.add(polyline);
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
            Toast.makeText(this, "이전 경로 제거", Toast.LENGTH_SHORT).show();
            for(int i =array.size()-1;i>-1;i--) {
                array.get(i).remove();
                array.remove(i);
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
    int polyCompare(Polyline poly){
        int count=0;
        for(int i=0;i<array.size();i++){
            if(array.get(i).getPoints().equals(poly.getPoints())){
                array.get(i).setColor(0x00000000);
                count++;
            }
        }
        return count;
    }
}