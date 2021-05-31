package com.bete.lamp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.bete.lamp.thread.LampLiuChengThread;
import com.bete.lamp.thread.LedIO;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.thread.PrintSerial;
import com.bete.lamp.thread.ScanSerial;
import com.myutils.CrashHandler;
import com.myutils.Canstant;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;
import com.utils.ShellUtils;
import com.utils.Utils;
import com.squareup.leakcanary.LeakCanary;

import org.xutils.x;

import java.io.File;
import java.util.Locale;

import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.PRODUCT_NAME;
import static com.myutils.GlobalDate.addr_productname;
import static com.myutils.GlobalDate.addr_productname_len;
import static com.myutils.GlobalDate.g_screenOrientationEnable;
import static com.myutils.GlobalDate.saomaqiflag;

/**
 * Created by whieenz on 2017/11/25.
 */

public class AppApplication extends Application {
    private static String TAG = "AppApplication";
    private static AppApplication instance;
    public static String lang = "zh";//"en" "zh"
    public static LampLiuChengThread onlyLampLiuChengThread = null;
    private Boolean isDebug = true;

    //tts语音播放
    //public static TextToSpeech mSpeech = null;
    @Override
    public void onCreate() {
        super.onCreate();
        initI18();
        CrashHandler.getInstance().init(this);
        //mSpeech = new TextToSpeech(getApplicationContext(), new TTSListener());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        Utils.init(getAppContext());
        x.Ext.init(this);
        instance = this;
        initLog();
        if (DEVICE) {
            if (g_screenOrientationEnable)
                ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:2", true);
            else
                ShellUtils.execCmd("content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0", true);
            McuSerial.getInstance();//.start()
            PrintSerial.getInstance().start();
            LedIO.getInstance().start();
            ScanSerial.getInstance().start();
            Object[] tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_productname,addr_productname_len);
            if ((int) tempObjects[1] != 0) {
//                PRODUCT_NAME = "*****";
            }
            else
            {
                char[] chars = new char[addr_productname_len];
                for (int i = 0; i < addr_productname_len; i++) {
                    chars[i] = (char) ((byte[]) tempObjects[2])[i];
                }
//                PRODUCT_NAME = String.valueOf(chars, 0, addr_productname_len).replace(" ","");
                PRODUCT_NAME = String.valueOf(chars, 0, addr_productname_len).trim();
            }
        }
    }

    // 获取Application
    public static Context getAppContext() {
        return instance;
    }

    // init it in ur application
    void initLog() {
        String logDir;
        logDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_LOG_DIR;
        FileCommonUtil.createOrExistsDir(logDir);

        LogUtils.Config config = LogUtils.getConfig()
                .setLogSwitch(isDebug())// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(isDebug())// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(true)// 打印 log 时是否存到文件的开关，默认关
                .setDir(logDir)// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("log")// 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)// 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.D)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(4)// log 栈深度，默认为 1
                .setStackOffset(0)// 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setSaveDays(3)// 设置日志可保留天数，默认为 -1 表示无限时长
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                ;
        LogUtils.d(config.toString());
    }


    /**
     * 初始化国际化语言，繁体字和简体字
     */
    private void initI18() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
//        if (LanguageUtil.getCountry(getApplicationContext()).equals("TW")) {
//            config.locale = Locale.TAIWAN;
//        } else {
//            config.locale = Locale.CHINESE;
//        }
//        config.locale=Locale.ENGLISH;
        if(lang.equals("en"))
        {
            config.locale = Locale.ENGLISH;
        }else {
            config.locale = Locale.CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }

    private Boolean isDebug() {
//        if (isDebug == null)
//            isDebug = AppUtils.isAppDebug();
        return isDebug;
    }
}
