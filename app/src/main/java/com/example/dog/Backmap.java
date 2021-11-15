package com.example.dog;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Backmap extends Service {
    private static final int TIME = 2000;
    private static final int DISTANCE = 4;
    double longitude, latitude;
    LatLng user_pos;
    ArrayList<LocationList> loctionary = new ArrayList<>();
    public Backmap() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(Backmap.this, Maps.class);
        intent.putExtra("Location", loctionary);
        Log.v("Backmap","servicegoeeeeee");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");
        }
        return super.onStartCommand(intent, flags, startId);
        */
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, LocationListener);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, LocationListener);
        }
        return flags;
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

    final android.location.LocationListener LocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.v("Backmap","servicego444444");
            LatLng tmploc = user_pos;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            user_pos = new LatLng(latitude, longitude);
            LocationList loc = new LocationList(tmploc,user_pos,location.getTime());
            loctionary.add(loc);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
}
class LocationList implements Serializable {
    LatLng tmplocation;
    LatLng curlocation;
    long time;
    LocationList(LatLng tmp, LatLng cur, long t){
        tmplocation=tmp;
        curlocation=cur;
        time=t;
    }
}

