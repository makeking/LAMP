package com.bete.lamp.ui.fsetting;

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
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.utils.FileCommonUtil;
import com.utils.FileIOUtils;
import com.utils.GsonUtils;
import com.utils.LogUtils;
import com.utils.ShellUtils;
import com.utils.StorageUtil;
import com.utils.SystemTimeSettingUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class SheZhiSettingUsbSoftUpdataFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingUsbSoftUpdataFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private List<ApplicationInfo> mListAppcations;
    private PackageManager mPackageManager;
    private PackageInfo packageInfo;
    private File updateFile;
    private ProgressDialog progressDialog;
    EditText editText;
    TextView textView;
    Button bt_check,bt_updata;
    UpdateVersion updateVersion;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                closeProgressDialog();
            }
        }
    };

    private EthernetManager mEthManager;
    //        private IpConfiguration mIpConfiguration;
    private ConnectivityManager mCM;
    private IpConfiguration.IpAssignment mIpAssignment = IpConfiguration.IpAssignment.DHCP;
    private StaticIpConfiguration mStaticIpConfiguration = null;
    public SheZhiSettingUsbSoftUpdataFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingUsbSoftUpdataFragment newInstance(String param1, String param2) {
//        SheZhiSettingUsbSoftUpdataFragment fragment = new SheZhiSettingUsbSoftUpdataFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_usb_soft_updata, container, false);
        textView = (TextView)view.findViewById(R.id.tv_response);

        bt_check = (Button)view.findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUpanUpdateCheckHandle();
            }
        });
        bt_updata = (Button)view.findViewById(R.id.bt_updata);
        bt_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUpanUpdateHandle();
            }
        });
        bt_updata.setEnabled(false);
        textView.setText(getString(R.string.jiancedaoxinbanben)+String.valueOf(getCode()));
        return view;
    }

    public void goUpanUpdateCheckHandle() {
        File updateDir= FileCommonUtil.getFileByPath(StorageUtil.UPANROOT+"/"+ Canstant.UPAN_UPDATE_DIR);
        if((updateDir==null)||(!updateDir.exists())||(!updateDir.isDirectory()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.weijiancedaoshengjimulu));
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
            return;
        }
        File updateVersionFile = FileCommonUtil.getFileByPath(StorageUtil.UPANROOT+"/"+ Canstant.UPAN_UPDATE_DIR+Canstant.UPAN_UPDATE_VERSION);
        if(((updateVersionFile==null))||(!updateVersionFile.exists())||(updateVersionFile.isDirectory()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.weijiancedaoshengjipeizhiwenjian));
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
            return;
        }
        try {
            String input = FileIOUtils.readFile2String(updateVersionFile, "UTF-8");
            updateVersion = GsonUtils.getInstance().fromJson(input, UpdateVersion.class);
        }
        catch (NumberFormatException e)
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
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
        LogUtils.e("goUpanUpdateCheckHandle: "+updateVersion.getFilename());
        updateFile = FileCommonUtil.getFileByPath(StorageUtil.UPANROOT+"/"+ Canstant.UPAN_UPDATE_DIR+updateVersion.getFilename());
        if((updateVersion==null)||updateVersion.getFilename().equals("")||(updateFile==null)||(!updateFile.exists()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
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
        if(updateVersion.getVersionCode() > getCode())
        {
            textView.setText(getString(R.string.jiancedaoxinbanben)+ updateVersion.getVersionName());
            bt_updata.setEnabled(true);
        }
        else
        {
            textView.setText(getString(R.string.dangqianbanbenweizuixinbanben)+String.valueOf(getCode()));
            bt_updata.setEnabled(false);
        }
    }

    public void goUpanUpdateHandle() {
        final ShellUtils.CommandResult[] result = {null};
        showProgressDialog();
        if(isInstall("com.bete.lamp")) {
            // 判断包名是否相同  再判断版本号，如果本地版本号小于服务器端软件版本号就升级
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    result[0] = ShellUtils.execCmd("mount -o rw,remount /system", true);
//                    LogUtils.d(result[0].toString());
//                    result[0] = ShellUtils.execCmd("cp /storage/usbotg/LAMP/update/app-debug.apk /system/app/pcr/", true);
//                    LogUtils.d(result[0].toString());
//                    result[0] = ShellUtils.execCmd("mount -o ro,remount /system", true);
//                    LogUtils.d(result[0].toString());

                    result[0] = ShellUtils.execCmd("pm install -r "+"/storage/usbotg/LAMP/update/"+updateVersion.getFilename(), true);
                    LogUtils.d(result[0].toString());
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    if(result[0].successMsg=="0") {
//                        Message msg = new Message();
//                        msg.what = 1;
//                        handler.sendMessage(msg);
//                    }
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                    Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                    PendingIntent restartIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
                    System.exit(0);
//                    File tempFile = FileCommonUtil.getFileByPath("/storage/usbotg/LAMP/update/"+updateVersion.getFilename());
//                    InstallUtil.silenceInstall(context,tempFile);
                }
            }).start();
        }
////要调用另一个APP的activity所在的包名
//        String packageName = "com.bete.updata";
//        //要调用另一个APP的activity名字
//        String activity = "com.bete.updata.ui.UsbSoftUpdateActivity";
//        ComponentName component = new ComponentName(packageName, activity);
//        Intent intent = new Intent();
//        intent.setComponent(component);
//        intent.setFlags(101);
//        intent.putExtra("FilePath", "/storage/usbotg/LAMP/update/"+updateVersion.getFilename());
//        intent.putExtra("UpdataPackage", "com.bete.lamp");
//        //intent.setClassName(packageName,activity);
//        startActivity(intent);
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        closeProgressDialog();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        LogUtils.d("showProgressDialog: ");
        progressDialog.setMessage(getString(R.string.shengjizhong));
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
     * @return
     */
    public boolean isInstall(String name){
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
            int version = packageInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取APP版本号
     *
     * @return
     */
    private String getAppVersion() {
        try {
            // PackageManager管理器
            PackageManager pm = getActivity().getPackageManager();
            // 获取相关信息
            packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            // 版本名称
            String name = packageInfo.versionName;
            // 版本号
            int version = packageInfo.versionCode;

            LogUtils.i("版本信息", "版本名称：" + name + "版本号" + version);

            return name;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果出现异常抛出
        return getString(R.string.wufahuoqu);
    }

    public void restartApplication(Context context) {
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
