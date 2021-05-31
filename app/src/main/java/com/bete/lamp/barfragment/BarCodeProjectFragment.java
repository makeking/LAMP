package com.bete.lamp.barfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bete.lamp.R;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;

public class BarCodeProjectFragment extends BaseFragment {

    public static final String TAG = "BarCodeProjectFragment";
    private EditText et_value1,et_value2,et_value3,et_value4,et_value5;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_bar_code_project, container, false);
        et_value1 = (EditText) view.findViewById(R.id.et_value1);
        et_value2 = (EditText) view.findViewById(R.id.et_value2);
        et_value3 = (EditText) view.findViewById(R.id.et_value3);
        et_value4 = (EditText) view.findViewById(R.id.et_value4);
        et_value5 = (EditText) view.findViewById(R.id.et_value5);
        //setDataToUI();
        return view;
    }

    private void setDataToUI()
    {
        BarRareData barRareData = ((SheZhiActivity)getActivity()).getBarRareData();
        et_value1.setText(String.valueOf(barRareData.bartype));
        et_value2.setText(String.format("%d",barRareData.productyear)+ String.format("%02d",barRareData.productmonth)+ String.format("%02d",barRareData.productday));
        et_value3.setText(String.valueOf(barRareData.num));
        et_value4.setText(String.format("%d",barRareData.limityear)+ String.format("%02d",barRareData.limitmonth) + String.format("%02d",barRareData.limitday));
        et_value5.setText(String.format("%d",barRareData.isi));
    }

    private void getDataFromUI()
    {
        BarRareData barRareData = ((SheZhiActivity)getActivity()).getBarRareData();

        //if ((et_value1.toString() != null) || (et_value1.toString().isEmpty()))
        try {
            barRareData.bartype = Integer.valueOf(et_value1.getText().toString());
        }catch (NumberFormatException e)
        {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.bartype = 0;
        }

        try {
            barRareData.productyear = Integer.valueOf(et_value2.getText().toString())/10000;
            barRareData.productmonth = (Integer.valueOf(et_value2.getText().toString())%10000)/100;
            barRareData.productday = Integer.valueOf(et_value2.getText().toString())%100;
        }catch (NumberFormatException e)
        {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.productyear = 0;
            barRareData.productmonth = 0;
            barRareData.productday = 0;
        }

        try {
            barRareData.num = Integer.valueOf(et_value3.getText().toString());
        }catch (NumberFormatException e)
        {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.num = 0;
        }

        try {
            barRareData.isi = Integer.valueOf(et_value5.getText().toString());
        }catch (NumberFormatException e)
        {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.isi = 0;
        }

//        try {
//            barRareData.limityear = Integer.valueOf(et_value4.getText().toString())/10000;
//            barRareData.limitmonth = (Integer.valueOf(et_value4.getText().toString())%10000)/100;
//            barRareData.limitday = Integer.valueOf(et_value4.getText().toString())%100;
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(barRareData.limityear,barRareData.limitmonth,barRareData.limitday);
//            calendar.add(Calendar.DAY_OF_YEAR,-180);
//            barRareData.limitday = calendar.get(Calendar.DAY_OF_MONTH);
//        }catch (NumberFormatException e)
//        {
//            LogUtils.d(e.toString());
//            e.printStackTrace();
//            barRareData.limityear = 0;
//            barRareData.limitmonth = 0;
//            barRareData.limitday = 0;
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        setDataToUI();
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
        getDataFromUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, "onDetach");
    }

}