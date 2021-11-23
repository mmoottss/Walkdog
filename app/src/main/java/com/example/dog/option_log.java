package com.example.dog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.net.URISyntaxException;

public class option_log extends PreferenceFragmentCompat {
    SharedPreferences prefs, data;
    EditTextPreference new_name, now_pw;
    String username, password;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.log, rootKey);

        data = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        password = data.getString("userPassword", "");
        username = data.getString("userName", "");

        new_name = (EditTextPreference) findPreference("change_name");
        now_pw = (EditTextPreference) findPreference("change_pw");

        new_name.setText(username);

        //현재 닉네임 확인
        //자동로그인시 Not set 처리되는 부분 수정 필요.
        if (new_name != null) {
            new_name.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = new_name.getText();
                    if (TextUtils.isEmpty(text)) {
                        return "Not set";
                    }
                    return "현재 닉네임: " + text;
                }
            });
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    //기능 구현
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //버튼 클릭 시 설정변화 추가
            if(key == "change_name"){
                new_name.getText();
                //DB에서 닉네임 끌어와 변경하기
            }
            if(key == "change_pw"){
                //위와 동일. 현재 비밀번호(확인용), 새로운 비밀번호 받아 변경하기(필요하다면 새로운 xml파일 생성)
                if(now_pw.getText().toString().equals(password)){
                    //새로운 비밀번호를 받는 edittextpref 생성(수정 필요)
                    PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getContext());
                    PreferenceCategory userCategory = new PreferenceCategory(getContext());
                    root.addPreference(userCategory);
                    EditTextPreference new_pw = new EditTextPreference(getContext());
                    new_pw.setKey("newpassword");
                    new_pw.setDialogTitle("새로운 비밀번호");
                    userCategory.addPreference(new_pw);
                    setPreferenceScreen(root);

                    //생성된 pref에 텍스트 받아 비밀번호 변경...
                    new_pw.getText();

                }else{
                    Toast.makeText(getContext(), "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);
    }
}