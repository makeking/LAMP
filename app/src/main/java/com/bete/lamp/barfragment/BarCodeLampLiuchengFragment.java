package com.bete.lamp.barfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.bete.lamp.R;
import com.bete.lamp.adapter.PCRLiuchengCanShuItemAdpter;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;

public class BarCodeLampLiuchengFragment extends BaseFragment {
    private RecyclerView rv_liucheng_step;
    private PCRLiuchengCanShuItemAdpter pcrLiuchengCanShuItemAdpter;
    private LinearLayoutManager layoutManager;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_code_lamp_liucheng, container, false);
        rv_liucheng_step = (RecyclerView) view.findViewById(R.id.rv_liucheng_step);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_liucheng_step.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        pcrLiuchengCanShuItemAdpter = new PCRLiuchengCanShuItemAdpter(getActivity(), pcrProject.pcrLiuChengCanShuItems);
        pcrLiuchengCanShuItemAdpter.setEditList(pcrProject.pcrLiuChengCanShuItems);
        rv_liucheng_step.setAdapter(pcrLiuchengCanShuItemAdpter);
        return view;
    }

    private void setDataToUI() {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        pcrLiuchengCanShuItemAdpter.setEditList(pcrProject.pcrLiuChengCanShuItems);
        rv_liucheng_step.setAdapter(pcrLiuchengCanShuItemAdpter);
        pcrLiuchengCanShuItemAdpter.notifyDataSetChanged();
    }

    private void getDataFromUI() {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
    }

    private int getIntFromSpiner(Spinner v) {
        String tempstr;
        int tempint;
        tempstr = v.getSelectedItem().toString();
        try {
            tempint = Integer.valueOf(tempstr);
            return tempint;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double getDoubleFromEditText(EditText v) {
        String tempstr;
        double tempdouble;
        tempstr = v.getText().toString();
        try {
            tempdouble = Double.valueOf(tempstr);
            return tempdouble;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int getIntFromEditText(EditText v) {
        String tempstr;
        int tempint;
        tempstr = v.getText().toString();
        try {
            tempint = Integer.valueOf(tempstr);
            return tempint;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart()");
//        setDataToUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
//        getDataFromUI();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            setDataToUI();
            LogUtils.d( "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            getDataFromUI();
            LogUtils.d("onHiddenChanged hide");
        }
    }
}
