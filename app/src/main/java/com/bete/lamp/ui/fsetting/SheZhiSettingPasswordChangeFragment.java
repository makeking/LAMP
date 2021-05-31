package com.bete.lamp.ui.fsetting;

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
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.db.UserPermission;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.utils.LogUtils;
import com.utils.SystemTimeSettingUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUserManageFragment;

public class SheZhiSettingPasswordChangeFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingPasswordChangeFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Button bt_ok;
    private EditText editText1;
    private EditText editText2;

    public SheZhiSettingPasswordChangeFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingPasswordChangeFragment newInstance(String param1, String param2) {
//        SheZhiSettingPasswordChangeFragment fragment = new SheZhiSettingPasswordChangeFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_password_change, container, false);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOk();
            }
        });

        editText1 = (EditText)view.findViewById(R.id.et_mima);
        editText2 = (EditText)view.findViewById(R.id.et_mima2);
        return view;
    }

    public void goOk() {
        String mima1=editText1.getText().toString();
        String mima2=editText2.getText().toString();
        if(!(mima1.equals(mima2))) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.liangcishurumimabuyizhi));
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        }
        else {
            List<UserPermission> userpermissions = LitePal.where("name like ?", "mainte").find(UserPermission.class);
            UserPermission admin = userpermissions.get(0);
            admin.setMima(mima1);
            admin.save();
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.shezhichenggong_qingchongxindenglu));
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();

            ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingFragment);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
