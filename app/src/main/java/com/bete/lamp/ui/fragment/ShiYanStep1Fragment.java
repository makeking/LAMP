package com.bete.lamp.ui.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.customWidget.EditSpinner;
import com.bete.lamp.message.ScanDateValueEvent;
import com.bete.lamp.thread.ScanSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.g_allcounts;
import static com.myutils.GlobalDate.g_alltimes;
import static com.myutils.GlobalDate.g_block_is_first;
import static com.myutils.GlobalDate.g_checkTimeStr;
import static com.myutils.GlobalDate.g_currentStep;
import static com.myutils.GlobalDate.g_flag_daowens;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_indexs_data;
import static com.myutils.GlobalDate.g_indexs_steps;
import static com.myutils.GlobalDate.g_lampQcData;
import static com.myutils.GlobalDate.g_pcrProjects;
import static com.myutils.GlobalDate.g_qcenable;
import static com.myutils.GlobalDate.g_samplenos;
import static com.myutils.GlobalDate.g_step2_guangs;
import static com.myutils.GlobalDate.g_step2_kongs;
import static com.myutils.GlobalDate.g_step3_guangs;
import static com.myutils.GlobalDate.g_temp_settings;
import static com.myutils.GlobalDate.g_time_daowens;
import static com.myutils.GlobalDate.g_time_holds;
import static com.myutils.GlobalDate.g_time_jianges;
import static com.myutils.GlobalDate.g_time_shengyus;
import static com.myutils.GlobalDate.nouse_pcrProject;
import static com.myutils.GlobalDate.sendPCRLiuChengProcessValueEvent;

public class ShiYanStep1Fragment extends BaseFragment {
    private static final String TAG = "ShiYanStep1Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    ///////////////////////////////////////////////////////////////////////////////////
    private RadioGroup rg_shiyan_step1_kong;
    private RadioButton rb_shiyan_step1_kong1, rb_shiyan_step1_kong2, rb_shiyan_step1_kong3, rb_shiyan_step1_kong4;
    private EditText et_sample_no1, et_sample_no2, et_sample_no3, et_sample_no4;
    private int current_et = 0;
    private Button bt_shiyan_step1_run;
    private EditSpinner spin_shiyan_project;
    private ImageView iv_project_tip;
    private CheckBox cb_shiyan_step1_qcenable;
    private Switch sw_shiyan_step1_qcenable;
    private TextView tv_shiyan_step1_qctime;
    private List<String> project_list = new ArrayList<String>();
    TextWatcher sample_no1_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString() != "PC")
                g_samplenos[g_currentBlock][0] = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher sample_no2_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString() != "NC")
                g_samplenos[g_currentBlock][1] = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher sample_no3_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            g_samplenos[g_currentBlock][2] = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher sample_no4_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            g_samplenos[g_currentBlock][3] = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public ShiYanStep1Fragment() {
        // Required empty public constructor
    }

//    public static ShiYanStep1Fragment newInstance(String param1, String param2) {
//        ShiYanStep1Fragment fragment = new ShiYanStep1Fragment();
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

    private int g_currentBlock = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shi_yan_step1, container, false);
        if (getArguments() != null) {
            int data = getArguments().getInt("param");
            LogUtils.e("param data:" + data);
            g_currentBlock = data;
        }

        bt_shiyan_step1_run = (Button) view.findViewById(R.id.bt_shiyan_step1_run);
        rg_shiyan_step1_kong = (RadioGroup) view.findViewById(R.id.rg_shiyan_step1_kong);
        cb_shiyan_step1_qcenable = (CheckBox) view.findViewById(R.id.cb_shiyan_step1_qcenable);
        sw_shiyan_step1_qcenable = (Switch) view.findViewById(R.id.sw_shiyan_step1_qcenable);
        tv_shiyan_step1_qctime = (TextView) view.findViewById(R.id.tv_shiyan_step1_qctime);
//        rb_shiyan_step1_kong1 = (RadioButton) view.findViewById(R.id.rb_shiyan_step1_kong1);
//        rb_shiyan_step1_kong2 = (RadioButton) view.findViewById(R.id.rb_shiyan_step1_kong2);
//        rb_shiyan_step1_kong3 = (RadioButton) view.findViewById(R.id.rb_shiyan_step1_kong3);
//        rb_shiyan_step1_kong4 = (RadioButton) view.findViewById(R.id.rb_shiyan_step1_kong4);

        et_sample_no1 = (EditText) view.findViewById(R.id.et_sample_no1);
        et_sample_no2 = (EditText) view.findViewById(R.id.et_sample_no2);
        et_sample_no3 = (EditText) view.findViewById(R.id.et_sample_no3);
        et_sample_no4 = (EditText) view.findViewById(R.id.et_sample_no4);

        et_sample_no1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_sample_no1.addTextChangedListener(sample_no1_text_watcher);
                    if (DEVICE) {
                        current_et = 1;
                        ScanSerial.getInstance().cmdScanOpen(2);
                    }
                } else {
                    et_sample_no1.removeTextChangedListener(sample_no1_text_watcher);
                    if (DEVICE) {
                        current_et = 0;
                        ScanSerial.getInstance().cmdScanClose();
                    }
                }
            }
        });

        et_sample_no2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_sample_no2.addTextChangedListener(sample_no2_text_watcher);
                    if (DEVICE) {
                        current_et = 2;
                        ScanSerial.getInstance().cmdScanOpen(2);
                    }
                } else {
                    et_sample_no2.removeTextChangedListener(sample_no2_text_watcher);
                    if (DEVICE) {
                        current_et = 0;
                        ScanSerial.getInstance().cmdScanClose();
                    }
                }
            }
        });

        et_sample_no3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_sample_no3.addTextChangedListener(sample_no3_text_watcher);
                    if (DEVICE) {
                        current_et = 3;
                        ScanSerial.getInstance().cmdScanOpen(2);
                    }
                } else {
                    et_sample_no3.removeTextChangedListener(sample_no3_text_watcher);
                    if (DEVICE) {
                        current_et = 0;
                        ScanSerial.getInstance().cmdScanClose();
                    }
                }
            }
        });

        et_sample_no4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_sample_no4.addTextChangedListener(sample_no4_text_watcher);
                    if (DEVICE) {
                        current_et = 4;
                        ScanSerial.getInstance().cmdScanOpen(2);
                    }
                } else {
                    et_sample_no4.removeTextChangedListener(sample_no4_text_watcher);
                    if (DEVICE) {
                        current_et = 0;
                        ScanSerial.getInstance().cmdScanClose();
                    }
                }
            }
        });

        iv_project_tip = (ImageView) view.findViewById(R.id.iv_project_tip);
        spin_shiyan_project = (EditSpinner) view.findViewById(R.id.spin_shiyan_project);

        cb_shiyan_step1_qcenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                g_qcenable[g_currentBlock] = isChecked;
                setDataToUI();
            }
        });

        sw_shiyan_step1_qcenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                g_qcenable[g_currentBlock] = isChecked;
                setDataToUI();
            }
        });

        bt_shiyan_step1_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g_pcrProjects[g_currentBlock].project_filename.equals("")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    LayoutInflater inflater = getActivity().getLayoutInflater();
//                    View contentView = inflater.inflate(R.layout.dialog_normal_feiquanping_message,null);
//                    builder.setView(contentView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).setMessage("请先选择项目！").setTitle("提醒").setCancelable(false);
//                    AlertDialog alertDialog=builder.create();
//                    alertDialog.setCanceledOnTouchOutside(false);
//                    alertDialog.show();
                    iv_project_tip.setVisibility(View.VISIBLE);
                    CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                    builder.setMessage("请先选择项目！");
                    builder.setTitle("提醒");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog customDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                    customDialog.setCancelable(false);
                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                } else {
                    g_block_is_first[g_currentBlock] = true;
                    g_indexs_steps[g_currentBlock] = 0;
                    g_time_shengyus[g_currentBlock] = 0;
                    g_allcounts[g_currentBlock] = 0;
                    g_indexs_data[g_currentBlock] = 0;
                    for (int x = g_indexs_steps[g_currentBlock]; x < g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.size(); x++) {
                        g_time_shengyus[g_currentBlock] += g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(x).holdTime * 60 * 1000;
                        if (g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(x).isRead)
                            g_allcounts[g_currentBlock] += g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(x).holdTime * 60 / g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(x).intervalTime;
                    }
                    for (int j = 0; j < BLOCKSIZE; j++) {
                        g_step2_guangs[g_currentBlock][j] = true;
                        g_step2_kongs[g_currentBlock][j] = true;
                        g_step3_guangs[g_currentBlock][j] = true;
                    }
                    LogUtils.e("g_currentBlock:" +g_currentBlock );
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    Calendar calendar = Calendar.getInstance();
                    g_checkTimeStr[g_currentBlock] = formatter.format(calendar.getTime()) + (char) ('A' + g_currentBlock);
                    g_alltimes[g_currentBlock] = g_time_shengyus[g_currentBlock];
                    LogUtils.d("g_time_shengyus[0]:" + g_time_shengyus[g_currentBlock]);
                    g_flag_daowens[g_currentBlock] = false;
                    g_temp_settings[g_currentBlock] = (int) g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(g_indexs_steps[g_currentBlock]).targetTemp;
                    g_time_holds[g_currentBlock] = (int) g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(g_indexs_steps[g_currentBlock]).holdTime;
                    g_time_jianges[g_currentBlock] = (int) g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(g_indexs_steps[g_currentBlock]).intervalTime;
                    g_time_daowens[g_currentBlock] = System.currentTimeMillis();
                    g_block_states[g_currentBlock] = GlobalDate.BlockStateType.running;
                    ((MainShiYanFragment) getParentFragment()).changeRadio(1,g_currentBlock);
                    sendPCRLiuChengProcessValueEvent(g_currentBlock, GlobalDate.EventType.blockStateChange);
                }
            }
        });
        return view;
    }

    private void initSpinner() {
        spin_shiyan_project.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        spin_shiyan_project.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        spin_shiyan_project.setDropDownVerticalOffset(0);
        spin_shiyan_project.setDropDownDrawableSpacing(50);
        spin_shiyan_project.setDropDownHeight(250);
        spin_shiyan_project.setEditable(false);
        spin_shiyan_project.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return project_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return project_list.toArray()[position];
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
        spin_shiyan_project.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        spin_shiyan_project.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        spin_shiyan_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    iv_project_tip.setVisibility(View.VISIBLE);
                } else {
                    iv_project_tip.setVisibility(View.GONE);
                }
                g_pcrProjects[g_currentBlock] = SharedPreferencesUtils.getPCRProject(Environment.getExternalStorageDirectory().getAbsolutePath() //
                        + File.separator + Canstant.INNER_PROJECT_DIR + project_list.get(position).toString());
                g_lampQcData[g_currentBlock] = SharedPreferencesUtils.getLampQcData(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_pcrProjects[g_currentBlock].project_filename);
                tv_shiyan_step1_qctime.setText(g_lampQcData[g_currentBlock].time);
                setDataToUI();
                spin_shiyan_project.clearFocus();
            }
        });

        spin_shiyan_project.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

//    public void getDataFromUI()
//    {
//        g_qcenable[g_currentBlock] = cb_shiyan_step1_qcenable.isChecked();
//        LogUtils.d("spin_shiyan_project.getSelectedItem().toString():"+spin_shiyan_project.getSelectedItem().toString());
//        g_pcrProjects[g_currentBlock]=SharedPreferencesUtils.getPCRProject(Environment.getExternalStorageDirectory().getAbsolutePath() //
//                + File.separator + Canstant.INNER_PROJECT_DIR+spin_shiyan_project.getSelectedItem().toString());
//        g_samplenos[g_currentBlock][0] = et_sample_no1.getText().toString();
//        g_samplenos[g_currentBlock][1] = et_sample_no2.getText().toString();
//        g_samplenos[g_currentBlock][2] = et_sample_no3.getText().toString();
//        g_samplenos[g_currentBlock][3] = et_sample_no4.getText().toString();
//    }

    public void setDataToUI() {
        cb_shiyan_step1_qcenable.setChecked(g_qcenable[g_currentBlock]);
        sw_shiyan_step1_qcenable.setChecked(g_qcenable[g_currentBlock]);
        if (g_pcrProjects[g_currentBlock] != null) {
            if (project_list.indexOf(g_pcrProjects[g_currentBlock].project_filename) != -1) {
                spin_shiyan_project.selectItem(project_list.indexOf(g_pcrProjects[g_currentBlock].project_filename));
            } else {
                spin_shiyan_project.selectItem(0);
                g_pcrProjects[g_currentBlock] = nouse_pcrProject;
            }
        }

        if (g_qcenable[g_currentBlock]) {
            et_sample_no1.setEnabled(false);
            et_sample_no1.clearFocus();
            et_sample_no1.setText("PC");
            et_sample_no2.setEnabled(false);
            et_sample_no2.clearFocus();
            et_sample_no2.setText("NC");

            if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
                et_sample_no3.setEnabled(true);
                et_sample_no3.setText(g_samplenos[g_currentBlock][2]);
            } else {
                et_sample_no3.setEnabled(false);
                et_sample_no3.clearFocus();
                et_sample_no3.setText("标");
            }
        } else {
            et_sample_no1.setEnabled(true);
            et_sample_no2.setEnabled(true);
            et_sample_no3.setEnabled(true);
            et_sample_no1.setText(g_samplenos[g_currentBlock][0]);
            et_sample_no2.setText(g_samplenos[g_currentBlock][1]);
            et_sample_no3.setText(g_samplenos[g_currentBlock][2]);
        }
        et_sample_no4.setText(g_samplenos[g_currentBlock][3]);
    }

    private void initProjectItems() {
        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR, false);
        File file;
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        project_list.clear();
        project_list.add("请选择项目");
        if (files != null && files.size() != 0) {
            for (int i = files.size() - 1; i >= 0; i--) {
                file = files.get(i);
                if ((!file.isDirectory()) && SharedPreferencesUtils.isPCRProject(file.getAbsolutePath())) {
                    project_list.add(file.getName());
                }
            }
        }
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        EventBus.getDefault().register(this);
        spin_shiyan_project.selectItem(0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // todo 重新获取值
            if (getArguments() != null) {
                int data = getArguments().getInt("param");
                g_currentBlock = data;
            }
            initProjectItems();
            initSpinner();
            setDataToUI();

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
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("ShiYanStep1Fragment.onDestroy()");
    }

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScanDateValueEvent scanDateValueEvent) {
        int len = Math.min(scanDateValueEvent.getLength(), 10);
        String et_string = null;
        String scan_string = new String(scanDateValueEvent.getScandataBytes(), 0, len);
        if (scanDateValueEvent.getType() == 2) {
            if (current_et == 1) {
                et_string = et_sample_no1.getText().toString();
                g_samplenos[g_currentBlock][current_et - 1] = scan_string;
                et_sample_no1.clearFocus();
                et_sample_no1.setText(scan_string);
            } else if (current_et == 2) {
                et_string = et_sample_no2.getText().toString();
                g_samplenos[g_currentBlock][current_et - 1] = scan_string;
                et_sample_no2.clearFocus();
                et_sample_no2.setText(scan_string);
            } else if (current_et == 3) {
                et_string = et_sample_no3.getText().toString();
                g_samplenos[g_currentBlock][current_et - 1] = scan_string;
                et_sample_no3.clearFocus();
                et_sample_no3.setText(scan_string);

            } else if (current_et == 4) {
                et_string = et_sample_no4.getText().toString();
                g_samplenos[g_currentBlock][current_et - 1] = scan_string;
                et_sample_no4.clearFocus();
                et_sample_no4.setText(scan_string);
            }
            if ((et_string == null) || et_string.isEmpty() || (!et_string.equals(scan_string))) {
                if (scanDateValueEvent.getLength() > 10) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("扫描数据长度大于10！");
                    builder.setTitle("扫描");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                } else {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("扫描成功");
                    builder.setTitle("扫描");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                }
            } else {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("重复扫描");
                builder.setTitle("扫描");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        }
    }

}
