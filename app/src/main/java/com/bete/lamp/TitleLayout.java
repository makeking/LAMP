package com.bete.lamp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleLayout extends LinearLayout {
    private TextView tv_time;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //LayoutInflater.from(context).inflate(R.layout.title, this);
        initView(context,attrs);
    }

    //初始化视图
    private void initView(final Context context, AttributeSet attributeSet) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.title, this);
        tv_time = (TextView)inflate.findViewById(R.id.title_time);
        init(context,attributeSet);
    }

    //初始化资源文件
    public void init(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TitleLayout);
        String title_left = typedArray.getString(R.styleable.TitleLayout_title_left_text);//
        String title_middle = typedArray.getString(R.styleable.TitleLayout_title_middle_text);//
        String title_right = typedArray.getString(R.styleable.TitleLayout_title_right_text);//

        int title_left_visibility = typedArray.getInt(R.styleable.TitleLayout_title_left_visibility,VISIBLE);//
        int title_middle_visibility = typedArray.getInt(R.styleable.TitleLayout_title_middle_visibility,VISIBLE);//
        int title_right_visibility = typedArray.getInt(R.styleable.TitleLayout_title_right_visibility,VISIBLE);//
        //是否可见
        tv_time.setVisibility(title_left_visibility);
        //赋值
        tv_time.setText(title_left);
    }

    public void setTitleLeftText(String string)
    {
        tv_time.setText(string);
    }

}
