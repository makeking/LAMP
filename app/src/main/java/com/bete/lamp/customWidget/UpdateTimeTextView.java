package com.bete.lamp.customWidget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateTimeTextView extends AppCompatTextView {

    private String TAG = "UpdateTimeTextView";
    private Thread runnable;
    private boolean mBoolean = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UpdateTimeTextView.this.setText((String) msg.obj);
        }
    };
    private String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss ";

    public UpdateTimeTextView(Context context) {
        super(context);
    }

    public UpdateTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();//加载方法
    }

    public UpdateTimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBoolean = false;
    }

    /**
     * 更新时间
     */
    private void init() {
        runnable = new Thread() {
            @Override
            public void run() {
                while (mBoolean) {
                    Calendar mCalendar = Calendar.getInstance();
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
                    String time = dateFormatter.format(Calendar.getInstance().getTime());//获取当前时间
                    String mWay = String.valueOf(mCalendar.get(Calendar.DAY_OF_WEEK));//获取星期
                    if ("1".equals(mWay)) {
                        mWay = "天";
                    } else if ("2".equals(mWay)) {
                        mWay = "一";
                    } else if ("3".equals(mWay)) {
                        mWay = "二";
                    } else if ("4".equals(mWay)) {
                        mWay = "三";
                    } else if ("5".equals(mWay)) {
                        mWay = "四";
                    } else if ("6".equals(mWay)) {
                        mWay = "五";
                    } else if ("7".equals(mWay)) {
                        mWay = "六";
                    }
                    //String tiems = time + "星期" + mWay;
                    String tiems = time;
                    LogUtils.e(TAG, "run: " + time + "1231231");
                    handler.sendMessage(handler.obtainMessage(100, tiems));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        runnable.start();
    }
}
