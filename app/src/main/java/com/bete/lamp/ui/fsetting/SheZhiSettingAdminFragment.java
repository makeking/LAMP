package com.bete.lamp.ui.fsetting;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bete.lamp.ActivityCollector;
import com.bete.lamp.R;
import com.bete.lamp.adapter.ViewAdapter;
import com.bete.lamp.navigator.ScaleCircleNavigator;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SelfCheckActivity;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.leon.lfilepickerlibrary.utils.NavigationBarUtil;
import com.myutils.GlobalDate;
import com.utils.LogUtils;
import com.utils.ShellUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingBarCodeFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingDeviceNoFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingEepromFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingEnableFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingLogUOrOFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingPasswordChangeFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingServerFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingSoftUOrOFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingSoftVersionFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingTimeFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUserManageFragment;
import static com.myutils.GlobalDate.BLOCKNUM;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.clearDefaultLauncher;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_device_state;
import static com.myutils.GlobalDate.packageVersion;
import static com.myutils.GlobalDate.setDefaultLauncher;

public class SheZhiSettingAdminFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingAdminFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Context context;
    private int gloabIndex, gloabCount;
    private LayoutInflater viewinflater;
    private View view1, view2, view3, view4;
    private Button setting_bt_deviceno,setting_bt_network,setting_bt_server;
    private Button setting_bt_time;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中;//view数组
    private MagicIndicator indicator;

    public SheZhiSettingAdminFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingAdminFragment newInstance(String param1, String param2) {
//        SheZhiSettingAdminFragment fragment = new SheZhiSettingAdminFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_admin, container, false);
        context = getActivity();
        viewPager = (ViewPager) view.findViewById(R.id.admin_viewpager);
        indicator = (MagicIndicator) view.findViewById(R.id.admin_bottom_indicator);

        viewinflater = getActivity().getLayoutInflater();
            initAdmin(inflater);
            viewList.add(view1);
            viewList.add(view2);
//            viewList.add(view3);

        PagerAdapter adapter = new ViewAdapter(viewList);
        viewPager.setAdapter(adapter);
        ScaleCircleNavigator navigator = new ScaleCircleNavigator(getActivity());
        navigator.setCircleCount(viewList.size());
        navigator.setNormalCircleColor(Color.DKGRAY);
        navigator.setSelectedCircleColor(Color.CYAN);
        navigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(navigator);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        ViewPagerHelper.bind(indicator, viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        gloabCount = pagerAdapter.getCount();
        return view;
    }

    void initAdmin(LayoutInflater inflater)
    {
        Button setting_bt_deviceno,setting_bt_server,setting_bt_time,setting_bt_selfcheck;
        Button setting_bt_enable,setting_bt_passwordchange,setting_bt_softupdate,setting_bt_hardwareupdata;
        Button setting_bt_usermanage,setting_bt_logexport,setting_bt_desktop,setting_bt_switch_pc;
        Button setting_bt_killprogram,setting_bt_barcode,setting_bt_eeprom;

        view1 = inflater.inflate(R.layout.fragment_setting_admin_viewpager_one, null);
        setting_bt_deviceno = (Button)view1.findViewById(R.id.setting_bt_deviceno);
        setting_bt_deviceno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DEVICE) {
                    ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingDeviceNoFragment);
                }
                else
                {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("非真机，不支持该操作").setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                    tempCustomDialog.setCanceledOnTouchOutside(false);
                    tempCustomDialog.show();
                }
            }
        });

        setting_bt_server = (Button)view1.findViewById(R.id.setting_bt_server);
        setting_bt_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingServerFragment);
            }
        });

        setting_bt_time = (Button)view1.findViewById(R.id.setting_bt_time);
        setting_bt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingTimeFragment);
            }
        });

        setting_bt_selfcheck =(Button)view1.findViewById(R.id.setting_bt_selfcheck);
        setting_bt_selfcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if((g_block_states[0]==GlobalDate.BlockStateType.running)||(g_block_states[1]==GlobalDate.BlockStateType.running)||(g_block_states[2]==GlobalDate.BlockStateType.running)||(g_block_states[3]==GlobalDate.BlockStateType.running))
                {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("实验中,请稍后再试");
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                    tempCustomDialog.setCanceledOnTouchOutside(false);
                    tempCustomDialog.show();
                    return;
                }
                else
                {
                    startActivity(new Intent(getActivity().getApplicationContext(), SelfCheckActivity.class));
                }
            }
        });

        setting_bt_enable  =(Button)view1.findViewById(R.id.setting_bt_enable);
        setting_bt_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingEnableFragment);
            }
        });

        setting_bt_passwordchange=(Button)view1.findViewById(R.id.setting_bt_passwordchange);
        setting_bt_passwordchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingPasswordChangeFragment);
            }
        });

        setting_bt_softupdate=(Button)view1.findViewById(R.id.setting_bt_softupdate);
        setting_bt_softupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingSoftUOrOFragment);
            }
        });

        setting_bt_hardwareupdata=(Button)view1.findViewById(R.id.setting_bt_hardwareupdate);
        setting_bt_hardwareupdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("暂时不支持该操作");
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                tempCustomDialog.setCanceledOnTouchOutside(false);
                tempCustomDialog.show();
                return;
                //((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingHardwareUpdataFragment);
            }
        });

        setting_bt_logexport=(Button)view1.findViewById(R.id.setting_bt_logexport);
        setting_bt_logexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingLogUOrOFragment);
            }
        });

        view2 = inflater.inflate(R.layout.fragment_setting_admin_viewpager_two, null);

        setting_bt_usermanage=(Button)view2.findViewById(R.id.setting_bt_usermanage);
        setting_bt_usermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingUserManageFragment);
            }
        });

        setting_bt_desktop=(Button)view2.findViewById(R.id.setting_bt_desktop);
        setting_bt_desktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                clearDefaultLauncher(context);
                String defPackageName = "com.android.launcher3";
                String defClassName = "com.android.launcher3.Launcher";
                setDefaultLauncher(context,defPackageName,defClassName);

                NavigationBarUtil.mt8735HideNavigationBar(context);
                NavigationBarUtil.showNavigationBar(getActivity().getWindow());
                ShellUtils.execCmd("wm overscan 0,0,0,0", true);
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });

        setting_bt_switch_pc =(Button)view2.findViewById(R.id.setting_bt_switch_pc);
        setting_bt_switch_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DEVICE)
                {
                    if((g_block_states[0]==GlobalDate.BlockStateType.running)||(g_block_states[1]==GlobalDate.BlockStateType.running)||(g_block_states[2]==GlobalDate.BlockStateType.running)||(g_block_states[3]==GlobalDate.BlockStateType.running))
                    {
                        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                        builder.setMessage("实验中，请稍后再试");
                        builder.setTitle(getString(R.string.tishi));
                        builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                        tempCustomDialog.setCanceledOnTouchOutside(false);
                        tempCustomDialog.show();
                        return;
                    }
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("切换PC后应用将无法进行检测");
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Object[]tempObjects = null;
                            byte[] bytebuff= new byte[1];
                            tempObjects = McuSerial.getInstance().cmdLampWriteEeprom(0x505,bytebuff,1);
                            if((int)(tempObjects[1])==0)
                            {
//                                g_device_state=GlobalDate.DeviceStateType.topc;
                                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                                builder.setMessage("切换成功");
                                builder.setTitle(getString(R.string.tishi));
                                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create(R.layout.dialog_normal_feiquanping_message).show();
                            }
                            else
                            {
                                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                                builder.setMessage("切换失败");
                                builder.setTitle(getString(R.string.tishi));
                                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create(R.layout.dialog_normal_feiquanping_message).show();
                            }
                        }
                    });
                    builder.setNegativeButton(getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                }
                else
                {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("非真机，不支持该操作");
                    builder.setTitle(getString(R.string.tishi));
                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                    tempCustomDialog.setCanceledOnTouchOutside(false);
                    tempCustomDialog.show();
                    return;
                }
            }
        });

        setting_bt_killprogram=(Button)view2.findViewById(R.id.setting_bt_killprogram);
        setting_bt_killprogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShellUtils.execCmd("wm overscan 0,0,0,0", true);
                exitAPP();
            }
        });

        setting_bt_barcode=(Button)view2.findViewById(R.id.setting_bt_barcode);
        setting_bt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingBarCodeFragment);
            }
        });

        setting_bt_eeprom =(Button)view2.findViewById(R.id.setting_bt_eeprom);
        setting_bt_eeprom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo 2021-5-21 暂时屏蔽
//                if(!DEVICE)
//                {
//                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//                    builder.setMessage("非真机，暂不支持该操作");
//                    builder.setTitle(getString(R.string.tishi));
//                    builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
//                    tempCustomDialog.setCanceledOnTouchOutside(false);
//                    tempCustomDialog.show();
//                    return;
//                }
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingEepromFragment);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {
        ShellUtils.execCmd("wm overscan 0,0,0,0", true);
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
        ActivityCollector.finishAll();
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        manager.restartPackage(getActivity().getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
