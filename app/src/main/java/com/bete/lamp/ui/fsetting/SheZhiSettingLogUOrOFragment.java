package com.bete.lamp.ui.fsetting;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.LogUtils;
import com.utils.StorageUtil;

import java.io.File;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingOnlineLogExportFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingOnlineSoftUpdataFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUsbLogExportFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUsbSoftUpdataFragment;
import static com.utils.StorageUtil.isUpanExist;

public class SheZhiSettingLogUOrOFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingLogUOrOFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Button bt_usb, bt_net;

    public SheZhiSettingLogUOrOFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingLogUOrOFragment newInstance(String param1, String param2) {
//        SheZhiSettingLogUOrOFragment fragment = new SheZhiSettingLogUOrOFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_log_u_or_o, container, false);
        bt_usb = (Button) view.findViewById(R.id.bt_usb);
        bt_usb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUSB();
//              changeRadio(1,new SettingEthernetFragment());
            }
        });

        bt_net = (Button) view.findViewById(R.id.bt_net);
        bt_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNet();
//              changeRadio(1,new SettingWifiConnectFragment());
            }
        });
        return view;
    }

    public void goNet() {
        if (!SharedPreferencesUtils.getServerAbleState()) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("未设置服务器！");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingOnlineLogExportFragment);
        }
    }

    private boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }

    public void goUSB() {
        if (!isUpanExist(getActivity())) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("未检测到设备U盘！");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingUsbLogExportFragment);
        }
    }
}
