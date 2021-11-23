package com.example.dog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class option_act extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    FragmentManager fragmentManager;
    Button community_button, map_button;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String userID, userPassword, userName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        fragmentManager = getSupportFragmentManager();
        community_button = findViewById(R.id.community);
        map_button = findViewById(R.id.mapmenu);

        prefs = getSharedPreferences("data", MODE_PRIVATE);
        editor = prefs.edit();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPassword = intent.getStringExtra("userPassword");
        userName = intent.getStringExtra("userName");

        //sharedpreference에 유저 정보 저장
        editor.putString("userPassword", userPassword);
        editor.putString("userName", userName);
        editor.putString("userID", userID);
        editor.commit();

        fragmentManager.beginTransaction()
            .add(R.id.show_option, new option())
            .commit();

        //하단바 커뮤니티로 이동
        community_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Community.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        //하단바 맵으로 이동
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        if(fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }else {
            fragmentManager.popBackStack();
        }

        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, @NonNull Preference pref) {
        final Bundle args = pref.getExtras();
            final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                    getClassLoader(),
                    pref.getFragment());
            fragment.setArguments(args);
            fragment.setTargetFragment(caller, 0);

        // Replace the existing Fragment with the new Fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.show_option, fragment)
                    .addToBackStack(null)
                    .commit();
        return true;
    }
}