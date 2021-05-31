package com.bete.lamp.barfragment;

        import android.content.Context;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.bete.lamp.R;
        import com.bete.lamp.bean.PCRProject;
        import com.bete.lamp.customWidget.EditSpinner;
        import com.bete.lamp.ui.normal.BaseFragment;
        import com.bete.lamp.ui.normal.SheZhiActivity;
        import com.myutils.GlobalDate;
        import com.utils.DecimalInputTextWatcher;
        import com.utils.LogUtils;

        import java.util.ArrayList;
        import java.util.List;

        import static android.view.View.inflate;
        import static com.myutils.GlobalDate.GUANGNUM;
        import static com.myutils.GlobalDate.danwei_list;
        import static com.myutils.GlobalDate.g_device_guang_ables;

public class BarCodeLampProjectFragment extends BaseFragment {
    private EditText et_lot, et_filename;
    private EditText et_limittime;
    private EditText et_neican;
    private EditSpinner sp_innerref;
    private EditSpinner sp_danwei;
    private EditSpinner sp_project_type;
    private TextView tv_danwei;
    private CheckBox[] cb_enables = new CheckBox[4];
    private EditText[] et_names = new EditText[4];
    private TextView[] tv_titles = new TextView[4];
    List<String> tongdao_list = new ArrayList<String>();
    List<String> projecttype_list = new ArrayList<String>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_bar_code_lamp_project, container, false);
//        rg_project_type =  (RadioGroup) view.findViewById(R.id.rg_project_type);
//        rb_type_dingxing = (RadioButton) view.findViewById(R.id.rb_type_dingxing);
//        rb_type_dingliang = (RadioButton) view.findViewById(R.id.rb_type_dingliang);
        sp_project_type = (EditSpinner) view.findViewById(R.id.sp_project_type);
        et_lot = (EditText) view.findViewById(R.id.et_lot);
        et_limittime = (EditText) view.findViewById(R.id.et_limittime);
        sp_innerref = (EditSpinner) view.findViewById(R.id.sp_innerref);
        et_neican = (EditText) view.findViewById(R.id.et_neican);
        sp_danwei = (EditSpinner) view.findViewById(R.id.sp_danwei);
        tv_danwei = (TextView) view.findViewById(R.id.tv_danwei);
        et_filename = (EditText) view.findViewById(R.id.et_filename);
        cb_enables[0] = (CheckBox) view.findViewById(R.id.cb_enable_1);
        cb_enables[1] = (CheckBox) view.findViewById(R.id.cb_enable_2);
        cb_enables[2] = (CheckBox) view.findViewById(R.id.cb_enable_3);
        cb_enables[3] = (CheckBox) view.findViewById(R.id.cb_enable_4);
        et_names[0] = (EditText) view.findViewById(R.id.et_name_1);
        et_names[1] = (EditText) view.findViewById(R.id.et_name_2);
        et_names[2] = (EditText) view.findViewById(R.id.et_name_3);
        et_names[3] = (EditText) view.findViewById(R.id.et_name_4);
        tv_titles[0] = (TextView) view.findViewById(R.id.tv_title_1);
        tv_titles[1] = (TextView) view.findViewById(R.id.tv_title_2);
        tv_titles[2] = (TextView) view.findViewById(R.id.tv_title_3);
        tv_titles[3] = (TextView) view.findViewById(R.id.tv_title_4);
        for(int i =0;i<GUANGNUM;i++) {
            cb_enables[i].setEnabled(g_device_guang_ables[i]);
            et_names[i].setEnabled(g_device_guang_ables[i]);
            tv_titles[i].setEnabled(g_device_guang_ables[i]);
        }

        et_neican.addTextChangedListener(new DecimalInputTextWatcher(2,0));

        tongdao_list.clear();
        tongdao_list.add("无");
        if(g_device_guang_ables[0])
        {
            tongdao_list.add(GlobalDate.g_guang_name1s.get(0));
        }
        if(g_device_guang_ables[1])
        {
            tongdao_list.add(GlobalDate.g_guang_name2s.get(0));
        }
        if(g_device_guang_ables[2])
        {
            tongdao_list.add(GlobalDate.g_guang_name3s.get(0));
        }
        if(g_device_guang_ables[3])
        {
            tongdao_list.add(GlobalDate.g_guang_name4s.get(0));
        }
//        tongdao_list.add("FAM");
//        tongdao_list.add("VIC/HEX");
//        tongdao_list.add("ROX");
//        tongdao_list.add("Cy5");

        projecttype_list.clear();
        projecttype_list.add("定性");
        projecttype_list.add("定量");

        initInnerRefSpinner();
        sp_innerref.selectItem(tongdao_list.size() - 1);

        initDanWeiSpinner();
        sp_danwei.selectItem(danwei_list.size() - 1);


        initProjectTypeSpinner();
        sp_project_type.selectItem(projecttype_list.size()-1);
        //setDataToUI();
        return view;
    }

    private void initInnerRefSpinner() {
        sp_innerref.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_innerref.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_innerref.setDropDownVerticalOffset(0);
        sp_innerref.setDropDownDrawableSpacing(60);
        sp_innerref.setDropDownHeight(240);
        sp_innerref.setEditable(false);
        sp_innerref.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return tongdao_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return tongdao_list.toArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(getActivity(), R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_innerref.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_innerref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_innerref.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        sp_innerref.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

    private void initDanWeiSpinner() {
        sp_danwei.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_danwei.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_danwei.setDropDownVerticalOffset(0);
        sp_danwei.setDropDownDrawableSpacing(60);
        sp_danwei.setDropDownHeight(240);
        sp_danwei.setEditable(false);
        sp_danwei.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return danwei_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return danwei_list.toArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(getActivity(), R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_danwei.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_danwei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_danwei.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        sp_danwei.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

    private void initProjectTypeSpinner() {
        sp_project_type.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_project_type.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_project_type.setDropDownVerticalOffset(0);
        sp_project_type.setDropDownDrawableSpacing(60);
        sp_project_type.setDropDownHeight(240);
        sp_project_type.setEditable(false);
        sp_project_type.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return projecttype_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return projecttype_list.toArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(getActivity(), R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_project_type.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_project_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_project_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if(position==0)
                {
                    tv_danwei.setVisibility(View.INVISIBLE);
                    sp_danwei.setVisibility(View.INVISIBLE);
                    pcrProject.project_type = GlobalDate.ProjectType.dingxing;
//                    rl_dingliang.setVisibility(View.GONE);
//                    rl_dingxing.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_danwei.setVisibility(View.VISIBLE);
                    sp_danwei.setVisibility(View.VISIBLE);
                    pcrProject.project_type = GlobalDate.ProjectType.dingliang;
//                    rl_dingxing.setVisibility(View.GONE);
//                    rl_dingliang.setVisibility(View.VISIBLE);
                }
            }
        });

        sp_project_type.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

    private void setDataToUI()
    {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        if (pcrProject.project_type == GlobalDate.ProjectType.dingliang) {
            sp_project_type.selectItem(1);
            tv_danwei.setVisibility(View.VISIBLE);
            sp_danwei.setVisibility(View.VISIBLE);
//            rb_type_dingliang.setChecked(true);
//            rb_type_dingxing.setChecked(false);
        } else{
            sp_project_type.selectItem(0);
            tv_danwei.setVisibility(View.INVISIBLE);
            sp_danwei.setVisibility(View.INVISIBLE);
//            rb_type_dingliang.setChecked(false);
//            rb_type_dingxing.setChecked(true);
        }
        et_filename.setText(String.valueOf(pcrProject.project_name));
        et_lot.setText(pcrProject.project_lot);
        et_limittime.setText(pcrProject.project_limit);
        et_neican.setText(String.valueOf(pcrProject.project_neican));
        sp_innerref.selectItem(pcrProject.project_neican_tongdao);
        sp_danwei.selectItem(danwei_list.indexOf(pcrProject.project_danwei));
        for (int i = 0; i < 4; i++) {
            if(g_device_guang_ables[i]) {
                cb_enables[i].setChecked(pcrProject.project_item_ables[i]);
                et_names[i].setText(pcrProject.project_item_names[i]);
            }
        }
    }

    private void getDataFromUI()
    {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();

//        if (rb_type_dingliang.isChecked())
//            pcrProject.project_type = GlobalDate.ProjectType.dingliang;
//        else if (rb_type_dingxing.isChecked())
//            pcrProject.project_type = GlobalDate.ProjectType.dingxing;
//        else
//            pcrProject.project_type = GlobalDate.ProjectType.dingxing;

//        if(sp_project_type.getText().toString().equals(projecttype_list.get(0)))
//            pcrProject.project_type = GlobalDate.ProjectType.dingxing;
//        else
//            pcrProject.project_type = GlobalDate.ProjectType.dingliang;

        pcrProject.project_filename = et_filename.getText().toString();
        pcrProject.project_name = et_filename.getText().toString();
        pcrProject.project_lot = et_lot.getText().toString();
        pcrProject.project_limit = et_limittime.getText().toString();
        pcrProject.project_neican = getDoubleFromEditText(et_neican);
        pcrProject.project_danwei = sp_danwei.getText().toString();
        pcrProject.project_neican_tongdao = getIntFromInnerString(sp_innerref.getText().toString());
        for (int i = 0; i < 4; i++) {
            if(g_device_guang_ables[i]) {
                pcrProject.project_item_ables[i] = cb_enables[i].isChecked();
                pcrProject.project_item_names[i] = et_names[i].getText().toString();
            }
        }
    }

    private int getIntFromInnerString(String v) {
        String tempstr;
        int tempint;
//        if(v.equals("FAM"))
//        {
//            return 1;
//        }
//        else if(v.equals("VIC/HEX"))
//        {
//            return 2;
//        }
//        else if(v.equals("ROX"))
//        {
//            return 3;
//        }
//        else if(v.equals("Cy5"))
//        {
//            return 4;
//        }
//        else
//            return 0;

        if(GlobalDate.g_guang_name1s.contains(v))
        {
            return 1;
        }
        else if(GlobalDate.g_guang_name2s.contains(v))
        {
            return 2;
        }
        else if(GlobalDate.g_guang_name3s.contains(v))
        {
            return 3;
        }
        else if(GlobalDate.g_guang_name4s.contains(v))
        {
            return 4;
        }
        else
            return 0;
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
        LogUtils.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d( "onStart");
//        setDataToUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
//        getDataFromUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("onDetach");
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