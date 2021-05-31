package com.bete.lamp.thread;

import android.content.Context;
import android.content.res.AssetManager;

import com.anaalgorithm.BaseLineParam;
import com.anaalgorithm.FunAmpNormalizedAnaParamInfo;
import com.anaalgorithm.eFilterType;
import com.bete.lamp.AppApplication;
import com.bete.lamp.bean.BarRareData;
import com.myutils.GlobalDate;
import com.utils.FileIOUtils;
import com.utils.GsonUtils;
import com.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.DeviceEepormTEMP;
import static com.myutils.GlobalDate.GUANGNUM;
import static com.myutils.GlobalDate.LiuChengMaxTemp;
import static com.myutils.GlobalDate.LiuChengMinTemp;
import static com.myutils.GlobalDate.g_anaAlgorithm;
import static com.myutils.GlobalDate.g_block_is_first;
import static com.myutils.GlobalDate.g_checktime;
import static com.myutils.GlobalDate.g_false_FunAbleToCalcuParamInfo;
import static com.myutils.GlobalDate.g_fenxiguiyidatas;
import static com.myutils.GlobalDate.g_fenxilvbodatas;
import static com.myutils.GlobalDate.g_jiaozhun;
import static com.myutils.GlobalDate.g_jiaozhun_avr;
import static com.myutils.GlobalDate.g_pcrDataHandleRef;
import static com.myutils.GlobalDate.g_quraoqian_datachart;
import static com.myutils.GlobalDate.g_raredatas;
import static com.myutils.GlobalDate.g_flag_daowens;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_NormalPulse;
import static com.myutils.GlobalDate.g_NormalSpeed;
import static com.myutils.GlobalDate.g_showguiyidatas;
import static com.myutils.GlobalDate.g_indexs_data;
import static com.myutils.GlobalDate.g_indexs_steps;
import static com.myutils.GlobalDate.g_jianceshuju;
import static com.myutils.GlobalDate.g_showlvbodatas;
import static com.myutils.GlobalDate.g_oldtime;
import static com.myutils.GlobalDate.g_pcrProjects;
import static com.myutils.GlobalDate.g_sleeptime;
import static com.myutils.GlobalDate.g_temp_settings;
import static com.myutils.GlobalDate.g_time_daowens;
import static com.myutils.GlobalDate.g_time_holds;
import static com.myutils.GlobalDate.g_time_jianges;
import static com.myutils.GlobalDate.g_time_readtime_daos;
import static com.myutils.GlobalDate.g_time_shengyus;
import static com.myutils.GlobalDate.sendPCRLiuChengProcessValueEvent;
import static com.myutils.GlobalDate.wenduselffinal;

public class LampLiuChengThread extends Thread {
    private Object mPauseLock;
    public boolean mPaused;
    private boolean mFinished;
    //    private McuSerial mcuSerial;
    public static int LiuChengErr;
    public static int runstate = 0;//0未开始实验，1实验中，2实验正常结束，3非正常结束。
    public static double wenduReadTime = 30;
    public static double wenduCur = 30;
    private Timer timer = new Timer();                    //创建一个定时器对象
    private TimerTask task = new TimerTask()        //创建定时器任务对象，必须实现run方法，在该方法中定义用户任务
    {
        @Override
        public void run() {
            timerUpdate();
            timer.cancel();
        }
    };

    public LampLiuChengThread() {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
//        if (DEVICE)
//            mcuSerial = McuSerial.getInstance();
    }

    public boolean isFinished() {
        return mPaused;
    }

    public void myPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    public void myResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }

    public void myStop() {
        mFinished = true;
    }

    void timerUpdate() {
        LogUtils.d("DeviceEepormTEMP:" + String.valueOf(DeviceEepormTEMP));
    }

//四个标记位，采集线程一直开着（判断标志位）。
// 四个block如何处理，流程 温度 时间 采样时间 步骤 到温 温度设置 到温 数据收集 采样每5秒一次，
// 一个线程（每X秒采集一次数据 判断block是否需要检测（开始，结束），
// 根据到温标志时间累加 判断每个block是否到时（（到时走下一个流程（流程index）更新剩余时间，更新hold时间，累计时间清零，更新到温标志）），是否需要设置，设置温度，
// 每两秒采集温度，最后2秒不采集温度（设置到温标志）；
// 累计时间到达间隔，取数，分别处理每个block数据（提示各个UI更新）。

    public void run() {
        int templength = 0;//采集数据长度
        super.run();
        if (DEVICE) {
            Object[] tempObjects;
            byte[] buff = new byte[700];
            int tempvalue;

            McuSerial mcuSerial = McuSerial.getInstance();
            LogUtils.d("LampLiuCheng thread start");
            g_oldtime = System.currentTimeMillis();
            while (!mFinished) {
                synchronized (mPauseLock) {
                    while (mPaused) {
                        try {
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if ((g_block_states[0] == GlobalDate.BlockStateType.running) || (g_block_states[1] == GlobalDate.BlockStateType.running) || (g_block_states[2] == GlobalDate.BlockStateType.running) || (g_block_states[3] == GlobalDate.BlockStateType.running)) {
                    tempObjects = mcuSerial.cmdLampSetPCRMOTOR(0x15, g_NormalPulse, 1, g_NormalSpeed);
                    if ((int) tempObjects[1] != 0) {
                    }
                    for (int j = 0; j < 4; j++) {
//                        if (j == 3)
//                            tempObjects = mcuSerial.cmdLampReadPCRFeng(j + 2);
//                        else
//                            tempObjects = mcuSerial.cmdLampReadPCRFeng(j + 1);

                        if (j == 0)
                            tempObjects = mcuSerial.cmdLampReadPCRFeng(1);
                        else if (j == 1)
                            tempObjects = mcuSerial.cmdLampReadPCRFeng(3);
                        else if (j == 2)
                            tempObjects = mcuSerial.cmdLampReadPCRFeng(5);
                        else
                            tempObjects = mcuSerial.cmdLampReadPCRFeng(2);
                        if ((int) tempObjects[1] != 0) {
                        }
                        templength = (int) (tempObjects[2]);
                        System.arraycopy((byte[]) (tempObjects[3]), 0, buff, 0, templength);
                        for (int z = 0; z < 16; z++) {
                            tempvalue = (int) ((buff[4 + 2 * z] & 0xFF) | ((buff[5 + 2 * z] & 0xFF) << 8));
                            g_jianceshuju[z / 4][j][z % 4] = tempvalue;
                        }
                    }
                    tempObjects = mcuSerial.cmdLampSetPCRMOTOR(0x13, g_NormalPulse, 0, g_NormalSpeed);
                    if ((int) tempObjects[1] != 0) {
                    }

                    //获取温度，获取时间
                    //for()
                    //判断温度是否到达
                    //if(开始标志)温度到达，设置flag_daotimes标志，设置time_daowen,
                    //for(int i=0;i<BLOCKNUM;i++) {
                    //    if ()
                    //}
                    long temptime = System.currentTimeMillis();
                    tempObjects = mcuSerial.cmdLampGetALLTEMP();
                    if ((int) tempObjects[1] != 0) {
                    }
                    for (int i = 0; i < 4; i++) {
                        if ((g_block_is_first[i] == false) && (g_block_states[i] == GlobalDate.BlockStateType.running)) {
                            if (!g_flag_daowens[i]) {
                                if (((byte) tempObjects[3] & (0x01 << i)) != 0) {
                                    g_flag_daowens[i] = true;
                                    g_time_daowens[i] = temptime;
                                }
                            } else {
                                //获取时间，
                                //for ()
                                //if (到温标志&&开始标志)
                                //判断时间间隔是否到达；获取时间>time_daowen+time_jiange*N;
                                //判断是否检测isread，获取第N组数据
                                if ((temptime - g_time_daowens[i]) <= g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime * 60 * 1000) {
                                    if ((g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).isRead) && (temptime - g_time_daowens[i] > g_time_readtime_daos[i])) {//&& (temptime - g_time_daowens[i] > g_time_jianges[i] * 1000 * g_indexs_data[i])
                                        g_time_readtime_daos[i] = g_time_readtime_daos[i] + g_time_jianges[i] * 1000;
                                        double[] px = new double[g_indexs_data[i] + 1];
                                        for (int x = 0; x < (g_indexs_data[i] + 1); x++)
                                            px[x] = x + 1;
                                        for (int x = 0; x < GUANGNUM; x++) {
                                            for (int y = 0; y < BLOCKSIZE; y++) {
                                                if (g_indexs_data[i] > 5) {
                                                } else if (g_indexs_data[i] == 5) {
                                                    g_jiaozhun_avr[i][x][y] = g_jiaozhun_avr[i][x][y] / 5;
                                                } else if (g_indexs_data[i] >= 1) {
                                                    g_jiaozhun_avr[i][x][y] += g_jianceshuju[i][x][y];
                                                } else if (g_indexs_data[i] == 0) {
                                                    g_jiaozhun_avr[i][x][y] = (float) g_jianceshuju[i][x][y];
                                                }
                                                g_quraoqian_datachart[i][x][y][g_indexs_data[i]] = g_jianceshuju[i][x][y];
                                            }
                                        }
                                        for (int x = 0; x < GUANGNUM; x++) {
                                            for (int y = 0; y < BLOCKSIZE; y++) {
                                                if (g_indexs_data[i] > 5) {
                                                    g_raredatas[i][x][y][g_indexs_data[i]] = g_quraoqian_datachart[i][x][y][g_indexs_data[i]]
                                                            - g_jiaozhun[x][0] * (g_quraoqian_datachart[i][0][y][g_indexs_data[i]] - g_jiaozhun_avr[i][0][y])
                                                            - g_jiaozhun[x][1] * (g_quraoqian_datachart[i][1][y][g_indexs_data[i]] - g_jiaozhun_avr[i][1][y])
                                                            - g_jiaozhun[x][2] * (g_quraoqian_datachart[i][2][y][g_indexs_data[i]] - g_jiaozhun_avr[i][2][y])
                                                            - g_jiaozhun[x][3] * (g_quraoqian_datachart[i][3][y][g_indexs_data[i]] - g_jiaozhun_avr[i][3][y]);
                                                } else if (g_indexs_data[i] == 5) {
                                                    for (int z = 0; z <= 5; z++) {
                                                        g_raredatas[i][x][y][g_indexs_data[i]] = g_quraoqian_datachart[i][x][y][g_indexs_data[i]]
                                                                - g_jiaozhun[x][0] * (g_quraoqian_datachart[i][0][y][z] - g_jiaozhun_avr[i][0][y])
                                                                - g_jiaozhun[x][1] * (g_quraoqian_datachart[i][1][y][z] - g_jiaozhun_avr[i][1][y])
                                                                - g_jiaozhun[x][2] * (g_quraoqian_datachart[i][2][y][z] - g_jiaozhun_avr[i][2][y])
                                                                - g_jiaozhun[x][3] * (g_quraoqian_datachart[i][3][y][z] - g_jiaozhun_avr[i][3][y]);
                                                    }
                                                } else {
                                                    g_raredatas[i][x][y][g_indexs_data[i]] = g_quraoqian_datachart[i][x][y][g_indexs_data[i]];
                                                }
                                                System.arraycopy(g_raredatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                System.arraycopy(g_raredatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                for (int l = 0; l < g_pcrDataHandleRef.g_zhongzhi_lvbo_cnt; l++) {
                                                    g_anaAlgorithm.DigitalFilter(g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], g_indexs_data[i] + 1, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                                                    g_anaAlgorithm.DigitalFilter(g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y], g_indexs_data[i] + 1, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                                                    System.arraycopy(g_fenxiguiyidatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                    System.arraycopy(g_showguiyidatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                }
                                                for (int l = 0; l < g_pcrDataHandleRef.g_huadong_lvbo_cnt; l++) {
                                                    g_anaAlgorithm.KdsptForwardMBackN(g_indexs_data[i] + 1, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//分析
                                                    g_anaAlgorithm.KdsptForwardMBackN_New(g_indexs_data[i] + 1, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//分析
                                                    System.arraycopy(g_fenxiguiyidatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                    System.arraycopy(g_showguiyidatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                }

                                                if (!g_anaAlgorithm.AbleToCalculate(px, g_fenxilvbodatas[i][x][y], g_indexs_data[i] + 1, g_false_FunAbleToCalcuParamInfo, 5)) {
                                                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                                                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                    else
                                                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                } else {
                                                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                                                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                    else
                                                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                }
                                            }
                                        }
                                        g_indexs_data[i]++;
                                        sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.dataChange);
                                        //发送数据采集到信号
                                    }
                                }
                                //判断是否到时
                                //获取时间
                                //for ()
                                //if(huoqushijian-time_daowen>holdtime)到时，step_indexs++;设置temp_settings，holdtime，g_time_jianges。
                                //设置温度，设置flag_daotimes标志，设置time_daowen
                                if (temptime - g_time_daowens[i] > g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime * 60 * 1000) {
                                    g_indexs_steps[i]++;
                                    while (g_indexs_steps[i] < g_pcrProjects[i].pcrLiuChengCanShuItems.size()) {
                                        if (((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp < LiuChengMinTemp) && ((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp > LiuChengMaxTemp) || ((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime == 0)) {
                                            g_indexs_steps[i]++;
                                        } else
                                            break;
                                    }
                                    if (g_indexs_steps[i] >= g_pcrProjects[i].pcrLiuChengCanShuItems.size()) {
                                        //block i 结束。通知ui。
                                        g_block_states[i] = GlobalDate.BlockStateType.done;
                                        mcuSerial.cmdLampSetALLTEMP((byte) (0x1 << i), wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
                                        sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.blockStateChange);
                                    } else {
                                        g_temp_settings[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp;
                                        g_time_holds[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime;
                                        g_time_jianges[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).intervalTime;
                                        g_time_readtime_daos[i] = 0;
                                        g_time_daowens[i] = temptime;
                                        mcuSerial.cmdLampSetALLTEMP((byte) (0x1 << i), g_temp_settings[0] * 100, g_temp_settings[1] * 100, g_temp_settings[2] * 100, g_temp_settings[3] * 100);
                                        g_flag_daowens[i] = false;
                                    }
                                }
                                g_time_shengyus[i] = 0;
                                for (int x = g_indexs_steps[i]; x < g_pcrProjects[i].pcrLiuChengCanShuItems.size(); x++)
                                    g_time_shengyus[i] += g_pcrProjects[i].pcrLiuChengCanShuItems.get(x).holdTime * 60 * 1000;
                                g_time_shengyus[i] = (g_time_shengyus[i] - (temptime - g_time_daowens[i]) > 0) ? g_time_shengyus[i] - (temptime - g_time_daowens[i]) : 0;
                            }
                        } else if ((g_block_is_first[i] == true) && (g_block_states[i] == GlobalDate.BlockStateType.running)) {
                            g_block_is_first[i] = false;
                            g_indexs_steps[i] = 0;
                            while (g_indexs_steps[i] < g_pcrProjects[i].pcrLiuChengCanShuItems.size()) {
                                if (((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp < LiuChengMinTemp) && ((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp > LiuChengMaxTemp) || ((int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime == 0)) {
                                    g_indexs_steps[i]++;
                                } else
                                    break;
                            }
                            if (g_indexs_steps[i] >= g_pcrProjects[i].pcrLiuChengCanShuItems.size()) {
                                //block i 结束。通知ui。
                                g_block_states[i] = GlobalDate.BlockStateType.done;
                                mcuSerial.cmdLampSetALLTEMP((byte) (0x1 << i), wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
                                sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.blockStateChange);
                            } else {
                                g_temp_settings[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp;
                                g_time_holds[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime;
                                g_time_jianges[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).intervalTime;
                                g_time_readtime_daos[i] = 0;
                                g_time_daowens[i] = temptime;
                                mcuSerial.cmdLampSetALLTEMP((byte) (0x1 << i), g_temp_settings[0] * 100, g_temp_settings[1] * 100, g_temp_settings[2] * 100, g_temp_settings[3] * 100);
                                g_flag_daowens[i] = false;
                            }
                            g_time_shengyus[i] = 0;
                            for (int x = g_indexs_steps[i]; x < g_pcrProjects[i].pcrLiuChengCanShuItems.size(); x++)
                                g_time_shengyus[i] += g_pcrProjects[i].pcrLiuChengCanShuItems.get(x).holdTime * 60 * 1000;
                            g_time_shengyus[i] = (g_time_shengyus[i] - (temptime - g_time_daowens[i]) > 0) ? g_time_shengyus[i] - (temptime - g_time_daowens[i]) : 0;
                        }
                        LogUtils.v("g_indexs_data[" + i + "]:" + g_indexs_data[i]);
                        LogUtils.v("g_temp_settings[" + i + "]:" + g_temp_settings[i]);
                        LogUtils.v("g_time_jianges[" + i + "]:" + g_time_jianges[i]);
                        LogUtils.v("g_time_daowens[" + i + "]:" + g_time_daowens[i]);
                        LogUtils.v("g_time_holds[" + i + "]:" + g_time_holds[i]);
                        LogUtils.v("g_indexs_steps[" + i + "]:" + g_indexs_steps[i]);
                        LogUtils.v("g_time_shengyus[" + i + "]:" + g_time_shengyus[i]);
                        sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.timeChange);
                    }
                }
                g_sleeptime = g_checktime + g_oldtime - System.currentTimeMillis();
                LogUtils.v("g_sleeptime:" + g_sleeptime);
                if (g_sleeptime > 0)
                    try {
                        sleep(g_sleeptime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                g_oldtime = g_oldtime + g_checktime;
            }
        } else {
            byte[] buff = new byte[700];
            int tempvalue;
            LogUtils.d("LampLiuCheng thread start");
            for (int z = 0; z < 4; z++) {
                //String csvfilesource = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + "null" + "/" + String.valueOf(z + 1) + "_0.csv";
                //String csvfile=QString("/opt/ps102/data/2019-03-07-15-43-59")+"/"+String.valueOf(z+1)+"_0.csv";
                //LogUtils.d(csvfilesource);
                //String tempString = FileIOUtils.readFile2String(csvfilesource);
                String tempString = getStringFromAssetsFile(AppApplication.getAppContext(), (z + 1) + "_0.csv");
                LogUtils.d(tempString);
                if (tempString != null) {
                    if (!tempString.isEmpty()) {
                        String[] tempOption = tempString.split("\r\n");//每行以\n区分
                        Double value;
                        //readtimes = tempOption.length;
                        LogUtils.d("tempOption.length:" + tempOption.length);
                        for (int i = 0; i <= tempOption.length - 1; i++) {
                            String[] tempbar = tempOption[i].split(",");//一行中的单元格以，区分
                            for (int j = 0; j <= tempbar.length - 1; j++) {
                                String valuetemp = tempbar[j];//一行中的单元格以，区分
                                value = Double.valueOf(valuetemp.trim());
                                g_raredatas[j / 4][z][j % 4][i] = value;
                            }
                        }
                    }
                }
            }
            g_oldtime = System.currentTimeMillis();
            while (!mFinished) {
                synchronized (mPauseLock) {
                    while (mPaused) {
                        try {
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if ((g_block_states[0] == GlobalDate.BlockStateType.running) || (g_block_states[1] == GlobalDate.BlockStateType.running) || (g_block_states[2] == GlobalDate.BlockStateType.running) || (g_block_states[3] == GlobalDate.BlockStateType.running)) {
                    for (int j = 0; j < 4; j++) {
                        for (int z = 0; z < 16; z++) {
                            tempvalue = j * 4 + z;
                            g_jianceshuju[z / 4][j][z % 4] = tempvalue;
                        }
                    }

                    //获取温度，获取时间
                    //for()
                    //判断温度是否到达
                    //if(开始标志)温度到达，设置flag_daotimes标志，设置time_daowen,
                    //for(int i=0;i<BLOCKNUM;i++) {
                    //    if ()
                    //}
                    long temptime = System.currentTimeMillis();
                    for (int i = 0; i < 4; i++) {
                        if (g_block_states[i] == GlobalDate.BlockStateType.running) {
                            if (!g_flag_daowens[i]) {
                                g_flag_daowens[i] = true;
                                g_time_daowens[i] = temptime;
                            } else {
                                //获取时间，
                                //for ()
                                //if (到温标志&&开始标志)
                                //判断时间间隔是否到达；获取时间>time_daowen+time_jiange*N;
                                //判断是否检测isread，获取第N组数据
                                if ((temptime - g_time_daowens[i]) <= g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime * 60 * 1000) {
                                    if ((g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).isRead) && (temptime - g_time_daowens[i] > g_time_readtime_daos[i])) {//&& (temptime - g_time_daowens[i] > g_time_jianges[i] * 1000 * g_indexs_data[i])
                                        g_time_readtime_daos[i] = g_time_readtime_daos[i] + g_time_jianges[i] * 1000;
                                        double[] px = new double[g_indexs_data[i] + 1];
                                        for (int x = 0; x < (g_indexs_data[i] + 1); x++)
                                            px[x] = x + 1;
                                        for (int x = 0; x < GUANGNUM; x++)
                                            for (int y = 0; y < BLOCKSIZE; y++) {
                                                System.arraycopy(g_raredatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                System.arraycopy(g_raredatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                for (int l = 0; l < g_pcrDataHandleRef.g_zhongzhi_lvbo_cnt; l++) {
                                                    g_anaAlgorithm.DigitalFilter(g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], g_indexs_data[i] + 1, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                                                    g_anaAlgorithm.DigitalFilter(g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y], g_indexs_data[i] + 1, eFilterType.FILTERTYPE_MEDIAN, g_pcrDataHandleRef.g_zhongzhi_lvbo_num);
                                                    System.arraycopy(g_fenxiguiyidatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                    System.arraycopy(g_showguiyidatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                }
                                                for (int l = 0; l < g_pcrDataHandleRef.g_huadong_lvbo_cnt; l++) {
                                                    g_anaAlgorithm.KdsptForwardMBackN(g_indexs_data[i] + 1, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//分析
                                                    g_anaAlgorithm.KdsptForwardMBackN_New(g_indexs_data[i] + 1, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y], g_pcrDataHandleRef.g_huadong_lvbo_num / 2, g_pcrDataHandleRef.g_huadong_lvbo_num / 2);//分析
                                                    System.arraycopy(g_fenxiguiyidatas[i][x][y], 0, g_fenxilvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                    System.arraycopy(g_showguiyidatas[i][x][y], 0, g_showlvbodatas[i][x][y], 0, g_indexs_data[i] + 1);
                                                }

                                                if (!g_anaAlgorithm.AbleToCalculate(px, g_fenxilvbodatas[i][x][y], g_indexs_data[i] + 1, g_false_FunAbleToCalcuParamInfo, 5)) {
                                                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                                                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                    else
                                                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(false), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                } else {
                                                    if (g_pcrDataHandleRef.g_jieguo_usedata_type == 1)
                                                        g_anaAlgorithm.DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_nbaseline_starts[x], g_pcrDataHandleRef.g_nbaseline_stops[x], !g_pcrDataHandleRef.g_nself_baselines[x]), g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                    else
                                                        g_anaAlgorithm.NormalizedAnalysisBySndDerivativeOpenBaseLineParam(g_indexs_data[i] + 1, px, g_fenxilvbodatas[i][x][y], g_fenxiguiyidatas[i][x][y], new FunAmpNormalizedAnaParamInfo(true), new BaseLineParam(g_pcrDataHandleRef.g_guiyibaseline_starts[x], g_pcrDataHandleRef.g_guiyibaseline_stops[x], !g_pcrDataHandleRef.g_guiyiself_baselines[x]), 0, g_showlvbodatas[i][x][y], g_showguiyidatas[i][x][y]);
                                                }
                                            }
                                        g_indexs_data[i]++;
                                        sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.dataChange);
                                        //发送数据采集到信号
                                    }
                                }
                                //判断是否到时
                                //获取时间
                                //for ()
                                //if(huoqushijian-time_daowen>holdtime)到时，step_indexs++;设置temp_settings，holdtime，g_time_jianges。
                                //设置温度，设置flag_daotimes标志，设置time_daowen
                                if ((temptime - g_time_daowens[i]) > g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime * 60 * 1000) {
                                    g_indexs_steps[i]++;
                                    if (g_indexs_steps[i] >= g_pcrProjects[i].pcrLiuChengCanShuItems.size()) {
                                        //block i 结束。通知ui。
                                        g_block_states[i] = GlobalDate.BlockStateType.done;
                                        sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.blockStateChange);
                                    } else {
                                        g_temp_settings[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).targetTemp;
                                        g_time_holds[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).holdTime;
                                        g_time_jianges[i] = (int) g_pcrProjects[i].pcrLiuChengCanShuItems.get(g_indexs_steps[i]).intervalTime;
                                        g_time_readtime_daos[i] = 0;
                                        g_time_daowens[i] = temptime;
                                        g_flag_daowens[i] = false;
                                    }
                                }
                                g_time_shengyus[i] = 0;
                                for (int x = g_indexs_steps[i]; x < g_pcrProjects[i].pcrLiuChengCanShuItems.size(); x++)
                                    g_time_shengyus[i] += g_pcrProjects[i].pcrLiuChengCanShuItems.get(x).holdTime * 60 * 1000;
                                g_time_shengyus[i] = (g_time_shengyus[i] - (temptime - g_time_daowens[i]) > 0) ? g_time_shengyus[i] - (temptime - g_time_daowens[i]) : 0;
                            }
                            LogUtils.v("g_indexs_data[" + i + "]:" + g_indexs_data[i]);
                            LogUtils.v("g_temp_settings[" + i + "]:" + g_temp_settings[i]);
                            LogUtils.v("g_time_jianges[" + i + "]:" + g_time_jianges[i]);
                            LogUtils.v("g_time_daowens[" + i + "]:" + g_time_daowens[i]);
                            LogUtils.v("g_time_holds[" + i + "]:" + g_time_holds[i]);
                            LogUtils.v("g_indexs_steps[" + i + "]:" + g_indexs_steps[i]);
                            LogUtils.v("g_time_shengyus[" + i + "]:" + g_time_shengyus[i]);
                            sendPCRLiuChengProcessValueEvent(i, GlobalDate.EventType.timeChange);
                        }
                    }
                }
                g_sleeptime = g_checktime + g_oldtime - System.currentTimeMillis();
                LogUtils.v("g_sleeptime:" + g_sleeptime);
                if (g_sleeptime > 0)
                    try {
                        sleep(g_sleeptime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                g_oldtime = g_oldtime + g_checktime;
            }
        }
    }

    /**
     * 从文件中获取字符串
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getStringFromAssetsFile(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

//        startTimeThread();
//        g_readtimes = 0;
//            tempObjects = mcuSerial.cmdSetMPPC(0, 3000);//设置mppc增益
//            if ((int) tempObjects[1] != 0) {
//                statecode = SHIYAN_SETMPPC_ERR;
//                sendStateStringEvent(statecode, 1);
//                myPause();
//                synchronized (mPauseLock) {
//                    while ( mPaused ) {
//                        try {
//                            mPauseLock.wait();
//                        } catch (InterruptedException e) {
//                        }
//                    }
//                }
//                // 检测是否停止
//                if (mFinished) {
//                    return;
//                }
//            }
//            while ( (pcrLiuChengCanShuItemCur != null) && (!mFinished) ) {
//                synchronized (mPauseLock) {
//                    while ( mPaused ) {
//                        try {
//                            mPauseLock.wait();
//                        } catch (InterruptedException e) {
//                        }
//                    }
//                }
//                gettime();
//                pcrLiuChengCanShuItemCur = handleKongItem(pcrLiuChengCanShuItemCur);
//            }
//            if ((LiuChengErr == 0) && (!mFinished)) {
//                handledata();
//                runstate = 2;
//            } else if (mFinished) {
//                LogUtils.d("人为结束");
//                runstate = 4;//
//            } else {
//                LogUtils.d("通信错误");
//                runstate = 3;//
//            }
//            handledata();
//        }
//        return;
//    }

    //    void sendStateStringEvent(int statecode, int handle) {
//        GlobalDate.g_statecode = statecode;
//        GlobalDate.g_handle = handle;
//        StateStringEvent se = new StateStringEvent(statecode, handle);
//        EventBus.getDefault().post(se);
//    }
//
//
//    PCRLiuChengCanShuItem handleKongItem(PCRLiuChengCanShuItem pcrLiuChengCanShuItem) {
//        PCRLiuChengCanShuItem tempItem = null;
////        int statecode = SHIYAN_READPCRFENG_ERR;
////        sendStateStringEvent(statecode, 1);
//            sendPCRLiuChengProcessValueEvent(g_readtimes);
//        }
//    }

//            tempObjects = mcuSerial.cmdSetPCRTEMP(xxx);
//            if ((int) tempObjects[1] != 0) {
//                int statecode = SHIYAN_SETMPPC_ERR;
//                sendStateStringEvent(statecode, 1);
//                myPause();
//                synchronized (mPauseLock) {
//                    while ( mPaused ) {
//                        try {
//                            mPauseLock.wait();
//                        } catch (InterruptedException e) {
//                        }
//                    }
//                }
//                return tempItem;
//            }
//
//            if (pcrLiuChengCanShuItem.isRead) {
//                for (int i = 0; i < 10; i++) {
//                    tempObjects = mcuSerial.cmdSetPCRMOTOR(0x15, 5000, 1, 25000);
//                    if ((int) tempObjects[1] != 0) {
//                        int statecode = SHIYAN_PCRMOTOR_ERR;
//                        sendStateStringEvent(statecode, 1);
//                        myPause();
//                        synchronized (mPauseLock) {
//                            while ( mPaused ) {
//                                try {
//                                    mPauseLock.wait();
//                                } catch (InterruptedException e) {
//                                }
//                            }
//                        }
//                        return tempItem;
//                    }
//                }
//                    tempObjects = mcuSerial.cmdReadPCRFeng(j + 1, 1);
//                    if ((int) tempObjects[1] != 0) {
//                        int statecode = SHIYAN_READPCRFENG_ERR;
//                        sendStateStringEvent(statecode, 1);
//                        myPause();
//                        synchronized (mPauseLock) {
//                            while ( mPaused ) {
//                                try {
//                                    mPauseLock.wait();
//                                } catch (InterruptedException e) {
//                                }
//                            }
//                        }
//                        return tempItem;
//                    }
//                sendPCRLiuChengProcessValueEvent(g_readtimes);
//            }
//        }
//        return tempItem;
//    }
//
//    void sendPCRLiuChengProcessValueEvent(int i, GlobalDate.EventType type) {
//        PCRLiuChengProcessValueEvent se = new PCRLiuChengProcessValueEvent();
//        se.setIndex(i);
//        se.setType(type);
//        EventBus.getDefault().post(se);
//    }

    //堆排序
    void HeapSort(double arr[], int len) {
        int i;
        //初始化堆，从最后一个父节点开始
        for (i = len / 2 - 1; i >= 0; --i) {
            Heapify(arr, i, len);
        }
        //从堆中的取出最大的元素再调整堆
        for (i = len - 1; i > 0; --i) {
            double temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            //调整成堆
            Heapify(arr, 0, i);
        }
    }

    //调整成堆的函数
    void Heapify(double arr[], int first, int end) {
        int father = first;
        int son = father * 2 + 1;
        while (son < end) {
            if (son + 1 < end && arr[son] > arr[son + 1]) ++son;
            //如果父节点大于子节点则表示调整完毕
            if (arr[father] < arr[son]) break;
            else {
                //不然就交换父节点和子节点的元素
                double temp = arr[father];
                arr[father] = arr[son];
                arr[son] = temp;
                //父和子节点变成下一个要比较的位置
                father = son;
                son = 2 * father + 1;
            }
        }
    }

    void saveBarcodeToFile(BarRareData tempBarRareData, String inifile) {
        String iniFile = inifile;
        writefile(inifile, GsonUtils.getInstance().toJson(tempBarRareData));
    }

    void writefile(String writefile, String writebuff) {
        FileIOUtils.writeFileFromString(writefile, writebuff, true);
    }
}
