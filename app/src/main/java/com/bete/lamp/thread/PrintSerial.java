package com.bete.lamp.thread;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.bete.lamp.service.Platform;
import com.utils.LogUtils;
import com.rd.io.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static android.system.OsConstants.O_NONBLOCK;
import static com.myutils.Canstant.PRINT_UART;
import static com.utils.ImageProcessing.DrawBitmap;
import static com.utils.ImageProcessing.PixOffset;

public class PrintSerial extends Thread {//
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;

    private final String TAG = "PrintSerial";
    private static int baudrate = 9600; //波率
    private static int PrintSerialSIZE = 500;
    private static int SCAN_CALLBACK_LEN = 515;
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Object lock = new Object();
    public int cmdState = 0;
    private int revlength, sendlength;
    private byte[] sendbuff = new byte[65535];
    private byte[] revbuff = new byte[65535];
    static int index;

    private void PrintSerial() {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        initPrintSerial(PRINT_UART, 9600);
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
    private static volatile PrintSerial instance = null;

    public static PrintSerial getInstance() {
        if (instance == null) {
            synchronized (PrintSerial.class) {
                if (instance == null) {
                    instance = new PrintSerial();
                    instance.PrintSerial();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        super.run();
        while ( true ) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化PrintSerial
     */
    private SerialPort initPrintSerial(String device, int baudrate) {
        try {
            if (device.equals("/dev/ttyMT3")) {
                Platform.initIO();
                Platform.SetGpioMode((int) 59, (int) 1);//表示把URXD3设置为URXD3
                Platform.SetGpioMode((int) 60, (int) 1);//表示把UTXD3设置为UTXD3
            }
            serialPort = new SerialPort(new File(device), baudrate, O_NONBLOCK);//

            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            LogUtils.d(TAG, "openSerialPort: 打开串口异常：" + e.toString());
            return serialPort;
        }
        LogUtils.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }

    public void destory() {
        try {
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
            serialPort.close();
        } catch (IOException e) {
            LogUtils.d(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    public int printString(String str)//-1 通信错误 2 无纸 0 成功 -2 转化失败
    {
        int ret = 0;
        synchronized (lock) {
            ret = paperEnough();
            if (ret != 0)
                return ret;

            ret = setFontSizeLittle();
            if (ret != 0)
                return ret;

//        String cmdbuff = "";
//        String[] list1 = str.split("\n");
//        if(list1!=null) {
//            for (int i = list1.length-1; i >= 0; i--)
//                cmdbuff += list1[i] + "\n";
//        }

            String str1 = "\n\n" + str + "\n\n\n\n";

            String str2 = "\n";
            byte[] sendData = new byte[0];
            byte[] sendData2 = new byte[0];
            try {
                sendData = str1.getBytes("gb2312");
                sendData2 = str2.getBytes("gb2312");
                LogUtils.d(str1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LogUtils.d(TAG, "sendSerialPort: 字符串转gb2312数组失败：" + e.toString());
                return -2;
            }
            ret = printSerialSend(sendData);
            LogUtils.d(sendData);
            ret = printSerialSend(sendData2);
            if (ret == 0) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    // 图像转为光栅位图
    public static byte[] Image1ToRasterCmd1(int width, int height, byte[] src, int offset) {
        int x = width;
        int y = height;
//        int x = width;
//        int y = height;
        int dstlen = 8 + x * y;

        LogUtils.d("x:" + x + ";y:" + y);

        byte[] dst = new byte[dstlen];

        dst[0] = 0x1d;
        dst[1] = 0x76;
        dst[2] = 0x30;
        dst[3] = 0x00;
        dst[4] = (byte) (x % 256);
        dst[5] = (byte) (x / 256);
        dst[6] = (byte) (y % 256);
        dst[7] = (byte) (y / 256);
        System.arraycopy(src, offset, dst, 8, x * y);
        return dst;
    }

    public int printImg(int width, int height, Bitmap bitmap)//-1 通信错误 2 无纸 0 成功 -2 转化失败
    {
        Bitmap bitmap1=imageScale(bitmap,width,height);
        byte[] src= DrawBitmap(0,0,bitmap1.getWidth(),bitmap1.getHeight(),0,bitmap1,0);
        int ret = 0;
        int offset = 0;
        int width1 = (bitmap1.getWidth() + 7) / 8;
        int height1 = bitmap1.getHeight();
        synchronized (lock) {
            ret = paperEnough();
            if (ret != 0)
                return ret;

            ret = setFontSizeLittle();
            if (ret != 0)
                return ret;
            String str2 = "\n";
            byte[] sendData2 = new byte[0];
            try {
                sendData2 = str2.getBytes("gb2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LogUtils.d(TAG, "sendSerialPort: 字符串转gb2312数组失败：" + e.toString());
                return -2;
            }

            byte[] sendData;
            while ( height1 >= 8 ) {
                sendData = Image1ToRasterCmd1(width1, 8, src, offset);
                LogUtils.d(sendData);
                ret = printSerialSend(sendData);
                offset += width1 * 8;
                height1 = height1 - 8;
                try {
                    sleep(350);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (height1 > 0) {
                byte[] sendData1 = Image1ToRasterCmd1(width1, height1, src, offset);
                LogUtils.d(sendData1);
                ret = printSerialSend(sendData1);
            }
            ret = printSerialSend(sendData2);
//            if (ret == 0) {
//                try {
//                    sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return ret;
    }

    /**
     * 调整图片大小
     *
     * @param bitmap
     *            源
     * @param dst_w
     *            输出宽度
     * @param dst_h
     *            输出高度
     * @return
     */
    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,true);
        return dstbmp;
    }

    public int paperEnough()//-1 通信错误 2 无纸 0 成功
    {
        //synchronized (lock) {
        byte[] cmdNewLine = new byte[3];
        byte[] revbuff = new byte[2];
        cmdNewLine[0] = 0x1C;
        cmdNewLine[1] = 0x76;
        cmdNewLine[2] = 0x00;
        if (printSerialSend(cmdNewLine, 2) == -1)
            return -1;
        int len = printSerialRev(revbuff);
        LogUtils.v("printSerialRev:" + len);
        LogUtils.v(revbuff);
        if ((len >= 1) && revbuff[0] == 0x04)
            return 0;
        else if ((len >= 1) && revbuff[0] != 0x04)
            return 2;
        else
            return -1;
        //}
    }

    public int setFontSizeLittle()//-1 通信错误 2 无纸 0 成功
    {
        int sendlength = 0;
        int revlength = 255;
        int retrycnt = 1;
        int timeout = 200;
        byte[] sendbuff = {0x1B, 0x4D, 0x01};
        byte[] sendbuff1 = {0x1B, 0x7B, 0x00};

        if (printSerialSend(sendbuff, 3) == -1)
            return -1;

        if (printSerialSend(sendbuff1, 3) == -1)
            return -1;

        return 0;
    }

    /**
     * 发送串口数据
     *
     * @param sendData byte[]
     */
    public int printSerialSend(byte[] sendData) {//0 成功 -1失败
        try {
            outputStream.write(sendData);
            outputStream.flush();
            //LogUtils.d("sendSerialPort: 串口数据发送成功 "+ bytesToHexString(sendData));
        } catch (IOException e) {
            LogUtils.d("printSerialSend Err:" + e.toString());
            return -1;
        }
        return 0;
    }

    public int printSerialSend(byte[] sendData, int size) {
        try {
            outputStream.write(sendData, 0, size);
            outputStream.flush();
            //LogUtils.d("sendSerialPort: 串口数据发送成功 "+ bytesToHexString(sendData));
        } catch (IOException e) {
            LogUtils.d("printSerialSend Err:" + e.toString());
            return -1;
        }
        return 0;
    }

    public int printSerialRev(byte[] sendData) {
        int templength = 0;
//        try {
//            sleep(1000);
//            return inputStream.read(sendData);
//        } catch (IOException e) {
//            LogUtils.d("printSerialRev Err");
//            return -1;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return -1;
//        }

        int i = 0;
        do {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
            try {
                templength = inputStream.read(sendData);
                if (templength > 0) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            i++;
        } while ( i * 5 < 1000 );
        return templength;
    }

    public int getLength(String str) {
        int nCount = str.length();
        char[] nihao = new char[nCount];
        int length = nCount;
        str.getChars(0, nCount, nihao, 0);
        for (int i = 0; i < nCount; i++) {
            char cha = nihao[i];
//            ushort uni = cha.unicode();
            if (cha >= 0x4E00 && cha <= 0x9FA5) {
                length++;
            }
        }
        return length;
    }

}