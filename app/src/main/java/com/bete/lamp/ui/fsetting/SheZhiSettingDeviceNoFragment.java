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
import android.widget.EditText;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingAdminFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingSoftVersionFragment;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.packageVersion;

public class SheZhiSettingDeviceNoFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingDeviceNoFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private EditText editText;
    ProgressDialog progressDialog;
    McuSerial mcuSerial;
    private Thread linThread;
    Button bt_ok;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                GlobalDate.deviceno = editText.getText().toString();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("设备编号设置成功！");
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingAdminFragment);
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            else
            {
                closeProgressDialog();
            }
        }
    };

    public SheZhiSettingDeviceNoFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingDeviceNoFragment newInstance(String param1, String param2) {
//        SheZhiSettingDeviceNoFragment fragment = new SheZhiSettingDeviceNoFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_deviceno, container, false);
        editText = (EditText) view.findViewById(R.id.et_deviceno);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOkHandle();
            }
        });
        if(DEVICE) {
            GlobalDate.deviceno = String.valueOf(McuSerial.getInstance().getDeviceSerialNo());
            editText.setText(GlobalDate.deviceno);
        }
        return view;
    }

    public void goOkHandle() {
        showProgressDialog();
        linThread = new Thread() {
            @Override
            public void run() {
                Object[] objects = null;
                McuSerial tempThread = McuSerial.getInstance();
                objects = tempThread.setDeviceSerialNo((editText.getText().toString()));
                if ((int) (objects[0]) == 1) {
                    Message msg1 = new Message();
                    msg1.what = 1;
                    handler.sendMessage(msg1);
                } else {
                    Message msg1 = new Message();
                    msg1.what = 2;
                    handler.sendMessage(msg1);
                }
            }
        };
        linThread.start();
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            LogUtils.d("showProgressDialog: ");
            progressDialog.setMessage("请稍后");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog =null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
        closeProgressDialog();
    }
}
