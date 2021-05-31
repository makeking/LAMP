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

import java.util.LinkedList;

public class CXChaXunFragment extends BaseFragment {
    private static final String TAG = "CXChaXunFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LinkedList<String> mDataList = new LinkedList<>();
    ////////////////////////////////////////////////////////////////
    protected View view;
    private Fragment[]mFragments;
    public ChaXunStep1Fragment chaXunStep1Fragment;
    public ChaXunStep2Fragment chaXunStep2Fragment;
    int currentIndex=0;
    ///////////////////////////////////////////////////////////////////////////////////////////
    public CXChaXunFragment() {
        // Required empty public constructor
    }

//    public static CXChaXunFragment newInstance(String param1, String param2) {
//        CXChaXunFragment fragment = new CXChaXunFragment();
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
        mFragments = new Fragment[] {
                //配置、实时、结果、设置
                chaXunStep1Fragment = new ChaXunStep1Fragment(),
                chaXunStep2Fragment = new ChaXunStep2Fragment()
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cx_cha_xun, container, false);

//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        //隐藏当前Fragment
//        ft.hide(mFragments[0]);
//        //判断Fragment是否已经添加
//        ft.add(R.id.cxchaxun_content, mFragments[0]).commit();
        setIndexSelected(0);
        return view;
    }

//    //设置Fragment页面
//    private void setIndexSelected(int index) {
////        if (g_currentStep[g_currentBlock] == index) {
////            return;
////        }
//        //开启事务
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        //隐藏当前Fragment
//        ft.hide(mFragments[currentIndex]);
//        //判断Fragment是否已经添加
//        if (!mFragments[index].isAdded()) {
//            ft.add(R.id.cxchaxun_content,mFragments[index]).show(mFragments[index]);
//        }else {
//            //显示新的Fragment
//            ft.show(mFragments[index]);
//        }
//        ft.commit();
//        //ft.commitAllowingStateLoss();
//        currentIndex = index;
//    }

    //设置Fragment页面
    private void setIndexSelected(int index) {
        FragmentManager mFragmentManager = getChildFragmentManager();
        //开启事务
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        //隐藏当前Fragment
        ft.hide(mFragments[currentIndex]);
        //判断Fragment是否已经添加
        if (mFragments[index].isAdded()||null != mFragmentManager.findFragmentByTag( index + "" )) {
            ft.show(mFragments[index]);
        } else {
            //显示新的Fragment
            mFragmentManager.executePendingTransactions();
            ft.add(R.id.cxchaxun_content, mFragments[index]).show(mFragments[index]);
            LogUtils.d(TAG+":"+"ADD!!!!!!!!!!!!!!!!");
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }

    public void changeRadio(int index)
    {
        if(index==0) {
            setIndexSelected(index);
        }
        if(index==1)
        {
            setIndexSelected(index);
        }
        if(index==2)
        {
            setIndexSelected(index);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }//

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
