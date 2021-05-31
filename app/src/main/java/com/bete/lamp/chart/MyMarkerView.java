package com.bete.lamp.chart;

import android.content.Context;
import android.widget.TextView;

import com.bete.lamp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent= (TextView) findViewById(R.id.tvContent);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
            tvContent.setText("X:"+e.getX()+"\r\nY:"+e.getY()+"X:"+getWidth()+"\r\nY:"+getHeight());
        super.refreshContent(e, highlight);
    }
    @Override
    public MPPointF getOffset() {
        return new MPPointF(0, -getHeight());//-(getWidth() / 2)
    }
}
