package com.bete.lamp.barfragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bete.lamp.R;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.myutils.GlobalDate;
import com.utils.DecimalInputTextWatcher;
import com.utils.LogUtils;

public class BarCodeLampCanshuFragment extends BaseFragment {
    public static final String TAG = "BarCodeLampCanshuFragment";
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
    private EditText et_dl_biao;
    private EditText[] et_dl_ks = new EditText[4];
    private EditText[] et_dl_bs = new EditText[4];
    /////////////////////////////////////////////////////////////////////
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
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        View view = inflater.inflate(R.layout.fragment_bar_code_lamp_canshu, container, false);
        ///////////////////////////////////////////////////////////////////////////////////////////
        rl_dingxing = (RelativeLayout) view.findViewById(R.id.rl_dingxing);
        et_dx_yuzhis[0] = (EditText) view.findViewById(R.id.et_dx_yuzhi_1);
        et_dx_yuzhis[1] = (EditText) view.findViewById(R.id.et_dx_yuzhi_2);
        et_dx_yuzhis[2] = (EditText) view.findViewById(R.id.et_dx_yuzhi_3);
        et_dx_yuzhis[3] = (EditText) view.findViewById(R.id.et_dx_yuzhi_4);

        et_dx_refs[0] = (EditText) view.findViewById(R.id.et_dx_ref_1);
        et_dx_refs[1] = (EditText) view.findViewById(R.id.et_dx_ref_2);
        et_dx_refs[2] = (EditText) view.findViewById(R.id.et_dx_ref_3);
        et_dx_refs[3] = (EditText) view.findViewById(R.id.et_dx_ref_4);

//        et_dx_pc = (EditText) view.findViewById(R.id.et_dx_pc);
        et_dx_pcs[0] = (EditText) view.findViewById(R.id.et_dx_pc_1);
        et_dx_pcs[1] = (EditText) view.findViewById(R.id.et_dx_pc_2);
        et_dx_pcs[2] = (EditText) view.findViewById(R.id.et_dx_pc_3);
        et_dx_pcs[3] = (EditText) view.findViewById(R.id.et_dx_pc_4);


//        et_dx_nc = (EditText) view.findViewById(R.id.et_dx_nc);
        et_dx_ncs[0] = (EditText) view.findViewById(R.id.et_dx_nc_1);
        et_dx_ncs[1] = (EditText) view.findViewById(R.id.et_dx_nc_2);
        et_dx_ncs[2] = (EditText) view.findViewById(R.id.et_dx_nc_3);
        et_dx_ncs[3] = (EditText) view.findViewById(R.id.et_dx_nc_4);

        et_dx_rns[0] = (EditText) view.findViewById(R.id.et_dx_rn_1);
        et_dx_rns[1] = (EditText) view.findViewById(R.id.et_dx_rn_2);
        et_dx_rns[2] = (EditText) view.findViewById(R.id.et_dx_rn_3);
        et_dx_rns[3] = (EditText) view.findViewById(R.id.et_dx_rn_4);
///////////////////////////////////////////////////////////////////////////////////////////
        rl_dingliang = (RelativeLayout) view.findViewById(R.id.rl_dingliang);
        et_dl_yuzhis[0] = (EditText) view.findViewById(R.id.et_dl_yuzhi_1);
        et_dl_yuzhis[1] = (EditText) view.findViewById(R.id.et_dl_yuzhi_2);
        et_dl_yuzhis[2] = (EditText) view.findViewById(R.id.et_dl_yuzhi_3);
        et_dl_yuzhis[3] = (EditText) view.findViewById(R.id.et_dl_yuzhi_4);

        et_dl_refs[0] = (EditText) view.findViewById(R.id.et_dl_ref_1);
        et_dl_refs[1] = (EditText) view.findViewById(R.id.et_dl_ref_2);
        et_dl_refs[2] = (EditText) view.findViewById(R.id.et_dl_ref_3);
        et_dl_refs[3] = (EditText) view.findViewById(R.id.et_dl_ref_4);

//        et_dl_pc = (EditText) view.findViewById(R.id.et_dl_pc);
        et_dl_pcs[0] = (EditText) view.findViewById(R.id.et_dl_pc_1);
        et_dl_pcs[1] = (EditText) view.findViewById(R.id.et_dl_pc_2);
        et_dl_pcs[2] = (EditText) view.findViewById(R.id.et_dl_pc_3);
        et_dl_pcs[3] = (EditText) view.findViewById(R.id.et_dl_pc_4);

//        et_dl_nc = (EditText) view.findViewById(R.id.et_dl_nc);
        et_dl_ncs[0] = (EditText) view.findViewById(R.id.et_dl_nc_1);
        et_dl_ncs[1] = (EditText) view.findViewById(R.id.et_dl_nc_2);
        et_dl_ncs[2] = (EditText) view.findViewById(R.id.et_dl_nc_3);
        et_dl_ncs[3] = (EditText) view.findViewById(R.id.et_dl_nc_4);

        et_dl_rns[0] = (EditText) view.findViewById(R.id.et_dl_rn_1);
        et_dl_rns[1] = (EditText) view.findViewById(R.id.et_dl_rn_2);
        et_dl_rns[2] = (EditText) view.findViewById(R.id.et_dl_rn_3);
        et_dl_rns[3] = (EditText) view.findViewById(R.id.et_dl_rn_4);

        et_dl_biao = (EditText) view.findViewById(R.id.et_dl_biao);

        et_dl_ks[0] = (EditText) view.findViewById(R.id.et_dl_k_1);
        et_dl_ks[1] = (EditText) view.findViewById(R.id.et_dl_k_2);
        et_dl_ks[2] = (EditText) view.findViewById(R.id.et_dl_k_3);
        et_dl_ks[3] = (EditText) view.findViewById(R.id.et_dl_k_4);

        et_dl_bs[0] = (EditText) view.findViewById(R.id.et_dl_b_1);
        et_dl_bs[1] = (EditText) view.findViewById(R.id.et_dl_b_2);
        et_dl_bs[2] = (EditText) view.findViewById(R.id.et_dl_b_3);
        et_dl_bs[3] = (EditText) view.findViewById(R.id.et_dl_b_4);
///////////////////////////////////////////////////////////////////////////////////////////
        //设置样式
        //type_arrAdapter.setDropDownViewResource(R.layout.spinner_option_items);
        et_dx_pcs[0].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if (!(editable.toString().isEmpty()))
                    try {
                        et_dx_pcs[0].setTextColor(Color.BLACK);
                        pcrProject.project_babiaos[0] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[1] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[2] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[3] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else {
                    pcrProject.project_babiaos[0] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[1] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[2] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[3] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            }
        });

        et_dx_ncs[0].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if (!(editable.toString().isEmpty()))
                    try {
                        et_dx_ncs[0].setTextColor(Color.BLACK);
                        pcrProject.project_ncs[0] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[1] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[2] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[3] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else {
                    pcrProject.project_ncs[0] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[1] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[2] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[3] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            }
        });

        et_dl_pcs[0].addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if (!(editable.toString().isEmpty()))
                    try {
                        et_dl_pcs[0].setTextColor(Color.BLACK);
                        pcrProject.project_babiaos[0] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[1] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[2] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_babiaos[3] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else {
                    pcrProject.project_babiaos[0] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[1] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[2] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_babiaos[3] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            }
        });

        et_dl_ncs[0].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if (!(editable.toString().isEmpty()))
                    try {
                        et_dl_ncs[0].setTextColor(Color.BLACK);
                        pcrProject.project_ncs[0] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[1] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[2] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        pcrProject.project_ncs[3] = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else {
                    pcrProject.project_ncs[0] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[1] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[2] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    pcrProject.project_ncs[3] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            }
        });

        et_dl_biao.addTextChangedListener(new DecimalInputTextWatcher(2, 1) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                if (!(editable.toString().isEmpty()))
                    try {
                        et_dl_biao.setTextColor(Color.BLACK);
                        pcrProject.project_dingliang_biaozhunnongdu = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else
                    pcrProject.project_dingliang_biaozhunnongdu = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            et_dx_refs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dx_refs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_refs[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_refs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dx_rns[i].addTextChangedListener(new DecimalInputTextWatcher(3, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dx_rns[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_rns[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_rns[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });
////////////////////////////////////////////////////////////////////////////////////////////////////////
            et_dl_refs[i].addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_refs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_refs[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_refs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });

            et_dl_rns[i].addTextChangedListener(new DecimalInputTextWatcher(3, 0) {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_rns[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_rns[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
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
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_ks[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_ks[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
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
                    PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
                    if (!(editable.toString().isEmpty()))
                        try {
                            et_dl_bs[finalI].setTextColor(Color.BLACK);
                            pcrProject.project_item_bs[finalI] = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        pcrProject.project_item_bs[finalI] = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                }
            });
        }
        return view;
    }

    private void setDataToUI() {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        if (pcrProject.project_type == GlobalDate.ProjectType.dingliang) {
            rl_dingliang.setVisibility(View.VISIBLE);
            rl_dingxing.setVisibility(View.GONE);
        } else{
            rl_dingliang.setVisibility(View.GONE);
            rl_dingxing.setVisibility(View.VISIBLE);
        }
        //if (pcrProject.project_type == 1) {
        et_dl_pcs[0].setText(String.valueOf(pcrProject.project_babiaos[0]));
        et_dl_ncs[0].setText(String.valueOf(pcrProject.project_ncs[0]));
        LogUtils.d("pcrProject.project_dingliang_biaozhunnongdu:"+pcrProject.project_dingliang_biaozhunnongdu);
        et_dl_biao.setText(String.valueOf(pcrProject.project_dingliang_biaozhunnongdu));
        for (int i = 0; i < 4; i++) {
            et_dl_refs[i].setText(String.valueOf(pcrProject.project_item_refs[i]));
            et_dl_rns[i].setText(String.valueOf(pcrProject.project_item_rns[i]));
            et_dl_ks[i].setText(String.valueOf(pcrProject.project_item_ks[i]));
            et_dl_bs[i].setText(String.valueOf(pcrProject.project_item_bs[i]));
        }
        //} else {
        et_dx_pcs[0].setText(String.valueOf(pcrProject.project_babiaos[0]));
        et_dx_ncs[0].setText(String.valueOf(pcrProject.project_ncs[0]));
        for (int i = 0; i < 4; i++) {
            et_dx_refs[i].setText(String.valueOf((int)pcrProject.project_item_refs[i]));
            et_dx_rns[i].setText(String.valueOf(pcrProject.project_item_rns[i]));
        }
        //}
    }

    private void getDataFromUI() {
        PCRProject pcrProject = ((SheZhiActivity)getActivity()).getPcrProject();
        if(pcrProject.project_type== GlobalDate.ProjectType.dingxing) {
//            pcrProject.project_babiao = getDoubleFromEditText(et_dx_pc);
//            pcrProject.project_nc = getIntFromEditText(et_dx_nc);
            for (int i = 0; i < 4; i++) {
                pcrProject.project_babiaos[i] = getDoubleFromEditText(et_dx_pcs[0]);
                pcrProject.project_ncs[i] = getIntFromEditText(et_dx_ncs[0]);
                pcrProject.project_item_refs[i] = getDoubleFromEditText(et_dx_refs[i]);
                pcrProject.project_item_rns[i] = getDoubleFromEditText(et_dx_rns[i]);
            }
        }
        else
        {
//            pcrProject.project_babiao = getDoubleFromEditText(et_dl_pc);
//            pcrProject.project_nc = getIntFromEditText(et_dl_nc);
            pcrProject.project_dingliang_biaozhunnongdu = getDoubleFromEditText(et_dl_biao);
            for (int i = 0; i < 4; i++) {
                pcrProject.project_babiaos[i] = getDoubleFromEditText(et_dl_pcs[0]);
                pcrProject.project_ncs[i] = getIntFromEditText(et_dl_ncs[0]);
                pcrProject.project_item_refs[i] = getDoubleFromEditText(et_dl_refs[i]);
                pcrProject.project_item_rns[i] = getDoubleFromEditText(et_dl_rns[i]);
                pcrProject.project_item_ks[i] = getDoubleFromEditText(et_dl_ks[i]);
                pcrProject.project_item_bs[i] = getDoubleFromEditText(et_dl_bs[i]);
            }
        }
    }

    private int getIntFromSpiner(Spinner v) {
        String tempstr;
        int tempint;
        tempstr = v.getSelectedItem().toString();
        try {
            tempint = Integer.parseInt(tempstr);
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
            tempdouble = Double.parseDouble(tempstr);
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
            tempint = Integer.parseInt(tempstr);
            return tempint;
        } catch (NumberFormatException e) {
            return 0;
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d( "onHiddenChanged show");
            setDataToUI();
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
            getDataFromUI();
        }
    }

}
