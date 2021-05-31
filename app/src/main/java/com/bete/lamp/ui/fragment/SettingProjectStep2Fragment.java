package com.bete.lamp.ui.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.PCRLiuchengCanShuItemAdpter;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.customWidget.EditSpinner;
import com.bete.lamp.navigator.ScaleCircleNavigator;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.DecimalInputTextWatcher;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.CAIJICOUNT;
import static com.myutils.GlobalDate.GUANGNUM;
import static com.myutils.GlobalDate.danwei_list;
import static com.myutils.GlobalDate.g_allcounts;
import static com.myutils.GlobalDate.g_device_guang_ables;
import static com.myutils.GlobalDate.g_indexs_steps;
import static com.myutils.GlobalDate.g_pcrProjects;
import static com.myutils.GlobalDate.g_time_shengyus;

public class SettingProjectStep2Fragment extends BaseFragment {
    private static final String TAG = "SettingProjectStep2Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    ///////////////////////////////////////////////////////////////////////////////////
    private LayoutInflater viewinflater;
    private Button bt_settingproject_return;
    private Button bt_settingproject_save;
    private View view1, view2, view3;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组
    private MagicIndicator indicator;
    private ScaleCircleNavigator navigator;
    private PagerAdapter pagerAdapter;
    public PCRProject pcrProject = new PCRProject();
    private String selectFile="";
///////////////////////////////////////////////////////////////////////
//    private RadioGroup rg_project_type;
//    private RadioButton rb_type_dingxing, rb_type_dingliang;
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
///////////////////////////////////////////////////////////////////////
    private RecyclerView rv_liucheng_step;
    private PCRLiuchengCanShuItemAdpter pcrLiuchengCanShuItemAdpter;
    private LinearLayoutManager layoutManager;
///////////////////////////////////////////////////////////////////////
    private RelativeLayout rl_dingxing;
    private EditText[] et_dx_yuzhis = new EditText[4];
    private EditText[] et_dx_refs = new EditText[4];
//    private EditText et_dx_pc;
    private EditText[] et_dx_pcs = new EditText[4];
//    private EditText et_dx_nc;
    private EditText[] et_dx_ncs = new EditText[4];
    private EditText[] et_dx_rns = new EditText[4];
//////////////////////////////////////////////////////////////////////
private RelativeLayout rl_dingliang;
    private EditText[] et_dl_yuzhis = new EditText[4];
    private EditText[] et_dl_refs = new EditText[4];
//    private EditText et_dl_pc;
    private EditText[] et_dl_pcs = new EditText[4];
//    private EditText et_dl_nc;
    private EditText[] et_dl_ncs = new EditText[4];
    private EditText[] et_dl_rns = new EditText[4];
//    private EditText et_dl_biao;
    private EditText[] et_dl_biaos = new EditText[4];
    private EditText[] et_dl_ks = new EditText[4];
    private EditText[] et_dl_bs = new EditText[4];
/////////////////////////////////////////////////////////////////////

    public SettingProjectStep2Fragment() {
        // Required empty public constructor
    }

//    public static SettingProjectStep2Fragment newInstance(String param1, String param2) {
//        SettingProjectStep2Fragment fragment = new SettingProjectStep2Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_project_step2, container, false);
        bt_settingproject_return = (Button) view.findViewById(R.id.bt_settingproject_return);
        bt_settingproject_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingProjectFragment) getParentFragment()).changeRadio(0);
            }
        });

        bt_settingproject_save = (Button) view.findViewById(R.id.bt_settingproject_save);
        bt_settingproject_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempFile = "";
                String etFile = "";
                getDateFromUI();
                etFile = et_filename.getText().toString();
                if (isProjectRight()) {
                    if (etFile.equals(selectFile)) {
                        tempFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + selectFile;
                    } else {
                        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR);
                        if (files.contains(FileCommonUtil.getFileByPath(etFile))) {
                            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                            builder.setMessage("项目中已存在相同文件名的配置文件,请重新命名");
                            builder.setTitle("项目管理");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create(R.layout.dialog_normal_feiquanping_message).show();
                            return;
                        } else {
                            tempFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + selectFile;
                            FileCommonUtil.deleteDirOrFile(tempFile);
                            tempFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + etFile;
                        }
                    }
                    SharedPreferencesUtils.savePCRProject(pcrProject, tempFile);
                    ((SettingProjectFragment) getParentFragment()).changeRadio(0);
                } else {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("请检查项目设置是否满足如下要求：\n1、至少选择一个项目。\n2、选中通道必须子项目名不可为空。\n3、子项目名不可相同。\n4、阳性对照、阴性对照的起止逻辑不对。\n5、检测总次数大于"+String.valueOf(CAIJICOUNT)+"。");
                    builder.setTitle("项目管理");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.layout.dialog_normal_feiquanping_message).show();
                }
            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.settingproject_viewpager);
        indicator=(MagicIndicator) view.findViewById(R.id.settingproject_bottom_indicator);
        viewinflater = getActivity().getLayoutInflater();
        view1 = inflater.inflate(R.layout.settingproject_viewpager_one, null);
        view2 = inflater.inflate(R.layout.settingproject_viewpager_two,null);
        view3 = inflater.inflate(R.layout.settingproject_viewpager_three, null);
        initProject();
        initView();
        setDateToUI();
        return view;
    }

    private void initView() {
//        rg_project_type =  (RadioGroup) view1.findViewById(R.id.rg_project_type);
//        rb_type_dingxing = (RadioButton) view1.findViewById(R.id.rb_type_dingxing);
//        rb_type_dingliang = (RadioButton) view1.findViewById(R.id.rb_type_dingliang);
        et_lot = (EditText) view1.findViewById(R.id.et_lot);
        et_limittime = (EditText) view1.findViewById(R.id.et_limittime);
        sp_project_type = (EditSpinner) view1.findViewById(R.id.sp_project_type);
        sp_innerref = (EditSpinner) view1.findViewById(R.id.sp_innerref);
        et_neican = (EditText) view1.findViewById(R.id.et_neican);
        sp_danwei = (EditSpinner) view1.findViewById(R.id.sp_danwei);
        tv_danwei = (TextView) view1.findViewById(R.id.tv_danwei);
        et_filename = (EditText) view1.findViewById(R.id.et_filename);

        cb_enables[0] = (CheckBox) view1.findViewById(R.id.cb_enable_1);
        cb_enables[1] = (CheckBox) view1.findViewById(R.id.cb_enable_2);
        cb_enables[2] = (CheckBox) view1.findViewById(R.id.cb_enable_3);
        cb_enables[3] = (CheckBox) view1.findViewById(R.id.cb_enable_4);
        et_names[0] = (EditText) view1.findViewById(R.id.et_name_1);
        et_names[1] = (EditText) view1.findViewById(R.id.et_name_2);
        et_names[2] = (EditText) view1.findViewById(R.id.et_name_3);
        et_names[3] = (EditText) view1.findViewById(R.id.et_name_4);
        tv_titles[0] = (TextView) view1.findViewById(R.id.tv_title_1);
        tv_titles[1] = (TextView) view1.findViewById(R.id.tv_title_2);
        tv_titles[2] = (TextView) view1.findViewById(R.id.tv_title_3);
        tv_titles[3] = (TextView) view1.findViewById(R.id.tv_title_4);

        for(int i =0;i<GUANGNUM;i++) {
            cb_enables[i].setEnabled(g_device_guang_ables[i]);
            et_names[i].setEnabled(g_device_guang_ables[i]);
            tv_titles[i].setEnabled(g_device_guang_ables[i]);
        }


        et_neican.addTextChangedListener(new DecimalInputTextWatcher(2,0));
//        rb_type_dingxing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                {
//                    LogUtils.d("                    tv_danwei.setVisibility(View.INVISIBLE);");
//                    tv_danwei.setVisibility(View.INVISIBLE);
//                    sp_danwei.setVisibility(View.INVISIBLE);
//                }
//                else
//                {
//                    LogUtils.d("                    tv_danwei.setVisibility(View.VISIBLE);");
//                    tv_danwei.setVisibility(View.VISIBLE);
//                    sp_danwei.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        rb_type_dingliang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(!isChecked)
//                {
//                    tv_danwei.setVisibility(View.INVISIBLE);
//                    sp_danwei.setVisibility(View.INVISIBLE);
//                }
//                else
//                {
//                    tv_danwei.setVisibility(View.VISIBLE);
//                    sp_danwei.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//////////////////////////////////////////////////////////////////////////////////////////
        rv_liucheng_step = (RecyclerView) view2.findViewById(R.id.rv_liucheng_step);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        rv_liucheng_step.setLayoutManager(layoutManager);
        pcrLiuchengCanShuItemAdpter = new PCRLiuchengCanShuItemAdpter(getActivity(), pcrProject.pcrLiuChengCanShuItems);
        pcrLiuchengCanShuItemAdpter.setEditList(pcrProject.pcrLiuChengCanShuItems);
        rv_liucheng_step.setAdapter(pcrLiuchengCanShuItemAdpter);
///////////////////////////////////////////////////////////////////////////////////////////
        rl_dingxing = (RelativeLayout) view3.findViewById(R.id.rl_dingxing);
        et_dx_yuzhis[0] = (EditText) view3.findViewById(R.id.et_dx_yuzhi_1);
        et_dx_yuzhis[1] = (EditText) view3.findViewById(R.id.et_dx_yuzhi_2);
        et_dx_yuzhis[2] = (EditText) view3.findViewById(R.id.et_dx_yuzhi_3);
        et_dx_yuzhis[3] = (EditText) view3.findViewById(R.id.et_dx_yuzhi_4);

        et_dx_refs[0] = (EditText) view3.findViewById(R.id.et_dx_ref_1);
        et_dx_refs[1] = (EditText) view3.findViewById(R.id.et_dx_ref_2);
        et_dx_refs[2] = (EditText) view3.findViewById(R.id.et_dx_ref_3);
        et_dx_refs[3] = (EditText) view3.findViewById(R.id.et_dx_ref_4);

//        et_dx_pc = (EditText) view3.findViewById(R.id.et_dx_pc);
        et_dx_pcs[0] = (EditText) view3.findViewById(R.id.et_dx_pc_1);
        et_dx_pcs[1] = (EditText) view3.findViewById(R.id.et_dx_pc_2);
        et_dx_pcs[2] = (EditText) view3.findViewById(R.id.et_dx_pc_3);
        et_dx_pcs[3] = (EditText) view3.findViewById(R.id.et_dx_pc_4);


//        et_dx_nc = (EditText) view3.findViewById(R.id.et_dx_nc);
        et_dx_ncs[0] = (EditText) view3.findViewById(R.id.et_dx_nc_1);
        et_dx_ncs[1] = (EditText) view3.findViewById(R.id.et_dx_nc_2);
        et_dx_ncs[2] = (EditText) view3.findViewById(R.id.et_dx_nc_3);
        et_dx_ncs[3] = (EditText) view3.findViewById(R.id.et_dx_nc_4);

        et_dx_rns[0] = (EditText) view3.findViewById(R.id.et_dx_rn_1);
        et_dx_rns[1] = (EditText) view3.findViewById(R.id.et_dx_rn_2);
        et_dx_rns[2] = (EditText) view3.findViewById(R.id.et_dx_rn_3);
        et_dx_rns[3] = (EditText) view3.findViewById(R.id.et_dx_rn_4);
///////////////////////////////////////////////////////////////////////////////////////////
        rl_dingliang = (RelativeLayout) view3.findViewById(R.id.rl_dingliang);
        et_dl_yuzhis[0] = (EditText) view3.findViewById(R.id.et_dl_yuzhi_1);
        et_dl_yuzhis[1] = (EditText) view3.findViewById(R.id.et_dl_yuzhi_2);
        et_dl_yuzhis[2] = (EditText) view3.findViewById(R.id.et_dl_yuzhi_3);
        et_dl_yuzhis[3] = (EditText) view3.findViewById(R.id.et_dl_yuzhi_4);

        et_dl_refs[0] = (EditText) view3.findViewById(R.id.et_dl_ref_1);
        et_dl_refs[1] = (EditText) view3.findViewById(R.id.et_dl_ref_2);
        et_dl_refs[2] = (EditText) view3.findViewById(R.id.et_dl_ref_3);
        et_dl_refs[3] = (EditText) view3.findViewById(R.id.et_dl_ref_4);

//        et_dl_pc = (EditText) view3.findViewById(R.id.et_dl_pc);
        et_dl_pcs[0] = (EditText) view3.findViewById(R.id.et_dl_pc_1);
        et_dl_pcs[1] = (EditText) view3.findViewById(R.id.et_dl_pc_2);
        et_dl_pcs[2] = (EditText) view3.findViewById(R.id.et_dl_pc_3);
        et_dl_pcs[3] = (EditText) view3.findViewById(R.id.et_dl_pc_4);

//        et_dl_nc = (EditText) view3.findViewById(R.id.et_dl_nc);
        et_dl_ncs[0] = (EditText) view3.findViewById(R.id.et_dl_nc_1);
        et_dl_ncs[1] = (EditText) view3.findViewById(R.id.et_dl_nc_2);
        et_dl_ncs[2] = (EditText) view3.findViewById(R.id.et_dl_nc_3);
        et_dl_ncs[3] = (EditText) view3.findViewById(R.id.et_dl_nc_4);
        et_dl_rns[0] = (EditText) view3.findViewById(R.id.et_dl_rn_1);
        et_dl_rns[1] = (EditText) view3.findViewById(R.id.et_dl_rn_2);
        et_dl_rns[2] = (EditText) view3.findViewById(R.id.et_dl_rn_3);
        et_dl_rns[3] = (EditText) view3.findViewById(R.id.et_dl_rn_4);

//        et_dl_biao = (EditText) view3.findViewById(R.id.et_dl_biao);
        et_dl_biaos[0] = (EditText) view3.findViewById(R.id.et_dl_biao_1);
        et_dl_biaos[1] = (EditText) view3.findViewById(R.id.et_dl_biao_2);
        et_dl_biaos[2] = (EditText) view3.findViewById(R.id.et_dl_biao_3);
        et_dl_biaos[3] = (EditText) view3.findViewById(R.id.et_dl_biao_4);

        et_dl_ks[0] = (EditText) view3.findViewById(R.id.et_dl_k_1);
        et_dl_ks[1] = (EditText) view3.findViewById(R.id.et_dl_k_2);
        et_dl_ks[2] = (EditText) view3.findViewById(R.id.et_dl_k_3);
        et_dl_ks[3] = (EditText) view3.findViewById(R.id.et_dl_k_4);

        et_dl_bs[0] = (EditText) view3.findViewById(R.id.et_dl_b_1);
        et_dl_bs[1] = (EditText) view3.findViewById(R.id.et_dl_b_2);
        et_dl_bs[2] = (EditText) view3.findViewById(R.id.et_dl_b_3);
        et_dl_bs[3] = (EditText) view3.findViewById(R.id.et_dl_b_4);
///////////////////////////////////////////////////////////////////////////////////////////
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
//        rg_project_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if(rb_type_dingliang.isChecked())
//                {
//                    sp_danwei.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    sp_danwei.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

////////////////////////////////////////////////////////////////////////////////////////

        //设置样式
        //type_arrAdapter.setDropDownViewResource(R.layout.spinner_option_items);
//        et_dx_pc.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                super.afterTextChanged(editable);
//                if (!(editable.toString().isEmpty()))
//                    try {
//                        et_dx_pc.setTextColor(Color.BLACK);
//                        pcrProject.project_babiao = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    pcrProject.project_babiao = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//        });
//
//        et_dx_nc.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                super.afterTextChanged(editable);
//                if (!(editable.toString().isEmpty()))
//                    try {
//                        et_dx_nc.setTextColor(Color.BLACK);
//                        pcrProject.project_nc = Integer.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    pcrProject.project_nc = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//        });
//
//        et_dl_pc.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                super.afterTextChanged(editable);
//                if (!(editable.toString().isEmpty()))
//                    try {
//                        et_dl_pc.setTextColor(Color.BLACK);
//                        pcrProject.project_babiao = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    pcrProject.project_babiao = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//        });
//
//        et_dl_nc.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                super.afterTextChanged(editable);
//                if (!(editable.toString().isEmpty()))
//                    try {
//                        et_dl_nc.setTextColor(Color.BLACK);
//                        pcrProject.project_nc = Integer.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    pcrProject.project_nc = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//        });
//
//        et_dl_biao.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                super.afterTextChanged(editable);
//                if (!(editable.toString().isEmpty()))
//                    try {
//                        et_dl_biao.setTextColor(Color.BLACK);
//                        pcrProject.project_dingliang_biaozhunnongdu = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    pcrProject.project_dingliang_biaozhunnongdu = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//        });

        for (int i = 0; i < 4; i++) {
            final int finalI2 = i;
            et_dx_pcs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dx_pcs[finalI2].setTextColor(Color.BLACK);
                            pcrProject.project_babiaos[finalI2] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_babiaos[finalI2] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            final int finalI1 = i;
            et_dx_ncs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dx_ncs[finalI1].setTextColor(Color.BLACK);
                            pcrProject.project_ncs[finalI1] = Integer.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_ncs[finalI1] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            final int finalI = i;
            et_dl_pcs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_pcs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_babiaos[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_babiaos[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            final int finalI3 = i;
            et_dl_ncs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_ncs[finalI3].setTextColor(Color.BLACK);
                            pcrProject.project_ncs[finalI3] = Integer.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_ncs[finalI3] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            final int finalI4 = i;
            et_dl_biaos[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_biaos[finalI4].setTextColor(Color.BLACK);
                            pcrProject.project_dingliang_biaozhunnongdu = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_dingliang_biaozhunnongdu = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });
        }
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            et_names[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            et_dx_refs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                                et_dx_refs[finalI].setTextColor(Color.BLACK);
                                pcrProject.project_item_refs[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_refs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dx_rns[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                                et_dx_rns[finalI].setTextColor(Color.BLACK);
                                pcrProject.project_item_rns[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_rns[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });
////////////////////////////////////////////////////////////////////////////////////////////////////////
            et_dl_refs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_refs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_refs[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_refs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dl_rns[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_rns[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_rns[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_rns[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dl_ks[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_ks[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_ks[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_ks[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dl_bs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_bs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_bs[finalI] = Double.valueOf(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_bs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });
        }

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
//        viewList.add(view4);

        navigator=new ScaleCircleNavigator(getActivity());
        navigator.setCircleCount(viewList.size());
        navigator.setNormalCircleColor(Color.DKGRAY);
        navigator.setSelectedCircleColor(Color.CYAN);
        navigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(navigator);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        ViewPagerHelper.bind(indicator, viewPager);
        pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
//        rg_project_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId==rb_type_dingliang.getId())
//                {
//                    rl_dingxing.setVisibility(View.GONE);
//                    rl_dingliang.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    rl_dingxing.setVisibility(View.VISIBLE);
//                    rl_dingliang.setVisibility(View.GONE);
//                }
//            }
//        });

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
                if(position==0)
                {
                    tv_danwei.setVisibility(View.INVISIBLE);
                    sp_danwei.setVisibility(View.INVISIBLE);
                    rl_dingliang.setVisibility(View.GONE);
                    rl_dingxing.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_danwei.setVisibility(View.VISIBLE);
                    sp_danwei.setVisibility(View.VISIBLE);
                    rl_dingxing.setVisibility(View.GONE);
                    rl_dingliang.setVisibility(View.VISIBLE);
                }
            }
        });

        sp_project_type.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

    private void getDateFromUI() {
//        if (rb_type_dingliang.isChecked())
//            pcrProject.project_type = GlobalDate.ProjectType.dingliang;
//        else if (rb_type_dingxing.isChecked())
//            pcrProject.project_type = GlobalDate.ProjectType.dingxing;
//        else
//            pcrProject.project_type = GlobalDate.ProjectType.dingxing;

        if(sp_project_type.getText().toString().equals(projecttype_list.get(0)))
            pcrProject.project_type = GlobalDate.ProjectType.dingxing;
        else
            pcrProject.project_type = GlobalDate.ProjectType.dingliang;

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

        if(pcrProject.project_type== GlobalDate.ProjectType.dingxing) {
//            pcrProject.project_babiao = getDoubleFromEditText(et_dx_pc);
//            pcrProject.project_nc = getIntFromEditText(et_dx_nc);
            for (int i = 0; i < 4; i++) {
                pcrProject.project_babiaos[i] = getDoubleFromEditText(et_dx_pcs[i]);
                pcrProject.project_ncs[i] = getIntFromEditText(et_dx_ncs[i]);
                pcrProject.project_item_refs[i] = getDoubleFromEditText(et_dx_refs[i]);
                pcrProject.project_item_rns[i] = getDoubleFromEditText(et_dx_rns[i]);
            }
        }
        else
        {
//            pcrProject.project_babiao = getDoubleFromEditText(et_dl_pc);
//            pcrProject.project_nc = getIntFromEditText(et_dl_nc);
//            pcrProject.project_dingliang_biaozhunnongdu = getDoubleFromEditText(et_dl_biao);
            for (int i = 0; i < 4; i++) {
                pcrProject.project_babiaos[i] = getDoubleFromEditText(et_dl_pcs[i]);
                pcrProject.project_ncs[i] = getIntFromEditText(et_dl_ncs[i]);
                pcrProject.project_dingliang_biaozhunnongdu = getDoubleFromEditText(et_dl_biaos[i]);
                pcrProject.project_item_refs[i] = getDoubleFromEditText(et_dl_refs[i]);
                pcrProject.project_item_rns[i] = getDoubleFromEditText(et_dl_rns[i]);
                pcrProject.project_item_ks[i] = getDoubleFromEditText(et_dl_ks[i]);
                pcrProject.project_item_bs[i] = getDoubleFromEditText(et_dl_bs[i]);
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

    private void setDateToUI() {
        if (pcrProject.project_type == GlobalDate.ProjectType.dingliang) {
//            rb_type_dingliang.setChecked(true);
//            rb_type_dingxing.setChecked(false);
            sp_project_type.selectItem(1);
            tv_danwei.setVisibility(View.VISIBLE);
            sp_danwei.setVisibility(View.VISIBLE);
            rl_dingliang.setVisibility(View.VISIBLE);
            rl_dingxing.setVisibility(View.GONE);
        } else{
//            rb_type_dingliang.setChecked(false);
//            rb_type_dingxing.setChecked(true);
            sp_project_type.selectItem(0);
            tv_danwei.setVisibility(View.INVISIBLE);
            sp_danwei.setVisibility(View.INVISIBLE);
            rl_dingliang.setVisibility(View.GONE);
            rl_dingxing.setVisibility(View.VISIBLE);
        }


        et_filename.setText(pcrProject.project_filename);
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
        pcrLiuchengCanShuItemAdpter.setEditList(pcrProject.pcrLiuChengCanShuItems);
        rv_liucheng_step.setAdapter(pcrLiuchengCanShuItemAdpter);
        pcrLiuchengCanShuItemAdpter.notifyDataSetChanged();

        //if (pcrProject.project_type == 1) {
//            et_dl_pc.setText(String.valueOf(pcrProject.project_babiao));
//            et_dl_nc.setText(String.valueOf(pcrProject.project_nc));
//            et_dl_biao.setText(String.valueOf(pcrProject.project_dingliang_biaozhunnongdu));
            for (int i = 0; i < 4; i++) {
                et_dl_pcs[i].setText(String.valueOf(pcrProject.project_babiaos[i]));
                et_dl_ncs[i].setText(String.valueOf(pcrProject.project_ncs[i]));
                et_dl_biaos[i].setText(String.valueOf(pcrProject.project_dingliang_biaozhunnongdu));
                et_dl_refs[i].setText(String.valueOf(pcrProject.project_item_refs[i]));
                et_dl_rns[i].setText(String.valueOf(pcrProject.project_item_rns[i]));
                et_dl_ks[i].setText(String.valueOf(pcrProject.project_item_ks[i]));
                et_dl_bs[i].setText(String.valueOf(pcrProject.project_item_bs[i]));
            }
        //} else {
//            et_dx_pc.setText(String.valueOf(pcrProject.project_babiao));
//            et_dx_nc.setText(String.valueOf(pcrProject.project_nc));
            for (int i = 0; i < 4; i++) {
                et_dx_pcs[i].setText(String.valueOf(pcrProject.project_babiaos[i]));
                et_dx_ncs[i].setText(String.valueOf(pcrProject.project_ncs[i]));
                et_dx_refs[i].setText(String.valueOf((int)pcrProject.project_item_refs[i]));
                et_dx_rns[i].setText(String.valueOf(pcrProject.project_item_rns[i]));
            }
        //}

    }

    private void initProject() {
        selectFile = ((SettingProjectFragment) getParentFragment()).getSelectFile();
        LogUtils.d("selectFile:" + selectFile);
        pcrProject = SharedPreferencesUtils.getPCRProject(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + selectFile);
        pcrProject.project_filename = selectFile;
    }

    public boolean isProjectRight() {
        HashSet<String> stringHashSet = new HashSet<>();
        int size = 0,cnt=0;
//        if(pcrProject.project_item_able[0]) {
//            temp +=String.valueOf(pcrProject.project_item_tongdao[0]);
//            size++;
//        }
//        if(pcrProject.project_item_able[1]) {
//            temp +=String.valueOf(pcrProject.project_item_tongdao[1]);
//            size++;
//        }
//        if(pcrProject.project_item_able[2]) {
//            temp +=String.valueOf(pcrProject.project_item_tongdao[2]);
//            size++;
//        }
//        if(pcrProject.project_item_able[3]) {
//            temp +=String.valueOf(pcrProject.project_item_tongdao[3]);
//            size++;
//        }
//
//        if( (sort2(temp).length()!=size) || (temp.contains("0")) )
//        {
//            return false;
//        }

        if (pcrProject.project_item_ables[0]) {
            stringHashSet.add(String.valueOf(pcrProject.project_item_names[0]));
            size++;
        }
        if (pcrProject.project_item_ables[1]) {
            stringHashSet.add(String.valueOf(pcrProject.project_item_names[1]));
            size++;
        }
        if (pcrProject.project_item_ables[2]) {
            stringHashSet.add(String.valueOf(pcrProject.project_item_names[2]));
            size++;
        }
        if (pcrProject.project_item_ables[3]) {
            stringHashSet.add(String.valueOf(pcrProject.project_item_names[3]));
            size++;
        }

        for (int x = 0; x < pcrProject.pcrLiuChengCanShuItems.size(); x++) {
            if (pcrProject.pcrLiuChengCanShuItems.get(x).isRead)
                cnt += pcrProject.pcrLiuChengCanShuItems.get(x).holdTime * 60 / pcrProject.pcrLiuChengCanShuItems.get(x).intervalTime;
        }

        if(cnt>=CAIJICOUNT)
        {
            return false;
        }

        if (stringHashSet.contains("") || stringHashSet.size() != size) {
            return false;
        }
        if (size == 0) {
            return false;
        }

        return true;
    }

    /**
     * 把字符串去重，并升序排序
     *
     * @param str
     * @return
     */
    public static String sort2(String str) {

        //把String变成单一字符数组
        String[] chars = str.split("");

        //把字符串数组放入TreeSet中，根据set元素不重复的特性去掉重复元素。根据treeSet的有序性排序
        TreeSet<String> treeSet = new TreeSet();
        for (String s : chars) {
            treeSet.add(s);
        }

        //把treeSet拼接成字符串
        str = "";
        for (String s : treeSet) {
            str += s;
        }
        return str;
    }

    public void onButtonPressed(Uri uri) {
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
            viewPager.setCurrentItem(0);
            initProject();
            setDateToUI();
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
}
