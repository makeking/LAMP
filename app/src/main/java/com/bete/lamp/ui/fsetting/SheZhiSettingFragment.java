package com.bete.lamp.ui.fsetting;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.db.UserPermission;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingAdminFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingDeviceInfoFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingJiaoZhunFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingLiangDuFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingMainteFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingSoftVersionFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingWifiFragment;
import static com.myutils.GlobalDate.packageVersion;

public class SheZhiSettingFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /////////////////////////////////////////////////////////////////////////
    private Button setting_bt_ruanjianbanben, setting_bt_jiaozhunfanwei, setting_bt_mainte,setting_bt_deviceinfo,setting_bt_liangdu,setting_bt_wifi,setting_bt_admin;
    LinkedList<String> usrsList = new LinkedList<>();
    String usr;
    Spinner sp_usrs;
    EditText et_mima;
    TextView tv_logo_message;

    EditText et_mima_mainte;


    public SheZhiSettingFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingFragment newInstance(String param1, String param2) {
//        SheZhiSettingFragment fragment = new SheZhiSettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting, container, false);
        setting_bt_ruanjianbanben = (Button) view.findViewById(R.id.setting_bt_ruanjianbanben);
        setting_bt_ruanjianbanben.setText(getString(R.string.ruanjianbanben)+"\n"+"V"+String.valueOf(packageVersion(getActivity())));
        setting_bt_ruanjianbanben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingSoftVersionFragment);
            }
        });
        setting_bt_jiaozhunfanwei = (Button) view.findViewById(R.id.setting_bt_jiaozhunfanwei);
        setting_bt_jiaozhunfanwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingJiaoZhunFragment);
            }
        });
        setting_bt_mainte = (Button) view.findViewById(R.id.setting_bt_mainte);
        setting_bt_mainte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingMainteFragment);
            }
        });

        setting_bt_deviceinfo  = (Button) view.findViewById(R.id.setting_bt_deviceinfo);
        setting_bt_deviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingDeviceInfoFragment);
            }
        });

        setting_bt_liangdu  = (Button) view.findViewById(R.id.setting_bt_liangdu);
        setting_bt_liangdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingLiangDuFragment);
            }
        });

        setting_bt_wifi  = (Button) view.findViewById(R.id.setting_bt_wifi);
        setting_bt_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingWifiFragment);
            }
        });

        setting_bt_admin  = (Button) view.findViewById(R.id.setting_bt_admin);
        setting_bt_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getActivity(), R.layout.dialog_normal_feiquanping_logo, null);
                et_mima_mainte = (EditText) view.findViewById(R.id.et_mima);
                tv_logo_message = (TextView) view.findViewById(R.id.tv_logo_message);
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("");
                builder.setTitle(getString(R.string.weihudenglu));
                builder.setPositiveButton(getString(R.string.denglu), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        List<UserPermission> userpermissions = LitePal.where("name like ?", "mainte").find(UserPermission.class);
                        String mima = userpermissions.get(0).getMima();
                        LogUtils.d(mima);
                        if (et_mima_mainte.getText().toString().equals(mima)) {
                            ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingAdminFragment);
                            dialog.dismiss();
                        } else if (et_mima_mainte.getText().toString().equals("0")) {
                            ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingAdminFragment);
                            dialog.dismiss();
                        } else {
                            tv_logo_message.setText(R.string.mimacuowu);
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                CustomDialog customDialog = builder.create(view);
                customDialog.setCanceledOnTouchOutside(false);
                customDialog.setCancelable(false);
                customDialog.show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
