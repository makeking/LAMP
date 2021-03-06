package com.bete.lamp.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anaalgorithm.BaseLineParam;
import com.anaalgorithm.FunAmpNormalizedAnaParamInfo;
import com.anaalgorithm.eFilterType;
import com.bete.lamp.R;
import com.bete.lamp.adapter.CheckBoxAdapter;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.adapter.LampResultItemAdapter;
import com.bete.lamp.bean.CheckBoxItem;
import com.bete.lamp.bean.CheckState;
import com.bete.lamp.bean.LampQcData;
import com.bete.lamp.bean.LampResultItem;
import com.bete.lamp.db.CheckDirItem;
import com.bete.lamp.thread.PrintSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.FileCommonUtil;
import com.utils.FileIOUtils;
import com.utils.LogUtils;
import com.utils.PointLengthFilter;
import com.utils.StorageUtil;
import com.utils.ZipUtil;

import org.litepal.LitePal;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT;
import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.CAIJICOUNT;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.GUANGNUM;
import static com.myutils.GlobalDate.chartColors;
import static com.myutils.GlobalDate.g_anaAlgorithm;
import static com.myutils.GlobalDate.g_chaxun_Thresvalue;
import static com.myutils.GlobalDate.g_chaxun_allcount;
import static com.myutils.GlobalDate.g_chaxun_blockno;
import static com.myutils.GlobalDate.g_chaxun_caijicount;
import static com.myutils.GlobalDate.g_chaxun_checkTimeStr;
import static com.myutils.GlobalDate.g_chaxun_checkstate_timestr;
import static com.myutils.GlobalDate.g_chaxun_ct;
import static com.myutils.GlobalDate.g_chaxun_fenxiguiyidatas;
import static com.myutils.GlobalDate.g_chaxun_fenxilvbodatas;
import static com.myutils.GlobalDate.g_chaxun_showguiyidatas;
import static com.myutils.GlobalDate.g_chaxun_indexs_data;
import static com.myutils.GlobalDate.g_chaxun_kadjustref;
import static com.myutils.GlobalDate.g_chaxun_lampQcData;
import static com.myutils.GlobalDate.g_chaxun_lamp_resultitem;
import static com.myutils.GlobalDate.g_chaxun_showlvbodatas;
import static com.myutils.GlobalDate.g_chaxun_pcrProject;
import static com.myutils.GlobalDate.g_chaxun_qcenable;
import static com.myutils.GlobalDate.g_chaxun_raredatas;
import static com.myutils.GlobalDate.g_chaxun_samplenos;
import static com.myutils.GlobalDate.g_chaxun_selectDir;
import static com.myutils.GlobalDate.g_chaxun_softcode;
import static com.myutils.GlobalDate.g_device_guang_ables;
import static com.myutils.GlobalDate.g_false_FunAbleToCalcuParamInfo;
import static com.myutils.GlobalDate.g_pcrDataHandleRef;
import static com.myutils.GlobalDate.g_ymin;
import static com.utils.StorageUtil.isUpanExist;
import static java.lang.Math.abs;

public class ChaXunStep2Fragment extends BaseFragment {
    private static final String TAG = "ChaXunStep2Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final boolean canxiugai = true;
//    private String mParam1;
//    private String mParam2;/*/////////////////////////////////////////////////////////////////////////////////*/
    private Button bt_chaxun_step2_done, bt_chaxun_step2_print, bt_chaxun_step2_export;
    protected View view;
    private TextView item_jieguo;
    private RelativeLayout ll_chaxun_step2_limit;
    private EditText et_chaxun_step2_limit;
    private boolean limitTragEnable = false;/*????????????*/
    CheckBoxAdapter checkBoxAdapterGuang;
    LinkedList<CheckBoxItem> guangList = new LinkedList<>();
    CheckBox cb_chaxun_step2_all;
    RecyclerView rv_chaxun_step2_guang;
    int selectGuang;
    LineChart chart_chaxun_step2;
    float Ylow = (float) 20000.0, Yhigh = (float) -20000.0;
    LineData lineData;
    List<ILineDataSet> dataSets = new LinkedList<>();
    YAxis leftAxis;
    XAxis axis;
    RecyclerView rv_chaxun_step2_jieguo;
    LinearLayoutManager layoutManagerRA2;
    LinkedList<LampResultItem> result_ct_jieguoList = new LinkedList();
    LampResultItemAdapter result_ct_Adapter;
    TextWatcher result_ct_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            float value = 0;
            if ((s.toString() != null) &&(!s.toString().isEmpty())) try {
                value = Float.valueOf(s.toString());
                g_chaxun_Thresvalue[selectGuang] = value;
                chart_chaxun_step2.getAxisLeft().removeAllLimitLines();
                LimitLine ll = new LimitLine((float) value, String.valueOf(value));
                ll.setLineColor(Color.BLACK);
                ll.setLineWidth(2f);
                ll.setTextColor(Color.GRAY);
                ll.setTextSize(12f);
                chart_chaxun_step2.getAxisLeft().addLimitLine(ll);
                chart_chaxun_step2.invalidate();
                updata_result_ct_list_only();
            } catch (NumberFormatException e) {
                LogUtils.d(e);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    CompoundButton.OnCheckedChangeListener result_ct_allListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            for (int i = 0; i < GUANGNUM; i++)
                if (guangList.get(i).isShow) guangList.get(i).setCheck(isChecked);
            checkBoxAdapterGuang.notifyDataSetChanged();
            updata_result_ct_list();
            updataCtLineChart(chart_chaxun_step2);
        }
    };/*????????????hanlder*/
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                LogUtils.d("copy maintenance err");
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("???????????????????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 2) {
                closeProgressDialog();
                LogUtils.d("copy maintenance success");
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("???????????????????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 3) {
                closeProgressDialog();
                LogUtils.d("copy maintenance success");
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("???????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 4) {
                closeProgressDialog();
                LogUtils.d("copy maintenance success");
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("??????????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 6) {
                closeProgressDialog();
                LogUtils.d("copy maintenance success");
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("??????????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 8) {
                result_ct_jieguoList.clear();
                int size = result_ct_jieguoList.size();
                for (int j = 0; j < GUANGNUM; j++)/*?????????1234//*/
                    if (guangList.get(j).isCheck && guangList.get(j).isShow&&g_device_guang_ables[j])
                        for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                            result_ct_jieguoList.add(g_chaxun_lamp_resultitem[j][z]);
                if (size >= result_ct_jieguoList.size())
                    result_ct_Adapter.notifyItemRangeRemoved(result_ct_jieguoList.size(), size - result_ct_jieguoList.size());
                else
                    result_ct_Adapter.notifyItemRangeInserted(size, result_ct_jieguoList.size() - size);
                result_ct_Adapter.notifyItemRangeChanged(layoutManagerRA2.findFirstVisibleItemPosition(), layoutManagerRA2.findLastVisibleItemPosition() - layoutManagerRA2.findFirstVisibleItemPosition() + 1);/*layoutManagerRA2.findFirstVisibleItemPosition()*/
            }
        }
    };

    /*///////////////////////////////////////////////////////////////////////////////////////////////////////////*/
    public ChaXunStep2Fragment() {/* Required empty public constructor*/}

//    public static ChaXunStep2Fragment newInstance(String param1, String param2) {
//        ChaXunStep2Fragment fragment = new ChaXunStep2Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {/* Inflate the layout for this fragment*/
        view = inflater.inflate(R.layout.fragment_cha_xun_step2, container, false);
        bt_chaxun_step2_done = (Button) view.findViewById(R.id.bt_chaxun_step2_done);
        bt_chaxun_step2_print = (Button) view.findViewById(R.id.bt_chaxun_step2_print);
        bt_chaxun_step2_export = (Button) view.findViewById(R.id.bt_chaxun_step2_export);/*getAntoThresvalue();*/
        readJiLuDir();
        init_result_ct_view(view);
        updata_result_ct_list();
        initCtLineChart(chart_chaxun_step2);
        updataCtLineChart(chart_chaxun_step2);
        if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing)
            item_jieguo.setText("??????");
        else item_jieguo.setText("??????(" + g_chaxun_pcrProject.project_danwei + ")");
        bt_chaxun_step2_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CXChaXunFragment) getParentFragment()).changeRadio(0);
            }
        });
        bt_chaxun_step2_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrintHandle();
            }
        });
        bt_chaxun_step2_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDaoChuHandle();
            }
        });
        return view;
    }

    private ProgressDialog progressDialog;
    private Thread copyThread;

    /**
     * ?????????????????????
     */
    private void showProgressDialog(String text) {
        closeProgressDialog();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(text);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * ?????????????????????
     */
    private void closeProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
        progressDialog = null;
    }

    public void readJiLuDir() {
        String selectfilepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_selectDir + File.separator + "shiyanstatedata";
        LogUtils.d(selectfilepath);
        CheckState checkState = SharedPreferencesUtils.getPCRCheckState(selectfilepath);
        g_chaxun_indexs_data = checkState.checkcnt;
        g_chaxun_allcount = checkState.checkcount;
        g_chaxun_softcode = checkState.version;/*????????????*/
        g_chaxun_checkstate_timestr = checkState.checktime;
        g_chaxun_blockno = checkState.blockno;/*????????????*/
        g_chaxun_caijicount = checkState.caijicount;/*????????????*/
        g_chaxun_qcenable = checkState.qcEnable;
        g_chaxun_pcrProject = checkState.pcrProject;
        g_chaxun_lampQcData = checkState.lampQcData;
        g_chaxun_kadjustref = checkState.lampQcData.kadjustref;
        double[] px = new double[g_chaxun_indexs_data];
        for (int x = 0; x < (g_chaxun_indexs_data); x++)
            px[x] = x + 1;
        for (int i = 0; i < BLOCKSIZE; i++) g_chaxun_samplenos[i] = checkState.sampleNos[i];
        for (int x = 0; x < GUANGNUM; x++) {
            g_chaxun_Thresvalue[x] = checkState.thresValues[x];
            for (int y = 0; y < BLOCKSIZE; y++) {
                g_chaxun_ct[x][y] = checkState.cts[x][y];
                LampResultItem lampResultItem = checkState.lampResultItems[x][y];
                g_chaxun_lamp_resultitem[x][y].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + x * BLOCKSIZE + y, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + x * BLOCKSIZE + y + 1, lampResultItem.bianhao, lampResultItem.projectname, lampResultItem.jieguo, lampResultItem.ct);
                for (int z = 0; z < checkState.caijicount; z++) {
                    g_chaxun_raredatas[x][y][z] = checkState.raredatas[x][y][z];
                    g_chaxun_showlvbodatas[x][y][z] = checkState.lvbodatas[x][y][z];
                    g_chaxun_showguiyidatas[x][y][z] = checkState.guiyidatas[x][y][z];
                }

                System.arraycopy(g_chaxun_raredatas[x][y], 0, g_chaxun_fenxilvbodatas[x][y], 0, g_chaxun_indexs_data);
                System.arraycopy(g_chaxun_raredatas[x][y], 0, g_chaxun_showlvbodatas[x][y], 0, g_chaxun_indexs_data);
                for (int l = 0; l < g_pcrDataHandleRef.g_zhongzhi_lvbo_cnt; l++) {
                    g_anaAlgorithm.DigitalFilter(g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], g_chaxun_indexs_data, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                    g_anaAlgorithm.DigitalFilter(g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y], g_chaxun_indexs_data, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                    System.arraycopy(g_chaxun_fenxiguiyidatas[x][y], 0, g_chaxun_fenxilvbodatas[x][y], 0, g_chaxun_indexs_data);
                    System.arraycopy(g_chaxun_showguiyidatas[x][y], 0, g_chaxun_showlvbodatas[x][y], 0, g_chaxun_indexs_data);
                }
                for (int l = 0; l < g_pcrDataHandleRef.g_huadong_lvbo_cnt; l++) {
                    g_anaAlgorithm.KdsptForwardMBackN(g_chaxun_indexs_data, g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//??????
                    g_anaAlgorithm.KdsptForwardMBackN_New(g_chaxun_indexs_data, g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//??????
                    System.arraycopy(g_chaxun_fenxiguiyidatas[x][y], 0, g_chaxun_fenxilvbodatas[x][y], 0, g_chaxun_indexs_data);
                    System.arraycopy(g_chaxun_showguiyidatas[x][y], 0, g_chaxun_showlvbodatas[x][y], 0, g_chaxun_indexs_data);
                }

                if (!g_anaAlgorithm.AbleToCalculate(px, g_chaxun_fenxilvbodatas[x][y], g_chaxun_indexs_data,g_false_FunAbleToCalcuParamInfo,5)) {
                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_chaxun_indexs_data, px, g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y]);
                    else
                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_chaxun_indexs_data, px, g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y]);
                } else {
                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_chaxun_indexs_data, px, g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y]);
                    else
                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_chaxun_indexs_data, px, g_chaxun_fenxilvbodatas[x][y], g_chaxun_fenxiguiyidatas[x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_chaxun_showlvbodatas[x][y], g_chaxun_showguiyidatas[x][y]);
                }
            }
        }

//        if (canxiugai)
//            getAntoThresvalue();
    }

    public void savedata() {
        List<CheckDirItem> checkDirItemList = LitePal.where("checkdatetime = ?", g_chaxun_checkTimeStr).find(CheckDirItem.class);
        if (checkDirItemList != null) for (int i = 0; i < checkDirItemList.size(); i++) {
            CheckDirItem checkDirItem = checkDirItemList.get(i);
            checkDirItem.delete();
        }
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkTimeStr + "/" + "shiyanstatedata";
        CheckState checkState = new CheckState(g_chaxun_softcode, g_chaxun_checkstate_timestr, g_chaxun_blockno, g_chaxun_indexs_data, g_chaxun_allcount, g_chaxun_caijicount, g_chaxun_raredatas, g_chaxun_showlvbodatas, g_chaxun_showguiyidatas, g_chaxun_ct, g_chaxun_qcenable, g_chaxun_Thresvalue, g_chaxun_samplenos, g_chaxun_pcrProject, g_chaxun_lamp_resultitem, g_chaxun_lampQcData);
        SharedPreferencesUtils.savePCRCheckState(checkState, filepath);
        if (g_chaxun_qcenable)
            if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
                String name = (g_chaxun_samplenos[2] != "") ? g_chaxun_samplenos[2] : "unknow";
                CheckDirItem tempsqlstru = new CheckDirItem(name, g_chaxun_checkTimeStr, g_chaxun_checkTimeStr);
                tempsqlstru.save();
                String name1 = (g_chaxun_samplenos[3] != "") ? g_chaxun_samplenos[3] : "unknow";
                CheckDirItem tempsqlstru1 = new CheckDirItem(name1, g_chaxun_checkTimeStr, g_chaxun_checkTimeStr);
                tempsqlstru1.save();
            } else {
                String name1 = (g_chaxun_samplenos[3] != "") ? g_chaxun_samplenos[3] : "unknow";
                CheckDirItem tempsqlstru1 = new CheckDirItem(name1, g_chaxun_checkTimeStr, g_chaxun_checkTimeStr);
                tempsqlstru1.save();
            }
        else for (int i = 0; i < BLOCKSIZE; i++) {
            String name = (g_chaxun_samplenos[i] != "") ? g_chaxun_samplenos[i] : "unknow";
            CheckDirItem tempsqlstru = new CheckDirItem(name, g_chaxun_checkTimeStr, g_chaxun_checkTimeStr);
            tempsqlstru.save();
        }/*savaDataToDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock], Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock]);*/
    }

    public void delNoUseFile(String srcdir) {
        List<File> files = FileCommonUtil.listFilesInDir(srcdir, false);
        if (files != null && files.size() != 0) for (File file : files)
            if ((file.isDirectory()) || (file.getName().contains("shiyanstatedata"))) continue;
            else FileCommonUtil.deleteFile(file);
    }

    public void savaDataToDir(String srcdir, String desdir) {
        String filepath = "";
        StringBuilder csvfilebuff = new StringBuilder();
        FileCommonUtil.createOrExistsDir(desdir);
        if (srcdir.equals(desdir)) {
            List<File> files = FileCommonUtil.listFilesInDir(srcdir, false);
            if (files != null && files.size() != 0) for (File file : files)
                if ((file.isDirectory()) || (file.getName().contains("shiyanstatedata"))) continue;
                else FileCommonUtil.deleteFile(file);
        } else {
            List<File> files = FileCommonUtil.listFilesInDir(srcdir, false);
            if (files != null && files.size() != 0) for (File file : files)
                if ((file.isDirectory()) || (file.getName().contains("shiyanstatedata")))
                    FileCommonUtil.copyFile(file.getAbsolutePath(), desdir + "/" + file.getName());
            FileCommonUtil.deleteDir(srcdir);
        }/*///////////////////////////////////????????????////////////////////////////////////////*/
        csvfilebuff.delete(0, csvfilebuff.length()).append("????????????," + g_chaxun_pcrProject.project_name + ", ," + g_chaxun_pcrProject.project_lot + ", , , ,\r\n");
        if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing)
            csvfilebuff.append("????????????," + "??????" + ", ," + g_chaxun_pcrProject.project_lot + ", , , ,\r\n");
        else
            csvfilebuff.append("????????????," + "??????" + ", ," + g_chaxun_pcrProject.project_lot + ", , , ,\r\n");
        csvfilebuff.append("????????????," + "????????????(???)" + ",????????????(min)," + "????????????(S)" + ",????????????, , ,\r\n");
        for (int i = 0; i < 3; i++)
            if (g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).isRead)
                csvfilebuff.append(String.valueOf(i+1) + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).targetTemp + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).holdTime + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).intervalTime + ",???, , ,\r\n");
            else
                csvfilebuff.append(String.valueOf(i+1) + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).targetTemp + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).holdTime + "," + g_chaxun_pcrProject.pcrLiuChengCanShuItems.get(i).intervalTime + ",???, , ,\r\n");
        csvfilebuff.append("????????????," + g_chaxun_checkstate_timestr + ", ," + "" + ", , , ,\r\n");
        if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing)
            csvfilebuff.append("????????????," + "??????" + ",??????," + "????????????" + ",?????????,??????,,\r\n");
        else
            csvfilebuff.append("????????????," + "??????" + ",??????," + "????????????" + ",?????????,??????," + g_chaxun_pcrProject.project_danwei + ",\r\n");
        for (int i = 0; i < BLOCKSIZE; i++)
            for (int j = 0; j < GUANGNUM; j++) {
                if (g_chaxun_pcrProject.project_item_ables[j]&&g_device_guang_ables[j]) {
                    if(g_chaxun_lamp_resultitem[j][i].ct==0x0ffff){
                        csvfilebuff.append(g_chaxun_lamp_resultitem[j][i].bianhao + "," + String.valueOf((char) ('A' + g_chaxun_blockno)) + String.valueOf(i + 1) + "," + "CH" + String.valueOf(j + 1) + "," + g_chaxun_lamp_resultitem[j][i].projectname + "," + "NoTt" + "," + g_chaxun_lamp_resultitem[j][i].jieguo + ", ,\r\n");
                    }
                    else
                    {
                        csvfilebuff.append(g_chaxun_lamp_resultitem[j][i].bianhao + "," + String.valueOf((char) ('A' + g_chaxun_blockno)) + String.valueOf(i + 1) + "," + "CH" + String.valueOf(j + 1) + "," + g_chaxun_lamp_resultitem[j][i].projectname + "," + g_chaxun_lamp_resultitem[j][i].ct + "," + g_chaxun_lamp_resultitem[j][i].jieguo + ", ,\r\n");
                    }
                }
            }
        filepath = desdir + "/" + "??????.csv";
        try {
            FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }/*///////////////////////////////////????????????////////////////////////////////////////*/
        for (int i = 0; i < GUANGNUM; i++) {
            if(g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length()).append("data,");
                for (int z = 0; z < BLOCKSIZE; z++) csvfilebuff.append(String.valueOf(z + 1) + ",");
                csvfilebuff.append("\r\n");
                if (g_chaxun_pcrProject.project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_rare.csv";
                else
                    filepath = desdir + "/" + g_chaxun_pcrProject.project_item_names[i] + "_rare.csv";
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < GUANGNUM; i++) {
            if(g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length());
                if (g_chaxun_pcrProject.project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_rare.csv";
                else
                    filepath = desdir + "/" + g_chaxun_pcrProject.project_item_names[i] + "_rare.csv";
                for (int j = 0; j < g_chaxun_indexs_data; j++) {
                    csvfilebuff.append(String.valueOf(j + 1) + ",");
                    for (int z = 0; z < BLOCKSIZE; z++)
                        csvfilebuff.append(String.valueOf(g_chaxun_raredatas[i][z][j]) + ",");
                    csvfilebuff.append("\r\n");
                }
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }/*///////////////////////////////////???????????????/////////////////////////////////////////*/
        for (int i = 0; i < GUANGNUM; i++) {
            if(g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length()).append("guiyi,");
                for (int z = 0; z < BLOCKSIZE; z++) csvfilebuff.append(String.valueOf(z + 1) + ",");
                csvfilebuff.append("\r\n");
                if (g_chaxun_pcrProject.project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_guiyi.csv";
                else
                    filepath = desdir + "/" + g_chaxun_pcrProject.project_item_names[i] + "_guiyi.csv";
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < GUANGNUM; i++) {
            if(g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length());
                if (g_chaxun_pcrProject.project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_guiyi.csv";
                else
                    filepath = desdir + "/" + g_chaxun_pcrProject.project_item_names[i] + "_guiyi.csv";
                for (int j = 0; j < g_chaxun_indexs_data; j++) {
                    csvfilebuff.append(String.valueOf(j + 1) + ",");
                    for (int z = 0; z < BLOCKSIZE; z++)
                        csvfilebuff.append(String.valueOf(g_chaxun_showguiyidatas[i][z][j]) + ",");
                    csvfilebuff.append("\r\n");
                }
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        ///////////////////////////////////????????????///////////////////////////////////////
    }

    private Thread printThread;

    public void goPrintHandle() {
        if (DEVICE) {
            showProgressDialog("?????????");
            printThread = new Thread() {
                @Override
                public void run() {
                    String printbuff;
                    String dispalytemp = "";
                    int templength;
                    printbuff = "\n\n";
                    for (int i = 0; i < BLOCKSIZE; i++) {
                        if (g_chaxun_qcenable)
                            if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
                                if (i <= 1) continue;
                            } else {
                                if (i <= 2) continue;
                            }
                        String check_project_name = "????????????:" + g_chaxun_pcrProject.project_name;
                        String check_project_lot = "??????:" + g_chaxun_pcrProject.project_lot;
                        templength = getLength(check_project_name);
                        templength += getLength(check_project_lot);
                        dispalytemp = check_project_name;
                        for (int z = 0; z < (48 - templength); z++) dispalytemp += " ";
                        dispalytemp += check_project_lot;
                        printbuff += dispalytemp;
                        printbuff += "\n";
                        String check_project_sampleno = "????????????:" + g_chaxun_samplenos[i];
                        String check_project_kongwei = "?????????:" + String.valueOf((char) ('A' + g_chaxun_blockno)) + String.valueOf(i + 1);
                        templength = getLength(check_project_sampleno);
                        templength += getLength(check_project_kongwei);
                        dispalytemp = check_project_sampleno;
                        for (int z = 0; z < (48 - templength); z++) dispalytemp += " ";
                        dispalytemp += check_project_kongwei;
                        printbuff += dispalytemp;
                        printbuff += "\n";
                        printbuff += "????????????:" + g_chaxun_checkstate_timestr + "\n";
                        printbuff += "????????????:" + "\n";
                        for (int j = 0; j < GUANGNUM; j++) {
                            if (g_chaxun_pcrProject.project_item_ables[j]) {
                                if (g_chaxun_qcenable) {
                                    if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
                                        if (i > 1) {
                                            if (g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")) {
                                                if(g_chaxun_lamp_resultitem[j][i].ct==0x0ffff)
                                                {
                                                    printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "," + "NoTt" + "\n";
                                                }
                                                else {
                                                    printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "," + g_chaxun_lamp_resultitem[j][i].ct + "\n";
                                                }
                                            }else
                                                printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "\n";
                                        }
                                    } else {
                                        if (i > 2) {
                                            if ((g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")) && (g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")))
                                                printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "\n";
                                            else
                                                printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + g_chaxun_pcrProject.project_danwei + "\n";
                                        }
                                    }
                                } else {
                                    if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
                                        if (g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")) {
                                            if(g_chaxun_lamp_resultitem[j][i].ct==0x0ffff){
                                                printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "," + "NoTt" + "\n";
                                            }
                                            else {
                                                printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "," + g_chaxun_lamp_resultitem[j][i].ct + "\n";
                                            }
                                        }else
                                            printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "\n";
                                    } else {
                                        if ((g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")) && (g_chaxun_lamp_resultitem[j][i].jieguo.equals("??????")))
                                            printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + "\n";
                                        else
                                            printbuff += g_chaxun_lamp_resultitem[j][i].projectname + ":" + g_chaxun_lamp_resultitem[j][i].jieguo + g_chaxun_pcrProject.project_danwei + "\n";
                                    }
                                }
                            }
                        }
                        printbuff += "\n\n";
                    }
                    PrintSerial printSerial = PrintSerial.getInstance();
                    int ret = printSerial.printString(printbuff);
                    if (ret == 0) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    } else if (ret == 2) {
                        Message msg = new Message();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 6;
                        handler.sendMessage(msg);
                        LogUtils.d("ret:" + ret);
                    }
                }
            };
            printThread.start();
        } else {
            final CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity()).setMessage("???????????????????????????").setTitle("??????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            CustomDialog tempCustomDialog = builder1.create(R.layout.dialog_normal_feiquanping_message);
            tempCustomDialog.setCanceledOnTouchOutside(false);
            tempCustomDialog.show();
        }
    }

    public int getLength(String str) {
        int length = 0;
        if (str != null) {
            int nCount = str.length();
            char[] nihao = new char[nCount];
            length = nCount;
            str.getChars(0, nCount, nihao, 0);
            for (int i = 0; i < nCount; i++) {
                char cha = nihao[i];/*            ushort uni = cha.unicode();*/
                if (cha >= 0x4E00 && cha <= 0x9FA5) length++;
            }
        }
        return length;
    }

    public void goDaoChuHandle() {
        savaDataToDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkstate_timestr, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkstate_timestr);
        if (!isUpanExist(getActivity())) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("??????????????????U??????").setTitle("U????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            showProgressDialog("?????????");/*savedata();*/
            savaDataToDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkstate_timestr, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkstate_timestr);
            copyThread = new Thread() {
                @Override
                public void run() {
                    File srcDir = FileCommonUtil.getFileByPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_selectDir);
                    String filename = srcDir.getName();
                    List<File> files2 = FileCommonUtil.listFilesInDir(srcDir);
                    String targetstr = StorageUtil.UPANROOT + File.separator + Canstant.UPAN_DAOCHU_DATA_DIR + filename + ".zip";
                    FileCommonUtil.deleteFile(targetstr);
                    FileCommonUtil.createOrExistsFile(targetstr);
                    final File targetfile = FileCommonUtil.getFileByPath(targetstr);
                    LogUtils.d("targetstr:" + targetstr);
                    ZipUtil.zipFiles(files2, targetfile, new ZipUtil.ZipListener() {
                        @Override
                        public void zipProgress(int zipProgress) {
                        }

                        @Override
                        public void onSuccess() {
                            try {
                                sleep(7000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (FileCommonUtil.isFileExists(targetfile)) {
                                delNoUseFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_chaxun_checkstate_timestr);
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onFailed() {
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    });
                }
            };
            copyThread.start();
        }
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {/*TODO now visible to user*/
            LogUtils.d(TAG, "onHiddenChanged show");
            char s = '1';
            readJiLuDir();
            init_result_ct_view(view);/*            guangList.clear(); for (int i = 0; i < GUANGNUM; i++) { guangList.add(new CheckBoxItem("CH" + String.valueOf((char) (s + i)), false, g_chaxun_pcrProject.project_item_ables[i])); }*/
            checkBoxAdapterGuang.notifyDataSetChanged();
            updata_result_ct_list();
            initCtLineChart(chart_chaxun_step2);
            updataCtLineChart(chart_chaxun_step2);
            if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing)
                item_jieguo.setText("??????");
            else item_jieguo.setText("??????(" + g_chaxun_pcrProject.project_danwei + ")");
        } else {/*TODO now invisible to user*/
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
//        onHiddenChanged(false);
    }/*////////////////////////////////////////////////////////////////////////////////////////////////////*/

    private void init_result_ct_view(View view) {
        chart_chaxun_step2 = (LineChart) view.findViewById(R.id.chart_chaxun_step2);
        cb_chaxun_step2_all = (CheckBox) view.findViewById(R.id.cb_chaxun_step2_all);
        rv_chaxun_step2_guang = (RecyclerView) view.findViewById(R.id.rv_chaxun_step2_guang);
        ll_chaxun_step2_limit = (RelativeLayout) view.findViewById(R.id.ll_chaxun_step2_limit);
        et_chaxun_step2_limit = (EditText) view.findViewById(R.id.et_chaxun_step2_limit);
        item_jieguo = (TextView) view.findViewById(R.id.item_jieguo);
        rv_chaxun_step2_jieguo = (RecyclerView) view.findViewById(R.id.rv_chaxun_step2_jieguo);
        rv_chaxun_step2_jieguo.setHasFixedSize(true);
        rv_chaxun_step2_jieguo.setItemViewCacheSize(0);
        rv_chaxun_step2_jieguo.setDrawingCacheEnabled(true);
        rv_chaxun_step2_jieguo.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        char s = '1';
        guangList.clear();
        for (int i = 0; i < GUANGNUM; i++) {/*guangList.add(new CheckBoxItem("CH" + String.valueOf((char) (s + i)), true, g_chaxun_pcrProject.project_item_ables[i]));*/
            guangList.add(new CheckBoxItem(g_chaxun_pcrProject.project_item_names[i], true, g_chaxun_pcrProject.project_item_ables[i]&&g_device_guang_ables[i]));
        }
        checkBoxAdapterGuang = new CheckBoxAdapter(guangList, R.layout.item_checkbox);
        LinearLayoutManager layoutManagerRA = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        rv_chaxun_step2_guang.setLayoutManager(layoutManagerRA);
        layoutManagerRA.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_chaxun_step2_guang.setAdapter(checkBoxAdapterGuang);
        checkBoxAdapterGuang.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                checkBoxAdapterGuang.notifyDataSetChanged();
                if (!checkBoxAdapterGuang.isAllSelect()) {
                    cb_chaxun_step2_all.setOnCheckedChangeListener(null);
                    cb_chaxun_step2_all.setChecked(false);
                    cb_chaxun_step2_all.setOnCheckedChangeListener(result_ct_allListener);
                }
                updata_result_ct_list();
                updataCtLineChart(chart_chaxun_step2);
            }
        });
        cb_chaxun_step2_all.setOnCheckedChangeListener(result_ct_allListener);
        et_chaxun_step2_limit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) et_chaxun_step2_limit.addTextChangedListener(result_ct_text_watcher);
                else et_chaxun_step2_limit.removeTextChangedListener(result_ct_text_watcher);
            }
        });
        et_chaxun_step2_limit.setFilters(new PointLengthFilter[]{new PointLengthFilter(2)});
        result_ct_jieguoList.clear();
        result_ct_Adapter = new LampResultItemAdapter(result_ct_jieguoList);
        layoutManagerRA2 = new LinearLayoutManager(getActivity());
        rv_chaxun_step2_jieguo.setLayoutManager(layoutManagerRA2);
        layoutManagerRA2.setOrientation(LinearLayoutManager.VERTICAL);
        result_ct_Adapter.setOnItemClickListener(new LampResultItemAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                updataCtLineChart(chart_chaxun_step2);
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                return false;
            }
        });
        result_ct_Adapter.setIdVisiable(false);
        result_ct_Adapter.setCtVisiable(true);
        result_ct_Adapter.setNongduVisiable(true);
        result_ct_Adapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
            }
        });
        rv_chaxun_step2_jieguo.setAdapter(result_ct_Adapter);
    }

    public void getAntoThresvalue() {
        for (int i = 0; i < GUANGNUM; i++) g_chaxun_Thresvalue[i] = 0;
        double dThresValue = 0;
        double[] dTemp = new double[1];
        dTemp[0] = g_chaxun_indexs_data;
        double[] dFluVal = new double[1];
        dFluVal[0] = 0;
        double[] dPara = new double[2];
        dPara[0] = 0;
        dPara[1] = 0;
        double[] px = new double[g_chaxun_indexs_data];
        for (int i = 0; i < g_chaxun_indexs_data; i++) px[i] = i + 1;
        for (int i = 0; i < GUANGNUM; i++) {
            double count = 0, sum = 0;
            for (int j = 0; j < BLOCKSIZE; j++) {
                if (!g_anaAlgorithm.AbleToCalculate(px, g_chaxun_fenxilvbodatas[i][j], g_chaxun_indexs_data, g_false_FunAbleToCalcuParamInfo, 5))
                    g_chaxun_ct[i][j] = 0x0ffff;
                else {
                    g_anaAlgorithm.GenStdCurveBySndDerivative(1, g_chaxun_indexs_data, px, g_chaxun_fenxiguiyidatas[i][j], dTemp, dPara, false, 4);
                    dThresValue = dTemp[0];
                    double min = 100000, max = -1000;
                    for (int k = 0; k < g_chaxun_indexs_data; k++) {
                        min = min < px[k] ? min : px[k];
                        max = max > px[k] ? max : px[k];
                    }
                    if (dThresValue <= max && dThresValue >= min) {
                        g_anaAlgorithm.GenStdCurveBySelFluThres(1, g_chaxun_indexs_data, g_chaxun_fenxiguiyidatas[i][j], px, dThresValue, dTemp, dPara, false);
//                        if (dTemp[0] > max || dTemp[0] < 1) dTemp[0] = 0;
                    } else dTemp[0] = 0;
                    if (dTemp[0] != 0) {
                        sum += dTemp[0];
                        count++;
                    }
                }
            }
            g_chaxun_Thresvalue[i] = (float) Math.round(sum / count * 100) / 100;
            if(g_pcrDataHandleRef.g_jieguo_usedata_type==0)//guiyi
            {
                if(g_pcrDataHandleRef.g_guiyiself_thresvalues[i])//??????
                {
                    g_chaxun_Thresvalue[i] = (float) g_chaxun_Thresvalue[i];
                }
                else
                {
                    g_chaxun_Thresvalue[i] = (float) g_pcrDataHandleRef.g_guiyithresvalues[i];
                }
            }
            else//Rn
            {
                if(g_pcrDataHandleRef.g_nself_thresvalues[i])//??????
                {
                    g_chaxun_Thresvalue[i] = (float) g_chaxun_Thresvalue[i];
                }
                else
                {
                    g_chaxun_Thresvalue[i] = (float) g_pcrDataHandleRef.g_nthresvalues[i];
                }
            }
        }
    }

    public void handledata() {
        double dThresValue = 0;
        double[] dTemp = new double[1];
        dTemp[0] = g_chaxun_indexs_data;
        double[] dFluVal = new double[1];
        dFluVal[0] = 0;
        double[] dPara = new double[2];
        dPara[0] = 0;
        dPara[1] = 0;
        double[] px = new double[g_chaxun_indexs_data];
        double ct_tt=0;
        for(int i=0;i<g_chaxun_pcrProject.getPcrLiuChengCanShuItems().size();i++)
        {
            if(g_chaxun_pcrProject.getPcrLiuChengCanShuItems().get(i).isRead)
            {
                ct_tt = g_chaxun_pcrProject.getPcrLiuChengCanShuItems().get(i).intervalTime/60.0;
            }
        }

        for (int i = 0; i < g_chaxun_indexs_data; i++) px[i] = i + 1;
        for (int i = 0; i < GUANGNUM; i++)
            for (int j = 0; j < BLOCKSIZE; j++) {
                if (g_pcrDataHandleRef.g_ct_suanfa_type == 0)//?????????
                {
                    if (!g_anaAlgorithm.AbleToCalculate(px, g_chaxun_fenxilvbodatas[i][j], g_chaxun_indexs_data, g_false_FunAbleToCalcuParamInfo, 5)) {/*                    g_chaxun_ct[i][j] = -1;*/
                        g_chaxun_ct[i][j] = 0x0ffff;
                    } else {
                        g_anaAlgorithm.GenStdCurveBySndDerivative(1, g_chaxun_indexs_data, px, g_chaxun_fenxiguiyidatas[i][j], dTemp, dPara, false, 4);
                        g_chaxun_ct[i][j] = (double) Math.round(dTemp[0] * 100*ct_tt) / 100;
                    }
                }
                else{
                    if (!g_anaAlgorithm.AbleToCalculate(px, g_chaxun_fenxilvbodatas[i][j], g_chaxun_indexs_data, g_false_FunAbleToCalcuParamInfo, 5)) {/*                    g_chaxun_ct[i][j] = -1;*/
                        g_chaxun_ct[i][j] = 0x0ffff;
                    } else {
                        dThresValue = g_chaxun_Thresvalue[i];
                        double min = 100000, max = -1000;
                        for (int k = 0; k < g_chaxun_indexs_data; k++) {
                            min = min < g_chaxun_fenxiguiyidatas[i][j][k] ? min : g_chaxun_fenxiguiyidatas[i][j][k];
                            max = max > g_chaxun_fenxiguiyidatas[i][j][k] ? max : g_chaxun_fenxiguiyidatas[i][j][k];
                        }
                        if (dThresValue <= max && dThresValue >= min) {
                            g_anaAlgorithm.GenStdCurveBySelFluThres(1, g_chaxun_indexs_data, px, g_chaxun_fenxiguiyidatas[i][j], dThresValue, dTemp, dPara, false);
                            if (dTemp[0] > g_chaxun_indexs_data || dTemp[0] < 1) {
                                g_chaxun_ct[i][j] = 0x0ffff;
                            } else
                                g_chaxun_ct[i][j] = (double) Math.round(dTemp[0] * 100 * ct_tt) / 100;
                        } else {
                            g_chaxun_ct[i][j] = 0x0ffff;
                        }
                    }
                }
            }
        jisuanjieguo();
    }

    static public void jisuanjieguo() {
        boolean qcright = true;
        double pcnongdu = 0;
        double biaonongdu = 0;
        double k1 = 0;
        double b1 = 0;
        boolean jiaozhun = false;
        double samplenongdu = 0;
        if (g_chaxun_pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
            if (g_chaxun_qcenable) {
                for (int i = 0; i < GUANGNUM; i++) {
                    if (g_chaxun_pcrProject.project_item_ables[i]) {
                        if (g_chaxun_pcrProject.project_neican_tongdao != i + 1) {
                            if (g_chaxun_ct[i][0] > g_chaxun_pcrProject.project_babiaos[i]) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("??????????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], String.valueOf(g_chaxun_ct[i][0]), g_chaxun_ct[i][0]);
                                LogUtils.d("????????????????????????Tt???");
                            }

                            if (g_chaxun_ct[i][1] < g_chaxun_pcrProject.project_ncs[i]) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                LogUtils.d("??????????????????");
                            } else {
                                if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                {
                                    if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][1] <= g_chaxun_pcrProject.project_neican) {
                                        g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                        LogUtils.d("??????????????????");
                                    } else {
                                        qcright = false;
                                        g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                        LogUtils.d("??????????????????");
                                    }
                                } else {
                                    g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                    LogUtils.d("??????????????????");
                                }
                            }

                            if (g_chaxun_ct[i][2] < g_chaxun_pcrProject.project_item_refs[i]) {
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("????????????");
                            } else {
                                if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                {
                                    if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][2] >= g_chaxun_pcrProject.project_neican) {
                                        g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                        LogUtils.d("????????????");
                                    } else {
                                        g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                        LogUtils.d("????????????");
                                    }
                                } else {
                                    g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                    LogUtils.d("????????????");
                                }
                            }

                            if (g_chaxun_ct[i][3] < g_chaxun_pcrProject.project_item_refs[i]) {
                                g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                LogUtils.d("????????????");
                            } else {
                                if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                {
                                    if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][3] >= g_chaxun_pcrProject.project_neican) {
                                        g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                        LogUtils.d("????????????");
                                    } else {
                                        g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                        LogUtils.d("????????????");
                                    }
                                } else {
                                    g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                    LogUtils.d("????????????");
                                }
                            }
                        }
                        else
                        {
                            if (g_chaxun_ct[i][0] > g_chaxun_pcrProject.project_neican) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("??????????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], String.valueOf(g_chaxun_ct[i][0]), g_chaxun_ct[i][0]);
                                LogUtils.d("????????????????????????Tt???");
                            }

                            if (g_chaxun_ct[i][1] > g_chaxun_pcrProject.project_neican) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                LogUtils.d("??????????????????");
                            } else {
                                    g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                    LogUtils.d("??????????????????");
                            }

                            if (g_chaxun_ct[i][2] < g_chaxun_pcrProject.project_neican) {
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("????????????");
                            } else {
                                    g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                    LogUtils.d("????????????");
                            }

                            if (g_chaxun_ct[i][3] < g_chaxun_pcrProject.project_neican) {
                                g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                LogUtils.d("????????????");
                            } else {
                                    g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                    LogUtils.d("????????????");
                            }
                        }
                    }
                }
            } else//????????? ????????? ???????????????????????? ?????????????????? ??????????????????
            {
                for (int i = 0; i < GUANGNUM; i++) {
                    for (int j = 0; j < BLOCKSIZE; j++) {
                        if (g_chaxun_pcrProject.project_item_ables[i]) {
                            if (g_chaxun_pcrProject.project_neican_tongdao != i + 1) {
                                if (g_chaxun_ct[i][j] < g_chaxun_pcrProject.project_item_refs[i]) {
                                    g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                    LogUtils.d("????????????");
                                } else {
                                    if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                    {
                                        if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][j] >= g_chaxun_pcrProject.project_neican) {
                                            g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                            LogUtils.d("????????????");
                                        } else {
                                            g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                            LogUtils.d("????????????");
                                        }
                                    } else {
                                        g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                        LogUtils.d("????????????");
                                    }
                                }
                            }
                            else
                            {
                                if (g_chaxun_ct[i][j] < g_chaxun_pcrProject.project_neican) {
                                    g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                    LogUtils.d("????????????");
                                } else {
                                        g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                        LogUtils.d("????????????");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (g_chaxun_qcenable) {
                for (int i = 0; i < GUANGNUM; i++) {
                    if (g_chaxun_pcrProject.project_item_ables[i]) {
                        if (g_chaxun_pcrProject.project_neican_tongdao != i + 1) {
                            if (g_chaxun_ct[i][1] < g_chaxun_pcrProject.project_ncs[i]) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                LogUtils.d("??????????????????");
                            } else {
                                if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                {
                                    if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][1] <= g_chaxun_pcrProject.project_neican) {
                                        g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                        LogUtils.d("??????????????????");
                                    } else {
                                        qcright = false;
                                        g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                        LogUtils.d("??????????????????");
                                    }
                                } else {
                                    g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                    LogUtils.d("??????????????????");
                                }
                            }

                            pcnongdu = g_chaxun_ct[i][0] * g_chaxun_pcrProject.project_item_ks[i] + g_chaxun_pcrProject.project_item_bs[i];
                            biaonongdu = g_chaxun_ct[i][2] * g_chaxun_pcrProject.project_item_ks[i] + g_chaxun_pcrProject.project_item_bs[i];
                            if ((Math.abs(pcnongdu - g_chaxun_pcrProject.project_babiaos[i]) <= 0.4) && (Math.abs(biaonongdu - g_chaxun_pcrProject.project_dingliang_biaozhunnongdu) <= 0.4)) {
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("PC????????????");
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("??????????????????");
                            } else if ((Math.abs(pcnongdu - g_chaxun_pcrProject.project_babiaos[i]) < 0.4) && (Math.abs(biaonongdu - g_chaxun_pcrProject.project_dingliang_biaozhunnongdu) > 0.4)) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("PC????????????");
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("??????????????????");
                            } else if ((Math.abs(pcnongdu - g_chaxun_pcrProject.project_babiaos[i]) > 0.4) && (Math.abs(biaonongdu - g_chaxun_pcrProject.project_dingliang_biaozhunnongdu) < 0.4)) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("PC????????????");
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("??????????????????");
                            } else {
                                //k=???y1???y2?????????x1???x2???
                                //b=???y2x1???y1x2?????????x1???x2???
                                k1 = (g_chaxun_pcrProject.project_babiaos[i] - g_chaxun_pcrProject.project_dingliang_biaozhunnongdu) / (g_chaxun_ct[i][0] - g_chaxun_ct[i][2]);
                                b1 = (g_chaxun_pcrProject.project_dingliang_biaozhunnongdu * g_chaxun_ct[i][0] - g_chaxun_pcrProject.project_babiaos[i] * g_chaxun_ct[i][2]) / (g_chaxun_ct[i][0] - g_chaxun_ct[i][2]);
                                if (Math.abs(k1 / g_chaxun_pcrProject.project_item_ks[i] - 1) > g_chaxun_kadjustref) {
                                    qcright = false;
                                    g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                    LogUtils.d("PC????????????");
                                    g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                    LogUtils.d("??????????????????");
                                } else {
                                    qcright = false;
                                    g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                    LogUtils.d("PC????????????");
                                    g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                    LogUtils.d("??????????????????");
                                    jiaozhun = true;
                                }
                            }

                            if (g_chaxun_ct[i][3] < g_chaxun_pcrProject.project_item_refs[i]) {
                                if (jiaozhun) {
                                    samplenongdu = g_chaxun_ct[i][3] * k1 + b1;
                                    g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], String.valueOf(samplenongdu), g_chaxun_ct[i][3]);
                                    LogUtils.d("????????????");
                                } else {
                                    samplenongdu = g_chaxun_ct[i][3] * g_chaxun_pcrProject.project_item_ks[i] + g_chaxun_pcrProject.project_item_bs[i];
                                    g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], String.valueOf(samplenongdu), g_chaxun_ct[i][3]);
                                    LogUtils.d("????????????");
                                }
                            } else {
                                if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                {
                                    if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][3] >= g_chaxun_pcrProject.project_neican) {
                                        g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                        LogUtils.d("????????????");
                                    } else {
                                        g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                        LogUtils.d("????????????");
                                    }
                                } else {
                                    g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                    LogUtils.d("????????????");
                                }
                            }
                        }
                        else
                        {
                            if (g_chaxun_ct[i][0] > g_chaxun_pcrProject.project_neican) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][0]);
                                LogUtils.d("??????????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][0].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], String.valueOf(g_chaxun_ct[i][0]), g_chaxun_ct[i][0]);
                                LogUtils.d("????????????????????????Tt???");
                            }

                            if (g_chaxun_ct[i][1] > g_chaxun_pcrProject.project_neican) {
                                qcright = false;
                                g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                LogUtils.d("??????????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][1].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "??????", g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][1]);
                                LogUtils.d("??????????????????");
                            }

                            if (g_chaxun_ct[i][2] < g_chaxun_pcrProject.project_neican) {
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][2].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_chaxun_samplenos[2], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][2]);
                                LogUtils.d("????????????");
                            }

                            if (g_chaxun_ct[i][3] < g_chaxun_pcrProject.project_neican) {
                                g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                LogUtils.d("????????????");
                            } else {
                                g_chaxun_lamp_resultitem[i][3].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_chaxun_samplenos[3], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][3]);
                                LogUtils.d("????????????");
                            }
                        }
                    }
                }
            } else//????????? ????????? ???????????????????????? ?????????????????? ??????????????????
            {
                for (int i = 0; i < GUANGNUM; i++) {
                    for (int j = 0; j < BLOCKSIZE; j++) {
                        if (g_chaxun_pcrProject.project_item_ables[i]) {
                            if (g_chaxun_pcrProject.project_neican_tongdao != i + 1) {
                                if (g_chaxun_ct[i][j] < g_chaxun_pcrProject.project_item_refs[i]) {
                                    samplenongdu = g_chaxun_ct[i][3] * g_chaxun_pcrProject.project_item_ks[i] + g_chaxun_pcrProject.project_item_bs[i];
                                    g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j],//
                                            g_chaxun_pcrProject.project_item_names[i], String.valueOf(samplenongdu), g_chaxun_ct[i][j]);
                                    LogUtils.d("????????????");
                                } else {
                                    if (g_chaxun_pcrProject.project_neican_tongdao != 0)//?????????
                                    {
                                        if (g_chaxun_ct[g_chaxun_pcrProject.project_neican_tongdao - 1][j] >= g_chaxun_pcrProject.project_neican) {
                                            g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j],//
                                                    g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                            LogUtils.d("????????????");
                                        } else {
                                            g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j],//
                                                    g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                            LogUtils.d("????????????");
                                        }
                                    } else {
                                        g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j],//
                                                g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                        LogUtils.d("????????????");
                                    }
                                }
                            }
                            else{
                                if (g_chaxun_ct[i][j] < g_chaxun_pcrProject.project_neican) {
                                    g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                    LogUtils.d("????????????");
                                } else {
                                    g_chaxun_lamp_resultitem[i][j].setLampResultItem(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_blockno * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_chaxun_samplenos[j], g_chaxun_pcrProject.project_item_names[i], "??????", g_chaxun_ct[i][j]);
                                    LogUtils.d("????????????");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (g_chaxun_qcenable) {
            if (qcright) {
                g_chaxun_lampQcData = new LampQcData(g_chaxun_checkTimeStr, CAIJICOUNT, g_chaxun_indexs_data, g_chaxun_raredatas, g_chaxun_showlvbodatas, g_chaxun_showguiyidatas, g_chaxun_Thresvalue, g_chaxun_ct, g_chaxun_kadjustref);
                SharedPreferencesUtils.saveLampQcData(g_chaxun_lampQcData, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_chaxun_pcrProject.project_filename);
            } else {
                FileCommonUtil.deleteFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_chaxun_pcrProject.project_filename);
            }
        } else {
            g_chaxun_lampQcData = SharedPreferencesUtils.getLampQcData(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_chaxun_pcrProject.project_filename);
        }
    }

    private void updata_result_ct_list() {/*handledata();*/
        if (canxiugai)
            handledata();
        result_ct_jieguoList.clear();
        for (int j = 0; j < GUANGNUM; j++)/*?????????1234//*/
            if (guangList.get(j).isCheck && guangList.get(j).isShow&&g_device_guang_ables[j])
                for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                    result_ct_jieguoList.add(g_chaxun_lamp_resultitem[j][z]);
        result_ct_Adapter.notifyDataSetChanged();
        updataLimitEditAndAll();
    }

    private void updata_result_ct_list_only() {/*handledata();*/
        if (canxiugai)
            handledata();
        result_ct_jieguoList.clear();
        for (int j = 0; j < GUANGNUM; j++)/*?????????1234//*/
            if (guangList.get(j).isCheck && guangList.get(j).isShow&&g_device_guang_ables[j])
                for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                    result_ct_jieguoList.add(g_chaxun_lamp_resultitem[j][z]);
        result_ct_Adapter.notifyDataSetChanged();
    }

    public class MyMarkerView1 extends MarkerView {
        private TextView tvContent;

        public MyMarkerView1(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            LineChart chart = (LineChart) getChartView();
            LineData lineData = ((LineChart) chart).getLineData();
            List<ILineDataSet> lineDataSets = lineData.getDataSets();
            for (int i = 0; i < lineDataSets.size(); i++) {
                LineDataSet lineDataSet = (LineDataSet) lineDataSets.get(i);
                if (lineDataSet.getValues().get((int) e.getX() - 1).getY() == e.getY()) {
                    tvContent.setTextColor(lineDataSet.getColor());
                    break;
                }
            }
            tvContent.setText("X:" + e.getX() + "\r\nY:" + e.getY());
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            LineChart chart = (LineChart) getChartView();
            return new MPPointF(-(getWidth() / 2) + chart.getMeasuredWidth(), -getHeight() - chart.getMeasuredHeight());/*-(getWidth() / 2)*/
        }
    }

    void initCtLineChart(LineChart lineChart) {
        final LineChart chartTemp = lineChart;
        Ylow = (float) 20000.0;
        Yhigh = (float) -20000.0;
        float temp;
        dataSets.clear();
//        chartTemp.clear();
//        chartTemp.invalidate();
        double ct_tt =0;
        for(int i=0;i<g_chaxun_pcrProject.getPcrLiuChengCanShuItems().size();i++)
        {
            if(g_chaxun_pcrProject.getPcrLiuChengCanShuItems().get(i).isRead)
            {
                ct_tt = g_chaxun_pcrProject.getPcrLiuChengCanShuItems().get(i).intervalTime/60.0;
            }
        }

        for (int j = 0; j < GUANGNUM; j++)/*?????????1234//*/
            for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/ {
                ArrayList<Entry> values = new ArrayList<>();
                if(g_chaxun_indexs_data!=0) {
                    for (int a = 0; a < g_chaxun_indexs_data; a++) {
                        temp = (float) g_chaxun_showguiyidatas[j][z][a];
                        values.add(new Entry((float) ((a + 1)*ct_tt), temp));
                        if (Ylow > temp) Ylow = temp;
                        if (Yhigh < temp) Yhigh = temp;
                    }
                }else{
                    values.add(new Entry((float) (0), 0));
                }
                LineDataSet set = new LineDataSet(values, String.valueOf(j * BLOCKSIZE + z) + "_" + String.valueOf(j + 1));/*DataSet 1*/
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                if (result_ct_Adapter.getCheckList().contains(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + j * BLOCKSIZE + z + 1)) {
                    set.setLineWidth(2f);
                    set.setColor(Color.BLUE);
                } else {
                    set.setLineWidth(1f);
                    set.setColor(chartColors[j]);
                }
                set.setDrawCircles(false);
                set.setDrawValues(false);  /*???????????????*/
                set.setHighLightColor(Color.BLACK);/*????????????????????????????????????????????????*/
                set.setHighlightEnabled(true);/*???????????????????????????*/
                dataSets.add(set);
            }
        lineData = new LineData(dataSets);
        chartTemp.setData(lineData);
        chartTemp.getDescription().setEnabled(false);
        chartTemp.setDrawGridBackground(false);
        chartTemp.animateX(100);
        Legend legend = chartTemp.getLegend();
        legend.setEnabled(false);
        axis = chartTemp.getXAxis();
        axis.setDrawLabels(true);
        axis.setDrawLimitLinesBehindData(true);
        axis.setDrawGridLines(false);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setAvoidFirstLastClipping(false);
        if (g_chaxun_allcount == 0) {
            axis.setAxisMaximum(1);
            axis.setLabelCount((int) (1 + 4) / 5 + 1);
        } else {
            axis.setAxisMaximum((float) (g_chaxun_allcount*ct_tt));
            axis.setLabelCount((int) (g_chaxun_allcount + 4) / 5 + 1);
        }
        axis.setAxisMinimum(0);
        leftAxis = chartTemp.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(true);
//        if (Yhigh - Ylow < 0.05) Yhigh = (float) (Ylow + 0.05);
        if (Yhigh < g_ymin)
            Yhigh = (float) g_ymin;
        if(Yhigh<Ylow)
            Ylow=0;
        leftAxis.setAxisMaximum((float) (Yhigh + (Yhigh - Ylow) * 0.1));
        leftAxis.setAxisMinimum((float) (Ylow - (Yhigh - Ylow) * 0.05));
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (abs(value) > 100)
                    return String.format(" %.0f", value);
                else if (abs(value) > 10)
                    return String.format(" %.1f", value);
                else
                    return String.format(" %.2f", value);
            }
        });
        chartTemp.getAxisRight().setEnabled(false);
        leftAxis.removeAllLimitLines();
        leftAxis.setDrawZeroLine(true);
        chartTemp.setTouchEnabled(true);
        chartTemp.setScaleEnabled(false);
        chartTemp.setDoubleTapToZoomEnabled(false);
        chartTemp.setHighlightPerTapEnabled(false);
        chartTemp.setHighlightPerDragEnabled(false);/*???????????????*/
        chartTemp.setDragDecelerationEnabled(false);/*????????????*/
        if(g_chaxun_allcount ==0 ) {
            chartTemp.setVisibleXRange(1,1);
//            chartTemp.setVisibleXRangeMaximum(1);
        }else{
            chartTemp.setVisibleXRange((float) (g_chaxun_allcount*ct_tt+1),(float) (g_chaxun_allcount*ct_tt+1));
//            chartTemp.setVisibleXRangeMaximum(g_chaxun_allcount);
        }
        chartTemp.setDrawBorders(false);     /*?????????????????????????????????????????????*/
        MyMarkerView1 mv = new MyMarkerView1(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(chartTemp);
        chartTemp.setMarker(mv);
        final float finalYhigh = Yhigh;
        final float finalYlow = Ylow;
        if (canxiugai) {
            chartTemp.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    if (chartTemp.getAxisLeft().getLimitLines().size() == 1 && Math.abs(chartTemp.getValuesByTouchPoint(me.getX(), me.getY(), LEFT).y - chartTemp.getAxisLeft().getLimitLines().get(0).getLimit()) / (chartTemp.getAxisLeft().getAxisMaximum() - chartTemp.getAxisLeft().getAxisMinimum()) < 0.1) {
                        chartTemp.getAxisLeft().removeAllLimitLines();
                        float temp = (float) chartTemp.getValuesByTouchPoint(me.getX(), me.getY(), LEFT).y;
                        if (temp > finalYhigh) temp = finalYhigh;
                        if (temp < finalYlow) temp = finalYlow;
                        temp = (float) Math.round(temp * 100) / 100;
                        LimitLine ll = new LimitLine(temp, String.valueOf(temp));
                        ll.setLineColor(Color.RED);
                        ll.setLineWidth(2f);
                        ll.setTextColor(Color.GRAY);
                        ll.setTextSize(12f);
                        chartTemp.getAxisLeft().addLimitLine(ll);
                        limitTragEnable = true;
                    }
                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    if (limitTragEnable) {
                        chartTemp.getAxisLeft().removeAllLimitLines();
                        float temp = (float) chartTemp.getValuesByTouchPoint(me.getX(), me.getY(), LEFT).y;
                        if (temp > finalYhigh) temp = finalYhigh;
                        if (temp < finalYlow) temp = finalYlow;
                        temp = (float) Math.round(temp * 100) / 100;
                        LimitLine ll = new LimitLine(temp, String.valueOf(temp));
                        ll.setLineColor(Color.BLACK);
                        ll.setLineWidth(2f);
                        ll.setTextColor(Color.GRAY);
                        ll.setTextSize(12f);
                        chartTemp.getAxisLeft().addLimitLine(ll);
                        limitTragEnable = false;
                        g_chaxun_Thresvalue[selectGuang] = temp;
                        et_chaxun_step2_limit.setText(String.valueOf(temp));
                        updata_result_ct_list_only();
                    }
                }

                @Override
                public void onChartLongPressed(MotionEvent me) {/*LogUtils.d("LongPress", "Chart longpressed.");*/}

                @Override
                public void onChartDoubleTapped(MotionEvent me) {/*LogUtils.d("DoubleTap", "Chart double-tapped.");*/}

                @Override
                public void onChartSingleTapped(MotionEvent me) {/*LogUtils.d("SingleTap", "Chart single-tapped.");*/}

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {/*LogUtils.d("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);*/}

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {/*LogUtils.d("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);*/}

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {/*LogUtils.d("Translate / Move", "dX: " + dX + ", dY: " + dY);*/}
            });
            chartTemp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            if (limitTragEnable) {
                                chartTemp.getAxisLeft().removeAllLimitLines();
                                float temp = (float) chartTemp.getValuesByTouchPoint(event.getX(), event.getY(), LEFT).y;
                                if (temp > finalYhigh) temp = finalYhigh;
                                if (temp < finalYlow) temp = finalYlow;
                                temp = (float) Math.round(temp * 100) / 100;
                                LimitLine ll = new LimitLine(temp, String.valueOf(temp));
                                ll.setLineColor(Color.RED);
                                ll.setLineWidth(2f);
                                ll.setTextColor(Color.GRAY);
                                ll.setTextSize(12f);
                                chartTemp.getAxisLeft().addLimitLine(ll);
                                g_chaxun_Thresvalue[selectGuang] = temp;
                                et_chaxun_step2_limit.setText(String.valueOf(temp));
                                updata_result_ct_list_only();
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        chartTemp.setViewPortOffsets(50, 20, 30, 20);
        chartTemp.invalidate();
    }

    void updataLimitEditAndAll() {
        LogUtils.d("updataLimitEditAndAll()");
        LinkedList<Integer> selectGuangs = checkBoxAdapterGuang.getCheckedList();
        int temp = selectGuangs.size();
        if (temp == checkBoxAdapterGuang.getShowItemCount()) {
            cb_chaxun_step2_all.setOnCheckedChangeListener(null);
            cb_chaxun_step2_all.setChecked(true);
            cb_chaxun_step2_all.setOnCheckedChangeListener(result_ct_allListener);
        } else {
            cb_chaxun_step2_all.setOnCheckedChangeListener(null);
            cb_chaxun_step2_all.setChecked(false);
            cb_chaxun_step2_all.setOnCheckedChangeListener(result_ct_allListener);
        }
        if (temp == 1) {
            if (canxiugai) {
                selectGuang = selectGuangs.get(0);
                et_chaxun_step2_limit.setEnabled(true);/*et_result_ct_limit.addTextChangedListener(result_ct_text_watcher);*/
                et_chaxun_step2_limit.setText(String.valueOf(g_chaxun_Thresvalue[selectGuang]));
            }
        } else {
            et_chaxun_step2_limit.setEnabled(false);/*et_result_ct_limit.removeTextChangedListener(result_ct_text_watcher);*/
            et_chaxun_step2_limit.setText("");
            et_chaxun_step2_limit.clearFocus();
        }
    }

    void updataCtLineChart(LineChart lineChart) {
        final LineChart chartTemp = lineChart;
        leftAxis.removeAllLimitLines();
        Ylow = (float) 20000.0;
        Yhigh = (float) -20000.0;
        for (int j = 0; j < GUANGNUM; j++)/*?????????1234//*/ {
            for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/ {
                LineDataSet set = (LineDataSet) dataSets.get(j * BLOCKSIZE + z);
                if ((!guangList.get(j).isCheck) || !(g_chaxun_pcrProject.project_item_ables[j])||!g_device_guang_ables[j])
                    set.setVisible(false);
                else {
                    set.setVisible(true);
                    Yhigh = (Yhigh > set.getYMax()) ? Yhigh : set.getYMax();
                    Ylow = (Ylow < set.getYMin()) ? Ylow : set.getYMin();
                    if (result_ct_Adapter.getCheckList().contains(g_chaxun_blockno * GUANGNUM * BLOCKSIZE + j * BLOCKSIZE + z + 1)) {
                        set.setLineWidth(3f);
                        set.setColor(Color.BLUE);
                    } else {
                        set.setLineWidth(1f);
                        set.setColor(chartColors[j]);
                    }
                }
            }
            if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j]) {
                if ((g_chaxun_Thresvalue[j] < Ylow) && (Ylow != 20000.0))
                    g_chaxun_Thresvalue[j] = Ylow;
                if ((g_chaxun_Thresvalue[j] > Yhigh) && (Yhigh != -20000.0))
                    g_chaxun_Thresvalue[j] = Yhigh;
                LimitLine lll = new LimitLine((float) g_chaxun_Thresvalue[j], "CH" + String.valueOf(j + 1) + " " + String.valueOf((float) Math.round((g_chaxun_Thresvalue[j]) * 100) / 100));
                lll.setLineColor(Color.BLACK);
                lll.setLineWidth(1f);
                lll.setTextColor(Color.GRAY);
                lll.setTextSize(12f);
                leftAxis.addLimitLine(lll);
            }
        }
        lineData = new LineData(dataSets);
        chartTemp.setData(lineData);
        leftAxis = chartTemp.getAxisLeft();
//        if (Yhigh - Ylow < 0.05) Yhigh = (float) (Ylow + 0.05);
        if (Yhigh < g_ymin)
            Yhigh = (float) g_ymin;
        if(Yhigh<Ylow)
            Ylow=0;
        leftAxis.setAxisMaximum((float) (Yhigh + (Yhigh - Ylow) * 0.1));
        leftAxis.setAxisMinimum((float) (Ylow - (Yhigh - Ylow) * 0.05));
        chartTemp.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        LogUtils.d("ChaXunStep2Fragment.onDestroy()");
    }
}
