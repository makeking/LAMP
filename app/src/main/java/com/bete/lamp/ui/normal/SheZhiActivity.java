package com.bete.lamp.ui.normal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;

import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.customWidget.EditSpinner;
import com.bete.lamp.ui.fsetting.SheZhiSettingAdminFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingBarCodeFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingDeviceInfoFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingDeviceNoFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingEepromFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingEnableFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingHardwareUpdataFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingJiaoZhunFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingLiangDuFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingLogUOrOFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingMainteFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingOnlineLogExportFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingOnlineSoftUpdataFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingPasswordChangeFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingServerFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingSoftUOrOFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingSoftVersionFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingTimeFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingUsbLogExportFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingUsbSoftUpdataFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingUserAddFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingUserManageFragment;
import com.bete.lamp.ui.fsetting.SheZhiSettingWifiFragment;
import com.myutils.GlobalDate;
import com.utils.LogUtils;
import com.utils.simpleArrayAdapter;

import java.util.List;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.activity_name_list;
import static com.myutils.GlobalDate.g_device_state;

public class SheZhiActivity extends BaseActivity implements SheZhiSettingFragment.OnFragmentInteractionListener {
    private static final String TAG = "SheZhiActivity";
    public static final int ISheZhiSettingFragment = 0;
    public static final int ISheZhiSettingSoftVersionFragment = 1;
    public static final int ISheZhiSettingJiaoZhunFragment = 2;
    public static final int ISheZhiSettingMainteFragment = 3;
    public static final int ISheZhiSettingDeviceInfoFragment = 4;
    public static final int ISheZhiSettingLiangDuFragment = 5;
    public static final int ISheZhiSettingWifiFragment = 6;
    public static final int ISheZhiSettingAdminFragment = 7;
    public static final int ISheZhiSettingDeviceNoFragment = 8;
    public static final int ISheZhiSettingServerFragment = 9;
    public static final int ISheZhiSettingTimeFragment = 10;
    public static final int ISheZhiSettingEnableFragment = 11;
    public static final int ISheZhiSettingPasswordChangeFragment = 12;
    public static final int ISheZhiSettingOnlineSoftUpdataFragment = 13;
    public static final int ISheZhiSettingUsbSoftUpdataFragment = 14;
    public static final int ISheZhiSettingSoftUOrOFragment = 15;
    public static final int ISheZhiSettingLogUOrOFragment = 16;
    public static final int ISheZhiSettingUserManageFragment = 17;
    public static final int ISheZhiSettingUserAddFragment = 18;
    public static final int ISheZhiSettingEepromFragment = 19;
    public static final int ISheZhiSettingBarCodeFragment = 20;
    public static final int ISheZhiSettingUsbLogExportFragment = 21;
    public static final int ISheZhiSettingOnlineLogExportFragment = 22;
    public static final int ISheZhiSettingHardwareUpdataFragment = 23;

    private Context context;
    private RadioGroup rg_radio;
    private EditSpinner sp_title;
    private Fragment[] mFragments;
    private RadioButton rb_shouye, rb_xitongshezhi;
    public SheZhiSettingFragment sheZhiSettingFragment;
    //当前Fragent的下标
    private int currentIndex;

    private PCRProject pcrProject = new PCRProject();
    private BarRareData barRareData = new BarRareData();
    public PCRProject getPcrProject() {
        return pcrProject;
    }

    public void setPcrProject(PCRProject pcrProject) {
        this.pcrProject = pcrProject;
    }

    public BarRareData getBarRareData() {
        return barRareData;
    }

    public void setBarRareData(BarRareData barRareData) {
        this.barRareData = barRareData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SheZhiActivity.this;
        setContentView(R.layout.activity_she_zhi);
        mFragments = new Fragment[255];
        sheZhiSettingFragment = new SheZhiSettingFragment();
        mFragments[0] = sheZhiSettingFragment;

        sp_title = (EditSpinner) findViewById(R.id.sp_title);
        initSpinner();
        // select the first item initially
        sp_title.selectItem(3);

        rg_radio = (RadioGroup) findViewById(R.id.rg_radio);
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置为默认界面 MainHomeFragment
        ft.add(R.id.shezhi_content, mFragments[0]).commit();
        rb_xitongshezhi = (RadioButton) findViewById(R.id.rb_xitongshezhi);
        //RadioGroup选中事件监听 改变fragment
        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_xitongshezhi:
                        changeRadio(ISheZhiSettingFragment);
                        break;
                }
            }
        });
        rb_xitongshezhi.setChecked(true);
        setIndexSelected(0);
    }

    private void initSpinner() {
        sp_title.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_title.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_title.setDropDownVerticalOffset(0);
        sp_title.setDropDownDrawableSpacing(60);
        sp_title.setDropDownHeight(240);
        sp_title.setEditable(false);
        sp_title.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return activity_name_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return activity_name_list.toArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(SheZhiActivity.this, R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_title.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("position:" + position);
                if(position==0)
                {
                    if (g_device_state == GlobalDate.DeviceStateType.needcheck) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(SheZhiActivity.this);
                        builder.setMessage(getString(R.string.yiqixuyaozijian));
                        builder.setTitle(getString(R.string.tishi));
                        builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(context, SelfCheckActivity.class));
                            }
                        });
                        builder.setNegativeButton(getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        CustomDialog customDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
                        customDialog.setCanceledOnTouchOutside(false);
                        customDialog.setCancelable(false);
                        customDialog.show();
                    }
                    else if (g_device_state == GlobalDate.DeviceStateType.ready) {
                        startActivity(new Intent(context, CheckActivity.class));
                    }
                }
                else if(position==1)
                {
                    startActivity(new Intent(context, ProjectActivity.class));
                }
                else if(position==2)
                {
                    startActivity(new Intent(context, ChaXunActivity.class));
                }
                else if(position==3)
                {
//                    startActivity(new Intent(context, SheZhiActivity.class));
                }
                else if(position==4)
                {
                    startActivity(new Intent(context, HelpActivity.class));
                }
                sp_title.selectItem(3);
                sp_title.clearFocus();
            }
        });

        sp_title.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

//    //设置Fragment页面
//    private void setIndexSelected(int index) {
//        if (currentIndex == index) {
//            return;
//        }
//        //开启事务
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        //隐藏当前Fragment
//        ft.hide(mFragments[currentIndex]);
//        //判断Fragment是否已经添加
//        if (!mFragments[index].isAdded()) {
//            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
//        } else {
//            //显示新的Fragment
//            ft.show(mFragments[index]);
//        }
//        ft.commit();
//        currentIndex = index;
//    }

    //设置Fragment页面
    private void setIndexSelected(int index) {
        if (currentIndex == index) {
            return;
        }
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        //隐藏当前Fragment
        ft.hide(mFragments[currentIndex]);
//        if((currentIndex!=ISheZhiSettingFragment)&&(currentIndex!=ISheZhiSettingAdminFragment)) {
//            if (mFragments[currentIndex] != null)
//                ft.remove(mFragments[currentIndex]);
//        }
        //判断Fragment是否已经添加
//        if (mFragments[index].isAdded()||(null != mFragmentManager.findFragmentByTag( index + "" ))) {
//            ft.show(mFragments[index]);
//        } else {
//            //显示新的Fragment
//            mFragmentManager.executePendingTransactions();
//            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
//            LogUtils.d(TAG+":"+"ADD!!!!!!!!!!!!!!!!");
//            ft.addToBackStack(null);
//        }
//        ft.commitAllowingStateLoss();
//        currentIndex = index;
        if(index==ISheZhiSettingFragment)
        {
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
            } else {
                //显示新的Fragment
                ft.show(mFragments[index]);
            }
        }
        else if(index==ISheZhiSettingSoftVersionFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingSoftVersionFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingJiaoZhunFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingJiaoZhunFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingMainteFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingMainteFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingDeviceInfoFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingDeviceInfoFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingLiangDuFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingLiangDuFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingWifiFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingWifiFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingAdminFragment)
        {
            if(mFragments[index]==null)
                mFragments[index] = new SheZhiSettingAdminFragment();
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
            } else {
                //显示新的Fragment
                ft.show(mFragments[index]);
            }
        }
        else if(index==ISheZhiSettingDeviceNoFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingDeviceNoFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingServerFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingServerFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingTimeFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingTimeFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingEnableFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingEnableFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingPasswordChangeFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingPasswordChangeFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingOnlineSoftUpdataFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingOnlineSoftUpdataFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingUsbSoftUpdataFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingUsbSoftUpdataFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingSoftUOrOFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingSoftUOrOFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingLogUOrOFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingLogUOrOFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingUserManageFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingUserManageFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingUserAddFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingUserAddFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingEepromFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingEepromFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingBarCodeFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingBarCodeFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingUsbLogExportFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingUsbLogExportFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingOnlineLogExportFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingOnlineLogExportFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
        else if(index==ISheZhiSettingHardwareUpdataFragment)
        {
            if(mFragments[index]!=null)
                ft.remove(mFragments[index]);
            mFragments[index] = new SheZhiSettingHardwareUpdataFragment();
            ft.add(R.id.shezhi_content, mFragments[index]).show(mFragments[index]);
        }
//        todo 2021-5-21 将commit 方法修改为 commitAllowingStateLoss
//        ft.commit();
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }

    public void changeRadio(int index) {
        if (currentIndex == index)
            return;
//        if (index == 0) {
            setIndexSelected(index);
            currentIndex = index;
//        }
    }

    public void goReturnHandle(View view) {
        Fragment fragment = getForegroundFragment();
        if(fragment==null)
        {
            startActivity(new Intent(context, MainActivity.class));
            return;
        }

        String fragmentname = fragment.getClass().getSimpleName();
        LogUtils.d(fragmentname);
        if(fragmentname.equals("SheZhiSettingSoftVersionFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingJiaoZhunFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingMainteFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingDeviceInfoFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingLiangDuFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingWifiFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingAdminFragment"))
        {
            changeRadio(ISheZhiSettingFragment);
        }
        else if(fragmentname.equals("SheZhiSettingDeviceNoFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingServerFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingTimeFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingEnableFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingPasswordChangeFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingOnlineSoftUpdataFragment"))
        {
            changeRadio(ISheZhiSettingSoftUOrOFragment);
        }
        else if(fragmentname.equals("SheZhiSettingUsbSoftUpdataFragment"))
        {
            changeRadio(ISheZhiSettingSoftUOrOFragment);
        }
        else if(fragmentname.equals("SheZhiSettingSoftUOrOFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingLogUOrOFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingUserManageFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingUserAddFragment"))
        {
            changeRadio(ISheZhiSettingUserManageFragment);
        }
        else if(fragmentname.equals("SheZhiSettingEepromFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingBarCodeFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingUsbLogExportFragment"))
        {
            changeRadio(ISheZhiSettingLogUOrOFragment);
        }
        else if(fragmentname.equals("SheZhiSettingOnlineLogExportFragment"))
        {
            changeRadio(ISheZhiSettingLogUOrOFragment);
        }
        else if(fragmentname.equals("SheZhiSettingHardwareUpdataFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("BarCodeLampProjectFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("BarCodeLampLiuchengFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("BarCodeLampCanshuFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("BarCodeImageFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("BarCodeScanFragment"))
        {
            changeRadio(ISheZhiSettingAdminFragment);
        }
        else if(fragmentname.equals("SheZhiSettingFragment"))
        {
            startActivity(new Intent(context, MainActivity.class));
            return;
        }
    }

    public Fragment getForegroundFragment()
    {
        Fragment fragment0 = null;
        synchronized (this) {
            FragmentManager ft = getSupportFragmentManager();
            List<Fragment> list = ft.getFragments();
            for (Fragment fragment : list) {
                    if (((BaseFragment) fragment).ismUserVisibleHint()) {
                        Fragment fragment1 = getForegroundChildFragment(fragment);
                        if(fragment1!=null)
                            return fragment1;
                        else
                            return fragment;
                    }
            }
        }
        return fragment0;
    }

    public Fragment getForegroundChildFragment(Fragment fragment) {
        if (fragment == null)
            return fragment;
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        for (Fragment fragment1 : list) {
                if (((BaseFragment) fragment1).ismUserVisibleHint()) {
                    Fragment fragment2 =getForegroundChildFragment(fragment1);
                    if(fragment2!=null)
                    {
                        return fragment1;
                    }
                }
        }
        return fragment;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForegroundActvity(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
//        boolean flag=false;
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
//                flag = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public Fragment getFragment(String className) {
        synchronized (this) {
            FragmentManager ft = getSupportFragmentManager();
            List<Fragment> list = ft.getFragments();
            for (Fragment fragment : list) {
                if (fragment.getClass().getSimpleName().equals(className)) {
                    LogUtils.d(fragment.getClass().getSimpleName() + "!!!!!!!!!!");
                    return fragment;
                }

                Fragment fragment1 = getChildFragment(fragment, className);
                if (fragment1 != null)
                    return fragment1;
            }
            return null;
        }
    }

    public Fragment getChildFragment(Fragment fragment, String className) {
        if (fragment == null || TextUtils.isEmpty(className))
            return null;
//        LogUtils.d(fragment.getClass().getSimpleName());
//        if (fragment.getClass().getSimpleName().equals(className)) {
//            LogUtils.d(fragment.getClass().getSimpleName() + "!!!!!!!!!!");
//            return fragment;
//        }
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        for (Fragment fragment1 : list) {
            if (fragment1.getClass().getSimpleName().equals(className)) {
                LogUtils.d(fragment1.getClass().getSimpleName() + "!!!!!!!!!!");
                return fragment1;
            }
            Fragment fragment2 = getChildFragment(fragment1, className);
            if (fragment2 != null)
                return fragment2;
        }
        return null;
    }

//    /**
//     * 判断某个界面是否在前台
//     *
//     * @param className 界面的类名
//     * @return 是否在前台显示
//     */
//    public boolean isForegroundFragment(String className) {
//        synchronized (this) {
//            FragmentManager ft = getSupportFragmentManager();
//            List<Fragment> list = ft.getFragments();
//            for (Fragment fragment : list) {
//                if (((BaseFragment) fragment).ismUserVisibleHint()) {
//                    if (fragment.getClass().getSimpleName().equals(className)) {
//                        LogUtils.d(fragment.getClass().getSimpleName());
//                        return true;
//                    } else {
//                        LogUtils.d(fragment.getClass().getSimpleName());
//                        if (isForegroundChildFragment(fragment, className))
//                            return true;
//                    }
//                }
//            }
//            return false;
//        }
//    }
//
//    public boolean isForegroundChildFragment(Fragment fragment, String className) {
//        if (fragment == null || TextUtils.isEmpty(className))
//            return false;
//        FragmentManager fragmentManager = fragment.getChildFragmentManager();
//        List<Fragment> list = fragmentManager.getFragments();
//        for (Fragment fragment1 : list) {
//            if (((BaseFragment) fragment1).ismUserVisibleHint()) {
//                if (fragment1.getClass().getSimpleName().equals(className)) {
//                    LogUtils.d(fragment1.getClass().getSimpleName());
//                    return true;
//                } else {
//                    LogUtils.d(fragment1.getClass().getSimpleName());
//                    if (isForegroundChildFragment(fragment1, className))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 判断某个界面是否在前台
     *
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public boolean isForegroundFragment(String className) {
        synchronized (this) {
            FragmentManager ft = getSupportFragmentManager();
            List<Fragment> list = ft.getFragments();
            for (Fragment fragment : list) {
                if (fragment.getClass().getSimpleName().equals(className)) {
                    if (((BaseFragment) fragment).ismUserVisibleHint()) {
                        return true;
                    }
                    return false;
                }
//                LogUtils.d(fragment.getClass().getSimpleName());
                if (isForegroundChildFragment(fragment, className))
                    return true;
            }
            return false;
        }
    }

    public boolean isForegroundChildFragment(Fragment fragment, String className) {
        if (fragment == null || TextUtils.isEmpty(className))
            return false;

        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        for (Fragment fragment1 : list) {
            if (fragment1.getClass().getSimpleName().equals(className)) {
                if (((BaseFragment) fragment1).ismUserVisibleHint()) {
                    return true;
                }
//                LogUtils.d(fragment1.getClass().getSimpleName());
                return false;
            }
//            LogUtils.d(fragment1.getClass().getSimpleName());
            if (isForegroundChildFragment(fragment1, className))
                return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(SheZhiActivity.this, "this is：" + uri, Toast.LENGTH_SHORT).show();
    }

    public void goHomeHandle(View view) {
        startActivity(new Intent(context, MainActivity.class));
    }

}
