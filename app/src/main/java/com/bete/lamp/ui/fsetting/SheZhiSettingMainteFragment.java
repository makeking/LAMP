package com.bete.lamp.ui.fsetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.LogUtils;

import static com.myutils.GlobalDate.g_kadjustref;

public class SheZhiSettingMainteFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingMainteFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    EditText et_k_jiaozhun;
    Button bt_jiaozhun_save;
    public SheZhiSettingMainteFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingMainteFragment newInstance(String param1, String param2) {
//        SheZhiSettingMainteFragment fragment = new SheZhiSettingMainteFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_mainte, container, false);
        et_k_jiaozhun = (EditText) view.findViewById(R.id.et_k_jiaozhun);
        bt_jiaozhun_save = (Button) view.findViewById(R.id.bt_jiaozhun_save);
        g_kadjustref = SharedPreferencesUtils.getKAdjustRef();
        et_k_jiaozhun.setText(String.valueOf(g_kadjustref));
        bt_jiaozhun_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_kadjustref = GlobalDate.getDoubleFromEditText(et_k_jiaozhun);
                SharedPreferencesUtils.saveKAdjustRef(g_kadjustref);
                g_kadjustref = SharedPreferencesUtils.getKAdjustRef();
                et_k_jiaozhun.setText(String.valueOf(g_kadjustref));
            }
        });
        return view;
    }
}
