package com.example.dog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class option_act extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    FragmentManager fragmentManager;
    Button community_button, map_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        fragmentManager = getSupportFragmentManager();
        community_button = findViewById(R.id.community);
        map_button = findViewById(R.id.mapmenu);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userName = intent.getStringExtra("userName");

        fragmentManager.beginTransaction()
                .replace(R.id.show_option, new option(), "main_option")
                .commit();

        //하단바 커뮤니티로 이동
        community_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option_act.this, Community.class);
                startActivity(intent);
            }
        });

        //하단바 커뮤니티로 이동
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);
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
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        //가능하다면 activity_profile로 진입하게 수정.
        final Bundle args = pref.getExtras();
            final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                    getClassLoader(),
                    pref.getFragment());
            fragment.setArguments(args);
            fragment.setTargetFragment(caller, 0);
            // Replace the existing Fragment with the new Fragment

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.show_option, fragment)
                    .addToBackStack(null)
                    .commit();
        return true;
    }
}