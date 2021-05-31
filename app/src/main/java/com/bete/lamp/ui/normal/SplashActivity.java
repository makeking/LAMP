package com.bete.lamp.ui.normal;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.bete.lamp.AppApplication;
import com.bete.lamp.R;
import com.bete.lamp.db.UserPermission;
import com.bete.lamp.thread.LampLiuChengThread;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.thread.PrintSerial;
import com.bete.lamp.thread.ScanSerial;
import com.bete.lamp.titleObserver.TitleData;
import com.leon.lfilepickerlibrary.utils.NavigationBarUtil;
import com.myutils.Canstant;
import com.myutils.SharedPreferencesUtils;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;
import com.utils.ShellUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

import static com.bete.lamp.AppApplication.lang;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.g_daohangjianEnable;
import static com.myutils.GlobalDate.g_screenOrientationEnable;
import static java.lang.Thread.sleep;

public class SplashActivity extends BaseActivity{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;
        if (!isTaskRoot()) {
//            final Intent intent = getIntent();
//            final String intentAction = intent.getAction();
//            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent
//                    .ACTION_MAIN)) {
                finish();
                return;
//            }
        }
        g_daohangjianEnable = SharedPreferencesUtils.getDaohangjianAbleState();
        if(DEVICE) {
            if (g_daohangjianEnable) {
                NavigationBarUtil.mt8735HideNavigationBar(context);
                NavigationBarUtil.showNavigationBar(getWindow());
                ShellUtils.execCmd("wm overscan 0,0,0,0", true);
            } else {
                NavigationBarUtil.mt8735ShowNavigationBar(context);
                NavigationBarUtil.hideNavigationBar(getWindow());
                if (g_screenOrientationEnable) {
                    ShellUtils.execCmd("wm overscan 0,-80,0,-40", true);
                } else {
                    ShellUtils.execCmd("wm overscan 0,-40,0,-80", true);
                }
            }
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);//,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            }
        }.start();
    }


//    @SuppressLint("NewApi")
//    private boolean isIgnoringBatteryOptimizations() {
//        boolean isIgnoring = false;
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        if (powerManager != null) {
//            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
//        }
//        return isIgnoring;
//    }
//
//    @SuppressLint("NewApi")
//    public void requestIgnoreBatteryOptimizations() {
//        try {
//            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//        if (requestCode == 1) {
//            if (!isIgnoringBatteryOptimizations()) {
//                requestIgnoreBatteryOptimizations();
//            }
//        }
//    }

    public void permissionSuccess(int requestCode) {
        switch (requestCode) {
            case 0x0002:
                LogUtils.d("获取权限成功=" + requestCode);
                LitePal.initialize(AppApplication.getAppContext());
                LitePal.getDatabase();
                List<UserPermission> users = LitePal.where("name like ?", "admin").find(UserPermission.class);
                if (users.size() == 0) {
                    UserPermission user = new UserPermission();
                    user.setName("admin");
                    user.setMima("000000");
                    user.setLevel(1);
                    user.save();
                }

                users = LitePal.where("name like ?", "mainte").find(UserPermission.class);
                if (users.size() == 0) {
                    UserPermission mainte = new UserPermission();
                    mainte.setName("mainte");
                    mainte.setMima("000000");
                    mainte.setLevel(0);
                    mainte.save();
                }

                String dataDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR;
                FileCommonUtil.createOrExistsDir(dataDir);
                String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DOWNLOAD_DIR;
                FileCommonUtil.createOrExistsDir(downloadDir);
                String hexDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_HEX_DIR;
                FileCommonUtil.createOrExistsDir(hexDir);
                String manitenanceDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_MANITENANCE_DIR;
                FileCommonUtil.createOrExistsDir(manitenanceDir);
                String tempDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_TEMP_DIR;
                FileCommonUtil.createOrExistsDir(tempDir);
                String crashDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_CRASH_DIR;
                FileCommonUtil.createOrExistsDir(crashDir);
                String peizhiDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PEIZHI_DIR;
                FileCommonUtil.createOrExistsDir(peizhiDir);
                String liuchengDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_LIUCHENG_DIR;
                FileCommonUtil.createOrExistsDir(liuchengDir);
                String projectDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR;
                FileCommonUtil.createOrExistsDir(projectDir);
                String eepromDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_EEPROM_DIR;
                FileCommonUtil.createOrExistsDir(eepromDir);
                String qcDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR;
                FileCommonUtil.createOrExistsDir(qcDir);
                String helpDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_HELP_DIR;
                FileCommonUtil.createOrExistsDir(helpDir);
                TitleData.getInstance();
                AppApplication.onlyLampLiuChengThread = new LampLiuChengThread();
                AppApplication.onlyLampLiuChengThread.start();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                determine();//判断是否第一次登录，返回true,进入主页面,返回false,进入登录页面
                break;
        }
    }

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    private int REQUEST_CODE_PERMISSION = 0x00099;

    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }


    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tishi)
                .setMessage(R.string.dangqianyinyongqueshaobiyaoquanxian)
                .setNegativeButton(R.string.quxiao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitAPP();
                    }
                })
                .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        manager.restartPackage(getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    public void permissionFail(int requestCode) {
        LogUtils.d("获取权限失败=" + requestCode);
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 判断是否第一次登录
     */
    private void determine() {
        if(lang.equals("zh")){
//            if (DEVICE)
                startActivity(new Intent(context, SelfCheckActivity.class));
//            else
//                startActivity(new Intent(context, MainActivity.class));
            finish();
        }
        else
        {
//            if (DEVICE)
                startActivity(new Intent(context, SelfCheckActivity.class));
//            else
//                startActivity(new Intent(context, MainActivity.class));
            finish();
        }
    }
}
