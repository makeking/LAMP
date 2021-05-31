package com.myutils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;

import com.anaalgorithm.AnaAlgorithm;
import com.anaalgorithm.FunAbleToCalcuParamInfo;
import com.bete.lamp.AppApplication;
import com.bete.lamp.bean.LampQcData;
import com.bete.lamp.bean.LampResultItem;
import com.bete.lamp.bean.LiuChengCanShu;
import com.bete.lamp.bean.PCRDataHandleRef;
import com.bete.lamp.bean.PCRPeiZhiRef;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.bean.ServerCanShu;
import com.bete.lamp.message.PCRLiuChengProcessValueEvent;
import com.bete.lamp.thread.McuSerial;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.myutils.SharedPreferencesUtils.getGuangNamesList;
import static com.myutils.SharedPreferencesUtils.getKAdjustRef;
import static com.myutils.SharedPreferencesUtils.getLiuChengCanShu;
import static com.myutils.SharedPreferencesUtils.getPCRDataHandleRef;
import static com.myutils.SharedPreferencesUtils.getPCRPeiZhiRef;
import static com.myutils.SharedPreferencesUtils.getStep2ShowDataType;

public class GlobalDate {
    public static String PRODUCT_NAME = "********";
    public static boolean DEVICE = true;
    public static boolean reverseDevice = true;
    public static final int LiuChengMaxTemp = 90;
    public static final int LiuChengMinTemp = 35;

    public static final int DefaultMinCheckTime = 5;//默认最小检测时间
    public static final int GUANGNUM = 4;//光数量
    public static final int BLOCKNUM = 4;//block数量
    public static final int BLOCKSIZE = 4;//每个block孔位
    public static final int LIUCHENGNUM = 5;
    public static final int CAIJICOUNT = 200;
    public static float g_ymin = (float) 0.2;
    public static int g_sqlsize = 5000;

    public static int MinCheckTime = 5;//最小检测时间
    public static boolean[] g_device_guang_ables = new boolean[GUANGNUM];//光路是否存在
    public static int g_device_gangnum=4;
    public static LinkedList<String> g_guang_name1s = new LinkedList<String>(Arrays.asList(new String[]{"FAM", "SYBR", "EVA"}));
    public static LinkedList<String> g_guang_name2s = new LinkedList<String>(Arrays.asList(new String[]{"VIC"}));
    public static LinkedList<String> g_guang_name3s = new LinkedList<String>(Arrays.asList(new String[]{"ROX", "HEX"}));
    public static LinkedList<String> g_guang_name4s = new LinkedList<String>(Arrays.asList(new String[]{"CY5"}));
    public static LinkedList<String> g_guang_names = getGuangNamesList();//光通道名称
    static {
        g_device_guang_ables[0] = true;
        g_device_guang_ables[1] = true;
        g_device_guang_ables[2] = true;
        g_device_guang_ables[3] = true;
    }

    public static FunAbleToCalcuParamInfo g_true_FunAbleToCalcuParamInfo = SharedPreferencesUtils.getFunAbleToCalcuParamInfo();
    public static FunAbleToCalcuParamInfo g_false_FunAbleToCalcuParamInfo = SharedPreferencesUtils.getFunAbleToCalcuParamInfo();

    static {
        g_true_FunAbleToCalcuParamInfo.bNormalization = true;
        g_false_FunAbleToCalcuParamInfo.bNormalization = false;
        SharedPreferencesUtils.saveFunAbleToCalcuParamInfo(g_true_FunAbleToCalcuParamInfo);
    }

    public static AnaAlgorithm g_anaAlgorithm = new AnaAlgorithm();
    public static int[] chartColors = new int[]{0xFF0000FF, 0xFF008200, 0xFFFF9309, 0xFFFF0000, Color.RED, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK, Color.GRAY, Color.BLUE, Color.LTGRAY, Color.WHITE, Color.CYAN, Color.MAGENTA};
    public static double g_quraoqian_datachart[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//获取
    public static double g_raredatas[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//原始
    public static float[][][] g_jiaozhun_avr = new float[BLOCKNUM][GUANGNUM][BLOCKSIZE];
    public static double g_fenxilvbodatas[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//滤波分析
    public static double g_fenxiguiyidatas[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//归一分析
    public static double g_showlvbodatas[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//滤波显示
    public static double g_showguiyidatas[][][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE][CAIJICOUNT];//归一显示
    public static double g_ct[][][] = new double[BLOCKNUM][GUANGNUM][BLOCKSIZE];

    public static long g_checktime = 5000;//检测时间间隔
    public static long g_oldtime = 0;//
    public static long g_sleeptime = 0;//
    public static long[] g_alltimes = new long[BLOCKNUM];//流程总时间
    public static long[] g_allcounts = new long[BLOCKNUM];//流程采集数
    public static BlockStateType[] g_block_states = new BlockStateType[BLOCKNUM];//block 状态 0：就绪 1：进行 2：实验结束
    public static boolean[] g_block_is_first = new boolean[BLOCKNUM];
    public static boolean[] g_flag_daowens = new boolean[BLOCKNUM];//温度到达标志
    public static long[] g_time_daowens = new long[BLOCKNUM];//设置到温时间点
    public static int[] g_temp_settings = new int[BLOCKNUM];//设置温度数据
    public static int[] g_time_jianges = new int[BLOCKNUM];//设置时间间隔数据
    public static long[] g_time_readtime_daos = new long[BLOCKNUM];//设置时间间隔数据
    public static int[] g_time_holds = new int[BLOCKNUM];//设置hold时间数据
    public static long[] g_time_shengyus = new long[BLOCKNUM];//block检测剩余时间
    public static long[] g_temp_time_shengyus = new long[BLOCKNUM];//block检测剩余时间显示时间
    public static int[] g_indexs_steps = new int[BLOCKNUM];//执行流程步骤
    public static PCRProject[] g_pcrProjects = new PCRProject[BLOCKNUM];//项目配置文件
    public static PCRProject nouse_pcrProject = new PCRProject();

    static {
        for (int i = 0; i < BLOCKNUM; i++) {
            g_pcrProjects[i] = nouse_pcrProject;
            g_block_states[i] = BlockStateType.ready;
        }
    }

    public static int[] g_indexs_data = new int[BLOCKNUM];//采集第几组数
    public static int[][][] g_jianceshuju = new int[BLOCKNUM][GUANGNUM][BLOCKSIZE];
    public static boolean[] g_qcenable = new boolean[BLOCKNUM];//质控是能
    public static String[][] g_samplenos = new String[BLOCKNUM][BLOCKSIZE];//样本编号
    public static boolean[][] g_step2_guangs = new boolean[BLOCKNUM][BLOCKSIZE];//step2 光路选中状态
    public static boolean[][] g_step2_kongs = new boolean[BLOCKNUM][BLOCKSIZE];//step2 孔位选中状态
    public static boolean[][] g_step3_guangs = new boolean[BLOCKNUM][BLOCKSIZE];//step3 光路选中状态
    public static double[][] g_AntoThresvalue = new double[BLOCKNUM][GUANGNUM];//自动阈值
    public static float[][] g_Thresvalue = new float[BLOCKNUM][GUANGNUM];//阈值

    static {
        for (int i = 0; i < BLOCKNUM; i++) {
            g_qcenable[i] = false;
            for (int j = 0; j < BLOCKSIZE; j++) {
                g_step2_guangs[i][j] = true;
                g_step2_kongs[i][j] = true;
                g_step3_guangs[i][j] = true;
                g_samplenos[i][j] = "";
            }
        }
    }

    //当前Fragent的下标
    public static int[] g_currentStep = new int[4];
//    public static int g_currentBlock = 0;
    public static LampResultItem[][][] g_lamp_resultitem = new LampResultItem[BLOCKNUM][GUANGNUM][BLOCKSIZE];//计算 需要开始reset

    static {
        for (int i = 0; i < BLOCKNUM; i++)
            for (int j = 0; j < GUANGNUM; j++)
                for (int k = 0; k < BLOCKSIZE; k++)
                    g_lamp_resultitem[i][j][k] = new LampResultItem(i * BLOCKSIZE * GUANGNUM + j * BLOCKSIZE + k, (float) i * BLOCKSIZE * GUANGNUM + j * BLOCKSIZE + k + 1);
    }

    public static LampQcData[] g_lampQcData = new LampQcData[BLOCKNUM];
    public static String[] g_checkTimeStr = new String[BLOCKNUM];


    ////////////////////////查询数据///////////////////////
    public static String g_chaxun_selectDir = "";
    public static int g_chaxun_softcode = 0;
    public static String g_chaxun_checkTimeStr;//计算
    public static int g_chaxun_blockno = 0;
    public static int g_chaxun_caijicount = 0;
    public static String g_chaxun_checkstate_timestr;//
    public static float[] g_chaxun_Thresvalue = new float[GUANGNUM];//阈值
    public static int g_chaxun_indexs_data = 0;
    public static int g_chaxun_allcount = 0;
    public static double g_chaxun_raredatas[][][] = new double[GUANGNUM][BLOCKSIZE][CAIJICOUNT];//原始
    public static double g_chaxun_showlvbodatas[][][] = new double[GUANGNUM][BLOCKSIZE][CAIJICOUNT];//滤波  显示
    public static double g_chaxun_showguiyidatas[][][] = new double[GUANGNUM][BLOCKSIZE][CAIJICOUNT];//归一 显示
    public static double g_chaxun_fenxilvbodatas[][][] = new double[GUANGNUM][BLOCKSIZE][CAIJICOUNT];//滤波  分析
    public static double g_chaxun_fenxiguiyidatas[][][] = new double[GUANGNUM][BLOCKSIZE][CAIJICOUNT];//归一 分析
    public static double g_chaxun_ct[][] = new double[GUANGNUM][BLOCKSIZE];
    public static double g_chaxun_kadjustref = 0.2;
    public static PCRProject g_chaxun_pcrProject = new PCRProject();//项目配置文件
    public static boolean g_chaxun_qcenable;
    public static String[] g_chaxun_samplenos = new String[BLOCKSIZE];
    public static LampResultItem[][] g_chaxun_lamp_resultitem = new LampResultItem[GUANGNUM][BLOCKSIZE];//计算 需要开始reset

    static {
        for (int j = 0; j < GUANGNUM; j++)
            for (int k = 0; k < BLOCKSIZE; k++)
                g_chaxun_lamp_resultitem[j][k] = new LampResultItem(0 * BLOCKSIZE * GUANGNUM + j * BLOCKSIZE + k, (float) 0 * BLOCKSIZE * GUANGNUM + j * BLOCKSIZE + k + 1);
    }

    public static LampQcData g_chaxun_lampQcData = new LampQcData();
    //查询
    public static String strstart = "", strend = "";
    public static long longstart = 0, longend = 0;
    public static int searchtype;//1  date;2 id
    public static String id;
    //////////////////////////////////////////////////////////
//    public static PCRDataHandleRef g_pcrDataHandleRef = new PCRDataHandleRef();
    public static PCRDataHandleRef g_pcrDataHandleRef;
    public static int g_step2_show_datatype = getStep2ShowDataType();//0:归一化后数据，1：滤波后数据，2：校准后数据，3：原始数据

    static {
        g_pcrDataHandleRef = getPCRDataHandleRef();
        SharedPreferencesUtils.savePCRDataHandleRef(g_pcrDataHandleRef);
    }

    public static double g_kadjustref = getKAdjustRef();

    public static PCRPeiZhiRef g_pcrPeiZhiRef;

    static {
        g_pcrPeiZhiRef = getPCRPeiZhiRef();
        for (int i = g_pcrPeiZhiRef.blockchannels.size(); i < BLOCKSIZE; i++)
            g_pcrPeiZhiRef.blockchannels.add(false);
        for (int i = g_pcrPeiZhiRef.guangchannels.size(); i < GUANGNUM; i++)
            g_pcrPeiZhiRef.guangchannels.add(false);
        SharedPreferencesUtils.savePCRPeiZhiRef(g_pcrPeiZhiRef);
    }

    public static PCRPeiZhiRef g_run_pcrPeiZhiRef;
    //1代表eeprom,2代表配置文件,3代表计算生成
    public static McuSerial mcuCom;
    public static int g_NormalSpeed, g_NormalPulse, g_ResetSpeed;

    static {
        if (DEVICE) {
            mcuCom = McuSerial.getInstance();
            g_NormalSpeed = mcuCom.getNormalSpeed();
            g_NormalPulse = mcuCom.getNormalPulse();
            g_ResetSpeed = mcuCom.getResetSpeed();
            mcuCom.cmdLampLedRun(true, true, true, true);//打开led灯
            //mcuCom.cmdLampSetPCRMOTOR(0x13,14000,1,g_ResetSpeed);//复位
        }
    }

    public static int DeviceEepormTEMP;//1

    public static LiuChengCanShu defaultLiuChengCanShu = getLiuChengCanShu();//2

    static {
        if (defaultLiuChengCanShu == null) {
            defaultLiuChengCanShu = new LiuChengCanShu();
        }
    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 1;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String packageVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        String code = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static double getDoubleFromEditText(EditText v) {
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

    public static List<String> danwei_list = new ArrayList<String>();

    static {
        danwei_list.add("选择单位");
        danwei_list.add("IU/mL");
        danwei_list.add("PFU/mL");
        danwei_list.add("Copies/mL");
        danwei_list.add("CFU/ml");
    }

    public static List<String> activity_name_list = new ArrayList<>();

    static {
        activity_name_list.add("检测");
        activity_name_list.add("项目");
        activity_name_list.add("数据查询");
        activity_name_list.add("设置参数");
        activity_name_list.add("帮助");
    }

    public enum BlockStateType {
        ready, running, done,
    }

    public enum EventType {
        blockStateChange, dataChange, timeChange,
    }

    public enum ProjectType {
        dingxing, dingliang,
    }

    public static void sendPCRLiuChengProcessValueEvent(int i, GlobalDate.EventType type) {
        PCRLiuChengProcessValueEvent se = new PCRLiuChengProcessValueEvent();
        se.setIndex(i);
        se.setType(type);
        EventBus.getDefault().post(se);
    }

    ////1代表eeprom,2代表配置文件,3代表计算生成
    public static int[] xinhaoabs = new int[4];
    public static String deviceno = "";
    public static String devicehardversion = "";
    public static String devicehardwareversion = "";
    public static String deviceoutdate="";
    public static int[] addr_xinhaoabs = new int[4];//两个字节
    static {
        addr_xinhaoabs[0] = 0x400;
        addr_xinhaoabs[1] = 0x404;
        addr_xinhaoabs[2] = 0x408;
        addr_xinhaoabs[3] = 0x40C;
    }

    public static double wenshengspeed = 4;
    public static double wenjiangspeed = 3;
    public static int[] xinhaohigh = new int[4];
    public static int[] xinhaolow = new int[4];
    public static int wenduabs = 0;
    public static int wenduself = 0;
    public static int wenduselffinal = 0xFFFF;
    public static int wenduwaittime = 0;

    public static int selfwenduthres = 60;
    public static int selfwenshengtime = 30;
    public static int selfwenshengvalue = 4;
    public static int selfwenshengabs = 2;
    public static int selfwenjiangtime = 30;
    public static int selfwenjiangvalue = 4;
    public static int selfwenjiangabs = 2;
    public static float[][] g_jiaozhun = new float[4][4];

    public static int addr_wenshengspeed = 0x18B8;//两个字节
    public static int addr_wenjiangspeed = 0x18BA;//两个字节
    public static int[] addr_xinhaohigh = new int[4];//两个字节
    static {
        addr_xinhaohigh[0] = 0x18A4;
        addr_xinhaohigh[1] = 0x18A8;
        addr_xinhaohigh[2] = 0x18AC;
        addr_xinhaohigh[3] = 0x18B0;
    }

    public static int[] addr_xinhaolow = new int[4];//两个字节
    static {
        addr_xinhaolow[0] = 0x18A6;
        addr_xinhaolow[1] = 0x18AA;
        addr_xinhaolow[2] = 0x18AE;
        addr_xinhaolow[3] = 0x18B2;
    }

    public static int addr_wenduself = 0x189C;//两个字节 温度*100 自检温度先
    public static int addr_wenduabs = 0x18B4;//两个字节 温度*100 自检温度范围
    public static int addr_wenduwaittime = 0x18B6;//两个字节 S   自检等待时间

    public static int addr_selfwenduthres = 0x1B04;//两个字节 温度 自检判断升降温的阈值线
    public static int addr_selfwenshengtime = 0x1B06;//两个字节 S 自检升温时间
    public static int addr_selfwenshengvalue = 0x1B08;//两个字节 温度   自检升温度数
    public static int addr_selfwenshengabs = 0x1B0A;//两个字节 温度   自检升温范围
    public static int addr_selfwenjiangtime = 0x1B0C;//两个字节 S    自检降温时间
    public static int addr_selfwenjiangvalue = 0x1B0E;//两个字节 温度   自检降温度数
    public static int addr_selfwenjiangabs = 0x1B10;//两个字节 温度   自检降温范围

    public static int addr_jiaozhun = 0xB9C;//校准地址

    public static int addr_deviceno = 0x200;//unsigned int      设备编号
    public static int addr_hardversion = 0x10;//unsigned int     硬件版本
    public static int addr_hardwareversion = 0x20;//unsigned int 固件版本
    public static int addr_deviceoutdate =0x100;//unsigned int 出厂日期

    public static int addr_productname = 0x0;
    public static int addr_productname_len = 16;
    public static int addr_gangnumable = 0x360;//第一个字节为光路个数，第二个每一位代表光路具体使能情况
    public static int addr_gangnames =0x300;
    public static int addr_gangnames_len =16;//6*5*4
    public static int addr_minchecktime=0xB02;//点击扫描时间

    public static int saomaqiflag = 0;
    public static int addr_saomaqiflag = 0x1000;
    static {
        if(DEVICE) {
            saomaqiflag = mcuCom.getTwoBytesToInt(addr_saomaqiflag);
        }
    }

    public static DeviceStateType g_device_state = DeviceStateType.ready;

    public enum DeviceStateType {
        needcheck, ready
    }

    ////////////////////////////////////软件版本，服务器设置，导航键，屏幕翻转，开机自启////////////////////////
    public static String SoftwareVersion = packageVersion(AppApplication.getAppContext());//1
    public static int SoftwareCode = packageCode(AppApplication.getAppContext());//1
    public static boolean g_daohangjianEnable = SharedPreferencesUtils.getDaohangjianAbleState();//SharedPreferencesUtils.getDaohangjianAbleState();
    public static boolean g_homeEnable = SharedPreferencesUtils.getHomeAbleState();
    public static boolean g_bootEnable = true;//SharedPreferencesUtils.getBootAbleState();
    public static boolean g_screenOrientationEnable = SharedPreferencesUtils.getScreenOrientation();
    public static ServerCanShu defaultServerCanShu = SharedPreferencesUtils.getServerCanShu();//2

    static {
        if (defaultServerCanShu == null) {
            defaultServerCanShu = new ServerCanShu();
        }
    }

    public static void setDefaultLauncher(Context context, String defPackageName, String defClassName) {
        try {
            if (!TextUtils.isEmpty(defPackageName) && !TextUtils.isEmpty(defClassName)) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.MAIN");
                filter.addCategory("android.intent.category.HOME");
                filter.addCategory("android.intent.category.DEFAULT");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                // 返回给定条件的所有ResolveInfo对象(本质上是Activity)
                List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                int bestMatch = 0;
                final int size = list.size();
                ComponentName[] set = new ComponentName[size];
                for (int i = 0; i < size; i++) {
                    ResolveInfo ri = list.get(i);
                    set[i] = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
                    if (ri.match > bestMatch) {
                        bestMatch = ri.match;
                    }
                }
                ComponentName preActivity = new ComponentName(defPackageName, defClassName);
                context.getPackageManager().addPreferredActivity(filter, bestMatch, set, preActivity);
            }
        } catch (java.lang.SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void clearDefaultLauncher(Context context) {
        try {
            ArrayList<IntentFilter> intentList = new ArrayList<IntentFilter>();
            ArrayList<ComponentName> cnList = new ArrayList<ComponentName>();
            context.getPackageManager().getPreferredActivities(intentList, cnList, null);
            for (int i = 0; i < cnList.size(); i++) {
                IntentFilter dhIF = intentList.get(i);
                if (dhIF.hasAction(Intent.ACTION_MAIN) && dhIF.hasCategory(Intent.CATEGORY_HOME)) {
                    //清除原有的默认launcher
                    context.getPackageManager().clearPackagePreferredActivities(cnList.get(i).getPackageName());
                }
            }
        } catch (java.lang.SecurityException e) {
            e.printStackTrace();
        }
    }

}
