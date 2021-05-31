package com.bete.lamp.barfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.bete.lamp.R;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;
import com.utils.simpleArrayAdapter;

import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class BarCodeQcFragment extends BaseFragment {
    public static final String TAG = "BarCodeQcFragment";
    private EditText et_value1, et_value2, et_value3, et_value4,et_value5,et_value6,et_value7;
    private Spinner spinner_item;
    private int oldPosition = 0;
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
        View view = inflater.inflate(R.layout.fragment_bar_code_qc, container, false);
        et_value1 = (EditText) view.findViewById(R.id.et_value1);
        et_value2 = (EditText) view.findViewById(R.id.et_value2);
        et_value3 = (EditText) view.findViewById(R.id.et_value3);
        et_value4 = (EditText) view.findViewById(R.id.et_value4);
        et_value5 = (EditText) view.findViewById(R.id.et_value5);
        et_value6 = (EditText) view.findViewById(R.id.et_value6);
        et_value7 = (EditText) view.findViewById(R.id.et_value7);

        spinner_item = (Spinner)view.findViewById(R.id.spinner_item);
        List<String> type_data_list = new ArrayList<String>();
        for (int i=1;i<10;i++) {
            type_data_list.add("质检项"+String.valueOf(i));
        }
        //适配器
        simpleArrayAdapter type_arrAdapter = new simpleArrayAdapter<String>(getActivity(), R.layout.spinner_showed_item, type_data_list);
        //设置样式
        type_arrAdapter.setDropDownViewResource(R.layout.spinner_option_items);
        //加载适配器
        spinner_item.setAdapter(type_arrAdapter);
        spinner_item.setSelection(oldPosition, true);
        setDataToUI(oldPosition);
        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("position:"+position);
                getDataFromUI(oldPosition);
                setDataToUI(position);
                oldPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void setDataToUI(int position) {
        BarRareData barRareData = ((SheZhiActivity)getActivity()).getBarRareData();
        et_value1.setText(String.valueOf(barRareData.itemqc[position].itemtype));
        et_value2.setText(String.valueOf(barRareData.itemqc[position].daYuShiNeng));
        et_value3.setText(String.valueOf(barRareData.itemqc[position].errHandle));
        et_value4.setText(String.valueOf(barRareData.itemqc[position].kongweino0));
        et_value5.setText(String.valueOf(barRareData.itemqc[position].kongweino1));
        et_value6.setText(String.valueOf(barRareData.itemqc[position].guangluno));
        et_value7.setText(String.valueOf(barRareData.itemqc[position].refvalue));
    }

    private void getDataFromUI(int position) {
        BarRareData barRareData = ((SheZhiActivity)getActivity()).getBarRareData();
        //if ((et_value1.toString() != null) || (et_value1.toString().isEmpty()))
        try {
            barRareData.itemqc[position].itemtype = Integer.valueOf(et_value1.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].itemtype = 0;
        }

        try {
            barRareData.itemqc[position].daYuShiNeng = Integer.valueOf(et_value2.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].daYuShiNeng = 0;
        }

        try {
            barRareData.itemqc[position].errHandle = Integer.valueOf(et_value3.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].errHandle = 0;
        }

        try {
            barRareData.itemqc[position].kongweino0 = Integer.valueOf(et_value4.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].kongweino0 = 0;
        }

        try {
            barRareData.itemqc[position].kongweino1 = Integer.valueOf(et_value5.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].kongweino1 = 0;
        }

        try {
            barRareData.itemqc[position].guangluno = Integer.valueOf(et_value6.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].guangluno = 0;
        }

        try {
            barRareData.itemqc[position].refvalue = Integer.valueOf(et_value7.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemqc[position].refvalue = 0;
        }
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
        setDataToUI(oldPosition);
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
        getDataFromUI(oldPosition);
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
