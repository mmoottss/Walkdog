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
import com.google.android.gms.maps.model.LatLng;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

    private GoogleMap mMap;
    Button button1,button2,button3, button4;
    double longitude,latitude;
    LatLng user_pos, first_pos;
    int start_flag =0, search_day =1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);

        first_pos = new LatLng(37, 128);
        button1 = findViewById(R.id.start);

        // 시작버튼, 본인좌표로 이동
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (start_flag == 0) {
                    start_flag =1;
                    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Maps.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else {
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                        longitude = Math.round(location.getLongitude()*100000)/100000.0;
                        latitude = Math.round(location.getLatitude()*100000)/100000.0;
                        user_pos = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
                    }
                }
                else {
                    start_flag =0;
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                        public void onLocationChanged(@NonNull Location location) {

                            longitude = Math.round(location.getLongitude()*100000)/100000.0;
                            latitude = Math.round(location.getLatitude()*100000)/100000.0;
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
                        }
                    });
                }
            }
        });
        button2= findViewById(R.id.cummunity);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, Community.class);
                startActivity(intent);
            }
        });
        button3= findViewById(R.id.location);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
            }
        });
        button4= findViewById(R.id.option);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_option);
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new option(), null).commit();
            }
        });
    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if(start_flag ==1) {
                LatLng tmp_user_pos = user_pos;
                longitude = Math.round(location.getLongitude() * 100000) / 100000.0;
                latitude = Math.round(location.getLatitude() * 100000) / 100000.0;
                user_pos = new LatLng(latitude, longitude);
                PolylineOptions polylineOptions = new PolylineOptions().add(tmp_user_pos).add(user_pos);
                Polyline polyline = mMap.addPolyline(polylineOptions);
                polyline.setWidth(20f);
                FileWrite(tmp_user_pos, user_pos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user_pos));
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        if(location==null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first_pos,18));
        }
        else {

            longitude = Math.round(location.getLongitude()*100000)/100000.0;
            latitude = Math.round(location.getLatitude()*100000)/100000.0;
            user_pos = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_pos,18));
        }
    }
    void FileWrite(LatLng tmp_location,LatLng location) {

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {}
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            String str = tmp_location+"-"+location;
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
    void FileRead(){
        List<Poly> array = new ArrayList<>();
        String line; // 한줄씩 읽기
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/loc_data"); // 저장 경로
// 폴더 생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        for(int i = 0; i< search_day; i++) {
            try {
                long now = System.currentTimeMillis(); // 현재시간 받아오기
                Date date = new Date(now); // Date 객체 생성
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String nowTime = sdf.format(date);
                String y = nowTime.substring(0, 4);
                String m = nowTime.substring(4,6);
                String d = nowTime.substring(6,8);
                d = Integer.toString(Integer.parseInt(d) - i);
                if(Integer.parseInt(d)<1){//0일이하일때
                    switch (Integer.parseInt(nowTime.substring(5,7))){
                        case 1: d=Integer.toString(31-Integer.parseInt(d));m="12";y=Integer.toString(Integer.parseInt(y)-1);break;
                        case 2: d=Integer.toString(28-Integer.parseInt(d));m="1";break;
                        case 3: d=Integer.toString(31-Integer.parseInt(d));m="2";break;
                        case 4: d=Integer.toString(30-Integer.parseInt(d));m="3";break;
                        case 5: d=Integer.toString(31-Integer.parseInt(d));m="4";break;
                        case 6: d=Integer.toString(30-Integer.parseInt(d));m="5";break;
                        case 7: d=Integer.toString(31-Integer.parseInt(d));m="6";break;
                        case 8: d=Integer.toString(31-Integer.parseInt(d));m="7";break;
                        case 9: d=Integer.toString(30-Integer.parseInt(d));m="8";break;
                        case 10:d=Integer.toString(31-Integer.parseInt(d));m="9";break;
                        case 11:d=Integer.toString(30-Integer.parseInt(d));m="10";break;
                        case 12:d=Integer.toString(31-Integer.parseInt(d));m="11";break;
                    }
                }
                String searchTime = y + m + d;

                BufferedReader buf = new BufferedReader(new FileReader(saveFile + "/" + searchTime + "a.txt"));
                while ((line = buf.readLine()) != null) {
                    String[] Loc = line.split("-");
                    //Loc[0]tmp  Loc[1] user

                    double lat=Double.parseDouble(Loc[0].substring(Loc[0].indexOf("(")+1,Loc[0].indexOf(",")));
                    double lng=Double.parseDouble(Loc[0].substring(Loc[0].indexOf(",")+1,Loc[0].indexOf(")")));
                    LatLng tmp_loc = new LatLng(lat,lng);
                    lat=Double.parseDouble(Loc[1].substring(Loc[1].indexOf("(")+1,Loc[1].indexOf(",")));
                    lng=Double.parseDouble(Loc[1].substring(Loc[1].indexOf(",")+1,Loc[1].indexOf(")")));
                    LatLng use_loc = new LatLng(lat,lng);
                    Poly po=new Poly(tmp_loc,use_loc,0);

                    //동적배열생성
                    compare(array,po);
                    PolylineOptions polylineOptions = new PolylineOptions().add(tmp_loc).add(use_loc);
                    Polyline polyline = mMap.addPolyline(polylineOptions);
                    polyline.setWidth(20f);
                    polyline.setColor(polyColor(po.count));
                    array.add(po);
                }
                buf.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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
    void compare(List<Poly> array, Poly p2){
        for(int i=0;i<array.size();i++){
            if(array.get(i).comp(p2)){
                p2.count++;
            }
        }
    }
    static class Poly{
        LatLng tmp_loc;
        LatLng use_loc;
        int count;
        Poly(LatLng tmp_loc,LatLng use_loc,int count){
            this.tmp_loc =tmp_loc;
            this.use_loc =use_loc;
            this.count=count;
        }
        boolean comp( Poly p2){
            if(this.use_loc ==p2.use_loc)
                return true;
            return this.tmp_loc == p2.tmp_loc;
        }
    }
}
