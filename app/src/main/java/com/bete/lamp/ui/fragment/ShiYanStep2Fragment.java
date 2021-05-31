package com.bete.lamp.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.CheckBoxAdapter;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.bean.CheckBoxItem;
import com.bete.lamp.message.PCRLiuChengProcessValueEvent;
import com.bete.lamp.ui.normal.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.GUANGNUM;
import static com.myutils.GlobalDate.chartColors;
import static com.myutils.GlobalDate.g_allcounts;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_currentStep;
import static com.myutils.GlobalDate.g_device_guang_ables;
import static com.myutils.GlobalDate.g_indexs_data;
import static com.myutils.GlobalDate.g_pcrProjects;
import static com.myutils.GlobalDate.g_step2_guangs;
import static com.myutils.GlobalDate.g_step2_kongs;
import static com.myutils.GlobalDate.g_step2_show_datatype;
import static com.myutils.GlobalDate.g_ymin;
import static com.myutils.GlobalDate.sendPCRLiuChengProcessValueEvent;
import static java.lang.Math.abs;

public class ShiYanStep2Fragment extends BaseFragment {
    private static final String TAG = "ShiYanStep2Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////////////////
    private Button bt_shiyan_step2_fenxi, bt_shiyan_step2_stop;
    protected View view;
    CheckBoxAdapter checkBoxAdapterKong;
    CheckBoxAdapter checkBoxAdapterGuang;
    LinkedList<CheckBoxItem> kongList = new LinkedList<>();
    LinkedList<CheckBoxItem> guangList = new LinkedList<>();
    RecyclerView rv_shiyan_step2_kong;
    RecyclerView rv_shiyan_step2_guang;
    TextView tv_shiyan_step2_projectname;
    LineChart chart;

    public ShiYanStep2Fragment() {
        // Required empty public constructor
    }

//    public static ShiYanStep2Fragment newInstance(String param1, String param2) {
//        ShiYanStep2Fragment fragment = new ShiYanStep2Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shi_yan_step2, container, false);
        if (getArguments() != null) {
            int data = getArguments().getInt("param");
            LogUtils.e("param data:" + data);
            g_currentBlock = data;
        }
        bt_shiyan_step2_fenxi = (Button) view.findViewById(R.id.bt_shiyan_step2_fenxi);
        chart = (LineChart) view.findViewById(R.id.chart_shiyan_step2);
        rv_shiyan_step2_kong = (RecyclerView) view.findViewById(R.id.rv_shiyan_step2_kong);
        rv_shiyan_step2_guang = (RecyclerView) view.findViewById(R.id.rv_shiyan_step2_guang);
        tv_shiyan_step2_projectname = (TextView) view.findViewById(R.id.tv_shiyan_step2_projectname);
        tv_shiyan_step2_projectname.setText(g_pcrProjects[g_currentBlock].project_name);
        updataViewChart();

        char s = '1';
        kongList.clear();
        for (int i = 0; i < BLOCKSIZE; i++) {
            kongList.add(new CheckBoxItem("A" + String.valueOf((char) (s + i)), false, true));
        }
        guangList.clear();
        for (int i = 0; i < GUANGNUM; i++) {
            guangList.add(new CheckBoxItem(g_pcrProjects[g_currentBlock].project_item_names[i], false, g_pcrProjects[g_currentBlock].project_item_ables[i] && g_device_guang_ables[i]));
        }
        checkBoxAdapterKong = new CheckBoxAdapter(kongList, R.layout.item_shiyanstep2_kong);
        checkBoxAdapterGuang = new CheckBoxAdapter(guangList, R.layout.item_shiyanstep2_guang);
        LinearLayoutManager layoutManagerRA = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_shiyan_step2_guang.setLayoutManager(layoutManagerRA);
        layoutManagerRA.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager layoutManagerRA1 = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        rv_shiyan_step2_kong.setLayoutManager(layoutManagerRA1);
        layoutManagerRA1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_shiyan_step2_kong.setAdapter(checkBoxAdapterKong);
        rv_shiyan_step2_guang.setAdapter(checkBoxAdapterGuang);
        checkBoxAdapterGuang.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                checkBoxAdapterGuang.notifyDataSetChanged();
                updataViewChart();
                for (int i = 0; i < GUANGNUM; i++)
                    g_step2_guangs[g_currentBlock][i] = guangList.get(i).isCheck;
            }
        });
        checkBoxAdapterKong.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                checkBoxAdapterKong.notifyDataSetChanged();
                updataViewChart();
                for (int i = 0; i < BLOCKSIZE; i++)
                    g_step2_kongs[g_currentBlock][i] = kongList.get(i).isCheck;
            }
        });

        bt_shiyan_step2_fenxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_block_states[g_currentBlock] = GlobalDate.BlockStateType.done;
                sendPCRLiuChengProcessValueEvent(g_currentBlock, GlobalDate.EventType.blockStateChange);
                //((MainShiYanFragment)getParentFragment()).changeRadio(2);
            }
        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d("onHiddenChanged show");
            // todo 重新获取值
            if (getArguments() != null) {
                int data = getArguments().getInt("param");
                LogUtils.e("param data:" + data);
                g_currentBlock = data;
            }
//            if (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done) {
//                ((MainShiYanFragment) getParentFragment()).changeRadio(2);
//            } else {
            kongList.clear();
            for (int i = 0; i < BLOCKSIZE; i++) {
                kongList.add(new CheckBoxItem(String.valueOf((char) ('A' + g_currentBlock)) + String.valueOf((char) ('1' + i)), g_step2_kongs[g_currentBlock][i], true));
            }
            guangList.clear();
            for (int i = 0; i < GUANGNUM; i++) {
                guangList.add(new CheckBoxItem(g_pcrProjects[g_currentBlock].project_item_names[i], g_step2_guangs[g_currentBlock][i], g_pcrProjects[g_currentBlock].project_item_ables[i]));
            }
            checkBoxAdapterKong.notifyDataSetChanged();
            checkBoxAdapterGuang.notifyDataSetChanged();
            tv_shiyan_step2_projectname.setText(g_pcrProjects[g_currentBlock].project_name);
            updataViewChart();
//            }
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    public void getDataFromUI() {

    }

    public void setDataToUI() {

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
        EventBus.getDefault().register(this);
    }

    void updataViewChart() {
        //block数量
        LogUtils.d("updataViewChart");
        LineChart chartTemp = chart;
        float Ylow = (float) 1, Yhigh = (float) 1;
        List<ILineDataSet> dataSets = new LinkedList<>();
        float temp;
        if ((g_step2_show_datatype == 0) && (GlobalDate.g_pcrDataHandleRef.g_jieguo_usedata_type == 0)) {
            Yhigh = (float) 2;
        } else {
            Yhigh = (float) 5000;
        }
        double ct_tt = 0;
        for (int i = 0; i < g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().size(); i++) {
            if (g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).isRead) {
                ct_tt = g_pcrProjects[g_currentBlock].getPcrLiuChengCanShuItems().get(i).intervalTime / 60.0;
            }
        }
        for (int j = 0; j < guangList.size(); j++)//光通道1234//
        {
            if (guangList.get(j).isCheck && guangList.get(j).isShow && g_device_guang_ables[j])
                for (int i = 0; i < BLOCKSIZE; i++)
                    if (kongList.get(i).isCheck) {//
                        ArrayList<Entry> values = new ArrayList<>();
                        //LineDataSet set ;
                        if (g_indexs_data[g_currentBlock] > 0) {
                            for (int a = 0; a < g_indexs_data[g_currentBlock]; a++) {
                                if (g_step2_show_datatype == 1) {
                                    temp = (float) GlobalDate.g_showlvbodatas[g_currentBlock][j][i][a];
                                } else if (g_step2_show_datatype == 2) {
                                    temp = (float) GlobalDate.g_raredatas[g_currentBlock][j][i][a];
                                } else if (g_step2_show_datatype == 3) {
                                    temp = (float) GlobalDate.g_quraoqian_datachart[g_currentBlock][j][i][a];
                                } else// if (GlobalDate.g_pcrDataHandleRef.g_jieguo_usedata_type == 0) {
                                    temp = (float) GlobalDate.g_showguiyidatas[g_currentBlock][j][i][a];
//                                }
//                                if (GlobalDate.g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
//                                    temp = (float) GlobalDate.g_showlvbodatas[g_currentBlock][j][i][a];
//                                else
//                                    temp = (float) GlobalDate.g_showguiyidatas[g_currentBlock][j][i][a];
                                values.add(new Entry((float) ((a + 1) * ct_tt), temp));
                                if (Ylow > temp)
                                    Ylow = temp;
                                if (Yhigh < temp)
                                    Yhigh = temp;
                            }
                        } else {
                            values.add(new Entry((float) (0), 0));
                        }
                        LineDataSet set = new LineDataSet(values, String.valueOf(g_currentBlock * BLOCKSIZE + i) + "_" + String.valueOf(j + 1));//DataSet 1
                        set.setMode(LineDataSet.Mode.LINEAR);
                        set.setLineWidth(1f);
                        set.setDrawCircles(false);
                        set.setDrawValues(false);  //不显示数值
                        //set.setColor( Color.rgb(192, 255, 140));
                        //set.setColor( Color.rgb(j*50, j*50, j*50));
                        set.setColor(chartColors[j]);
                        dataSets.add(set);
                    }
        }

        LineData data = new LineData(dataSets);
        //chartTemp.clearValues();
//        chartTemp.fitScreen();
//        chartTemp.invalidate();

        chartTemp.setData(data);
        chartTemp.getDescription().setEnabled(false);
        chartTemp.setDrawGridBackground(false);
//        chartTemp.animateX(100);
        Legend legend = chartTemp.getLegend();
        //legend.setWordWrapEnabled(true);
        legend.setEnabled(false);
        YAxis leftAxis = chartTemp.getAxisLeft();
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                String fmt = String.format("%6.2f", value);
//                while ( fmt.length() < 9 ) {
//                    fmt = " " + fmt;
//                }
//                return fmt;
                if (abs(value) > 100)
                    return String.format(" %.0f", value);
                else if (abs(value) > 10)
                    return String.format(" %.1f", value);
                else
                    return String.format(" %.2f", value);
            }
        });
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(true);
        if (Yhigh < g_ymin)
            Yhigh = (float) g_ymin;
        if (Yhigh < Ylow)
            Ylow = 0;
        leftAxis.setAxisMaximum((float) (Yhigh + (Yhigh - Ylow) * 0.1));
        leftAxis.setAxisMinimum((float) (Ylow - (Yhigh - Ylow) * 0.05));//
        leftAxis.setMinWidth(70);
        chartTemp.getAxisRight().setEnabled(false);

        XAxis axis = chartTemp.getXAxis();
        axis.setDrawLabels(true);
        axis.setDrawLimitLinesBehindData(true);
        axis.setDrawGridLines(false);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        if (g_allcounts[g_currentBlock] == 0) {
            axis.setAxisMaximum(1);
            axis.setLabelCount((int) (1 + 4) / 5 + 1);
        } else {
            axis.setAxisMaximum((float) (g_allcounts[g_currentBlock] * ct_tt));
            axis.setLabelCount((int) (g_allcounts[g_currentBlock] + 4) / 5 + 1);
        }
        axis.setAxisMinimum(0);

        chartTemp.setTouchEnabled(false);
        chartTemp.setExtraBottomOffset(10);
        //chartTemp.setViewPortOffsets(100, 20, 60, 50);
        chartTemp.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PCRLiuChengProcessValueEvent pcrLiuChengProcessValueEvent) {
        if ((pcrLiuChengProcessValueEvent.getType() == GlobalDate.EventType.dataChange) && (pcrLiuChengProcessValueEvent.getIndex() == g_currentBlock)) {
            updataViewChart();
        }
        if ((pcrLiuChengProcessValueEvent.getType() == GlobalDate.EventType.blockStateChange) && (pcrLiuChengProcessValueEvent.getIndex() == g_currentBlock) && (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done)) {
            ((MainShiYanFragment) getParentFragment()).changeRadio(2,g_currentBlock);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("ShiYanStep2Fragment.onDestroy()");
    }

}
