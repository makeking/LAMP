package com.myutils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

import com.anaalgorithm.FunAbleToCalcuParamInfo;
import com.bete.lamp.AppApplication;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.bean.BlockProjectEntity;
import com.bete.lamp.bean.CheckState;
import com.bete.lamp.bean.LampQcData;
import com.bete.lamp.bean.LiuChengCanShu;
import com.bete.lamp.bean.PCRDataHandleRef;
import com.bete.lamp.bean.PCRLiuCheng;
import com.bete.lamp.bean.PCRLiuChengCanShuItem;
import com.bete.lamp.bean.PCRPeiZhiRef;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.bean.SelfCheckCanShuItem;
import com.bete.lamp.bean.ServerCanShu;
import com.utils.FileCommonUtil;
import com.utils.FileIOUtils;
import com.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.utils.LogUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import static com.myutils.GlobalDate.g_guang_name1s;
import static com.myutils.GlobalDate.g_guang_name2s;
import static com.myutils.GlobalDate.g_guang_name3s;
import static com.myutils.GlobalDate.g_guang_name4s;

public class SharedPreferencesUtils {
    private static String TAG = "SharedPreferencesUtils";
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/LAMP/";
    public static final Context context = AppApplication.getAppContext();
    public static String fileName = "lampshared";
    public static final boolean isDebug = true;

    private SharedPreferencesUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void clear(Context paramContext, String paramString) {
        Editor editor = getSharedPreferences(paramContext, paramString).edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public static boolean contains(Context paramContext, String paramString1, String paramString2) {
        return getSharedPreferences(paramContext, paramString1).contains(paramString2);
    }

    public static Object get(Context paramContext, String paramString1, String paramString2, Object paramObject) {
        SharedPreferences sp = getSharedPreferences(paramContext, paramString1);
        if ((paramObject instanceof String)) {
            return sp.getString(paramString2, (String) paramObject);
        }
        if ((paramObject instanceof Integer)) {
            return Integer.valueOf(sp.getInt(paramString2, ((Integer) paramObject).intValue()));
        }
        if ((paramObject instanceof Boolean)) {
            return Boolean.valueOf(sp.getBoolean(paramString2, ((Boolean) paramObject).booleanValue()));
        }
        if ((paramObject instanceof Float)) {
            return Float.valueOf(sp.getFloat(paramString2, ((Float) paramObject).floatValue()));
        }
        if ((paramObject instanceof Long)) {
            return Long.valueOf(sp.getLong(paramString2, ((Long) paramObject).longValue()));
        }
        return null;
    }

    public static Map<String, ?> getAll(Context paramContext, String paramString) {
        return getSharedPreferences(paramContext, paramString).getAll();
    }

    private static SharedPreferences getSharedPreferences(Context paramContext, String paramString) {
        SharedPreferences mySharedPreferences = null;
        try {
            Field field;
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            Object obj = field.get(paramContext);
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            field.set(obj, new File(FILE_PATH));
            Log.d(TAG, FILE_PATH);
            mySharedPreferences = paramContext.getSharedPreferences(paramString, 0);
        } catch (NoSuchFieldException localNoSuchFieldException) {
            localNoSuchFieldException.printStackTrace();
            mySharedPreferences = paramContext.getSharedPreferences(paramString, 0);
        } catch (IllegalArgumentException localIllegalArgumentException) {
            localIllegalArgumentException.printStackTrace();
        } catch (IllegalAccessException localIllegalAccessException) {
            localIllegalAccessException.printStackTrace();
        }
        return mySharedPreferences;
    }

    public static void put(Context paramContext, String paramString1, String paramString2, Object paramObject) {
        Editor editor = getSharedPreferences(paramContext, paramString1).edit();
        if ((paramObject instanceof String)) {
            editor.putString(paramString2, (String) paramObject);
        } else if ((paramObject instanceof Float)) {
            editor.putFloat(paramString2, ((Float) paramObject).floatValue());
        } else if ((paramObject instanceof Boolean)) {
            editor.putBoolean(paramString2, ((Boolean) paramObject).booleanValue());
        } else if ((paramObject instanceof Integer)) {
            editor.putInt(paramString2, ((Integer) paramObject).intValue());
        } else if ((paramObject instanceof Long)) {
            editor.putLong(paramString2, ((Long) paramObject).longValue());
        } else {
            editor.putString(paramString2, paramObject.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    public static void remove(Context paramContext, String paramString1, String paramString2) {
        Editor editor = getSharedPreferences(paramContext, paramString1).edit();
        editor.remove(paramString2);
        SharedPreferencesCompat.apply(editor);
    }

    public static double getKAdjustRef() {
        String str = (String) get(context, fileName, "SYSTEM/KAdjust", "0.2");
        if (!str.equals("")) {
            return Double.parseDouble(str);
        }
        return 0;
    }

    public static void saveKAdjustRef(double ref) {
        put(context, fileName, "SYSTEM/KAdjust", ref);
        LogUtils.d("保存K校准参数数值！");
    }


    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        public static void apply(SharedPreferences.Editor paramEditor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(paramEditor, new Object[0]);
                    return;
                }
            } catch (InvocationTargetException localInvocationTargetException) {
                paramEditor.commit();
                return;
            } catch (IllegalAccessException localIllegalAccessException) {
                for (; ; ) {
                }
            } catch (IllegalArgumentException localIllegalArgumentException) {
                for (; ; ) {
                }
            }
        }

        private static Method findApplyMethod() {
            try {
                Method localMethod = SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
                return localMethod;
            } catch (NoSuchMethodException localNoSuchMethodException) {
            }
            return null;
        }
    }

    public static LiuChengCanShu getLiuChengCanShu() {
        LiuChengCanShu liuChengCanShu = new LiuChengCanShu();
        String str = (String) get(context, "liuchengini", "LiuChengCanShu", "");
        if (!str.equals("")) {
            liuChengCanShu = (LiuChengCanShu) GsonUtils.getInstance().fromJson(str, LiuChengCanShu.class);
        }
        return liuChengCanShu;
    }

    public static void saveLiuChengCanShu(LiuChengCanShu paramArrayOfPointMap) {
        put(context, "liuchengini", "LiuChengCanShu", GsonUtils.getInstance().toJson(paramArrayOfPointMap));
        LogUtils.d("保存流程参数！");
    }

    public static LinkedList<SelfCheckCanShuItem> getSelfCheckCanShu() {
        LinkedList localLinkedList = new LinkedList();
        String str = (String) get(context, "selfcheckini", "SelfCheckCanShu", "");
        if (!str.equals("")) {
            localLinkedList = (LinkedList) GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<SelfCheckCanShuItem>>() {
            }.getType());
        }
        return localLinkedList;
    }

    public static void saveSelfCheckCanShu(LinkedList<SelfCheckCanShuItem> paramLinkedList) {
        put(context, "selfcheckini", "SelfCheckCanShu", GsonUtils.getInstance().toJson(paramLinkedList));
        LogUtils.d("保存自检参数！");
    }

    public static boolean getSelfCheckAbleState() {
        return (Boolean) get(context, "selfcheckini", "SelfCheckAbleState", Boolean.valueOf(true));
    }

    public static void setSelfCheckAbleState(boolean string) {
        put(context, "selfcheckini", "SelfCheckAbleState", Boolean.valueOf(string));
        LogUtils.d("保存自检使能状态！");
    }


    public static ServerCanShu getServerCanShu() {
        String str = (String) get(context, "serverini", "ServerCanShu", "");
        if (!str.equals("")) {
            return GsonUtils.getInstance().fromJson(str, ServerCanShu.class);
        }
        return null;
    }

    public static void saveServerCanShu(ServerCanShu paramLinkedList) {
        put(context, "serverini", "ServerCanShu", GsonUtils.getInstance().toJson(paramLinkedList));
        LogUtils.d("保存服务器参数！");
    }

    public static boolean getServerAbleState() {
        return (Boolean) get(context, "serverini", "ServerAbleState", Boolean.valueOf(true));
    }

    public static void setServerAbleState(boolean string) {
        put(context, "serverini", "ServerAbleState", Boolean.valueOf(string));
        LogUtils.d("保存服务器使能状态！");
    }

    public static PCRLiuCheng getPCRLiuCheng() {
        PCRLiuCheng pcrLiuCheng = new PCRLiuCheng();
        String str = (String) get(context, "pcrliuchengini", "PCRLiuCheng", "");
        if (!str.equals("")) {
            pcrLiuCheng = (PCRLiuCheng) GsonUtils.getInstance().fromJson(str, PCRLiuCheng.class);
        }
        return pcrLiuCheng;
    }

    public static void savePCRLiuCheng(PCRLiuCheng paramArrayOfPointMap) {
        put(context, "pcrliuchengini", "PCRLiuCheng", GsonUtils.getInstance().toJson(paramArrayOfPointMap));
        LogUtils.d("保存PCR流程参数！");
    }


    public static LinkedList<BlockProjectEntity> getBlockProjectEntityLinkedList() {
        LinkedList localLinkedList = new LinkedList();
        String str = (String) get(context, "blockprojectentityini", "BlockProjectEntity", "");
        if (!str.equals("")) {
            localLinkedList = (LinkedList) GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<BlockProjectEntity>>() {
            }.getType());
        }
        return localLinkedList;
    }

    public static void saveBlockProjectEntityLinkedList(LinkedList<BlockProjectEntity> paramLinkedList) {
        put(context, "blockprojectentityini", "BlockProjectEntity", GsonUtils.getInstance().toJson(paramLinkedList));
        LogUtils.d("保存Block参数！");
    }

    public static LinkedList<PCRLiuChengCanShuItem> getPCRLiuChengCanShuLinkedList() {
        LinkedList localLinkedList = new LinkedList();
        String str = (String) get(context, "pcrLiuChengCanShuItemLinkListini", "PCRLiuChengCanShuItem", "");
        if (!str.equals("")) {
            localLinkedList = (LinkedList) GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<PCRLiuChengCanShuItem>>() {
            }.getType());
        }
        return localLinkedList;
    }

    public static void savePCRLiuChengCanShuLinkedList(LinkedList<PCRLiuChengCanShuItem> paramLinkedList) {
        put(context, "pcrLiuChengCanShuItemLinkListini", "PCRLiuChengCanShuItem", GsonUtils.getInstance().toJson(paramLinkedList));
        LogUtils.d("保存PCR流程参数！");
    }

    public static BarRareData getBarRareData(String inifile) {
        try {
            String str = FileIOUtils.readFile2String(inifile);
            if (!str.equals("")) {
                return (BarRareData) GsonUtils.getInstance().fromJson(str, BarRareData.class);
            }
        } catch (Exception e) {
            LogUtils.d("读取配置文件有问题！");
        }

        return null;
    }

    public static void saveBarRareData(BarRareData barRareData, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(barRareData));
        LogUtils.d("保存二维码信息到" + iniFile);
    }

    public static LinkedList<String> getGuangNamesList()
    {
        try {
            String str = (String)get(context, "GuangNamesini", "GuangNamesItem", "");
            if (!str.equals("")) {
                return (LinkedList)GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<String>>() {}.getType());
            }
        }catch (Exception e)
        {
            LogUtils.d("获取光路名称错误");
        }
        //return new LinkedList(Arrays.asList(new String[]{"FAM", "VIC", "ROX", "CY5"}));
        return new LinkedList(Arrays.asList(new String[]{g_guang_name1s.get(0), g_guang_name2s.get(0), g_guang_name3s.get(0), g_guang_name4s.get(0)}));//g_guang_name1s.get(0)
    }

    public static void saveGuangNamesList(LinkedList<String> paramLinkedList)
    {
        put(context, "GuangNamesini", "GuangNamesItem", GsonUtils.getInstance().toJson(paramLinkedList));
        LogUtils.d("保存光路名称参数！");
    }

//    public static LinkedList<CheckBoxItem> getBlockList()
//    {
//        LinkedList localLinkedList = new LinkedList();
//        String str = (String)get(context, "PeiZhiRefini", "BlockListItem", "");
//        if (!str.equals("")) {
//            localLinkedList = (LinkedList)GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<CheckBoxItem>>() {}.getType());
//        }
//        return localLinkedList;
//    }
//
//    public static void saveBlockList(LinkedList<CheckBoxItem> paramLinkedList)
//    {
//        put(context, "PeiZhiRefini", "BlockListItem", GsonUtils.getInstance().toJson(paramLinkedList));
//        LogUtils.d("保存BLOCK参数！");
//    }
//
//    public static LinkedList<CheckBoxItem> getGuangList()
//    {
//        LinkedList localLinkedList = new LinkedList();
//        String str = (String)get(context, "PeiZhiRefini", "GuangListItem", "");
//        if (!str.equals("")) {
//            localLinkedList = (LinkedList)GsonUtils.getInstance().fromJson(str, new TypeToken<LinkedList<CheckBoxItem>>() {}.getType());
//        }
//        return localLinkedList;
//    }
//
//    public static void saveGuangList(LinkedList<CheckBoxItem> paramLinkedList)
//    {
//        put(context, "PeiZhiRefini", "GuangListItem", GsonUtils.getInstance().toJson(paramLinkedList));
//        LogUtils.d("保存GUANG参数！");
//    }

    public static PCRDataHandleRef getPCRDataHandleRef() {
        PCRDataHandleRef pcrDataHandleRef = new PCRDataHandleRef();
        String str = (String) get(context, "PeiZhiRefini", "PCRDataHandleRef", "");
        if (!str.equals("")) {
            pcrDataHandleRef = (PCRDataHandleRef) GsonUtils.getInstance().fromJson(str, PCRDataHandleRef.class);
        }
        return pcrDataHandleRef;
    }

    public static void savePCRDataHandleRef(PCRDataHandleRef pcrDataHandleRef) {
        put(context, "PeiZhiRefini", "PCRDataHandleRef", GsonUtils.getInstance().toJson(pcrDataHandleRef));
        LogUtils.d("保存PCR流程参数！");
    }

    public static PCRPeiZhiRef getPCRPeiZhiRef() {
        PCRPeiZhiRef pcrPeiZhiRef = new PCRPeiZhiRef();
        String str = (String) get(context, "PeiZhiRefini", "PCRPeiZhiRef", "");
        if (!str.equals("")) {
            pcrPeiZhiRef = (PCRPeiZhiRef) GsonUtils.getInstance().fromJson(str, PCRPeiZhiRef.class);
        }
        return pcrPeiZhiRef;
    }

    public static void savePCRPeiZhiRef(PCRPeiZhiRef pcrPeiZhiRef) {
        put(context, "PeiZhiRefini", "PCRPeiZhiRef", GsonUtils.getInstance().toJson(pcrPeiZhiRef));
        LogUtils.d("保存PCR流程参数！");
    }

    public static PCRPeiZhiRef getPCRPeiZhiRef(String inifile) {
        PCRPeiZhiRef pcrPeiZhiRef = new PCRPeiZhiRef();
        String str = FileIOUtils.readFile2String(inifile);
        if (!str.equals("")) {
            pcrPeiZhiRef = (PCRPeiZhiRef) GsonUtils.getInstance().fromJson(str, PCRPeiZhiRef.class);
        }
        return pcrPeiZhiRef;
    }

    public static void savePCRPeiZhiRef(PCRPeiZhiRef pcrPeiZhiRef, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(pcrPeiZhiRef));
        LogUtils.d("保存PCR流程参数到" + iniFile);
    }

    public static void writeIniFile(String writefile, String writebuff) {
        FileIOUtils.writeFileFromString(writefile, writebuff, false);
    }

    public static boolean isPCRLiuCheng(String inifile) {
        try {
            String str = FileIOUtils.readFile2String(inifile);
            if (!str.equals("")) {
                PCRLiuCheng tempPcrLiuCheng = (PCRLiuCheng) GsonUtils.getInstance().fromJson(str, PCRLiuCheng.class);
                return true;
            }
        } catch (Exception e) {
            LogUtils.d("获取PCR流程参数错误");
        }

        return false;
    }

    public static PCRLiuCheng getPCRLiuCheng(String inifile) {
        try {
            String str = FileIOUtils.readFile2String(inifile);
            if (!str.equals("")) {
                return (PCRLiuCheng) GsonUtils.getInstance().fromJson(str, PCRLiuCheng.class);
            }
        } catch (Exception e) {
            LogUtils.d("获取PCR流程参数错误");
        }

        return new PCRLiuCheng();
    }

    public static void savePCRLiuCheng(PCRLiuCheng pcrLiuCheng, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(pcrLiuCheng));
        LogUtils.d("保存PCR流程参数到" + iniFile);
    }

    public static boolean isPCRProject(String inifile) {
        try {
            String str = FileIOUtils.readFile2String(inifile);
            if (!str.equals("")) {
                PCRProject tempPcrProject = (PCRProject) GsonUtils.getInstance().fromJson(str, PCRProject.class);
                return true;
            }
        } catch (Exception e) {
            LogUtils.d("获取PCR流程参数错误");
        }

        return false;
    }

    public static PCRProject getPCRProject(String inifile) {
        if ((FileCommonUtil.isFileExists(inifile)) && (!FileCommonUtil.isDir(inifile))) {
            try {
                String str = FileIOUtils.readFile2String(inifile);
                if (!str.equals("")) {
                    return (PCRProject) GsonUtils.getInstance().fromJson(str, PCRProject.class);
                }
            } catch (Exception e) {
                LogUtils.d("获取PCR流程参数错误");
            }
        }
        return new PCRProject();
    }

    public static void savePCRProject(PCRProject pcrProject, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(pcrProject));
        LogUtils.d("保存PCR流程参数到" + iniFile);
    }

    public static CheckState getPCRCheckState(String inifile) {
        if ((FileCommonUtil.isFileExists(inifile)) && (!FileCommonUtil.isDir(inifile))) {
            try {
                String str = FileIOUtils.readFile2String(inifile);
                if (!str.equals("")) {
                    return (CheckState) GsonUtils.getInstance().fromJson(str, CheckState.class);
                }
            } catch (Exception e) {
                LogUtils.d(e.getStackTrace().toString());
                LogUtils.d("获取实验状态数据错误！");
            }
        }
        return new CheckState();
    }

    public static void savePCRCheckState(CheckState checkState, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(checkState));
        LogUtils.d("保存实验状态数据到" + iniFile);
    }

    public static LampQcData getLampQcData(String inifile) {
        if ((FileCommonUtil.isFileExists(inifile)) && (!FileCommonUtil.isDir(inifile))) {
            try {
                String str = FileIOUtils.readFile2String(inifile);
                if (!str.equals("")) {
                    return (LampQcData) GsonUtils.getInstance().fromJson(str, LampQcData.class);
                }
            } catch (Exception e) {
                LogUtils.d("获取质控数据错误！");
            }
        }
        return new LampQcData();
    }

    public static void saveLampQcData(LampQcData lampQcData, String inifile) {
        String iniFile = inifile;
        writeIniFile(inifile, GsonUtils.getInstance().toJson(lampQcData));
        LogUtils.d("保存质控数据数据到" + iniFile);
    }

    public static int getStep2ShowDataType() {
        return (Integer) get(context, "systemini", "Step2ShowDataType", Integer.valueOf(0));
    }

    public static void setStep2ShowDataType(int string) {
        put(context, "systemini", "Step2ShowDataType", Integer.valueOf(string));
        LogUtils.d("保存实时显示数据类型！");
    }

    public static boolean getDEVICEState() {
        return (Boolean) get(context, "systemini", "DEVICEState", Boolean.valueOf(true));
    }

    public static void setDEVICEState(boolean string) {
        put(context, "systemini", "DEVICEState", Boolean.valueOf(string));
        LogUtils.d("保存仪器模拟状态！");
    }

    public static boolean getBootAbleState() {
        return (Boolean) get(context, "systemini", "BootAbleState", Boolean.valueOf(true));
    }

    public static void setBootAbleState(boolean string) {
        put(context, "systemini", "BootAbleState", Boolean.valueOf(string));
        LogUtils.d("保存自启状态状态！");
    }

    public static boolean getHomeAbleState() {
        return (Boolean) get(context, "systemini", "HomeAbleState", Boolean.valueOf(true));
    }

    public static void setHomeAbleState(boolean string) {
        put(context, "systemini", "HomeAbleState", Boolean.valueOf(string));
        LogUtils.d("保存是否主应用状态！");
    }

    public static boolean getDaohangjianAbleState() {
        return (Boolean) get(context, "systemini", "DaohangjianAbleState", Boolean.valueOf(true));
    }

    public static void setDaohangjianAbleState(boolean string) {
        put(context, "systemini", "DaohangjianAbleState", Boolean.valueOf(string));
        LogUtils.d("保存导航键状态！");
    }

    public static boolean getScreenOrientation() {
        try {
            String str = FileIOUtils.readFile2String("/sdcard/screenini");
            if (!str.equals("")) {
                return (boolean) GsonUtils.getInstance().fromJson(str, boolean.class);
            }
        } catch (Exception e) {
            LogUtils.d("获取屏幕翻转参数错误");
        }
        return true;
    }

    public static void setScreenOrientation(boolean string) {
        writeIniFile("/sdcard/screenini", GsonUtils.getInstance().toJson(string));
        LogUtils.d("保存屏幕方向！");
    }


    public static FunAbleToCalcuParamInfo getFunAbleToCalcuParamInfo() {
        try {
            String str = (String) get(context, "PCRCtSuanFaRefini", "FunAbleToCalcuParamInfo", "");
            if (!str.equals("")) {
                return (FunAbleToCalcuParamInfo) GsonUtils.getInstance().fromJson(str, FunAbleToCalcuParamInfo.class);
            }
        } catch (Exception e) {
            LogUtils.d("获取PCR FunAbleToCalcuParamInfo参数错误！");
        }
        return new FunAbleToCalcuParamInfo(50,1.1,0.007,1.03,3,0.0015d);
    }

    public static void saveFunAbleToCalcuParamInfo(FunAbleToCalcuParamInfo funAbleToCalcuParamInfo) {
        put(context, "PCRCtSuanFaRefini", "FunAbleToCalcuParamInfo", GsonUtils.getInstance().toJson(funAbleToCalcuParamInfo));
        LogUtils.d("保存PCR FunAbleToCalcuParamInfo参数！");
    }

}
