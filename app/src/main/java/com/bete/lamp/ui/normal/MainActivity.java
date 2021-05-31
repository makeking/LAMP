package com.bete.lamp.ui.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.titleObserver.TitleData;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.myutils.GlobalDate.PRODUCT_NAME;
import static com.myutils.GlobalDate.g_device_state;


/**
 * 主页面
 *
 * @author les
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Context context;
    private Button bt_shiyan;

    private TimeThread timeThread;
    private static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private TextView tv_product_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.activity_main);
        bt_shiyan = (Button) findViewById(R.id.bt_shiyan);
        tv_product_type = (TextView) findViewById(R.id.tv_product_type);
        tv_product_type.setText(PRODUCT_NAME);
        startTitleThread();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void goShiYanHandle(View view) {
        if (g_device_state == GlobalDate.DeviceStateType.needcheck) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
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
        } else if (g_device_state == GlobalDate.DeviceStateType.ready) {
            startActivity(new Intent(context, CheckActivity.class));
        }
    }

    public void goXiangMuHandle(View view) {
//        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
        startActivity(new Intent(context, ProjectActivity.class));
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void goChaXunHandle(View view) {
        startActivity(new Intent(context, ChaXunActivity.class));
    }

    public void goSheZhiHandle(View view) {
        startActivity(new Intent(context, SheZhiActivity.class));
    }

    public void goHelpHandle(View view) {
        startActivity(new Intent(context, HelpActivity.class));
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
                        String time = dateFormatter.format(Calendar.getInstance().getTime());//获取当前时间
                        TitleData.getInstance().setTitleTime(time);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void startTitleThread() {
        if (timeThread != null)
            timeThread.myStop();
        timeThread = new TimeThread();
        timeThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeThread != null)
            timeThread.myStop();
        timeThread = null;
    }

}
