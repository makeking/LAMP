package com.bete.lamp.ui.fsetting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bete.lamp.R;
import com.bete.lamp.adapter.UsrItemAdapter;
import com.bete.lamp.bean.UsrItem;
import com.bete.lamp.db.UserPermission;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingMainteFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUserAddFragment;
import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUserManageFragment;

public class SheZhiSettingUserAddFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingUserAddFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private RecyclerView recycler_view_usr;
    List<UsrItem> UserItemList;
    List<UserPermission> userPermissionList;
    private Button bt_ok,bt_cancel;
    private EditText et_id,et_mima,et_mima2;
    public SheZhiSettingUserAddFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingUserAddFragment newInstance(String param1, String param2) {
//        SheZhiSettingUserAddFragment fragment = new SheZhiSettingUserAddFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_user_add, container, false);
        et_id = (EditText) view.findViewById(R.id.et_id);
        et_mima = (EditText) view.findViewById(R.id.et_mima);
        et_mima2 = (EditText) view.findViewById(R.id.et_mima2);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOkHandle(v);
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingUserManageFragment);
            }
        });
        return view;
    }

    public void goOkHandle(View view) {
//        startActivity(new Intent(getApplicationContext(), SearchListActivity.class));
        String ID = et_id.getText().toString();
        String mima = et_mima.getText().toString();
        String mima2 = et_mima2.getText().toString();
        if(mima.isEmpty()||mima2.isEmpty())
        {
            CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
            builder1.setMessage("密码不可为空！");
            builder1.setTitle("添加用户");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder1.create(R.layout.dialog_normal_feiquanping_message).show();
        }
        else if(!mima.equals(mima2))
        {
            CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
            builder1.setMessage("两次输入密码不一致！");
            builder1.setTitle("添加用户");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder1.create(R.layout.dialog_normal_feiquanping_message).show();
        }
        else {
            userPermissionList = LitePal.where("name = ?", ID).find(UserPermission.class);
            if ((userPermissionList == null)||(userPermissionList.size()==0)) {
                UserPermission userPermission = new UserPermission();
                userPermission.setName(ID);
                userPermission.setMima(mima);
                userPermission.setLevel(2);
                userPermission.save();
                CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                builder1.setMessage("用户添加成功！");
                builder1.setTitle("添加用户");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingUserManageFragment);
                    }
                });
                builder1.create(R.layout.dialog_normal_feiquanping_message).show();
            } else {
                CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                builder1.setMessage("用户已存在！");
                builder1.setTitle("添加用户");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
