package com.bete.lamp.ui.fsetting;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.leon.lfilepickerlibrary.utils.NavigationBarUtil;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.LogUtils;
import com.utils.ShellUtils;

import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.clearDefaultLauncher;
import static com.myutils.GlobalDate.g_daohangjianEnable;
import static com.myutils.GlobalDate.g_homeEnable;
import static com.myutils.GlobalDate.g_screenOrientationEnable;
import static com.myutils.GlobalDate.packageVersion;
import static com.myutils.GlobalDate.setDefaultLauncher;
import static com.myutils.SharedPreferencesUtils.setScreenOrientation;

public class SheZhiSettingEnableFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingSoftVersionFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Switch switch_daohangjianable, switch_bootable, switch_deviceable, switch_screenable,switch_homeable;

    public SheZhiSettingEnableFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingEnableFragment newInstance(String param1, String param2) {
//        SheZhiSettingEnableFragment fragment = new SheZhiSettingEnableFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_enable, container, false);
        switch_daohangjianable = (Switch) view.findViewById(R.id.switch_daohangjianable);
        switch_daohangjianable.setChecked(g_daohangjianEnable);
        switch_daohangjianable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NavigationBarUtil.mt8735HideNavigationBar(getContext());
                    g_daohangjianEnable = true;
                    SharedPreferencesUtils.setDaohangjianAbleState(g_daohangjianEnable);
                    NavigationBarUtil.showNavigationBar(getActivity().getWindow());
                    ShellUtils.execCmd("wm overscan 0,0,0,0", true);
                } else {
                    NavigationBarUtil.mt8735ShowNavigationBar(getContext());
                    g_daohangjianEnable = false;
                    SharedPreferencesUtils.setDaohangjianAbleState(g_daohangjianEnable);
                    if (g_screenOrientationEnable) {
                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,-80,0,-40", true);
                    } else {
                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,-40,0,-80", true);
                    }
                }
            }
        });

        switch_homeable = (Switch) view.findViewById(R.id.switch_homeable);
        switch_homeable.setChecked(g_homeEnable);
        switch_homeable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clearDefaultLauncher(getContext());
                    String defPackageName = getActivity().getPackageName();
                    String defClassName = "com.bete.lamp.ui.normal.SplashActivity";
                    setDefaultLauncher(getContext(),defPackageName,defClassName);
                    getDefaultHome();
                    g_homeEnable = true;
                    SharedPreferencesUtils.setHomeAbleState(g_homeEnable);
                } else {
                    clearDefaultLauncher(getContext());
                    String defPackageName = "com.android.launcher3";
                    String defClassName = "com.android.launcher3.Launcher";
                    setDefaultLauncher(getContext(),defPackageName,defClassName);
                    getDefaultHome();
                    g_homeEnable = false;
                    SharedPreferencesUtils.setHomeAbleState(g_homeEnable);
                }
            }
        });

        switch_screenable = (Switch) view.findViewById(R.id.switch_screenable);
        switch_screenable.setChecked(g_screenOrientationEnable);
        switch_screenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    g_screenOrientationEnable = true;
                    setScreenOrientation(true);
                } else {
                    g_screenOrientationEnable = false;
                    setScreenOrientation(false);
                }

//                if (g_screenOrientationEnable) {
//                    ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:2", true);
//                    if(g_daohangjianEnable)
//                    {
//                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
//                        ShellUtils.execCmd("wm overscan 0,-60,0,0", true);
//                    }
//                    else
//                    {
//                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
//                        ShellUtils.execCmd("wm overscan 0,-60,0,0", true);
//                    }
//                } else {
//                    ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0", true);
//                    if(g_daohangjianEnable)
//                    {
//                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
//                        ShellUtils.execCmd("wm overscan 0,0,0,-60", true);
//                    }
//                    else
//                    {
//                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
//                        ShellUtils.execCmd("wm overscan 0,-60,0,0", true);
//                    }
//                }

                if (g_screenOrientationEnable) {
                    ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:2", true);
                    if(g_daohangjianEnable)
                    {
                        NavigationBarUtil.showNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,0,0,0", true);
                    }
                    else
                    {
                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,-80,0,-40", true);
                    }
                } else {
                    ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0", true);
                    if(g_daohangjianEnable)
                    {
                        NavigationBarUtil.showNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,0,0,0", true);
                    }
                    else
                    {
                        NavigationBarUtil.hideNavigationBar(getActivity().getWindow());
                        ShellUtils.execCmd("wm overscan 0,-40,0,-80", true);
                    }
                }

            }
        });

        switch_deviceable = (Switch) view.findViewById(R.id.switch_deviceable);
        switch_deviceable.setChecked(DEVICE);
        switch_deviceable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtils.setDEVICEState(true);
                    DEVICE = true;
                } else {
                    SharedPreferencesUtils.setDEVICEState(false);
                    DEVICE = false;
                }
            }
        });

        switch_bootable = (Switch) view.findViewById(R.id.switch_bootable);
        switch_bootable.setChecked(GlobalDate.g_bootEnable);
        switch_bootable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtils.setBootAbleState(true);
                    GlobalDate.g_bootEnable = true;
                } else {
                    SharedPreferencesUtils.setBootAbleState(false);
                    GlobalDate.g_bootEnable = false;
                }
            }
        });
        return view;
    }

    private void getDefaultHome() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getActivity().getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            LogUtils.d("resolveActivity--->activityInfo null");
            // should not happen. A home is always installed, isn't it?
        } else if (res.activityInfo.packageName.equals("android")) {
            // No default selected
            LogUtils.d("resolveActivity--->无默认设置");
        } else {
            // res.activityInfo.packageName and res.activityInfo.name gives
            // you the default app
            LogUtils.d("默认桌面为：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
        }
    }

}
