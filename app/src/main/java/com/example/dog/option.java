package com.example.dog;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class option extends PreferenceFragmentCompat {

    SharedPreferences prefs;

    PreferenceScreen logscreen, alarmscreen;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.option);
        logscreen = (PreferenceScreen) findPreference("Log");
        alarmscreen = (PreferenceScreen) findPreference("alarm");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());



        prefs.registerOnSharedPreferenceChangeListener(prefListener);


    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    };
}