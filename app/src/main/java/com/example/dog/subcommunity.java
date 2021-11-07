package com.example.dog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class subcommunity extends LinearLayout {
    public subcommunity(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public subcommunity(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_community,this,true);
    }
}
