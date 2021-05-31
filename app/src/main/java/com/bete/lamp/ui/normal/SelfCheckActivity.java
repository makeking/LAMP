package com.bete.lamp.ui.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.bete.lamp.customWidget.ProgressSeek;
import com.bete.lamp.R;
import com.bete.lamp.message.SelfCheckProcessValueEvent;
import com.bete.lamp.thread.SelfCheckThread;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.jessyan.autosize.internal.CustomAdapt;

import static com.myutils.GlobalDate.g_device_state;

public class SelfCheckActivity extends BaseActivity{
    private Context context;
    private ProgressSeek ps_selfcheck;
    private TextView tv_selfcheck_tishi;
    private int preocesvalue = 0;
    private SelfCheckThread selfCheckThread;
    private TextView tv_version;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                CustomDialog.Builder builder = new CustomDialog.Builder(SelfCheckActivity.this);

                builder.setMessage(getString(R.string.zijianshibai_shifouchongxinzijian));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ps_selfcheck.setProgress(0);
                        tv_selfcheck_tishi.setText("");
                        selfCheckThread = new SelfCheckThread();
                        selfCheckThread.start();
                    }
                });
                builder.setNegativeButton(getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        g_device_state = GlobalDate.DeviceStateType.needcheck;
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                });
                CustomDialog customDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
                customDialog.setCanceledOnTouchOutside(false);
                customDialog.setCancelable(false);
                customDialog.show();
            }
            else if(msg.what==2)
            {
                g_device_state = GlobalDate.DeviceStateType.ready;
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check);
        context = SelfCheckActivity.this;
        ps_selfcheck = (ProgressSeek) findViewById(R.id.ps_selfcheck);
        tv_selfcheck_tishi = (TextView) findViewById(R.id.tv_selfcheck_tishi);
        tv_selfcheck_tishi.setText("");
        ps_selfcheck.setProgress(0);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("V" + GlobalDate.SoftwareVersion);
    }

    @Override
    protected void onStart() {
        super.onStart();
//            g_device_state = GlobalDate.DeviceStateType.needcheck;
            selfCheckThread = new SelfCheckThread();
            selfCheckThread.start();
        if (preocesvalue > 0) {
            ps_selfcheck.setProgress(preocesvalue);
        }
        if (preocesvalue > 100) {
            ps_selfcheck.setProgress(0);
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
        LogUtils.d("progress：" + preocesvalue);
        if ((preocesvalue >= 1) && (preocesvalue < 25)) {
            return;
        } else if ((preocesvalue >= 25) && (preocesvalue < 50)) {
            return;
        } else if ((preocesvalue >= 50) && (preocesvalue < 75)) {
            return;
        } else if ((preocesvalue >= 75) && (preocesvalue < 100)) {
            return;
        } else if (preocesvalue == 100) {
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
            return;
        } else if (preocesvalue == 101) {
            tv_selfcheck_tishi.setText("通信模块错误，自检失败");
        } else if (preocesvalue == 102) {
            tv_selfcheck_tishi.setText("运动模块错误，自检失败");
        } else if (preocesvalue == 103) {
            tv_selfcheck_tishi.setText("光路模块错误，自检失败");
        } else if (preocesvalue == 104) {
            tv_selfcheck_tishi.setText("温控模块错误，自检失败");
        }
    }

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelfCheckProcessValueEvent selfCheckProcessValueEvent) {
        preocesvalue = selfCheckProcessValueEvent.getProcessValue();
        if (preocesvalue > 0) {
            ps_selfcheck.setProgress(preocesvalue);
        }
        if (preocesvalue > 100) {
            ps_selfcheck.setProgress(0);
            tv_selfcheck_tishi.setText("自检未通过，请重新自检");
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
        LogUtils.v("progress：" + preocesvalue);
        if (preocesvalue < 100) {
            tv_selfcheck_tishi.setText("自检. . . "+preocesvalue+"%");
            return;
        } else if (preocesvalue == 100) {
            tv_selfcheck_tishi.setText("自检通过！");
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
            return;
        } else if (preocesvalue == 101) {
            tv_selfcheck_tishi.setText("通信模块错误，自检失败");
        } else if (preocesvalue == 102) {
            tv_selfcheck_tishi.setText("运动模块错误，自检失败");
        } else if (preocesvalue == 103) {
            tv_selfcheck_tishi.setText("光路模块错误，自检失败");
        } else if (preocesvalue == 104) {
            tv_selfcheck_tishi.setText("温控模块错误，自检失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
