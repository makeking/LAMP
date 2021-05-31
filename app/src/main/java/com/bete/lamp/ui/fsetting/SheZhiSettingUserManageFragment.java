package com.bete.lamp.ui.fsetting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bete.lamp.R;
import com.bete.lamp.adapter.IItem;
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

import static com.bete.lamp.ui.normal.SheZhiActivity.ISheZhiSettingUserAddFragment;

public class SheZhiSettingUserManageFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingUserManageFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private RecyclerView recycler_view_usr;
    List<UsrItem> UserItemList;
    List<UserPermission> userPermissionList;
    private Button bt_add,bt_dec;
    UsrItemAdapter usritem_adapter;
    int position;
    public SheZhiSettingUserManageFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingUserManageFragment newInstance(String param1, String param2) {
//        SheZhiSettingUserManageFragment fragment = new SheZhiSettingUserManageFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_user_manage, container, false);
        recycler_view_usr = (RecyclerView) view.findViewById(R.id.recycler_view_usr);
        bt_add = (Button) view.findViewById(R.id.bt_add);
        bt_dec = (Button) view.findViewById(R.id.bt_dec);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonAddHandle(v);
            }
        });
        bt_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDecHandle(v);
            }
        });
        updateItem();
        return view;
    }

    private void updateItem(){
        userPermissionList = LitePal.where("level = ?", "2").find(UserPermission.class);
//        userPermissionList = LitePal.findAll(UserPermission.class);
        LinearLayoutManager layoutManagerRB = new LinearLayoutManager(getActivity());
        recycler_view_usr.setLayoutManager(layoutManagerRB);
        layoutManagerRB.setOrientation(LinearLayoutManager.VERTICAL);

        UserItemList = getUserItemListFromList(userPermissionList);
        usritem_adapter = new UsrItemAdapter(UserItemList);
        recycler_view_usr.setAdapter(usritem_adapter);
        usritem_adapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                userPermissionList.get(position).delete();
                updateItem();
            }
        });
    }

    List<UsrItem> getUserItemListFromList(List<UserPermission> userPermissionItems)
    {
        List<UsrItem> tempList = new ArrayList<>();
        UserPermission userPermissionItem;
        UsrItem usrItem;
        String name="";
        for (int i=0;i<userPermissionItems.size();i++)
        {
            name="";
            userPermissionItem = userPermissionItems.get(i);
            name = userPermissionItem.getName();//getItemName
            LogUtils.d("name:"+name);
            usrItem = new UsrItem(name);
            tempList.add(usrItem);
        }
        return tempList;
    }

    public void onButtonAddHandle(View view) {
        ((SheZhiActivity)getActivity()).changeRadio(ISheZhiSettingUserAddFragment);
    }

    public void onButtonDecHandle(View view) {
        position = usritem_adapter.getMposition();
        if(position==-1)
        {
            CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
            builder1.setMessage("未选择删除项");
            builder1.setTitle("提示");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder1.create(R.layout.dialog_normal_feiquanping_message).show();
        }
        else
        {
            CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
            builder1.setMessage("请确认是否删除用户！");
            builder1.setTitle("提示");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    userPermissionList.get(position).delete();
                    updateItem();
                    dialog.dismiss();
                }
            });
            builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder1.create(R.layout.dialog_normal_feiquanping_message).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
