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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.bete.lamp.R;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.LogUtils;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingAdminFragment;
import static com.myutils.GlobalDate.DEVICE;

public class SheZhiSettingServerFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingServerFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Button bt_ok;
    private Switch switch_serverable;
    private EditText et_serverip,et_port, et_updateaddr, et_downloadaddr, et_timeout;

    public SheZhiSettingServerFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingServerFragment newInstance(String param1, String param2) {
//        SheZhiSettingServerFragment fragment = new SheZhiSettingServerFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_server, container, false);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOkHandle(v);
            }
        });
        switch_serverable = (Switch) view.findViewById(R.id.switch_serverable);

        et_serverip = (EditText) view.findViewById(R.id.et_serverip);
        et_port = (EditText) view.findViewById(R.id.et_port);
        et_updateaddr = (EditText) view.findViewById(R.id.et_updateaddr);
        et_downloadaddr = (EditText) view.findViewById(R.id.et_downloadaddr);
        et_timeout = (EditText) view.findViewById(R.id.et_timeout);
        et_serverip.setText(GlobalDate.defaultServerCanShu.getIpaddr());
        et_port.setText(GlobalDate.defaultServerCanShu.getPort());
        et_timeout.setText(String.valueOf(GlobalDate.defaultServerCanShu.getTimeout()) );
        et_downloadaddr.setText(String.valueOf(GlobalDate.defaultServerCanShu.getDownloaddir()) );
        et_updateaddr.setText(String.valueOf(GlobalDate.defaultServerCanShu.getUpdatedir()) );
        switch_serverable.setChecked(SharedPreferencesUtils.getServerAbleState());
        switch_serverable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    SharedPreferencesUtils.setServerAbleState(true);
                }
                else
                {
                    SharedPreferencesUtils.setServerAbleState(false);
                }
            }
        });
        return view;
    }

    public void goOkHandle(View view) {
        GlobalDate.defaultServerCanShu.setIpaddr(et_serverip.getText().toString());
        GlobalDate.defaultServerCanShu.setPort(et_port.getText().toString());
        GlobalDate.defaultServerCanShu.setTimeout(Integer.valueOf(et_timeout.getText().toString()) );
        GlobalDate.defaultServerCanShu.setUpdatedir(et_updateaddr.getText().toString());
        GlobalDate.defaultServerCanShu.setDownloaddir(et_downloadaddr.getText().toString());
        SharedPreferencesUtils.saveServerCanShu(GlobalDate.defaultServerCanShu);
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage("设置成功");
        builder.setTitle(getString(R.string.tishi));
        builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create(R.layout.dialog_normal_feiquanping_message).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
