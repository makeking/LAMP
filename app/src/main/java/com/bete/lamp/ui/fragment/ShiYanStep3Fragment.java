package com.bete.lamp.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import static com.myutils.GlobalDate.SoftwareCode;
import static com.myutils.GlobalDate.chartColors;
import static com.myutils.GlobalDate.g_AntoThresvalue;
import static com.myutils.GlobalDate.g_Thresvalue;
import static com.myutils.GlobalDate.g_allcounts;
import static com.myutils.GlobalDate.g_anaAlgorithm;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_checkTimeStr;
import static com.myutils.GlobalDate.g_ct;
import static com.myutils.GlobalDate.g_currentStep;
import static com.myutils.GlobalDate.g_device_guang_ables;
import static com.myutils.GlobalDate.g_false_FunAbleToCalcuParamInfo;
import static com.myutils.GlobalDate.g_fenxiguiyidatas;
import static com.myutils.GlobalDate.g_fenxilvbodatas;
import static com.myutils.GlobalDate.g_pcrDataHandleRef;
import static com.myutils.GlobalDate.g_showguiyidatas;
import static com.myutils.GlobalDate.g_indexs_data;
import static com.myutils.GlobalDate.g_kadjustref;
import static com.myutils.GlobalDate.g_lampQcData;
import static com.myutils.GlobalDate.g_lamp_resultitem;
import static com.myutils.GlobalDate.g_showlvbodatas;
import static com.myutils.GlobalDate.g_pcrProjects;
import static com.myutils.GlobalDate.g_qcenable;
import static com.myutils.GlobalDate.g_raredatas;
import static com.myutils.GlobalDate.g_samplenos;
import static com.myutils.GlobalDate.g_sqlsize;
import static com.myutils.GlobalDate.g_step2_guangs;
import static com.myutils.GlobalDate.g_step2_kongs;
import static com.myutils.GlobalDate.g_step3_guangs;
import static com.myutils.GlobalDate.g_ymin;
import static com.myutils.GlobalDate.sendPCRLiuChengProcessValueEvent;
import static com.utils.StorageUtil.isUpanExist;
import static java.lang.Math.abs;

public class ShiYanStep3Fragment extends BaseFragment {
    private static final String TAG = "ShiYanStep3Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;/*//////////////////////////////////////////////////////////////*/
    private Button bt_shiyan_step3_done, bt_shiyan_step3_print, bt_shiyan_step3_export;
    protected View view;
    private TextView item_jieguo;
    private RelativeLayout ll_shiyan_step3_limit;
    private EditText et_shiyan_step3_limit;
    private boolean limitTragEnable = false;/*曲线专用*/
    CheckBoxAdapter checkBoxAdapterGuang;
    LinkedList<CheckBoxItem> guangList = new LinkedList<>();
    CheckBox cb_shiyan_step3_all;
    RecyclerView rv_shiyan_step3_guang;
    int selectGuang;
    LineChart chart_shiyan_step3;
    float Ylow = (float) 20000.0, Yhigh = (float) -20000.0;
    LineData lineData;
    List<ILineDataSet> dataSets = new LinkedList<>();
    YAxis leftAxis;
    XAxis axis;
    RecyclerView rv_shiyan_step3_jieguo;
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
            if ((s.toString() != null) && (!s.toString().isEmpty())) try {
                value = Float.valueOf(s.toString());
                g_Thresvalue[g_currentBlock][selectGuang] = value;
                chart_shiyan_step3.getAxisLeft().removeAllLimitLines();
                LimitLine ll = new LimitLine((float) value, String.valueOf(value));
                ll.setLineColor(Color.BLACK);
                ll.setLineWidth(2f);
                ll.setTextColor(Color.GRAY);
                ll.setTextSize(12f);
                chart_shiyan_step3.getAxisLeft().addLimitLine(ll);
                chart_shiyan_step3.invalidate();
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
                if (guangList.get(i).isShow) {
                    guangList.get(i).setCheck(isChecked);
                    g_step3_guangs[g_currentBlock][i] = isChecked;
                }
            checkBoxAdapterGuang.notifyDataSetChanged();
            updata_result_ct_list();
            updataCtLineChart(chart_shiyan_step3);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("导出实验数据失败！").setTitle("导出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 2) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("导出实验数据成功！").setTitle("导出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 3) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("打印完成！").setTitle("打印").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 4) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("打印机无纸！").setTitle("打印").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 6) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("打印机错误！").setTitle("打印").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 8) {
                result_ct_jieguoList.clear();
                int size = result_ct_jieguoList.size();
                for (int j = 0; j < GUANGNUM; j++)/*光通道1234//*/
                    if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j])
                        for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                            result_ct_jieguoList.add(g_lamp_resultitem[g_currentBlock][j][z]);
                if (size >= result_ct_jieguoList.size())
                    result_ct_Adapter.notifyItemRangeRemoved(result_ct_jieguoList.size(), size - result_ct_jieguoList.size());
                else
                    result_ct_Adapter.notifyItemRangeInserted(size, result_ct_jieguoList.size() - size);
                result_ct_Adapter.notifyItemRangeChanged(layoutManagerRA2.findFirstVisibleItemPosition(), layoutManagerRA2.findLastVisibleItemPosition() - layoutManagerRA2.findFirstVisibleItemPosition() + 1);/*layoutManagerRA2.findFirstVisibleItemPosition()*/
            }
            if (msg.what == 10) {
                g_block_states[g_currentBlock] = GlobalDate.BlockStateType.ready;
                g_indexs_data[g_currentBlock] = 0;
                g_qcenable[g_currentBlock] = false;
                for (int i = 0; i < BLOCKSIZE; i++) {
                    g_samplenos[g_currentBlock][i] = "";
                    g_step2_guangs[g_currentBlock][i] = false;
                    g_step2_kongs[g_currentBlock][i] = false;
                    g_step3_guangs[g_currentBlock][i] = false;
                }
                sendPCRLiuChengProcessValueEvent(g_currentBlock, GlobalDate.EventType.blockStateChange);
                ((MainShiYanFragment) getParentFragment()).changeRadio(0,g_currentBlock);
            }
        }
    };/*/////////////////////////////////////////////////////////////////////////////////////////*/

    public ShiYanStep3Fragment() {/* Required empty public constructor*/}

//    public static ShiYanStep3Fragment newInstance(String param1, String param2) {
//        ShiYanStep3Fragment fragment = new ShiYanStep3Fragment();
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

    private static int g_currentBlock = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {/* Inflate the layout for this fragment*/
        view = inflater.inflate(R.layout.fragment_shi_yan_step3, container, false);
        if (getArguments() != null) {
            int data = getArguments().getInt("param");
            LogUtils.e("param data:"+ data );
                g_currentBlock = data;
        }
        bt_shiyan_step3_done = (Button) view.findViewById(R.id.bt_shiyan_step3_done);
        bt_shiyan_step3_print = (Button) view.findViewById(R.id.bt_shiyan_step3_print);
        bt_shiyan_step3_export = (Button) view.findViewById(R.id.bt_shiyan_step3_export);
        getAntoThresvalue();
        init_result_ct_view(view);
        updata_result_ct_list();
        initCtLineChart(chart_shiyan_step3);
        updataCtLineChart(chart_shiyan_step3);
        if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing)
            item_jieguo.setText("结果");
        else item_jieguo.setText("结果(" + g_pcrProjects[g_currentBlock].project_danwei + ")");
        bt_shiyan_step3_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        LogUtils.d("g_indexs_data[g_currentBlock]:" + g_indexs_data[g_currentBlock] + ",g_qcenable[g_currentBlock]:" + g_qcenable[g_currentBlock] + ",g_samplenos[g_currentBlock]:" + g_samplenos[g_currentBlock]);
                        savedata();
                        LogUtils.d("清除操作");/*                        try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }*/
                        Message msg = new Message();
                        msg.what = 10;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
        bt_shiyan_step3_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrintHandle();
            }
        });
        bt_shiyan_step3_export.setOnClickListener(new View.OnClickListener() {
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
     * 显示进度对话框
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
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
        progressDialog = null;
    }

    public void savedata() {
        Cursor cursor = LitePal.findBySQL("select * from CheckDirItem group by checkdatetime order by checkdatetime asc");/*desc*/
        int size = cursor.getCount();
        LogUtils.d("size:" + size);
        int count = size - g_sqlsize;
        if (count > 0 && (cursor != null) && (cursor.moveToFirst())) {
            LogUtils.d("count:" + count);
            do {
                String name = cursor.getString(cursor.getColumnIndex("samplenumber"));
                String hp = cursor.getString(cursor.getColumnIndex("checkdatetime"));
                String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + cursor.getString(cursor.getColumnIndex("dir"));
                FileCommonUtil.deleteDir(dir);
                LitePal.deleteAll(CheckDirItem.class, "checkdatetime = ?", hp);
            } while (cursor.moveToNext() && (count-- > 0));/* 关闭*/
            cursor.close();
        }
        int indexs_data = g_indexs_data[g_currentBlock];
        boolean qcenable = g_qcenable[g_currentBlock];
        String[] samplenos = new String[BLOCKSIZE];
        for (int i = 0; i < BLOCKSIZE; i++) samplenos[i] = g_samplenos[g_currentBlock][i];
        LogUtils.d("indexs_data:" + indexs_data + ",qcenable:" + qcenable + ",samplenos:" + samplenos);
        List<CheckDirItem> checkDirItemList = LitePal.where("checkdatetime = " + "?", g_checkTimeStr[g_currentBlock]).find(CheckDirItem.class);
        if (checkDirItemList != null) for (int i = 0; i < checkDirItemList.size(); i++) {
            CheckDirItem checkDirItem = checkDirItemList.get(i);
            checkDirItem.delete();
        }
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock] + "/" + "shiyanstatedata";
        LogUtils.d("indexs_data:" + indexs_data + ",qcenable:" + qcenable + ",samplenos:" + samplenos);
        CheckState checkState = new CheckState(SoftwareCode, g_checkTimeStr[g_currentBlock], g_currentBlock, indexs_data, (int) g_allcounts[g_currentBlock], CAIJICOUNT, g_raredatas[g_currentBlock], g_showlvbodatas[g_currentBlock], g_showguiyidatas[g_currentBlock], g_ct[g_currentBlock], qcenable, g_Thresvalue[g_currentBlock], samplenos, g_pcrProjects[g_currentBlock], g_lamp_resultitem[g_currentBlock], g_lampQcData[g_currentBlock]);/*        CheckState checkState = new CheckState(SoftwareCode,g_checkTimeStr[g_currentBlock], g_currentBlock,g_indexs_data[g_currentBlock], (int) g_allcounts[g_currentBlock],// CAIJICOUNT, g_raredatas[g_currentBlock],g_lvbodatas[g_currentBlock], g_guiyidatas[g_currentBlock],g_ct[g_currentBlock], g_qcenable[g_currentBlock],// g_Thresvalue[g_currentBlock], g_samplenos[g_currentBlock],g_pcrProjects[g_currentBlock],g_lamp_resultitem[g_currentBlock],g_lampQcData[g_currentBlock]);*/
        SharedPreferencesUtils.savePCRCheckState(checkState, filepath);
        if (g_qcenable[g_currentBlock]) {
            if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
                String name = (g_samplenos[g_currentBlock][2] != "") ? g_samplenos[g_currentBlock][2] : "unknow";
                CheckDirItem tempsqlstru = new CheckDirItem(name, g_checkTimeStr[g_currentBlock], g_checkTimeStr[g_currentBlock]);
                tempsqlstru.save();
                String name1 = (g_samplenos[g_currentBlock][3] != "") ? g_samplenos[g_currentBlock][3] : "unknow";
                CheckDirItem tempsqlstru1 = new CheckDirItem(name1, g_checkTimeStr[g_currentBlock], g_checkTimeStr[g_currentBlock]);
                tempsqlstru1.save();
            } else {
                String name1 = (g_samplenos[g_currentBlock][3] != "") ? g_samplenos[g_currentBlock][3] : "unknow";
                CheckDirItem tempsqlstru1 = new CheckDirItem(name1, g_checkTimeStr[g_currentBlock], g_checkTimeStr[g_currentBlock]);
                tempsqlstru1.save();
            }
        } else {
            for (int i = 0; i < BLOCKSIZE; i++) {
                String name = (g_samplenos[g_currentBlock][i] != "") ? g_samplenos[g_currentBlock][i] : "unknow";
                CheckDirItem tempsqlstru = new CheckDirItem(name, g_checkTimeStr[g_currentBlock], g_checkTimeStr[g_currentBlock]);
                tempsqlstru.save();
            }
        }
        /*savaDataToDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock], Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock]);*/
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
        }/*///////////////////////////////////结果数据////////////////////////////////////////*/
        csvfilebuff.delete(0, csvfilebuff.length()).append("项目名称," + g_pcrProjects[g_currentBlock].project_name + ", ," + g_pcrProjects[g_currentBlock].project_lot + ", , , ,\r\n");
        if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing)
            csvfilebuff.append("分析方法," + "定性" + ", ," + g_pcrProjects[g_currentBlock].project_lot + ", , , ,\r\n");
        else
            csvfilebuff.append("分析方法," + "定量" + ", ," + g_pcrProjects[g_currentBlock].project_lot + ", , , ,\r\n");
        csvfilebuff.append("扩增程序," + "目标温度(℃)" + ",保持时间(min)," + "检测间隔(S)" + ",是否检测, , ,\r\n");
        for (int i = 0; i < 3; i++)
            if (g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).isRead)
                csvfilebuff.append(String.valueOf(i + 1) + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).targetTemp + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).holdTime + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).intervalTime + ",是, , ,\r\n");
            else
                csvfilebuff.append(String.valueOf(i + 1) + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).targetTemp + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).holdTime + "," + g_pcrProjects[g_currentBlock].pcrLiuChengCanShuItems.get(i).intervalTime + ",否, , ,\r\n");
        csvfilebuff.append("检测时间," + g_checkTimeStr[g_currentBlock] + ", ," + "" + ", , , ,\r\n");
        if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing)
            csvfilebuff.append("样本编号," + "孔位" + ",通道," + "靶标名称" + ",检测值,结果,,\r\n");
        else
            csvfilebuff.append("样本编号," + "孔位" + ",通道," + "靶标名称" + ",检测值,结果," + g_pcrProjects[g_currentBlock].project_danwei + ",\r\n");
        for (int i = 0; i < BLOCKSIZE; i++)
            for (int j = 0; j < GUANGNUM; j++) {
                if (g_pcrProjects[g_currentBlock].project_item_ables[j] && g_device_guang_ables[j]) {
                    if (g_lamp_resultitem[g_currentBlock][j][i].ct == 0x0ffff)
                        csvfilebuff.append(g_lamp_resultitem[g_currentBlock][j][i].bianhao + "," + String.valueOf((char) ('A' + g_currentBlock)) + String.valueOf(i + 1) + "," + "CH" + String.valueOf(j + 1) + "," + g_lamp_resultitem[g_currentBlock][j][i].projectname + "," + "NoTt" + "," + g_lamp_resultitem[g_currentBlock][j][i].jieguo + ", ,\r\n");
                    else
                        csvfilebuff.append(g_lamp_resultitem[g_currentBlock][j][i].bianhao + "," + String.valueOf((char) ('A' + g_currentBlock)) + String.valueOf(i + 1) + "," + "CH" + String.valueOf(j + 1) + "," + g_lamp_resultitem[g_currentBlock][j][i].projectname + "," + g_lamp_resultitem[g_currentBlock][j][i].ct + "," + g_lamp_resultitem[g_currentBlock][j][i].jieguo + ", ,\r\n");
                }
            }
        filepath = desdir + "/" + "结果.csv";
        try {
            FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }/*///////////////////////////////////原始数据////////////////////////////////////////*/
        for (int i = 0; i < GUANGNUM; i++) {
            if (g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length()).append("data,");
                for (int z = 0; z < BLOCKSIZE; z++) csvfilebuff.append(String.valueOf(z + 1) + ",");
                csvfilebuff.append("\r\n");
                if (g_pcrProjects[g_currentBlock].project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_rare.csv";
                else
                    filepath = desdir + "/" + g_pcrProjects[g_currentBlock].project_item_names[i] + "_rare.csv";
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < GUANGNUM; i++) {
            if (g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length());
                if (g_pcrProjects[g_currentBlock].project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_rare.csv";
                else
                    filepath = desdir + "/" + g_pcrProjects[g_currentBlock].project_item_names[i] + "_rare.csv";
                for (int j = 0; j < g_indexs_data[g_currentBlock]; j++) {
                    csvfilebuff.append(String.valueOf(j + 1) + ",");
                    for (int z = 0; z < BLOCKSIZE; z++)
                        csvfilebuff.append(String.valueOf(g_raredatas[g_currentBlock][i][z][j]) + ",");
                    csvfilebuff.append("\r\n");
                }
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }/*///////////////////////////////////归一化数据/////////////////////////////////////////*/
        for (int i = 0; i < GUANGNUM; i++) {
            if (g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length()).append("guiyi,");
                for (int z = 0; z < BLOCKSIZE; z++) csvfilebuff.append(String.valueOf(z + 1) + ",");
                csvfilebuff.append("\r\n");
                if (g_pcrProjects[g_currentBlock].project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_guiyi.csv";
                else
                    filepath = desdir + "/" + g_pcrProjects[g_currentBlock].project_item_names[i] + "_guiyi.csv";
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < GUANGNUM; i++) {
            if (g_device_guang_ables[i]) {
                csvfilebuff.delete(0, csvfilebuff.length());
                if (g_pcrProjects[g_currentBlock].project_item_names[i].equals(""))
                    filepath = desdir + "/" + "CH" + (i + 1) + "_guiyi.csv";
                else
                    filepath = desdir + "/" + g_pcrProjects[g_currentBlock].project_item_names[i] + "_guiyi.csv";
                for (int j = 0; j < g_indexs_data[g_currentBlock]; j++) {
                    csvfilebuff.append(String.valueOf(j + 1) + ",");
                    for (int z = 0; z < BLOCKSIZE; z++)
                        csvfilebuff.append(String.valueOf(g_showguiyidatas[g_currentBlock][i][z][j]) + ",");
                    csvfilebuff.append("\r\n");
                }
                try {
                    FileIOUtils.writeFileFromBytesByStream(filepath, csvfilebuff.toString().getBytes("gb2312"), true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }/*/////////////////////////////////结果数据///////////////////////////////////////*/
    }

    private Thread printThread;

    public void goPrintHandle() {
        if (DEVICE) {
            showProgressDialog("打印中");
            printThread = new Thread() {
                @Override
                public void run() {
                    String printbuff;
                    String dispalytemp = "";
                    int templength;
                    printbuff = "\n\n";
                    for (int i = 0; i < BLOCKSIZE; i++) {
                        if (g_qcenable[g_currentBlock]) {
                            if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
                                if (i <= 1) continue;
                            } else {
                                if (i <= 2) continue;
                            }
                        }
                        String check_project_name = "检测项目:" + g_pcrProjects[g_currentBlock].project_name;
                        String check_project_lot = "批号:" + g_pcrProjects[g_currentBlock].project_lot;
                        templength = getLength(check_project_name);
                        templength += getLength(check_project_lot);
                        dispalytemp = check_project_name;
                        for (int z = 0; z < (48 - templength); z++) dispalytemp += " ";
                        dispalytemp += check_project_lot;
                        printbuff += dispalytemp;
                        printbuff += "\n";
                        String check_project_sampleno = "样本编号:" + g_samplenos[g_currentBlock][i];
                        String check_project_kongwei = "反应孔:" + String.valueOf((char) ('A' + g_currentBlock)) + String.valueOf(i + 1);
                        templength = getLength(check_project_sampleno);
                        templength += getLength(check_project_kongwei);
                        dispalytemp = check_project_sampleno;
                        for (int z = 0; z < (48 - templength); z++) dispalytemp += " ";
                        dispalytemp += check_project_kongwei;
                        printbuff += dispalytemp;
                        printbuff += "\n";
                        printbuff += "检测时间:" + g_checkTimeStr[g_currentBlock] + "\n";
                        printbuff += "检测结果:" + "\n";
                        for (int j = 0; j < GUANGNUM; j++) {
                            if (g_pcrProjects[g_currentBlock].project_item_ables[j]) {
                                if (g_qcenable[g_currentBlock]) {
                                    if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
                                        if (i > 1) {
                                            if (g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("阳性")) {
                                                if (g_lamp_resultitem[g_currentBlock][j][i].ct == 0x0ffff) {
                                                    printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "," + "NoTt" + "\n";
                                                } else {
                                                    printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "," + String.format("%.2f", g_lamp_resultitem[g_currentBlock][j][i].ct) + "\n";
                                                }
                                            } else
                                                printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "\n";
                                        }
                                    } else {
                                        if (i > 2) {
                                            if ((g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("阳性")) && (g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("无效")))
                                                printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "\n";
                                            else
                                                printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + g_pcrProjects[g_currentBlock].project_danwei + "\n";
                                        }
                                    }
                                } else {
                                    if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
                                        if (g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("阳性")) {
                                            if (g_lamp_resultitem[g_currentBlock][j][i].ct == 0x0ffff) {
                                                printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "," + "NoTt" + "\n";
                                            } else {
                                                printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "," + String.format("%.2f", g_lamp_resultitem[g_currentBlock][j][i].ct) + "\n";
                                            }
                                        } else
                                            printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "\n";
                                    } else {
                                        if ((g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("阳性")) && (g_lamp_resultitem[g_currentBlock][j][i].jieguo.equals("无效")))
                                            printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + "\n";
                                        else
                                            printbuff += g_lamp_resultitem[g_currentBlock][j][i].projectname + ":" + g_lamp_resultitem[g_currentBlock][j][i].jieguo + g_pcrProjects[g_currentBlock].project_danwei + "\n";
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
            }

            ;
            printThread.start();
        } else {
            final CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity()).setMessage("非真机不支持打印！").setTitle("打印").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        if (!isUpanExist(getActivity())) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()).setMessage("未检测到设备U盘！").setTitle("U盘不存在").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            showProgressDialog("拷贝中");
            savedata();
            savaDataToDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock], Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock]);
            copyThread = new Thread() {
                @Override
                public void run() {
                    File srcDir = FileCommonUtil.getFileByPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock]);
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
                                delNoUseFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + g_checkTimeStr[g_currentBlock]);
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
            // todo 重新获取值
            if (getArguments() != null) {
                int data = getArguments().getInt("param");
                LogUtils.e("param data:"+ data );
                    g_currentBlock = data;
            }
            char s = '1';
            guangList.clear();
            for (int i = 0; i < GUANGNUM; i++) {
                guangList.add(new CheckBoxItem(g_pcrProjects[g_currentBlock].project_item_names[i], g_step3_guangs[g_currentBlock][i], g_pcrProjects[g_currentBlock].project_item_ables[i]));/*                guangList.add(new CheckBoxItem("CH" + String.valueOf((char) (s + i)), g_step3_guangs[g_currentBlock][i], g_pcrProjects[g_currentBlock].project_item_ables[i])); guangList.add(new CheckBoxItem(g_pcrProjects[g_currentBlock] .project_item_names[i], false, g_pcrProjects[g_currentBlock] .project_item_ables[i]));*/
            }
            getAntoThresvalue();
            checkBoxAdapterGuang.notifyDataSetChanged();
            updata_result_ct_list();
            initCtLineChart(chart_shiyan_step3);
            updataCtLineChart(chart_shiyan_step3);
            if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing)
                item_jieguo.setText("结果");
            else
                item_jieguo.setText("结果(" + g_pcrProjects[g_currentBlock].project_danwei + ")");
            savedata();/* cb_shiyan_step3_all.setChecked(true);*/
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
        chart_shiyan_step3 = (LineChart) view.findViewById(R.id.chart_chaxun_step3);
        cb_shiyan_step3_all = (CheckBox) view.findViewById(R.id.cb_chaxun_step3_all);
        rv_shiyan_step3_guang = (RecyclerView) view.findViewById(R.id.rv_chaxun_step3_guang);
        ll_shiyan_step3_limit = (RelativeLayout) view.findViewById(R.id.ll_chaxun_step3_limit);
        et_shiyan_step3_limit = (EditText) view.findViewById(R.id.et_chaxun_step3_limit);
        item_jieguo = (TextView) view.findViewById(R.id.item_jieguo);
        rv_shiyan_step3_jieguo = (RecyclerView) view.findViewById(R.id.rv_shiyan_step3_jieguo);
        rv_shiyan_step3_jieguo.setHasFixedSize(true);
        rv_shiyan_step3_jieguo.setItemViewCacheSize(0);
        rv_shiyan_step3_jieguo.setDrawingCacheEnabled(true);
        rv_shiyan_step3_jieguo.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        char s = '1';
        guangList.clear();
        for (int i = 0; i < GUANGNUM; i++) {/*guangList.add(new CheckBoxItem("CH" + String.valueOf((char) (s + i)), true, g_pcrProjects[g_currentBlock].project_item_ables[i]));*/
            guangList.add(new CheckBoxItem(g_pcrProjects[g_currentBlock].project_item_names[i], true, g_pcrProjects[g_currentBlock].project_item_ables[i] && g_device_guang_ables[i]));
        }
        checkBoxAdapterGuang = new CheckBoxAdapter(guangList, R.layout.item_checkbox);
        LinearLayoutManager layoutManagerRA = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_shiyan_step3_guang.setLayoutManager(layoutManagerRA);
        layoutManagerRA.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shiyan_step3_guang.setAdapter(checkBoxAdapterGuang);
        checkBoxAdapterGuang.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                checkBoxAdapterGuang.notifyDataSetChanged();
                if (!checkBoxAdapterGuang.isAllSelect()) {
                    cb_shiyan_step3_all.setOnCheckedChangeListener(null);
                    cb_shiyan_step3_all.setChecked(false);
                    cb_shiyan_step3_all.setOnCheckedChangeListener(result_ct_allListener);
                }
                updata_result_ct_list();
                updataCtLineChart(chart_shiyan_step3);
                for (int i = 0; i < GUANGNUM; i++)
                    g_step3_guangs[g_currentBlock][i] = guangList.get(i).isCheck;
            }
        });
        cb_shiyan_step3_all.setOnCheckedChangeListener(result_ct_allListener);
        et_shiyan_step3_limit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et_shiyan_step3_limit.addTextChangedListener(result_ct_text_watcher);
                else
                    et_shiyan_step3_limit.removeTextChangedListener(result_ct_text_watcher);
            }
        });
        et_shiyan_step3_limit.setFilters(new PointLengthFilter[]{new PointLengthFilter(2)});
        result_ct_jieguoList.clear();
        result_ct_Adapter = new LampResultItemAdapter(result_ct_jieguoList);
        layoutManagerRA2 = new LinearLayoutManager(getActivity());
        rv_shiyan_step3_jieguo.setLayoutManager(layoutManagerRA2);
        layoutManagerRA2.setOrientation(LinearLayoutManager.VERTICAL);
        result_ct_Adapter.setOnItemClickListener(new LampResultItemAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                updataCtLineChart(chart_shiyan_step3);
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
        rv_shiyan_step3_jieguo.setAdapter(result_ct_Adapter);
    }

    public void getAntoThresvalue() {
        double dThresValue = 0;
        double[] dTemp = new double[1];
        dTemp[0] = g_indexs_data[g_currentBlock];
        double[] dFluVal = new double[1];
        dFluVal[0] = 0;
        double[] dPara = new double[2];
        dPara[0] = 0;
        dPara[1] = 0;
        double[] px = new double[g_indexs_data[g_currentBlock]];
        for (int i = 0; i < g_indexs_data[g_currentBlock]; i++) px[i] = i + 1;
        for (int i = 0; i < GUANGNUM; i++) {
            double count = 0, sum = 0;
            for (int j = 0; j < BLOCKSIZE; j++) {
                if (!g_anaAlgorithm.AbleToCalculate(px, g_fenxilvbodatas[g_currentBlock][i][j], g_indexs_data[g_currentBlock], g_false_FunAbleToCalcuParamInfo, 5))
                    g_ct[g_currentBlock][i][j] = 0x0ffff;
                else {
                    g_anaAlgorithm.GenStdCurveBySndDerivative(1, g_indexs_data[g_currentBlock], px, g_fenxiguiyidatas[g_currentBlock][i][j], dTemp, dPara, false, 4);
                    dThresValue = dTemp[0];
                    double min = 100000, max = -1000;
                    for (int k = 0; k < g_indexs_data[g_currentBlock]; k++) {
                        min = min < px[k] ? min : px[k];
                        max = max > px[k] ? max : px[k];
                    }
                    if (dThresValue <= max && dThresValue >= min) {
                        g_anaAlgorithm.GenStdCurveBySelFluThres(1, g_indexs_data[g_currentBlock], g_fenxiguiyidatas[g_currentBlock][i][j], px, dThresValue, dTemp, dPara, false);
//                        if (dTemp[0] > max || dTemp[0] < 1) dTemp[0] = 0;
                    } else dTemp[0] = 0;
                    if (dTemp[0] != 0) {
                        sum += dTemp[0];
                        count++;
                    }
                }
            }
            g_AntoThresvalue[g_currentBlock][i] = (double) Math.round(sum / count * 100) / 100;
            if (g_pcrDataHandleRef.g_jieguo_usedata_type == 0)//guiyi
            {
                if (g_pcrDataHandleRef.g_guiyiself_thresvalues[i])//自动
                {
                    g_Thresvalue[g_currentBlock][i] = (float) g_AntoThresvalue[g_currentBlock][i];
                } else {
                    g_Thresvalue[g_currentBlock][i] = (float) g_pcrDataHandleRef.g_guiyithresvalues[i];
                }
            } else//Rn
            {
                if (g_pcrDataHandleRef.g_nself_thresvalues[i])//自动
                {
                    g_Thresvalue[g_currentBlock][i] = (float) g_AntoThresvalue[g_currentBlock][i];
                } else {
                    g_Thresvalue[g_currentBlock][i] = (float) g_pcrDataHandleRef.g_nthresvalues[i];
                }
            }
        }
    }

    public void handledata() {
        double dThresValue = 0;
        double[] dTemp = new double[1];
        dTemp[0] = g_indexs_data[g_currentBlock];
        double[] dFluVal = new double[1];
        dFluVal[0] = 0;
        double[] dPara = new double[2];
        dPara[0] = 0;
        dPara[1] = 0;
        double[] px = new double[g_indexs_data[g_currentBlock]];
        double ct_tt = 0;
        for (int i = 0; i < g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().size(); i++) {
            if (g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).isRead) {
                ct_tt = g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).intervalTime / 60.0;
            }
        }
        for (int i = 0; i < g_indexs_data[g_currentBlock]; i++) px[i] = i + 1;
        for (int i = 0; i < GUANGNUM; i++)
            for (int j = 0; j < BLOCKSIZE; j++) {
                if (g_pcrDataHandleRef.g_ct_suanfa_type == 0)//二阶导
                {
                    if (!g_anaAlgorithm.AbleToCalculate(px, g_fenxilvbodatas[g_currentBlock][i][j], g_indexs_data[g_currentBlock], g_false_FunAbleToCalcuParamInfo, 5)) {
                        g_ct[g_currentBlock][i][j] = 0x0ffff;
                    } else {
                        g_anaAlgorithm.GenStdCurveBySndDerivative(1, g_indexs_data[g_currentBlock], px, g_fenxiguiyidatas[g_currentBlock][i][j], dTemp, dPara, false, 4);
                        g_ct[g_currentBlock][i][j] = (double) Math.round(dTemp[0] * 100 * ct_tt) / 100;
                    }
                } else {
                    if (!g_anaAlgorithm.AbleToCalculate(px, g_fenxilvbodatas[g_currentBlock][i][j], g_indexs_data[g_currentBlock], g_false_FunAbleToCalcuParamInfo, 5)) {
                        g_ct[g_currentBlock][i][j] = 0x0ffff;
                    } else {
                        dThresValue = g_Thresvalue[g_currentBlock][i];
                        double min = 100000, max = -1000;
                        for (int k = 0; k < g_indexs_data[g_currentBlock]; k++) {
                            min = min < g_fenxiguiyidatas[g_currentBlock][i][j][k] ? min : g_fenxiguiyidatas[g_currentBlock][i][j][k];
                            max = max > g_fenxiguiyidatas[g_currentBlock][i][j][k] ? max : g_fenxiguiyidatas[g_currentBlock][i][j][k];
                        }
                        if (dThresValue <= max && dThresValue >= min) {
                            g_anaAlgorithm.GenStdCurveBySelFluThres(1, g_indexs_data[g_currentBlock], px, g_fenxiguiyidatas[g_currentBlock][i][j], dThresValue, dTemp, dPara, false);
                            if (dTemp[0] > g_indexs_data[g_currentBlock] || dTemp[0] < 1) {
                                g_ct[g_currentBlock][i][j] = 0x0ffff;
                            } else
                                g_ct[g_currentBlock][i][j] = (double) Math.round(dTemp[0] * 100 * ct_tt) / 100;
                        } else {
                            g_ct[g_currentBlock][i][j] = 0x0ffff;
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
        if (g_pcrProjects[g_currentBlock].project_type == GlobalDate.ProjectType.dingxing) {
            if (g_qcenable[g_currentBlock]) {
                for (int i = 0; i < GUANGNUM; i++) {
                    if (g_pcrProjects[g_currentBlock].project_item_ables[i]) {
                        if (g_pcrProjects[g_currentBlock].project_neican_tongdao != i + 1) {
                            if (g_ct[g_currentBlock][i][0] > g_pcrProjects[g_currentBlock].project_babiaos[i]) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][0]);
                                LogUtils.d("阳性质控无效");
                            } else {
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(g_ct[g_currentBlock][i][0]), g_ct[g_currentBlock][i][0]);
                                LogUtils.d("阳性质控有效，报Tt值");
                            }

                            if (g_ct[g_currentBlock][i][1] < g_pcrProjects[g_currentBlock].project_ncs[i]) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                                LogUtils.d("阴性质控无效");
                            } else {
                                if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                    if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][1] <= g_pcrProjects[g_currentBlock].project_neican) {
                                        g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                                        LogUtils.d("阴性质控有效");
                                    } else {
                                        qcright = false;
                                        g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                                        LogUtils.d("阴性质控无效");
                                    }
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                                    LogUtils.d("阴性质控有效");
                                }
                            }


                            if (g_ct[g_currentBlock][i][2] < g_pcrProjects[g_currentBlock].project_item_refs[i]) {
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("样本阳性");
                            } else {
                                if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                    if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][2] >= g_pcrProjects[g_currentBlock].project_neican) {
                                        g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][2]);
                                        LogUtils.d("样本无效");
                                    } else {
                                        g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][2]);
                                        LogUtils.d("样本阴性");
                                    }
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][2]);
                                    LogUtils.d("样本阴性");
                                }
                            }
                            if (g_ct[g_currentBlock][i][3] < g_pcrProjects[g_currentBlock].project_item_refs[i]) {
                                g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][3]);
                                LogUtils.d("样本阳性");
                            } else {
                                if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                    if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][3] >= g_pcrProjects[g_currentBlock].project_neican) {
                                        g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][3]);
                                        LogUtils.d("样本无效");
                                    } else {
                                        g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                                        LogUtils.d("样本阴性");
                                    }
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                                    LogUtils.d("样本阴性");
                                }
                            }
                        } else {
                            if (g_ct[g_currentBlock][i][0] > g_pcrProjects[g_currentBlock].project_neican) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][0]);
                                LogUtils.d("阳性质控无效");
                            } else {
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(g_ct[g_currentBlock][i][0]), g_ct[g_currentBlock][i][0]);
                                LogUtils.d("阳性质控有效，报Tt值");
                            }

                            if (g_ct[g_currentBlock][i][1] > g_pcrProjects[g_currentBlock].project_neican) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                                LogUtils.d("阴性质控无效");
                            } else {
                                g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                                LogUtils.d("阴性质控有效");
                            }

                            if (g_ct[g_currentBlock][i][2] < g_pcrProjects[g_currentBlock].project_neican) {
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("样本阳性");
                            } else {
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("样本阴性");
                            }

                            if (g_ct[g_currentBlock][i][3] < g_pcrProjects[g_currentBlock].project_neican) {
                                g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][3]);
                                LogUtils.d("样本阳性");
                            } else {
                                g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                                LogUtils.d("样本阴性");
                            }
                        }
                    }
                }
            } else {//无质控 分两种 一种用之前得质控 一种不用质控 定性是一样的
                for (int i = 0; i < GUANGNUM; i++) {
                    for (int j = 0; j < BLOCKSIZE; j++) {
                        if (g_pcrProjects[g_currentBlock].project_item_ables[i]) {
                            if (g_pcrProjects[g_currentBlock].project_neican_tongdao != i + 1) {
                                if (g_ct[g_currentBlock][i][j] < g_pcrProjects[g_currentBlock].project_item_refs[i]) {
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本阳性");
                                } else {
                                    if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                        if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][j] >= g_pcrProjects[g_currentBlock].project_neican) {
                                            g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][j]);
                                            LogUtils.d("样本无效");
                                        } else {
                                            g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                            LogUtils.d("样本阴性");
                                        }
                                    } else {
                                        g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                        LogUtils.d("样本阴性");
                                    }
                                }
                            } else {
                                if (g_ct[g_currentBlock][i][j] < g_pcrProjects[g_currentBlock].project_neican) {
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本阳性");
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本阴性");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (g_qcenable[g_currentBlock]) {
                for (int i = 0; i < GUANGNUM; i++) {
                    if (g_pcrProjects[g_currentBlock].project_item_ables[i]) {
                        if (g_pcrProjects[g_currentBlock].project_neican_tongdao != i + 1) {
                            if (g_ct[g_currentBlock][i][1] < g_pcrProjects[g_currentBlock].project_ncs[i]) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                                LogUtils.d("阴性质控无效");
                            } else {
                                if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                    if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][1] <= g_pcrProjects[g_currentBlock].project_neican) {
                                        g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                                        LogUtils.d("阴性质控有效");
                                    } else {
                                        qcright = false;
                                        g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                                        LogUtils.d("阴性质控无效");
                                    }
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                                    LogUtils.d("阴性质控有效");
                                }
                            }
                            pcnongdu = g_ct[g_currentBlock][i][0] * g_pcrProjects[g_currentBlock].project_item_ks[i] + g_pcrProjects[g_currentBlock].project_item_bs[i];
                            biaonongdu = g_ct[g_currentBlock][i][2] * g_pcrProjects[g_currentBlock].project_item_ks[i] + g_pcrProjects[g_currentBlock].project_item_bs[i];
                            if ((Math.abs(pcnongdu - g_pcrProjects[g_currentBlock].project_babiaos[i]) <= 0.4) && (Math.abs(biaonongdu - g_pcrProjects[g_currentBlock].project_dingliang_biaozhunnongdu) <= 0.4)) {
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][0]);
                                LogUtils.d("PC质控有效");
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "标品", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("标品质控有效");
                            } else if ((Math.abs(pcnongdu - g_pcrProjects[g_currentBlock].project_babiaos[i]) < 0.4) && (Math.abs(biaonongdu - g_pcrProjects[g_currentBlock].project_dingliang_biaozhunnongdu) > 0.4)) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][0]);
                                LogUtils.d("PC质控有效");
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "标品", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("标品质控无效");
                            } else if ((Math.abs(pcnongdu - g_pcrProjects[g_currentBlock].project_babiaos[i]) > 0.4) && (Math.abs(biaonongdu - g_pcrProjects[g_currentBlock].project_dingliang_biaozhunnongdu) < 0.4)) {
                                qcright = false;
                                g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][0]);
                                LogUtils.d("PC质控无效");
                                g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "标品", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][2]);
                                LogUtils.d("标品质控有效");
                            } else {/*k=﹙y1－y2﹚／﹙x1－x2﹚ b=﹙y2x1－y1x2﹚／﹙x1－x2﹚*/
                                k1 = (g_pcrProjects[g_currentBlock].project_babiaos[i] - g_pcrProjects[g_currentBlock].project_dingliang_biaozhunnongdu) / (g_ct[g_currentBlock][i][0] - g_ct[g_currentBlock][i][2]);
                                b1 = (g_pcrProjects[g_currentBlock].project_dingliang_biaozhunnongdu * g_ct[g_currentBlock][i][0] - g_pcrProjects[g_currentBlock].project_babiaos[i] * g_ct[g_currentBlock][i][2]) / (g_ct[g_currentBlock][i][0] - g_ct[g_currentBlock][i][2]);
                                if (Math.abs(k1 / g_pcrProjects[g_currentBlock].project_item_ks[i] - 1) > g_kadjustref) {
                                    qcright = false;
                                    g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][0]);
                                    LogUtils.d("PC质控无效");
                                    g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "标品", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][2]);
                                    LogUtils.d("标品质控无效");
                                } else {
                                    qcright = false;
                                    g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][0]);
                                    LogUtils.d("PC质控有效");
                                    g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, "标品", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][2]);
                                    LogUtils.d("标品质控有效");
                                    jiaozhun = true;
                                }
                            }
                            if (g_ct[g_currentBlock][i][3] < g_pcrProjects[g_currentBlock].project_item_refs[i]) {
                                if (jiaozhun) {
                                    samplenongdu = g_ct[g_currentBlock][i][3] * k1 + b1;
                                    g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(samplenongdu), g_ct[g_currentBlock][i][3]);
                                    LogUtils.d("样本浓度");
                                } else {
                                    samplenongdu = g_ct[g_currentBlock][i][3] * g_pcrProjects[g_currentBlock].project_item_ks[i] + g_pcrProjects[g_currentBlock].project_item_bs[i];
                                    g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(samplenongdu), g_ct[g_currentBlock][i][3]);
                                    LogUtils.d("样本浓度");
                                }
                            } else {
                                if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                    if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][3] >= g_pcrProjects[g_currentBlock].project_neican) {
                                        g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][3]);
                                        LogUtils.d("样本无效");
                                    } else {
                                        g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                                        LogUtils.d("样本阴性");
                                    }
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                                    LogUtils.d("样本阴性");
                                }
                            }
                        }
                    } else {
                        if (g_ct[g_currentBlock][i][0] > g_pcrProjects[g_currentBlock].project_neican) {
                            qcright = false;
                            g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][0]);
                            LogUtils.d("阳性质控无效");
                        } else {
                            g_lamp_resultitem[g_currentBlock][i][0].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 0 + 1, "阳对", g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(g_ct[g_currentBlock][i][0]), g_ct[g_currentBlock][i][0]);
                            LogUtils.d("阳性质控有效，报Tt值");
                        }

                        if (g_ct[g_currentBlock][i][1] > g_pcrProjects[g_currentBlock].project_neican) {
                            qcright = false;
                            g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][1]);
                            LogUtils.d("阴性质控无效");
                        } else {
                            g_lamp_resultitem[g_currentBlock][i][1].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 1 + 1, "阴对", g_pcrProjects[g_currentBlock].project_item_names[i], "有效", g_ct[g_currentBlock][i][1]);
                            LogUtils.d("阴性质控有效");
                        }

                        if (g_ct[g_currentBlock][i][2] < g_pcrProjects[g_currentBlock].project_neican) {
                            g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][2]);
                            LogUtils.d("样本阳性");
                        } else {
                            g_lamp_resultitem[g_currentBlock][i][2].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 2 + 1, g_samplenos[g_currentBlock][2], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][2]);
                            LogUtils.d("样本阴性");
                        }

                        if (g_ct[g_currentBlock][i][3] < g_pcrProjects[g_currentBlock].project_neican) {
                            g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][3]);
                            LogUtils.d("样本阳性");
                        } else {
                            g_lamp_resultitem[g_currentBlock][i][3].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + 3 + 1, g_samplenos[g_currentBlock][3], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][3]);
                            LogUtils.d("样本阴性");
                        }
                    }
                }
            } else {//无质控 分两种 一种用之前得质控 一种不用质控 定量是一样的
                for (int i = 0; i < GUANGNUM; i++) {
                    for (int j = 0; j < BLOCKSIZE; j++) {
                        if (g_pcrProjects[g_currentBlock].project_item_ables[i]) {
                            if (g_pcrProjects[g_currentBlock].project_neican_tongdao != i + 1) {
                                if (g_ct[g_currentBlock][i][j] < g_pcrProjects[g_currentBlock].project_item_refs[i]) {
                                    samplenongdu = g_ct[g_currentBlock][i][3] * g_pcrProjects[g_currentBlock].project_item_ks[i] + g_pcrProjects[g_currentBlock].project_item_bs[i];
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], String.valueOf(samplenongdu), g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本浓度");
                                } else {
                                    if (g_pcrProjects[g_currentBlock].project_neican_tongdao != 0) {/*有内参*/
                                        if (g_ct[g_currentBlock][g_pcrProjects[g_currentBlock].project_neican_tongdao - 1][j] >= g_pcrProjects[g_currentBlock].project_neican) {
                                            g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "无效", g_ct[g_currentBlock][i][j]);
                                            LogUtils.d("样本无效");
                                        } else {
                                            g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                            LogUtils.d("样本阴性");
                                        }
                                    } else {
                                        g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                        LogUtils.d("样本阴性");
                                    }
                                }
                            } else {
                                if (g_ct[g_currentBlock][i][j] < g_pcrProjects[g_currentBlock].project_neican) {
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阳性", g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本阳性");
                                } else {
                                    g_lamp_resultitem[g_currentBlock][i][j].setLampResultItem(g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_currentBlock * GUANGNUM * BLOCKSIZE + i * BLOCKSIZE + j + 1, g_samplenos[g_currentBlock][j], g_pcrProjects[g_currentBlock].project_item_names[i], "阴性", g_ct[g_currentBlock][i][j]);
                                    LogUtils.d("样本阴性");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (g_qcenable[g_currentBlock]) {
            if (qcright) {
                g_lampQcData[g_currentBlock] = new LampQcData(g_checkTimeStr[g_currentBlock], CAIJICOUNT, g_indexs_data[g_currentBlock], g_raredatas[g_currentBlock],
                        g_showlvbodatas[g_currentBlock], g_showguiyidatas[g_currentBlock], g_Thresvalue[g_currentBlock], g_ct[g_currentBlock], g_kadjustref);
                SharedPreferencesUtils.saveLampQcData(g_lampQcData[g_currentBlock], Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_pcrProjects[g_currentBlock].project_filename);
            } else {
                FileCommonUtil.deleteFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_pcrProjects[g_currentBlock].project_filename);
            }

        } else {
            g_lampQcData[g_currentBlock] = SharedPreferencesUtils.getLampQcData(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_QC_DIR + g_pcrProjects[g_currentBlock].project_filename);
        }
    }

    private void updata_result_ct_list() {
        handledata();
        result_ct_jieguoList.clear();
        for (int j = 0; j < GUANGNUM; j++)/*光通道1234//*/
            if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j])
                for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                    result_ct_jieguoList.add(g_lamp_resultitem[g_currentBlock][j][z]);
        result_ct_Adapter.notifyDataSetChanged();
        updataLimitEditAndAll();
    }

    private void updata_result_ct_list_only() {
        handledata();
        result_ct_jieguoList.clear();
        for (int j = 0; j < GUANGNUM; j++)/*光通道1234//*/
            if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j])
                for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/
                    result_ct_jieguoList.add(g_lamp_resultitem[g_currentBlock][j][z]);
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
        double ct_tt = 0;
        for (int i = 0; i < g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().size(); i++) {
            if (g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).isRead) {
                ct_tt = g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).intervalTime / 60.0;
            }
        }
        for (int j = 0; j < GUANGNUM; j++)/*光通道1234//*/
            for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/ {
                ArrayList<Entry> values = new ArrayList<>();
                if (g_indexs_data[g_currentBlock] != 0) {
                    for (int a = 0; a < g_indexs_data[g_currentBlock]; a++) {
                        temp = (float) g_showguiyidatas[g_currentBlock][j][z][a];
                        values.add(new Entry((float) ((a + 1) * ct_tt), temp));
                        if (Ylow > temp) Ylow = temp;
                        if (Yhigh < temp) Yhigh = temp;
                    }
                } else {
                    values.add(new Entry((float) (0), 0));
                }
                LineDataSet set = new LineDataSet(values, String.valueOf(j * BLOCKSIZE + z) + "_" + String.valueOf(j + 1));/*DataSet 1*/
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                if (result_ct_Adapter.getCheckList().contains(g_currentBlock * GUANGNUM * BLOCKSIZE + j * BLOCKSIZE + z + 1)) {
                    set.setLineWidth(2f);
                    set.setColor(Color.BLUE);
                } else {
                    set.setLineWidth(1f);
                    set.setColor(chartColors[j]);
                }
                set.setDrawCircles(false);
                set.setDrawValues(false);  /*不显示数值*/
                set.setHighLightColor(Color.BLACK);/*设置点击交点后显示交高亮线的颜色*/
                set.setHighlightEnabled(true);/*是否使用点击高亮线*/
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

        if (g_allcounts[g_currentBlock] == 0) {
            axis.setAxisMaximum(1);
            axis.setLabelCount((int) (1 + 4) / 5 + 1);
        } else {
            axis.setAxisMaximum((float) (g_allcounts[g_currentBlock] * ct_tt));
            axis.setLabelCount((int) (g_allcounts[g_currentBlock] + 4) / 5 + 1);
        }
        axis.setAxisMinimum(0);
        leftAxis = chartTemp.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(true);
//        if (Yhigh - Ylow < 0.05) Yhigh = (float) (Ylow + 0.05);
        if (Yhigh < g_ymin)
            Yhigh = (float) g_ymin;
        if (Yhigh < Ylow)
            Ylow = 0;
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
        chartTemp.setHighlightPerDragEnabled(false);/*能否拖拽高*/
        chartTemp.setDragDecelerationEnabled(false);/*拖拽滚动*/
        if (g_allcounts[g_currentBlock] == 0) {
            chartTemp.setVisibleXRange(1, 1);
//            chartTemp.setVisibleXRangeMaximum(1);
        } else {
            chartTemp.setVisibleXRange((float) (g_allcounts[g_currentBlock] * ct_tt + 1), (float) (g_allcounts[g_currentBlock] * ct_tt + 1));
//            chartTemp.setVisibleXRangeMaximum(g_allcounts[g_currentBlock]);
        }
        chartTemp.setDrawBorders(false);     /*设置图表内格子外的边框是否显示*/
        MyMarkerView1 mv = new MyMarkerView1(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(chartTemp);
        chartTemp.setMarker(mv);
        final float finalYhigh = Yhigh;
        final float finalYlow = Ylow;
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
                    g_Thresvalue[g_currentBlock][selectGuang] = temp;/*et_shiyan_step3_limit.removeTextChangedListener (result_ct_text_watcher);*/
                    et_shiyan_step3_limit.setText(String.valueOf(temp));
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
                            g_Thresvalue[g_currentBlock][selectGuang] = temp;/*et_shiyan_step3_limit.removeTextChangedListener (result_ct_text_watcher);*/
                            et_shiyan_step3_limit.setText(String.valueOf(temp));
                            updata_result_ct_list_only();/*et_shiyan_step3_limit.addTextChangedListener (result_ct_text_watcher);*/
                        }
                        break;
                }
                return false;
            }
        });
//        chartTemp.setViewPortOffsets(100, 20, 60, 50);
        chartTemp.setViewPortOffsets(50, 20, 30, 20);
        chartTemp.invalidate();
    }

    void updataLimitEditAndAll() {
        LogUtils.d("updataLimitEditAndAll()");
        LinkedList<Integer> selectGuangs = checkBoxAdapterGuang.getCheckedList();
        int temp = selectGuangs.size();
        if (temp == checkBoxAdapterGuang.getShowItemCount()) {
            cb_shiyan_step3_all.setOnCheckedChangeListener(null);
            cb_shiyan_step3_all.setChecked(true);
            cb_shiyan_step3_all.setOnCheckedChangeListener(result_ct_allListener);
        } else {
            cb_shiyan_step3_all.setOnCheckedChangeListener(null);
            cb_shiyan_step3_all.setChecked(false);
            cb_shiyan_step3_all.setOnCheckedChangeListener(result_ct_allListener);
        }
        if (temp == 1) {
            selectGuang = selectGuangs.get(0);
            et_shiyan_step3_limit.setEnabled(true);/*et_result_ct_limit.addTextChangedListener(result_ct_text_watcher);*/
            et_shiyan_step3_limit.setText(String.valueOf(g_Thresvalue[g_currentBlock][selectGuang]));
        } else {
            et_shiyan_step3_limit.setEnabled(false);/*et_result_ct_limit.removeTextChangedListener (result_ct_text_watcher);*/
            et_shiyan_step3_limit.setText("");
            et_shiyan_step3_limit.clearFocus();
        }
    }

    void updataCtLineChart(LineChart lineChart) {
        final LineChart chartTemp = lineChart;
        leftAxis.removeAllLimitLines();
        Ylow = (float) 20000.0;
        Yhigh = (float) -20000.0;
        for (int j = 0; j < GUANGNUM; j++)/*光通道1234//*/ {
            for (int z = 0; z < BLOCKSIZE; z++)/*sample 1234*/ {
                LineDataSet set = (LineDataSet) dataSets.get(j * BLOCKSIZE + z);
                if ((!guangList.get(j).isCheck) || !(g_pcrProjects[g_currentBlock].project_item_ables[j]) || !g_device_guang_ables[j])
                    set.setVisible(false);
                else {
                    set.setVisible(true);
                    Yhigh = (Yhigh > set.getYMax()) ? Yhigh : set.getYMax();
                    Ylow = (Ylow < set.getYMin()) ? Ylow : set.getYMin();
                    if (result_ct_Adapter.getCheckList().contains(g_currentBlock * GUANGNUM * BLOCKSIZE + j * BLOCKSIZE + z + 1)) {
                        set.setLineWidth(3f);
                        set.setColor(Color.BLUE);
                    } else {
                        set.setLineWidth(1f);
                        set.setColor(chartColors[j]);
                    }
                }
            }
            if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j]) {
                if ((g_Thresvalue[g_currentBlock][j] < Ylow) && (Ylow != 20000.0))
                    g_Thresvalue[g_currentBlock][j] = Ylow;
                if ((g_Thresvalue[g_currentBlock][j] > Yhigh) && (Yhigh != -20000.0))
                    g_Thresvalue[g_currentBlock][j] = Yhigh;
                LimitLine lll = new LimitLine((float) g_Thresvalue[g_currentBlock][j], "CH" + String.valueOf(j + 1) + " " + String.valueOf((float) Math.round((g_Thresvalue[g_currentBlock][j]) * 100) / 100));
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
        if (Yhigh < Ylow)
            Ylow = 0;

        leftAxis.setAxisMaximum((float) (Yhigh + (Yhigh - Ylow) * 0.1));
        leftAxis.setAxisMinimum((float) (Ylow - (Yhigh - Ylow) * 0.05));
        chartTemp.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        LogUtils.d("ShiYanStep3Fragment.onDestroy()");
    }

}
