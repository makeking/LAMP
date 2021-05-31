package com.bete.lamp.ui.fsetting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.thread.LampLiuChengThread;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.myutils.GlobalDate;
import com.utils.LogUtils;
import com.utils.SystemTimeSettingUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.bete.lamp.AppApplication.onlyLampLiuChengThread;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingAdminFragment;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.g_block_states;

public class SheZhiSettingTimeFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingTimeFragment";
    //    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
//    private CheckBox cb_timesyns;
    private Switch sw_netenable;
    private TextView tv_time, tv_date;
    private String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private TimePickerView pvCustomTime;
    private TimeThread timeUpdataThread;
    private Button bt_date_edit, bt_time_edit;

    private boolean curretnState = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                SimpleDateFormat df1 = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
                String date = df.format(Calendar.getInstance().getTime());//获取当前时间
                tv_date.setText(date);
                String time = df1.format(Calendar.getInstance().getTime());//获取当前时间
                tv_time.setText(time);
            }
        }
    };

    public SheZhiSettingTimeFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingTimeFragment newInstance(String param1, String param2) {
//        SheZhiSettingTimeFragment fragment = new SheZhiSettingTimeFragment();
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
            for (GlobalDate.BlockStateType g_block_state : g_block_states) {
                if (g_block_state == GlobalDate.BlockStateType.running) {
                    curretnState = true;
                    break;
                }
                if (g_block_states[g_block_states.length - 1] != GlobalDate.BlockStateType.running) {
                    curretnState = false;
                }

            }
            sw_netenable.setClickable(!curretnState);
            if (sw_netenable.isClickable() && !sw_netenable.isChecked()) {
                // 设置状态
                bt_date_edit.setClickable(true);
                bt_time_edit.setClickable(true);
            } else {
                // 设置状态
                bt_date_edit.setClickable(false);
                bt_time_edit.setClickable(false);
            }
            LogUtils.d(TAG, "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (GlobalDate.BlockStateType g_block_state : g_block_states) {
            if (g_block_state == GlobalDate.BlockStateType.running) {
                curretnState = true;
                break;
            }
            if (g_block_states[g_block_states.length - 1] != GlobalDate.BlockStateType.running) {
                curretnState = false;
            }

        }
        sw_netenable.setClickable(!curretnState);
        if (sw_netenable.isClickable() && !sw_netenable.isChecked()) {
            // 设置状态
            bt_date_edit.setClickable(true);
            bt_time_edit.setClickable(true);
        } else {
            // 设置状态
            bt_date_edit.setClickable(false);
            bt_time_edit.setClickable(false);
        }
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_time, container, false);
        bt_date_edit = (Button) view.findViewById(R.id.bt_date_edit);
        bt_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datehandle(v);
            }
        });
        bt_time_edit = (Button) view.findViewById(R.id.bt_time_edit);
        bt_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timehandle(v);
            }
        });
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        updateTimeThread();
        //cb_timesyns = (CheckBox) view.findViewById(R.id.cb_netenable);
        sw_netenable = (Switch) view.findViewById(R.id.sw_netenable);
        boolean checkd = SystemTimeSettingUtil.isDateTimeAuto(getContext());

        sw_netenable.setChecked(checkd);
        // 1. 判断当前按钮的状态
        bt_date_edit.setClickable(!checkd);
        bt_time_edit.setClickable(!checkd);
        for (GlobalDate.BlockStateType g_block_state : g_block_states) {
            if (g_block_state == GlobalDate.BlockStateType.running) {
                curretnState = true;
                break;
            }
            if (g_block_states[g_block_states.length - 1] != GlobalDate.BlockStateType.running) {
                curretnState = false;
            }

        }
        sw_netenable.setClickable(!curretnState);
        if (sw_netenable.isClickable() && !sw_netenable.isChecked()) {
            // 设置状态
            bt_date_edit.setClickable(true);
            bt_time_edit.setClickable(true);
        } else {
            // 设置状态
            bt_date_edit.setClickable(false);
            bt_time_edit.setClickable(false);
        }
        sw_netenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SystemTimeSettingUtil.setAutoDateTime(getContext(), isChecked ? 1 : 0);
                bt_date_edit.setClickable(!isChecked);
                bt_time_edit.setClickable(!isChecked);
            }
        });
        return view;
    }

    public void datehandle(View view) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器
        pvCustomTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                SystemTimeSettingUtil.setSysDate(getContext(), year, month, day);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
        pvCustomTime.show();
        //finish();
    }

    public void timehandle(View view) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器
        pvCustomTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                SystemTimeSettingUtil.setSysTime(getContext(), hour, min, second);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{false, false, false, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
        pvCustomTime.show();
        //finish();
    }

    void updateTimeThread() {
        if (timeUpdataThread != null)
            timeUpdataThread.myStop();
        timeUpdataThread = new TimeThread();
        timeUpdataThread.start();
    }

    private class TimeThread extends Thread {
        private boolean mFinished = false;
        private boolean myPause = false;

        public void myStop() {
            mFinished = true;
        }

        public void myPause() {
            myPause = true;
        }

        @Override
        public void run() {
            while (!mFinished) {
                Message msg1 = new Message();
                msg1.what = 1;
                handler.sendMessage(msg1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
        if (timeUpdataThread != null)
            timeUpdataThread.myStop();
        timeUpdataThread = null;
    }


}
