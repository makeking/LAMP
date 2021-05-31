package com.bete.lamp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.bete.lamp.ui.normal.SplashActivity;
import com.utils.LogUtils;

import static com.myutils.GlobalDate.clearDefaultLauncher;
import static com.myutils.GlobalDate.g_bootEnable;
import static com.myutils.GlobalDate.g_homeEnable;
import static com.myutils.GlobalDate.setDefaultLauncher;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //Thread.sleep(10L);
            if (g_bootEnable) {
//                intent = new Intent(context, SplashActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                if (g_homeEnable) {
                    clearDefaultLauncher(context);
                    String defPackageName ="com.bete.lamp";
                    String defClassName = "com.bete.lamp.ui.normal.SplashActivity";
                    setDefaultLauncher(context,defPackageName,defClassName);
                } else {
                    clearDefaultLauncher(context);
                    String defPackageName = "com.android.launcher3";
                    String defClassName = "com.android.launcher3.Launcher";
                    setDefaultLauncher(context,defPackageName,defClassName);
                }
                return;
            }
        }
    }
}
