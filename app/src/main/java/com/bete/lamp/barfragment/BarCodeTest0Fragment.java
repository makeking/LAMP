package com.bete.lamp.barfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.bete.lamp.R;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;
import com.utils.simpleArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class BarCodeTest0Fragment extends BaseFragment {
    public static final String TAG = "BarCodeTest0Fragment";
    private EditText et_value1,et_value6,et_value7;
    private EditText et_value8, et_value9, et_value10, et_value11,et_value12,et_value13;
    private EditText et_valueA1,et_valueA2,et_valueA3,et_valueA4,et_valueA5,et_valueA6;
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
        View view = inflater.inflate(R.layout.fragment_bar_code_test0, container, false);
        et_value1 = (EditText) view.findViewById(R.id.et_value1);
        et_valueA1 = (EditText) view.findViewById(R.id.et_valueA1);
        et_valueA2 = (EditText) view.findViewById(R.id.et_valueA2);
        et_valueA3 = (EditText) view.findViewById(R.id.et_valueA3);
        et_valueA4 = (EditText) view.findViewById(R.id.et_valueA4);
        et_valueA5 = (EditText) view.findViewById(R.id.et_valueA5);
        et_valueA6 = (EditText) view.findViewById(R.id.et_valueA6);
        et_value6 = (EditText) view.findViewById(R.id.et_value6);
        et_value7 = (EditText) view.findViewById(R.id.et_value7);
        et_value8 = (EditText) view.findViewById(R.id.et_value8);
        et_value9 = (EditText) view.findViewById(R.id.et_value9);
        et_value10 = (EditText) view.findViewById(R.id.et_value10);
        et_value11 = (EditText) view.findViewById(R.id.et_value11);
        et_value12 = (EditText) view.findViewById(R.id.et_value12);
        et_value13 = (EditText) view.findViewById(R.id.et_value13);
        spinner_item = (Spinner)view.findViewById(R.id.spinner_item);
        List<String> type_data_list = new ArrayList<String>();
        for (int i=1;i<5;i++) {
            type_data_list.add("?????????"+String.valueOf(i));
        }
        //?????????
        simpleArrayAdapter type_arrAdapter = new simpleArrayAdapter<String>(getActivity(), R.layout.spinner_showed_item, type_data_list);
        //????????????
        type_arrAdapter.setDropDownViewResource(R.layout.spinner_option_items);
        //???????????????
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
        et_value1.setText(String.valueOf(barRareData.itemtest0[position].itemtype));
        et_valueA1.setText(String.valueOf(barRareData.itemtest0[position].A[0]-16382));
        et_valueA2.setText(String.valueOf(barRareData.itemtest0[position].A[1]-16382));
        et_valueA3.setText(String.valueOf(barRareData.itemtest0[position].A[2]));
        et_valueA4.setText(String.valueOf(barRareData.itemtest0[position].A[3]));
        et_valueA5.setText(String.valueOf(barRareData.itemtest0[position].A[4]));
        et_valueA6.setText(String.valueOf(barRareData.itemtest0[position].A[5]));
        et_value6.setText(String.valueOf(barRareData.itemtest0[position].fangfa));
        et_value7.setText(String.valueOf(barRareData.itemtest0[position].danwei));
        et_value8.setText(String.valueOf(barRareData.itemtest0[position].kongweino));
        et_value9.setText(String.valueOf(barRareData.itemtest0[position].guangluno));
        et_value10.setText(String.valueOf(barRareData.itemtest0[position].ARG0));
        et_value11.setText(String.valueOf(barRareData.itemtest0[position].ARG1));
        et_value12.setText(String.valueOf(barRareData.itemtest0[position].ARG2));
        et_value13.setText(String.valueOf(barRareData.itemtest0[position].ARG3));
    }

    private void getDataFromUI(int position) {
        BarRareData barRareData = ((SheZhiActivity)getActivity()).getBarRareData();
        //if ((et_value1.toString() != null) || (et_value1.toString().isEmpty()))
        try {
            barRareData.itemtest0[position].itemtype = Integer.valueOf(et_value1.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].itemtype = 0;
        }

        try {
            barRareData.itemtest0[position].A[0] = Integer.valueOf(et_valueA1.getText().toString())+16382;
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[0] = 16382;
        }

        try {
            barRareData.itemtest0[position].A[1] = Integer.valueOf(et_valueA2.getText().toString())+16382;
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[1] = 16382;
        }

        try {
            barRareData.itemtest0[position].A[2] = Integer.valueOf(et_valueA3.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[2] = 0;
        }

        try {
            barRareData.itemtest0[position].A[3] = Integer.valueOf(et_valueA4.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[3] = 0;
        }

        try {
            barRareData.itemtest0[position].A[4] = Integer.valueOf(et_valueA5.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[4] = 0;
        }

        try {
            barRareData.itemtest0[position].A[5] = Integer.valueOf(et_valueA6.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].A[5] = 0;
        }

        try {
            barRareData.itemtest0[position].fangfa = Integer.valueOf(et_value6.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].fangfa = 0;
        }

        try {
            barRareData.itemtest0[position].danwei = Integer.valueOf(et_value7.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].danwei = 0;
        }

        try {
            barRareData.itemtest0[position].kongweino = Integer.valueOf(et_value8.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].kongweino = 0;
        }

        try {
            barRareData.itemtest0[position].guangluno = Integer.valueOf(et_value9.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].guangluno = 0;
        }

        try {
            barRareData.itemtest0[position].ARG0 = Integer.valueOf(et_value10.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].ARG0 = 0;
        }

        try {
            barRareData.itemtest0[position].ARG1 = Integer.valueOf(et_value11.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].ARG1 = 0;
        }

        try {
            barRareData.itemtest0[position].ARG2 = Integer.valueOf(et_value12.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].ARG2 = 0;
        }

        try {
            barRareData.itemtest0[position].ARG3 = Integer.valueOf(et_value13.getText().toString());
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            barRareData.itemtest0[position].ARG3 = 0;
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
