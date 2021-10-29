package com.example.dog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Community extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment1 fragment1;
    Fragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        /*getSupportFragmentManager().beginTransaction().
                replace(R.id.main_layout,fragment1).commitAllowingStateLoss();*/

        /*BottomNavigationView bottimNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, fragment1).commit();
                        return true;

                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, fragment2).commit();
                        return true;
                    }

                    return false;
                }
            });*/
        }
    }