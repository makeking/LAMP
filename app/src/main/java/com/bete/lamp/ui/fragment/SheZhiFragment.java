package com.bete.lamp.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bete.lamp.R;
import com.bete.lamp.ui.fsetting.SheZhiSettingFragment;
import com.bete.lamp.ui.normal.BaseFragment;
import com.utils.LogUtils;

public class SheZhiFragment extends BaseFragment {
    private static final String TAG = "Admin1Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /////////////////////////////////////////////////////////////////////////
    protected View view;
    private Fragment[] mFragments = new Fragment[2];
    public SheZhiSettingFragment sheZhiSettingFragment;
    public Fragment fragment;
    int currentIndex = 0;
    Fragment lastVisibleFragment;

    public SheZhiFragment() {
        // Required empty public constructor
    }

//    public static SheZhiFragment newInstance(String param1, String param2) {
//        SheZhiFragment fragment = new SheZhiFragment();
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
                sheZhiSettingFragment = new SheZhiSettingFragment(),
                fragment = new Fragment()
        };
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shezhi, container, false);
        setIndexSelected(0, null);
        return view;
    }

    //设置Fragment页面
    private void setIndexSelected(int index, Fragment fragment) {
        //开启事务
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        //隐藏当前Fragment
        ft.hide(mFragments[currentIndex]);
        if (index == 0) {
            //判断Fragment是否已经添加
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.shezhi_content, mFragments[index], "SheZhiSettingFragment").show(mFragments[index]);
            } else {
                //显示新的Fragment
                ft.show(mFragments[index]);
            }
        } else {
            mFragments[index] = fragment;
            ft.add(R.id.shezhi_content, mFragments[index],"Fragment").show(mFragments[index]);
        }
        ft.commit();
        //ft.commitAllowingStateLoss();
        currentIndex = index;
    }

    public void changeRadio(int index, Fragment fragment) {
        if (index == 0) {
            setIndexSelected(index, fragment);
        }
        if (index == 1) {
            setIndexSelected(index, fragment);
        }
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

    /*5、内存销毁活动后，保存ctivity中当前显示的fragment的tag*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVisibleFragment = mFragments[currentIndex];
        outState.putString("lastVisibleFragment", lastVisibleFragment.getTag());
    }
}
