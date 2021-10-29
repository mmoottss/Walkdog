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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button button1,button2,button3;
    TextView txtResult;
    double longitude,latitude;
    LatLng userpos,firstpos;
    int startflag=0;
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


        firstpos = new LatLng(37, 128);
        button1 = (Button) findViewById(R.id.start);
        txtResult = (TextView) findViewById(R.id.x);

        // 시작버튼, 본인좌표로 이동
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (startflag == 0) {
                    startflag=1;
                    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Maps.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else {
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                        longitude = Math.round(location.getLongitude()*100000)/100000.0;
                        latitude = Math.round(location.getLatitude()*100000)/100000.0;
                        txtResult.setText("위도 : " + longitude + "\n" + "경도 : " + latitude + "\n");
                        userpos = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
                    }
                }
                else {
                    startflag=0;
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                        public void onLocationChanged(@NonNull Location location) {

                            longitude = Math.round(location.getLongitude()*100000)/100000.0;
                            latitude = Math.round(location.getLatitude()*100000)/100000.0;
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
                        }
                    });
                }
            }
        });
        button2=(Button)findViewById(R.id.cummunity);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, Community.class);
                startActivity(intent);
            }
        });
        button3=(Button)findViewById(R.id.location);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
            }
        });
    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LatLng tmpuserpos = userpos;
            longitude = Math.round(location.getLongitude()*100000)/100000.0;
            latitude = Math.round(location.getLatitude()*100000)/100000.0;
            txtResult.setText("위도 : " + longitude + "\n" +"경도 : " + latitude + "\n");
            userpos = new LatLng(latitude, longitude);
            PolylineOptions polylineOptions = new PolylineOptions().add(tmpuserpos).add(userpos);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polyline.setWidth(20f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    public void onMapReady(final GoogleMap googleMap) {
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstpos,18));
        }
        else {

            longitude = Math.round(location.getLongitude()*100000)/100000.0;
            latitude = Math.round(location.getLatitude()*100000)/100000.0;
            userpos = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userpos,18));
        }
    }
}