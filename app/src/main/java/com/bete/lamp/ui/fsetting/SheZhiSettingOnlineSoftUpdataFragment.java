package com.bete.lamp.ui.fsetting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.StaticIpConfiguration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.UpdateVersion;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.utils.*;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class SheZhiSettingOnlineSoftUpdataFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingOnlineSoftUpdataFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Context context;
    private List<ApplicationInfo> mListAppcations;
    private PackageManager mPackageManager;
    private PackageInfo packageInfo;
    private String updateStr;
    private String updateVersionStr;
    private ProgressDialog progressDialog;
    private static final String APK_TYPE = "application/vnd.android.package-archive";
    private String VERSIONFILEURL = "";
    private String UPDATAFILEURL = "";
    TextView textView;
    private UpdateVersion updateVersion;
    Button bt_check, bt_updata;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                Activity actvity =   getActivity();
//                if (actvity!=null){
                    Intent intent = actvity.getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                    PendingIntent restartIntent = PendingIntent.getActivity(actvity.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager mgr = (AlarmManager) actvity.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); // 1秒钟后重启应用
                    System.exit(0);
//                }
            }
            if (msg.what == 2) {
                closeProgressDialog();
                LogUtils.d("download versionfile err");
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(getString(R.string.xiazaipeizhiwenjianshibai));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 3) {
                closeProgressDialog();
                LogUtils.d("download apk err");
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(getString(R.string.xiazaishengjiwenjianshibai));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 4) {
                //apk下载完成后，调用系统的安装方法
                LogUtils.d("download versionfile Success");
                try {
                    String input = FileIOUtils.readFile2String(updateVersionStr, "UTF-8");
                    updateVersion = GsonUtils.getInstance().fromJson(input, UpdateVersion.class);
                    LogUtils.d(input);
                } catch (NumberFormatException e) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setMessage(getString(R.string.peizhiwenjianyouwenti));
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                    return;
                }
                LogUtils.d("goOnLineUpdateCheckHandle: " + updateVersion.getFilename());
                UPDATAFILEURL = "http://" + GlobalDate.defaultServerCanShu.getIpaddr() + ":" + GlobalDate.defaultServerCanShu.getPort() + GlobalDate.defaultServerCanShu.getDownloaddir() + updateVersion.getFilename();
                showProgressDialog(getString(R.string.xiazaizhong));
                downloadApkFile(UPDATAFILEURL, updateStr);
            }
            if (msg.what == 5) {
                closeProgressDialog();
                LogUtils.d("download apk Success");
                if ((updateVersion == null) || updateVersion.getFilename().equals("") || (!FileCommonUtil.isFileExists(updateStr))) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setMessage(getString(R.string.shengjiwenjianyouwenti));
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                    return;
                }
                if (updateVersion.getVersionCode() > getCode()) {
                    textView.setText(getString(R.string.jiancedaoxinbanben) + updateVersion.getVersionName());
                    bt_updata.setEnabled(true);
                } else {
                    textView.setText(getString(R.string.dangqianbanbenweizuixinbanben) + String.valueOf(getCode()));
                    bt_updata.setEnabled(false);
                }
            }
        }
    };

    private EthernetManager mEthManager;
    //        private IpConfiguration mIpConfiguration;
    private ConnectivityManager mCM;
    private IpConfiguration.IpAssignment mIpAssignment = IpConfiguration.IpAssignment.DHCP;
    private StaticIpConfiguration mStaticIpConfiguration = null;

    public SheZhiSettingOnlineSoftUpdataFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingOnlineSoftUpdataFragment newInstance(String param1, String param2) {
//        SheZhiSettingOnlineSoftUpdataFragment fragment = new SheZhiSettingOnlineSoftUpdataFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d(TAG, "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
//        onHiddenChanged(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_online_soft_updata, container, false);
        textView = (TextView) view.findViewById(R.id.tv_response);

        bt_check = (Button) view.findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCheck();
            }
        });
        bt_updata = (Button) view.findViewById(R.id.bt_updata);
        bt_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUpdata();
            }
        });
        bt_updata.setEnabled(false);
        textView.setText(getString(R.string.dangqianbanben) + getCode());
        updateVersionStr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DOWNLOAD_DIR + "temp.json";
        updateStr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DOWNLOAD_DIR + "temp.apk";
        VERSIONFILEURL = "http://" + GlobalDate.defaultServerCanShu.getIpaddr() + ":" + GlobalDate.defaultServerCanShu.getPort() + GlobalDate.defaultServerCanShu.getDownloaddir() + "version.json";

        return view;
    }

    public void goCheck() {
        showProgressDialog(getString(R.string.xiazaizhong));
        downloadVersionFile(VERSIONFILEURL, updateVersionStr);
    }

    public void goUpdata() {
        final ShellUtils.CommandResult[] result = {null};
        showProgressDialog(getString(R.string.shengjizhong));
        if (isInstall("com.bete.lamp")) {
            // 判断包名是否相同  再判断版本号，如果本地版本号小于服务器端软件版本号就升级
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    result[0] = ShellUtils.execCmd("mount -o rw,remount /system", true);
//                    LogUtils.d(result[0].toString());
//                    result[0] = ShellUtils.execCmd("cp "+ updateStr +" /system/app/fast/"+updateVersion.getFilename(), true);
//                    LogUtils.d(result[0].toString());
//                    result[0] = ShellUtils.execCmd("mount -o ro,remount /system", true);
//                    LogUtils.d(result[0].toString());
                    result[0] = ShellUtils.execCmd("pm install -r " + updateStr, true);
                    LogUtils.d(result[0].toString());
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (result[0].successMsg == "0") {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    //InstallUtil.normalInstall(context,FileCommonUtil.getFileByPath(updateStr));
//                    PackageManager pm = getPackageManager();
//                    File file = new File(updateStr);
//                    pm.installPackage(Uri.fromFile(file), (IPackageInstallObserver) null,
//                            PackageManager.INSTALL_REPLACE_EXISTING, "com.bete.lamp");

                }
            }).start();
        }
    }

    private void downloadVersionFile(String httpUrl, String filePath) {
        LogUtils.d(httpUrl);
        RequestParams params = new RequestParams(httpUrl);
        params.setSaveFilePath(filePath);
        params.setConnectTimeout(20000);
        params.setReadTimeout(20000);
        LogUtils.d(filePath);
        //自动为文件命名
        params.setAutoRename(false);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                LogUtils.d("onSuccess");
                //apk下载完成后，调用系统的安装方法
                try {
                    String input = FileIOUtils.readFile2String(updateVersionStr, "UTF-8");
                    updateVersion = GsonUtils.getInstance().fromJson(input, UpdateVersion.class);
                    LogUtils.d(input);
                } catch (NumberFormatException e) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setMessage(getString(R.string.peizhiwenjianyouwenti));
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                    return;
                }
                LogUtils.d("goOnLineUpdateCheckHandle: " + updateVersion.getFilename());
                UPDATAFILEURL = "http://" + GlobalDate.defaultServerCanShu.getIpaddr() + ":" + GlobalDate.defaultServerCanShu.getPort() + GlobalDate.defaultServerCanShu.getDownloaddir() + updateVersion.getFilename();
                showProgressDialog(getString(R.string.xiazaizhong));
                downloadApkFile(UPDATAFILEURL, updateStr);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                closeProgressDialog();
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
                LogUtils.d("download versionfile err");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                LogUtils.d("onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtils.d("onFinished");
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                LogUtils.d("onWaiting");
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                LogUtils.d("onStarted");
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                LogUtils.i("JAVA", "current：" + current + "，total：" + total);
            }
        });
//        OkhttpDownloadUtil.getInstance().download(httpUrl, filePath, defaultServerCanShu.getTimeout(), new OkhttpDownloadUtil.OnDownloadListener() {
//            @Override
//            public void onDownloadSuccess() {
//                Message msg = new Message();
//                msg.what=4;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onDownloading(int progress) {
//                LogUtils.d("onWaiting");
//            }
//
//            @Override
//            public void onDownloadFailed() {
//                Message msg = new Message();
//                msg.what=2;
//                handler.sendMessage(msg);
//            }
//        });
    }

    private void downloadApkFile(String httpUrl, String filePath) {
        LogUtils.d(httpUrl);
        RequestParams params = new RequestParams(httpUrl);
        params.setSaveFilePath(filePath);
        params.setConnectTimeout(20000);
        params.setReadTimeout(20000);
        LogUtils.d("filePath" + filePath);
        //自动为文件命名
        params.setAutoRename(false);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                closeProgressDialog();
                LogUtils.d("onSuccess");
                if ((updateVersion == null) || updateVersion.getFilename().equals("") || (!FileCommonUtil.isFileExists(updateStr))) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setMessage(getString(R.string.shengjiwenjianyouwenti));
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                    return;
                }
                if (updateVersion.getVersionCode() > getCode()) {
                    textView.setText(getString(R.string.jiancedaoxinbanben) + updateVersion.getVersionName());
                    bt_updata.setEnabled(true);
                } else {
                    textView.setText(getString(R.string.dangqianbanbenweizuixinbanben) + String.valueOf(getCode()));
                    bt_updata.setEnabled(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                closeProgressDialog();
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                LogUtils.d("download apk err");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                LogUtils.d("onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtils.d("onFinished");
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                LogUtils.d("onWaiting");
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                LogUtils.d("onStarted");
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                LogUtils.i("JAVA", "current：" + current + "，total：" + total);
            }
        });
    }

    /**
     * 下载至 Environment.getExternalStorageDirectory().getPath() + "/update.apk"
     *
     * @param httpUrl
     * @return
     */
    private File downLoadFile(String httpUrl, String filePath) {
        if (TextUtils.isEmpty(httpUrl)) throw new IllegalArgumentException();
        String pathDir = FileCommonUtil.getDirName(filePath);
        FileCommonUtil.createOrExistsDir(pathDir);

        FileCommonUtil.deleteDirOrFile(filePath);
        File file = FileCommonUtil.getFileByPath(filePath);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ( (len = inputStream.read(buffer)) > 0 ) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
        return file;
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(String text) {
        closeProgressDialog();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        LogUtils.d("showProgressDialog: ");
        progressDialog.setMessage(text);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 判断软件是否安装
     *
     * @return
     */
    public boolean isInstall(String name) {
        mPackageManager = getActivity().getPackageManager();
        mListAppcations = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo info : mListAppcations) {
            LogUtils.e("getPackagename: ---" + info.packageName);
            // 匹配QQ的包名 如果手机中安装了QQ
            if (info.packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取versionCode
     */
    private int getCode() {
        // PackageManager管理器
        PackageManager pm = getActivity().getPackageManager();
        // 获取相关信息
        try {
            packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            // 版本号
            int version = packageInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
        closeProgressDialog();
    }
}
