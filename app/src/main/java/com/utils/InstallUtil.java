package com.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.bete.lamp.AppApplication;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InstallUtil {

    private static String TAG = "InstallUtil";

    /**
     * 静默安装 并启动
     *
     * @return
     * 0:"安装成功！"
     * 1:"安装失败！"
     * 2:"未知情况！"
     */
    public static boolean silenceInstall(Context context,File file) {
        boolean result = false;
        Process process = null;
        OutputStream out = null;
        Log.i(TAG , "file.getPath()：" + file.getPath());
        if (file.exists()) {
            System.out.println(file.getPath() + "==");
            try {
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                // 获取文件所有权限
                dataOutputStream.writeBytes("chmod 777 " + file.getPath()+ "\n");
                // 进行静默安装命令
                dataOutputStream.writeBytes("pm install -r "+ file.getPath());//LD_LIBRARY_PATH=/vendor/lib:/system/lib
                dataOutputStream.writeBytes("cp /storage/usbotg/LAMP/update/app-debug.apk /system/priv-app/");//LD_LIBRARY_PATH=/vendor/lib:/system/lib
                dataOutputStream.flush();
                // 关闭流操作
                dataOutputStream.close();
                out.close();
                int value = process.waitFor();
                // 代表成功
                if (value == 0) {
                    Log.i(TAG , "安装成功！");
                    result = true;
                    // 失败
                } else if (value == 1) {
                    Log.i(TAG , "安装失败！");
                    result = false;
                    // 未知情况
                } else {
                    Log.i(TAG , "未知情况！");
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!result) {
                Log.i(TAG , "root权限获取失败，将进行普通安装");
                normalInstall(context,file);
                result = true;
            }
        }
        Log.i(TAG , "未知情况！"+ String.valueOf(result));
        return result;
    }

    /**
     * 传统安装
     *
     * @param context
     */
    public static void normalInstall(Context context,File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        startApp(context);
    }

    /**
     * 安装后自启动apk
     *
     * @param context
     */
    private static void startApp(Context context) {
        execRootShellCmd("am start -S  " + context.getPackageName() + "/" + AppApplication.class.getCanonicalName() + " \n");
    }

    /**
     * 执行shell命令
     *
     * @param cmds
     * @return
     */
    private static boolean execRootShellCmd(String... cmds) {
        if (cmds == null || cmds.length == 0) {
            return false;
        }
        DataOutputStream dos = null;
        InputStream dis = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());

            for (int i = 0; i < cmds.length; i++) {
                dos.writeBytes(cmds[i] + " \n");
            }
            dos.writeBytes("exit \n");

            int code = p.waitFor();

            return code == 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (p != null) {
                    p.destroy();
                    p = null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return false;
    }
}
