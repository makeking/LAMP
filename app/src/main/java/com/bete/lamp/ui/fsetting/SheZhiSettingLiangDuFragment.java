package com.bete.lamp.ui.fsetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.utils.BrightnessUtils;
import com.utils.LogUtils;

import static com.myutils.GlobalDate.packageVersion;

public class SheZhiSettingLiangDuFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingLiangDuFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private View view;
    private TextView tv_lightvalue;
    private int BACKLIGHT_DISPLAYMAX=240;
    private int BACKLIGHT_DISPLAYMIN=40;
    private int realvalue;
    private int jisuanvalue;
    private Button bt_add,bt_dec;
    private SeekBar sb_liangdu;

    public SheZhiSettingLiangDuFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingLiangDuFragment newInstance(String param1, String param2) {
//        SheZhiSettingLiangDuFragment fragment = new SheZhiSettingLiangDuFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_liangdu, container, false);
        tv_lightvalue = (TextView)view.findViewById(R.id.tv_lightvalue);
        sb_liangdu = (SeekBar) view.findViewById(R.id.sb_liangdu);
        jisuanvalue= BrightnessUtils.getBrightness();
        realvalue = (jisuanvalue-BACKLIGHT_DISPLAYMIN)/2;
        realvalue = (realvalue>100)?100:realvalue;
        tv_lightvalue.setText(String.valueOf(realvalue));
        sb_liangdu.setProgress(realvalue);
        sb_liangdu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                realvalue = progress;
                jisuanvalue = (realvalue*2)+BACKLIGHT_DISPLAYMIN;
                tv_lightvalue.setText(String.valueOf(realvalue));
                BrightnessUtils.setBrightness(jisuanvalue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
}
