package com.bete.lamp.thread;

import android.util.Log;

import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.message.ScanDateValueEvent;
import com.bete.lamp.service.Platform;
import com.utils.LogUtils;
import com.rd.io.SerialPort;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.system.OsConstants.EBUSY;
import static android.system.OsConstants.O_NONBLOCK;
import static com.myutils.Canstant.SCAN_UART;
import static com.myutils.GlobalDate.saomaqiflag;

public class ScanSerial extends Thread{//52a0ecfe74
    public static byte[] SUCESS_DATA = new byte[]{0x52, (byte) 0xA0, (byte) 0xEC, (byte) 0xFE,0x74};
    public static byte[] ERR_DATA = new byte[]{0x52, (byte) 0xA0, (byte) 0xE0, (byte) 0xFE, (byte) 0x80};
    private Object mPauseLock= new Object();;
    private boolean mPaused;
    private boolean mFinished;

    private final String TAG = "ScanSerial";
    private static int baudrate = 9600; //波率
    private static int SCANSERIALSIZE = 500;
    private static int SCAN_CALLBACK_LEN = 515;
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Object lock = new Object();
    public  int cmdState=0;
    private int revlength,sendlength;
    private byte[] sendbuff = new byte[65535];
    private byte[] revbuff = new byte[65535];
    static int  index;

    public static int type = 1;

    private void ScanSerial(){
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        initScanSerial(SCAN_UART,115200);
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
    private static volatile ScanSerial instance = null;
    public static ScanSerial getInstance() {
        if (instance == null) {
            synchronized (ScanSerial.class) {
                if (instance == null) {
                    instance = new ScanSerial();
                    instance.ScanSerial();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        byte[] buf = new byte[SCANSERIALSIZE];
        byte[] buff1 = new byte[SCANSERIALSIZE];
        int ret;
        int lineLength=0;
        super.run();
        LogUtils.d("scan thread start");
        if(saomaqiflag==0)
        {
            cmdSaoMiaoDengKaiQi();
            cmdBuGuangDengKaiQi();
            cmdLianXuSaoMiao();
            cmdScanClose();
        }
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
                java.util.Arrays.fill(buf, (byte) 0);
                try {
                    lineLength = inputStream.read(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (lineLength > 0) {
                    LogUtils.d("SCAN REV LEN:" + String.valueOf(lineLength) + "DATA:" + new String(buf));
                    if (index + lineLength > SCANSERIALSIZE)
                        lineLength = SCANSERIALSIZE - index;
                    System.arraycopy(buf, 0, buff1, index, lineLength);
                    index += lineLength;
                } else {
                    if (index != 0) {
                        System.arraycopy(buff1, 0, revbuff, 0, index);
                        LogUtils.d(buff1, index);
                        cmdScanClose();
                        try {
                            lineLength = inputStream.read(buf);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ret = handerRevbuff();
                        if (ret == 0) {
                            LogUtils.d("SCAN REV:FAIL");
                        }
                        if (ret == 1) {
                            LogUtils.d("SCAN REV:sucess");
                        }
                        LogUtils.d("scan delete");
                        java.util.Arrays.fill(revbuff, (byte) 0);
                        index = 0;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化ScanSerial
     */
    private SerialPort initScanSerial(String device, int baudrate) {
        try {
            if (device.equals("/dev/ttyMT3")) {
                Platform.initIO();
                Platform.SetGpioMode((int)59, (int)1);//表示把URXD3设置为URXD3
                Platform.SetGpioMode((int)60, (int)1);//表示把UTXD3设置为UTXD3
            }
            serialPort = new SerialPort(new File(device), baudrate, O_NONBLOCK );
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

    public void destory(){
        try {
            if (inputStream!=null)
                inputStream.close();
            if (outputStream!=null)
                outputStream.close();
            serialPort.close();
        } catch (IOException e) {
            LogUtils.d(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    //0 is error;EBUSY is busy;1 is sucess
    int handerRevbuff()
    {
        int ret =0;
        byte[] scan_callback = new byte[SCAN_CALLBACK_LEN];
        BarRareData p;

        LogUtils.d("SCAN handerRevbuff");
        java.util.Arrays.fill(scan_callback, (byte) 0);

        LogUtils.d(revbuff,index);
        System.arraycopy(revbuff,0,scan_callback,0,index);
        ret =1;
        if(ret==1)
        {
            sendScanDateValueEvent(scan_callback,index,type);
        }
        java.util.Arrays.fill(revbuff, (byte) 0);
        index=0;
        return ret;
    }

    private byte sumrc(byte[] data, int size)
    {
        byte sum=0;
        for(int i=0;i<size;i++)
        {
            sum+=data[i];
        }
        return sum;
    }

    void sendScanDateValueEvent(byte[] buff,int length,int type) {
        ScanDateValueEvent se = new ScanDateValueEvent(buff,length,type);
        EventBus.getDefault().post(se);
    }

    public boolean scanSerialSend(byte[] sendData, int size) {
        try {
            outputStream.write(sendData, 0, size);
            outputStream.flush();
            //LogUtils.d("sendSerialPort: 串口数据发送成功 "+ bytesToHexString(sendData));
        } catch (IOException e) {
            LogUtils.d("printSerialSend Err:" + e.toString());
            return false;
        }
        return true;
    }

    public boolean cmdScanOpen(int type1) {
        type = type1;
        if(saomaqiflag==0) {
            byte[] sendbuff = {0x05, 0x57, (byte) 0xA0,0x01,0x01, (byte) 0xFF, (byte) 0x02};
            return scanSerialSend(sendbuff, sendbuff.length);
        }
        else
        {
            byte[] sendbuff = {0x16, 0x54, 0x0D};
            return scanSerialSend(sendbuff, sendbuff.length);
        }
    }

    public boolean cmdScanClose() {
        if(saomaqiflag==0) {
            byte[] sendbuff = {0x05, 0x57, (byte) 0xA0,0x01,0x00, (byte) 0xFF, (byte) 0x03};
            return scanSerialSend(sendbuff, sendbuff.length);
        }
        else
        {
            byte[] sendbuff = {0x16, 0x55, 0x0D};
            return scanSerialSend(sendbuff, sendbuff.length);
        }
    }

    //瞄准灯扫描时开启
    public boolean cmdSaoMiaoDengKaiQi() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        int j = 0;
        boolean ret = false;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            sendlength = 7;
            sendbuff[0] = 0x05;
            sendbuff[1] = 0x57;
            sendbuff[2] = (byte) 0xA1;
            sendbuff[3] = 0x03;
            sendbuff[4] = 0x01;
            sendbuff[5] = (byte) 0xFE;
//            sendbuff[6] = (byte) 0xFE;
            sendbuff[6] = (byte) 0xFF;
            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength >= 5) {
                    if((revbuff[0]==SUCESS_DATA[0])&&(revbuff[1]==SUCESS_DATA[1])&&(revbuff[2]==SUCESS_DATA[2])&&(revbuff[3]==SUCESS_DATA[3])&&(revbuff[4]==SUCESS_DATA[4]))
                    {
                        ret = true;
                        return ret;
                    }
                    else
                    {
                        ret = false;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            return  ret;
        }
    }

    //补光灯工作模式扫描时开启
    public boolean cmdBuGuangDengKaiQi() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        int j = 0;
        boolean ret = false;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            sendlength = 7;
            sendbuff[0] = 0x05;
            sendbuff[1] = 0x57;
            sendbuff[2] = (byte) 0xA1;
            sendbuff[3] = 0x04;
            sendbuff[4] = 0x01;
            sendbuff[5] = (byte) 0xFE;
            sendbuff[6] = (byte) 0xFE;
            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength >= 5) {
                    if((revbuff[0]==SUCESS_DATA[0])&&(revbuff[1]==SUCESS_DATA[1])&&(revbuff[2]==SUCESS_DATA[2])&&(revbuff[3]==SUCESS_DATA[3])&&(revbuff[4]==SUCESS_DATA[4]))
                    {
                        ret = true;
                        return ret;
                    }
                    else
                    {
                        ret = false;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            return  ret;
        }
    }

    //扫描模式 连续扫描
    public boolean cmdLianXuSaoMiao() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        int j = 0;
        boolean ret = false;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            sendlength = 7;
            sendbuff[0] = 0x05;
            sendbuff[1] = 0x57;
            sendbuff[2] = (byte) 0xA1;
//            sendbuff[3] = 0x04;
//            sendbuff[4] = 0x01;
            sendbuff[3] = 0x02;
            sendbuff[4] = 0x03;
            sendbuff[5] = (byte) 0xFE;
            sendbuff[6] = (byte) 0xFE;
            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength >= 5) {
                    if((revbuff[0]==SUCESS_DATA[0])&&(revbuff[1]==SUCESS_DATA[1])&&(revbuff[2]==SUCESS_DATA[2])&&(revbuff[3]==SUCESS_DATA[3])&&(revbuff[4]==SUCESS_DATA[4]))
                    {
                        ret = true;
                        return ret;
                    }
                    else
                    {
                        ret = false;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            return  ret;
        }
    }

    int cmdPrev(byte[] revbuff, int revlength) {
        int size, len, ret = -EBUSY;
        int index = 0;
        while ( (revbuff[index] != 0x52) && (index < revlength) ) {
            index++;
        }
        revlength = revlength - index;
        System.arraycopy(revbuff, index, revbuff, 0, revlength);

        if (revlength < 5) {
            LogUtils.d("scan rev size < 5:" + String.valueOf(index) + String.valueOf(revlength));
            return ret;
        }
        return revlength;
    }

    int sendAndRev(byte[] sendbuff, int sendlength, int targetlength, int timeout) {
        int i = 0, ret = -EBUSY;
        byte[] tempRevBuff = new byte[65535];
        byte[] tempSendBuff = new byte[sendlength];
        System.arraycopy(sendbuff, 0, tempSendBuff, 0, sendlength);
        revlength = 0;
        try {
            int x = inputStream.read(tempRevBuff);
            if(x>0)
                LogUtils.d(bytesToHexString(tempRevBuff, x));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanSerialSend(tempSendBuff, sendlength);
        LogUtils.d("Scan SEND:" + bytesToHexString(tempSendBuff));

        do {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                int templength = inputStream.read(tempRevBuff);
                if (templength > 0) {
                    if ((revlength) + templength > targetlength)
                        templength = targetlength - (revlength);
                    System.arraycopy(tempRevBuff, 0, revbuff, revlength, templength);
                    revlength += templength;
//                    if ((revlength > 4) && (targetlength == 65535)) {
//                        targetlength = (int) (revbuff[3] & 0xFF);
//                    }
                    if ((revlength) >= 5)//targetlength
                        break;
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;

        } while ( i * 5 < timeout );
        if (revlength > 0) {
            LogUtils.d("  Scan Rev:" + bytesToHexString(revbuff, revlength));
        }
        return revlength;
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src, int size) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0 || src.length <= size) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
