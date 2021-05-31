package com.bete.lamp.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.utils.LogUtils;

public class SettingProjectFragment extends BaseFragment {
    private static final String TAG = "SettingProjectFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /////////////////////////////////////////////////////////////////////////////////////
    public SettingProjectStep1Fragment settingProjectStep1Fragment;
    public SettingProjectStep2Fragment settingProjectStep2Fragment;
    private Fragment[] mFragments;

    private String selectFile = "";

    public String getSelectFile() {
        return selectFile;
    }

    public void setSelectFile(String selectFile) {
        this.selectFile = selectFile;
    }

    public SettingProjectFragment() {
        // Required empty public constructor
    }

//    public static SettingProjectFragment newInstance(String param1, String param2) {
//        SettingProjectFragment fragment = new SettingProjectFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        mFragments = new Fragment[]{
                //配置、实时、结果、设置
                settingProjectStep1Fragment = new SettingProjectStep1Fragment(),
                settingProjectStep2Fragment = new SettingProjectStep2Fragment()
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_project, container, false);
        changeRadio(0);
        return view;
    }
//    //设置Fragment页面
//    private void setIndexSelected(int index) {
//        //开启事务
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        //隐藏当前Fragment
//        ft.hide(mFragments[0]);
//        ft.hide(mFragments[1]);
//        //判断Fragment是否已经添加
//        if (!mFragments[index].isAdded()) {
//            ft.add(R.id.settingproject_content, mFragments[index]).show(mFragments[index]);
//            LogUtils.d("ADD!!!!!!!!!!!!!!!!");
//        } else {
//            //显示新的Fragment
//            ft.show(mFragments[index]);
//        }
//        ft.commit();
//    }

    //设置Fragment页面
    private void setIndexSelected(int index) {
        FragmentManager mFragmentManager = getChildFragmentManager();
        //开启事务
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        //隐藏当前Fragment
        ft.hide(mFragments[0]);
        ft.hide(mFragments[1]);
        //判断Fragment是否已经添加
        if (mFragments[index].isAdded()||null != mFragmentManager.findFragmentByTag( index + "" )) {
            ft.show(mFragments[index]);
        } else {
            //显示新的Fragment
            mFragmentManager.executePendingTransactions();
            ft.add(R.id.settingproject_content, mFragments[index]).show(mFragments[index]);
            LogUtils.d(TAG+":"+"ADD!!!!!!!!!!!!!!!!");
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public void changeRadio(int index) {
        if (index == 0) {
            setIndexSelected(0);
        }
        if (index == 1) {
            setIndexSelected(1);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d("onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
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
