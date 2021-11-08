package com.example.dog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class subcommunity extends LinearLayout {
    public subcommunity(Context context, AttributeSet attrs, SampleItem sampleitem) {
        super(context, attrs);
        init(context, sampleitem);
    }
    public subcommunity(Context context, SampleItem sampleItem) {
        super(context);
        init(context, sampleItem);
    }
    private void init(Context context, SampleItem sampleItem){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_community,this,true);

        TextView name = (TextView) findViewById(R.id.cont_name);
        TextView title = (TextView) findViewById(R.id.cont_title);
        TextView content = (TextView) findViewById(R.id.cont_text);
        ImageView image = (ImageView) findViewById(R.id.cont_image);

        //받은 값 적용
        name.setText(sampleItem.getName());
        title.setText(sampleItem.getTitle());
        content.setText(sampleItem.getContent());

        //사진 띄우기
        Glide.with(this).load(sampleItem.getImage().toString()).override(300,300).centerCrop().into(image);
    }
}
