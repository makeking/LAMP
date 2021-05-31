package com.bete.lamp.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyYValueFormatter extends ValueFormatter {
    private DecimalFormat mFormat;

    public MyYValueFormatter() {
        mFormat = new DecimalFormat("###");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + " %";
    }
}