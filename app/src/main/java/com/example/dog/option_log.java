package com.example.dog;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class option_log extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.log, rootKey);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
    //기능 구현
}