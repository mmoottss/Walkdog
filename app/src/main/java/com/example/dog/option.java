package com.example.dog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

//https://android-uni.tistory.com/7 2021.3 그나마 최신버전 Preference 사용법
//https://recipes4dev.tistory.com/58 프래그먼트 사용법(위에서 참고했다고 함)
//preference - https://developer.android.com/guide/topics/ui/settings/organize-your-settings?hl=ko

//https://developer.android.com/guide/topics/ui/settings/organize-your-settings?hl=ko#preferencescreens
// ㄴ> screen 중첩 계층 구조 선언 불가하다는 내용

//https://developer.android.com/guide/topics/ui/settings/customize-your-settings?hl=ko
// ㄴ>환경설정 작업 :: fragment 생성 후 인텐트 설정해보기

//https://developer.android.com/training/basics/fragments/creating?hl=ko
// ㄴ>프래그먼트 만드는 법
//https://devatom.tistory.com/4
// ㄴ>중첩 프래그먼트...

public class option extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.option, rootKey);
//        Intent intent = new Intent(getContext(), fragment.class);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            return false;
        }
    };

//    public boolean onPreferenceTreeClick(Preference preference) {
//        return super.onPreferenceTreeClick(preference);
//    }

    public boolean onPreferenceStartScreen (PreferenceFragmentCompat caller, PreferenceScreen pref){
        caller.setPreferenceScreen(pref);
        return true;
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //버튼 클릭 시 설정변화 추가
            if(key.equals("log")){
                getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            }
                //야간모드 설정
            if (prefs.getBoolean("night_mode", true)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    };
}