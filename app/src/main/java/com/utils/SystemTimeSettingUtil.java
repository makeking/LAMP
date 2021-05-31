package com.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.TimeZone;

public class SystemTimeSettingUtil {

    //判断系统的时区是否是自动获取的
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static public boolean isTimeZoneAuto(Context context){
        try {
            return  android.provider.Settings.Global.getInt(context.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME_ZONE) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //设置系统的时区是否自动获取
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static public void setAutoTimeZone(Context context, int checked){
        android.provider.Settings.Global.putInt(context.getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME_ZONE, checked);
    }

    //判断系统的时间是否自动获取的
    static public boolean isDateTimeAuto(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return Settings.System.getInt(context.getContentResolver(),Settings.System.AUTO_TIME,1) > 0;
            try {
                return Settings.Global.getInt(context.getContentResolver(),Settings.Global.AUTO_TIME) > 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //设置系统的时间是否需要自动获取
    static public void setAutoDateTime(Context context, int checked){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.putInt(context.getContentResolver(), Settings.Global.AUTO_TIME, checked);
//            Settings.Global.putInt(context.getContentResolver(),
//                    Settings.Global.AUTO_TIME, checked);
        }
    }

    //设置系统日期
    static public void setSysDate(Context context,int year,int month,int day){
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        long when = c.getTimeInMillis();

        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    //设置系统时间
    static public void setSysTime(Context context,int hour,int minute,int second){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        long when = c.getTimeInMillis();

        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
            //((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTimeZone();
        }
    }

    //设置系统时区
    static public void setTimeZone(String timeZone){
        final Calendar now = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        now.setTimeZone(tz);
    }

    //获取系统当前的时区
    static public String getDefaultTimeZone(){
        return TimeZone.getDefault().getDisplayName();
    }

}
