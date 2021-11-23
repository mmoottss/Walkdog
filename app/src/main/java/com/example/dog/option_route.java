package com.example.dog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class option_route extends PreferenceFragmentCompat {
    SharedPreferences prefs, data;
    SharedPreferences.Editor editor;
    ListPreference route_accu;
    AlertDialog.Builder ask;
    boolean dialogResult;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.route, rootKey);

        ask = new AlertDialog.Builder(getActivity());

        data = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = data.edit();

        route_accu = (ListPreference) findPreference("route_accu");
        //현재 값 summary 설정
        if (route_accu != null) {
            route_accu.setSummaryProvider(new Preference.SummaryProvider<ListPreference>() {
                @Override
                public CharSequence provideSummary(ListPreference preference) {
                    String text = route_accu.getValue();
                    if (TextUtils.isEmpty(text)) {
                        return "Not set";
                    }
                    return "현재 적용된 누적일: " + text + "일";
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
            if(key == "route_accu"){
                //Maps에 설정한 값 보내기
                //Intent intent = new Intent(getContext(), Maps.class);
                //intent.putExtra("accu", route_accu.getValue());

                //sharedpreference에 값 저장하는 방법(Maps에 sharedpreference 선언 필요)
                editor.putString("route", route_accu.getValue());
            }
            if(key == "route_reset"){
                //경로를 초기화하시겠습니까? 메시지 띄우기
                //예 누르면 산책 시작 버튼 누르고부터의 값 삭제
                ask.setMessage("정말로 초기화하시겠습니까? 초기화하면 산책 시작 후 현재까지 산책한 경로를 삭제합니다.").setCancelable(false);
                ask.setView(getView());
                ask.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //산책 시작 버튼 누르고부터의 값 삭제
                    }
                });

                ask.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialogResult = false;
                    }
                });
            }
            if(key == "route_delete"){
                //저장된 경로값 모두 삭제.
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
