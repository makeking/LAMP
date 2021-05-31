package com.bete.lamp.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * created by Carbs.Wang
 */
public class CoverView extends View {

    public CoverView(Context context) {
        super(context);
    }
    public CoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}