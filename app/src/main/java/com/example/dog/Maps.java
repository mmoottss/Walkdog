package com.example.dog;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dog.databinding.ActivityMapsBinding;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button button1,button2;
    TextView txtResult;
    double longitude,latitude;
    LatLng userpos,firstpos;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        firstpos = new LatLng(37, 128);
        button1 = (Button) findViewById(R.id.option);
        txtResult = (TextView) findViewById(R.id.x);

        // 설정버튼, 본인좌표로 이동
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Maps.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, gpsLocationListener);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    txtResult.setText("위도 : " + longitude + "\n" +"경도 : " + latitude + "\n");
                    userpos = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
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
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LatLng tmpuserpos = userpos;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            txtResult.setText("위도 : " + longitude + "\n" +"경도 : " + latitude + "\n");
            userpos = new LatLng(latitude, longitude);
            PolylineOptions polylineOptions = new PolylineOptions().add(tmpuserpos).add(userpos);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polyline.setWidth(20f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userpos));
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
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstpos,16));
    }
}