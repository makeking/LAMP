package com.bete.lamp.thread;

import com.bete.lamp.service.Platform;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import static com.bete.lamp.thread.McuSerial.cmdMcuStateFlag;
import static com.bete.lamp.thread.McuSerial.cmdMcuCommiFlag;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_device_state;

public class LedIO extends Thread{
    private final String TAG = "LedIO";
    private Object mPauseLock= new Object();;
    private boolean mPaused;
    private boolean mFinished;
    private Object lock = new Object();
    public static int type = 1;
    public static int RED_IO=52;
    public static int GREEN_IO=51;
    public static boolean redIOState = true;
    public static boolean greenIOState = true;
    private void LedIO(){
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        initLEDIO();
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

    /**
     * 单例
     */
    private static volatile LedIO instance = null;
    public static LedIO getInstance() {
        if (instance == null) {
            synchronized (LedIO.class) {
                if (instance == null) {
                    instance = new LedIO();
                    instance.LedIO();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        int ret;
        super.run();
        LogUtils.d(TAG, " start");
        while (!mFinished )
        {
            synchronized (mPauseLock) {
                while ( mPaused ) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            synchronized (lock) {
                if ((g_block_states[0] == GlobalDate.BlockStateType.running)||(g_block_states[1] == GlobalDate.BlockStateType.running)||(g_block_states[2] == GlobalDate.BlockStateType.running)||(g_block_states[3] == GlobalDate.BlockStateType.running)) {
                    greenIOState = !greenIOState;
                } else {
                    greenIOState = true;
                }

                if (greenIOState) {
                    Platform.SetGpioDataHigh(GREEN_IO);
                } else {
                    Platform.SetGpioDataLow(GREEN_IO);
                }

                if ((!cmdMcuCommiFlag)||(!cmdMcuStateFlag)||(g_device_state== GlobalDate.DeviceStateType.needcheck)) {
                    redIOState = !redIOState;
                } else {
                    redIOState = true;
                }

                if (redIOState) {
                    Platform.SetGpioDataHigh(RED_IO);
                } else {
                    Platform.SetGpioDataLow(RED_IO);
                }

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化ScanSerial
     */
    private void initLEDIO() {
        Platform.initIO();
        Platform.SetGpioMode(RED_IO, 0);//表示把SCL2设置为GPIO
        Platform.SetGpioMode(GREEN_IO, 0);//表示把SDA2设置为GPIO
        Platform.SetGpioOutput(RED_IO);//表示把GPIO52设置为输出
        Platform.SetGpioOutput(GREEN_IO);//表示把GPIO52设置为输出
//            Platform.SetGpioDataHigh(RED_IO);
//            Platform.SetGpioDataLow(GREEN_IO);
    }

    public void destory(){
        LogUtils.d(TAG, "destory");
    }
}
