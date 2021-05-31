package com.bete.lamp.ui.fsetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

public class SheZhiSettingDeviceInfoFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingDeviceInfoFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
//    从EEprom中读取。
//    产品名称
//    软件版本
//    pcb版本
//    固件版本
//    通道数
//    序列号
//    出厂日期
    private TextView tv_product, tv_softwareversion, tv_hardwareversion, tv_hardversion, tv_serialno,tv_guangnum,tv_gooutdate;
    private TextView tv_info;

    public SheZhiSettingDeviceInfoFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingDeviceInfoFragment newInstance(String param1, String param2) {
//        SheZhiSettingDeviceInfoFragment fragment = new SheZhiSettingDeviceInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_deviceinfo, container, false);
        tv_product = (TextView) view.findViewById(R.id.tv_product);
        tv_softwareversion = (TextView) view.findViewById(R.id.tv_softwareversion);
        tv_hardversion = (TextView) view.findViewById(R.id.tv_hardversion);
        tv_hardwareversion = (TextView) view.findViewById(R.id.tv_hardwareversion);
        tv_serialno = (TextView) view.findViewById(R.id.tv_serialno);
        tv_guangnum = (TextView) view.findViewById(R.id.tv_guangnum);
        tv_gooutdate = (TextView) view.findViewById(R.id.tv_gooutdate);

        tv_product.setText(GlobalDate.PRODUCT_NAME);
        tv_softwareversion.setText("V" + GlobalDate.SoftwareVersion);
        tv_hardversion.setText(GlobalDate.devicehardversion);
        tv_hardwareversion.setText(GlobalDate.devicehardwareversion);
        tv_serialno.setText(GlobalDate.deviceno);
        tv_guangnum.setText(String.valueOf(GlobalDate.g_device_gangnum));
        tv_gooutdate.setText(GlobalDate.deviceoutdate);

        tv_info = (TextView) view.findViewById(R.id.tv_info);
        String info_str;
        info_str = getString(R.string.chanpinxinghao)+ GlobalDate.PRODUCT_NAME+"\r\n";
        info_str =info_str + getString(R.string.ruanjianbanben_)+"V"+GlobalDate.SoftwareVersion+"\r\n";
        info_str =info_str + getString(R.string.shebeibianhao_)+ GlobalDate.deviceno +"\r\n";
        info_str =info_str + getString(R.string.pcbbanben_)+ GlobalDate.devicehardversion +"\r\n";
        tv_info.setText(info_str);
        return view;
    }
}
