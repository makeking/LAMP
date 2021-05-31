package com.bete.lamp.thread;

import android.util.Log;

import com.bete.lamp.service.Platform;
import com.utils.Byte2TypeUtils;
import com.utils.LogUtils;
import com.bete.lamp.message.StateCodeEvent;
import com.rd.io.SerialPort;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.system.OsConstants.EBUSY;
import static android.system.OsConstants.O_NONBLOCK;
import static com.myutils.Canstant.CMD_UART;
import static com.myutils.Command.CMD_BOOTCRC;
import static com.myutils.Command.CMD_BOOTCRC_RESP;
import static com.myutils.Command.CMD_BOOTERASEFLASH;
import static com.myutils.Command.CMD_BOOTERASEFLASH_RESP;
import static com.myutils.Command.CMD_BOOTPROGRAMFLASH;
import static com.myutils.Command.CMD_BOOTPROGRAMFLASH_RESP;
import static com.myutils.Command.CMD_BOOTREADVERSION;
import static com.myutils.Command.CMD_BOOTREADVERSION_RESP;
import static com.myutils.Command.CMD_HELLO;
import static com.myutils.Command.CMD_HELLO_RESP;
import static com.myutils.Command.CMD_JMPTOAPP;
import static com.myutils.Command.CMD_JMPTOAPP_RESP;
import static com.myutils.Command.CMD_LAMPGETALLTEMP;
import static com.myutils.Command.CMD_LAMPGETALLTEMP_RESP;
import static com.myutils.Command.CMD_LAMPGETCAPTEMP;
import static com.myutils.Command.CMD_LAMPGETCAPTEMP_RESP;
import static com.myutils.Command.CMD_LAMPGETTEMP;
import static com.myutils.Command.CMD_LAMPGETTEMP_RESP;
import static com.myutils.Command.CMD_LAMPLEDRUN;
import static com.myutils.Command.CMD_LAMPLEDRUN_RESP;
import static com.myutils.Command.CMD_LAMPREADAD;
import static com.myutils.Command.CMD_LAMPREADAD_RESP;
import static com.myutils.Command.CMD_LAMPREADEEPROM;
import static com.myutils.Command.CMD_LAMPREADEEPROM_RESP;
import static com.myutils.Command.CMD_LAMPREADPCRFENG;
import static com.myutils.Command.CMD_LAMPREADPCRFENG_RESP;
import static com.myutils.Command.CMD_LAMPSETALLTEMP;
import static com.myutils.Command.CMD_LAMPSETALLTEMP_RESP;
import static com.myutils.Command.CMD_LAMPSETCAPTEMP;
import static com.myutils.Command.CMD_LAMPSETCAPTEMP_RESP;
import static com.myutils.Command.CMD_LAMPSETMPPC;
import static com.myutils.Command.CMD_LAMPSETMPPC_RESP;
import static com.myutils.Command.CMD_LAMPSETPCRMOTOR;
import static com.myutils.Command.CMD_LAMPSETPCRMOTOR_RESP;
import static com.myutils.Command.CMD_LAMPSETPCRTEMP;
import static com.myutils.Command.CMD_LAMPSETPCRTEMP_RESP;
import static com.myutils.Command.CMD_LAMPWRITEEEPROM;
import static com.myutils.Command.CMD_LAMPWRITEEEPROM_RESP;
import static com.myutils.Command.CMD_RESETMCU;
import static com.myutils.Command.CMD_RESETMCU_RESP;
import static com.myutils.Command.COMMU_SWITCH;
import static com.myutils.Command.FRAMEHEAD;
import static com.myutils.Command.POWER_SWITCH;
import static com.myutils.Command.PRINTER_SCAN_SWITCH;
import static com.myutils.GlobalDate.addr_deviceno;
import static com.myutils.GlobalDate.deviceno;
import static com.myutils.GlobalDate.g_jianceshuju;
import static com.myutils.GlobalDate.reverseDevice;
import static com.myutils.StateCode.CMD_BOOTCRC_ERR;
import static com.myutils.StateCode.CMD_BOOTERASEFLASH_ERR;
import static com.myutils.StateCode.CMD_BOOTPROGRAMFLASH_ERR;
import static com.myutils.StateCode.CMD_BOOTREADVERSION_ERR;
import static com.myutils.StateCode.CMD_HELLO_ERR;
import static com.myutils.StateCode.CMD_JMPTOAPP_ERR;
import static com.myutils.StateCode.CMD_LAMPGETALLTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPGETCAPTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPGETTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPLEDRUN_ERR;
import static com.myutils.StateCode.CMD_LAMPREADAD_ERR;
import static com.myutils.StateCode.CMD_LAMPREADEEPROM_ERR;
import static com.myutils.StateCode.CMD_LAMPREADPCRFENG_ERR;
import static com.myutils.StateCode.CMD_LAMPSETALLTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPSETCAPTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPSETMPPC_ERR;
import static com.myutils.StateCode.CMD_LAMPSETPCRMOTOR_ERR;
import static com.myutils.StateCode.CMD_LAMPSETPCRTEMP_ERR;
import static com.myutils.StateCode.CMD_LAMPWRITEEEPROM_ERR;
import static com.myutils.StateCode.CMD_RESETMCU_ERR;
import static com.myutils.StateCode.COMMU_ERR;
import static com.myutils.StateCode.COMMU_INSERR;
import static com.myutils.StateCode.COMMU_SUCESS;

public class McuSerial extends Thread {//
    private final String TAG = "McuSerial";
    private static String path = "/dev/ttyS4";  // 可根据设备不同修改
    private static int baudrate = 9600; //波率

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Object lock = new Object();
    public int cmdState = 0;
    public static boolean cmdMcuStateFlag = false;
    public static boolean cmdMcuCommiFlag = false;
    private int revlength, sendlength;
    private byte[] sendbuff = new byte[65535];
    private byte[] revbuff = new byte[65535];

    private void McuSerial() {
        initMcuSerial(CMD_UART, 115200);
    }

    /**
     * 单例
     */
    private static volatile McuSerial instance = null;

    public static McuSerial getInstance() {
        if (instance == null) {
            synchronized (McuSerial.class) {
                if (instance == null) {
                    instance = new McuSerial();
                    instance.McuSerial();
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
     * 初始化McuSerial
     */
    private SerialPort initMcuSerial(String device, int baudrate) {
        try {
            if (device.equals("/dev/ttyMT3")) {
                Platform.initIO();
                Platform.SetGpioMode((int) 59, (int) 1);//表示把URXD3设置为URXD3
                Platform.SetGpioMode((int) 60, (int) 1);//表示把UTXD3设置为UTXD3
            }
            serialPort = new SerialPort(new File(device), baudrate, O_NONBLOCK);
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

    /**
     * 发送串口数据
     *
     * @param sendData byte[]
     */
    public void mcuSerialSend(byte[] sendData, int size) {
        try {
            outputStream.write(sendData, 0, size);
            outputStream.flush();
            //Log.d(TAG, "sendSerialPort: 串口数据发送成功 "+ bytesToHexString(sendData));
        } catch (IOException e) {
            LogUtils.file(TAG, "sendSerialPort: 串口数据发送失败：" + e.toString());
        }
    }

    int sendAndRev(byte[] sendbuff, int sendlength, int targetlength, int timeout) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        int i = 0, ret = -EBUSY;
        byte[] tempRevBuff = new byte[65535];
        byte[] tempSendBuff = new byte[sendlength];
        System.arraycopy(sendbuff, 0, tempSendBuff, 0, sendlength);
        revlength = 0;
        try {
            int x = inputStream.read(tempRevBuff);
//            LogUtils.d(bytesToHexString(tempRevBuff, x));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mcuSerialSend(tempSendBuff, sendlength);
        LogUtils.d(name + "  SEND:" + bytesToHexString(tempSendBuff));

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
                    if ((revlength > 4) && (targetlength == 65535)) {
                        targetlength = (int) (revbuff[3] & 0xFF);
                    }
                    if ((revlength) >= targetlength)
                        break;
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;

        } while ( i * 5 < timeout );
        if (revlength > 0) {
            LogUtils.d(name + "  REV:" + bytesToHexString(revbuff, revlength));
        }
        return revlength;
    }

    int cmdPrev(byte[] revbuff, int revlength) {
        int size, len, ret = -EBUSY;
        int index = 0;
        while ( (revbuff[index] != FRAMEHEAD) && (index < revlength) ) {
            index++;
        }
        revlength = revlength - index;
        System.arraycopy(revbuff, index, revbuff, 0, revlength);

        if (revlength < 5) {
            LogUtils.d("cmd rev size < 5:" + String.valueOf(index) + String.valueOf(revlength));
            //emit sendStateCode(COMMU_LENLT);
            //sendStateCodeEvent(COMMU_LENLT);
            return ret;
        }

        len = (int) (revbuff[3] & 0xFF);
        size = len;

        if (len < 5) {
            LogUtils.d("cmd len <5:" + String.valueOf(len));
            //emit sendStateCode(COMMU_LENLT);
            //sendStateCodeEvent(COMMU_LENLT);
            return ret;
        }

        if (len > revlength) {
            LogUtils.d("cmd len:" + String.valueOf(len) + " no match rev length" + String.valueOf(revlength));
            //emit sendStateCode(COMMU_LENNOMATCH);
            //sendStateCodeEvent(COMMU_LENNOMATCH);
            return ret;
        }

        if (revbuff[size - 1] != sumrc(revbuff, size - 1)) {
            LogUtils.d("BCC ERR:" + String.valueOf(sumrc(revbuff, size - 1)));
            //emit sendStateCode(COMMU_BCCERR);
            //sendStateCodeEvent(COMMU_BCCERR);
            return ret;
        }
        return revlength;
    }

    private byte sumrc(byte[] data, int size) {
        byte sum = 0;
        for (int i = 0; i < size; i++) {
            sum += data[i];
        }
        return sum;
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
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

    /**
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private Object[] updataState(Object[] tempObjects)
    {
        cmdMcuCommiFlag = ((int)(tempObjects[0])==COMMU_SUCESS);
        cmdMcuStateFlag = ((int)(tempObjects[01]) == 0);
        return tempObjects;
    }

    public Object[] cmdHello() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 5;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_HELLO;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) sendlength;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_HELLO_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            ret = CMD_HELLO_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSetMPPC(int channle, int value) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];

        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            if (channle == 0)
                cmdState = -1;
            sendlength = 8;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETMPPC;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) (value >> 8);
            sendbuff[5] = (byte) value;
            if (reverseDevice && (channle < 5))
                sendbuff[6] = (byte) ((byte) 5 - channle);
            else
                sendbuff[6] = (byte) channle;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETMPPC_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPSETMPPC_ERR);
            ret = CMD_LAMPSETMPPC_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSelfLed(boolean ledable) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 7;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPLEDRUN;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 1;
            if (ledable)
                sendbuff[5] = (byte) 0xff;
            else
                sendbuff[5] = (byte) 0x0;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPLEDRUN_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPLEDRUN_ERR);
            ret = CMD_LAMPLEDRUN_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampReadAD() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        byte[] buff = null;
        Object[] tempObjects = new Object[4];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPREADAD;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 1;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if ((revbuff[1] == CMD_LAMPREADAD_RESP)) {
                        LogUtils.d(name + " sucess");
                        buff = new byte[revlength];
                        System.arraycopy(revbuff, 0, buff, 0, revlength);
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = revlength;
                        tempObjects[3] = buff;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            //sendStateCodeEvent(CMD_LAMPREADPCRFENG_ERR);
            ret = CMD_LAMPREADAD_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = revlength;
            tempObjects[3] = buff;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampLedRun(boolean ledable1, boolean ledable2, boolean ledable3, boolean ledable4) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 7;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPLEDRUN;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 2;
            if (reverseDevice)
                sendbuff[5] = (byte) ((ledable4 ? 1 : 0) | ((ledable3 ? 1 : 0) << 1) | ((ledable2 ? 1 : 0) << 2) | ((ledable1 ? 1 : 0) << 4));
            else
                sendbuff[5] = (byte) ((ledable1 ? 1 : 0) | ((ledable2 ? 1 : 0) << 1) | ((ledable3 ? 1 : 0) << 2) | ((ledable4 ? 1 : 0) << 4));
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPLEDRUN_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPLEDRUN_ERR);
            ret = CMD_LAMPLEDRUN_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSetPCRTEMP(int blocktemp1, int blocktemp2, int blocktemp3, int blocktemp4) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 10000;
        int speed = 100;
        /*  0：正常PCR控温类型
                正常模式下，三路帕尔贴目标温度控制一致，升降温速度一致，一般都是最大升降温；
            1：熔解曲线控温类型
                熔解曲线下，由于升降温速度很低，而且目标温度变化在（0，1），需要修改PID参数或者其他控温方式；
            2：梯度PCR控温类型
                梯度模式下，三路帕尔贴单独目标温度控制，且同时工作。
            3：其他调试测试类型
        */
        int kongWenType = 2;

        synchronized (lock) {
            cmdState = -1;
            sendlength = 16;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETPCRTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            if (reverseDevice) {
                sendbuff[4] = (byte) (blocktemp4 >> 8);
                sendbuff[5] = (byte) blocktemp4;
                sendbuff[6] = (byte) (blocktemp3 >> 8);
                sendbuff[7] = (byte) blocktemp3;
                sendbuff[8] = (byte) (blocktemp2 >> 8);
                sendbuff[9] = (byte) blocktemp2;
                sendbuff[10] = (byte) (blocktemp1 >> 8);
                sendbuff[11] = (byte) blocktemp1;
            } else {
                sendbuff[4] = (byte) (blocktemp1 >> 8);
                sendbuff[5] = (byte) blocktemp1;
                sendbuff[6] = (byte) (blocktemp2 >> 8);
                sendbuff[7] = (byte) blocktemp2;
                sendbuff[8] = (byte) (blocktemp3 >> 8);
                sendbuff[9] = (byte) blocktemp3;
                sendbuff[10] = (byte) (blocktemp4 >> 8);
                sendbuff[11] = (byte) blocktemp4;
            }
            sendbuff[12] = (byte) (speed >> 8);
            sendbuff[13] = (byte) speed;
            sendbuff[14] = (byte) kongWenType;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETPCRTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPSETPCRTEMP_ERR);

            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }
    /*
    辅助码fuzhu  高字节：1：X 扫描电机 2：Y 前后运动电机 3：Z 上下运动电机
            低字节：0：正传 1：反转 2：脱机 3：复位 4：绝对运动 5：扫描 6：组合
    maichong        电机运动的脉冲值，P1 是高 8 位，P2 是低 8 位，组成 16 位的脉冲值
    dir             电机运动的方向值
    speed           电机运动的速度值，P4 是高 8 位，P5 是低 8 位，组成 16 位的速度值
     */

    public Object[] cmdLampSetPCRMOTOR(int fuzhu, int maichong, int dir, int speed) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 5000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 11;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETPCRMOTOR;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) (fuzhu);
            sendbuff[5] = (byte) (maichong >> 8);
            sendbuff[6] = (byte) (maichong);
            sendbuff[7] = (byte) (dir);
            sendbuff[8] = (byte) (speed >> 8);
            sendbuff[9] = (byte) (speed);
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETPCRMOTOR_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPSETPCRMOTOR_ERR);
            ret = CMD_LAMPSETPCRMOTOR_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampReadPCRFeng(int channel) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        byte[] buff = null;
        Object[] tempObjects = new Object[4];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 3000;
        byte tempbyte;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 7;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPREADPCRFENG;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) channel;
            sendbuff[5] = (byte) 1;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if ((revbuff[1] == CMD_LAMPREADPCRFENG_RESP)) {
                        LogUtils.d(name + " sucess");
                        buff = new byte[revlength];
                        if(reverseDevice)
                        {
                            System.arraycopy(revbuff, 0, buff, 0, revlength);
                            for(int z=0;z<(15-z);z++) {
                                tempbyte = buff[4 + 2 * z];
                                buff[4 + 2 * z] = buff[4 + 2 * (15-z)];
                                buff[4 + 2 * (15-z)] = tempbyte;

                                tempbyte = buff[5 + 2 * z];
                                buff[5 + 2 * z] = buff[5 + 2 * (15-z)];
                                buff[5 + 2 * (15-z)] = tempbyte;
                            }
                        }
                        else
                            System.arraycopy(revbuff, 0, buff, 0, revlength);
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = revlength;
                        tempObjects[3] = buff;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            //sendStateCodeEvent(CMD_LAMPREADPCRFENG_ERR);
            ret = CMD_LAMPREADPCRFENG_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = revlength;
            tempObjects[3] = buff;
            return updataState(tempObjects);
        }
    }

    /*
    0	环境温度
    1	电源温度
    2	功率温度
    3	扫描头温度
    4	热盖温度
    5	热沉散热器
    6	帕尔贴1温度
    7	帕尔贴2温度
    8	帕尔贴3温度
    9	帕尔贴整体温度（计算后）
    10	帕尔贴4温度
    11	帕尔贴5温度
    12	帕尔贴6温度
    9	帕尔贴整体温度（计算后）
    P1-P2  16 位温度数据，P1 高位，P2 低位;温度*100，温度范围不超过 150℃
    P3  标志位：0 未到温； 1 到温
     */
    public Object[] cmdLampGetTEMP(int no) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[4];
        int value = 0;
        boolean tempok = false;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 5000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPGETTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            if ((reverseDevice) && (no < 5))
                sendbuff[4] = (byte) ((byte) 5 - no);
            else
                sendbuff[4] = (byte) no;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPGETTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
//                        value = revbuff[4] & 0xFF;
//                        value = (value << 8) | revbuff[5];
                        value = (int) ((revbuff[5] & 0xFF) | ((revbuff[4] & 0xFF) << 8));
                        if (revbuff[6] > 0)
                            tempok = true;
                        else
                            tempok = false;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = value;
                        tempObjects[3] = tempok;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPGETTEMP_ERR);
            LampLiuChengThread.LiuChengErr = 1;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = value;
            tempObjects[3] = tempok;
            return updataState(tempObjects);
        }
    }


    public Object[] cmdLampGetALLTEMP() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[4];
        int[] values = new int[4];
        byte state = 0;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 5000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPGETALLTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 0x0F;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPGETALLTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        if (reverseDevice) {
                            values[0] = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                            values[1] = (int) ((revbuff[8] & 0xFF) | ((revbuff[7] & 0xFF) << 8));
                            values[2] = (int) ((revbuff[10] & 0xFF) | ((revbuff[9] & 0xFF) << 8));
                            values[3] = (int) ((revbuff[12] & 0xFF) | ((revbuff[11] & 0xFF) << 8));
                            if ((((byte) revbuff[13] & (0x01 << 0)) != 0))
                                state = (byte) (state | 0x08);
                            if ((((byte) revbuff[13] & (0x01 << 1)) != 0))
                                state = (byte) (state | 0x04);
                            if ((((byte) revbuff[13] & (0x01 << 2)) != 0))
                                state = (byte) (state | 0x02);
                            if ((((byte) revbuff[13] & (0x01 << 3)) != 0))
                                state = (byte) (state | 0x01);
                            state = (byte) (state | (revbuff[13] & 0xF0));
                        } else {
                            values[3] = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                            values[2] = (int) ((revbuff[8] & 0xFF) | ((revbuff[7] & 0xFF) << 8));
                            values[1] = (int) ((revbuff[10] & 0xFF) | ((revbuff[9] & 0xFF) << 8));
                            values[0] = (int) ((revbuff[12] & 0xFF) | ((revbuff[11] & 0xFF) << 8));
                            state = revbuff[13];
                        }
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = values;
                        tempObjects[3] = state;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            LampLiuChengThread.LiuChengErr = 1;
            ret = CMD_LAMPGETALLTEMP_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = values;
            tempObjects[3] = state;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSetALLTEMP(byte ables, int blocktemp1, int blocktemp2, int blocktemp3, int blocktemp4) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 10000;
        byte realables = 0;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 14;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETALLTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);

            if (reverseDevice) {
                if ((((byte) ables & (0x01 << 0)) != 0))
                    realables = (byte) (realables | 0x08);
                if ((((byte) ables & (0x01 << 1)) != 0))
                    realables = (byte) (realables | 0x04);
                if ((((byte) ables & (0x01 << 2)) != 0))
                    realables = (byte) (realables | 0x02);
                if ((((byte) ables & (0x01 << 3)) != 0))
                    realables = (byte) (realables | 0x01);
                realables = (byte) (realables | (ables & 0xF0));

                sendbuff[4] = realables;
                sendbuff[5] = (byte) (blocktemp4 >> 8);
                sendbuff[6] = (byte) blocktemp4;
                sendbuff[7] = (byte) (blocktemp3 >> 8);
                sendbuff[8] = (byte) blocktemp3;
                sendbuff[9] = (byte) (blocktemp2 >> 8);
                sendbuff[10] = (byte) blocktemp2;
                sendbuff[11] = (byte) (blocktemp1 >> 8);
                sendbuff[12] = (byte) blocktemp1;
            } else {
                realables = ables;
                sendbuff[4] = (byte) realables;
                sendbuff[5] = (byte) (blocktemp1 >> 8);
                sendbuff[6] = (byte) blocktemp1;
                sendbuff[7] = (byte) (blocktemp2 >> 8);
                sendbuff[8] = (byte) blocktemp2;
                sendbuff[9] = (byte) (blocktemp3 >> 8);
                sendbuff[10] = (byte) blocktemp3;
                sendbuff[11] = (byte) (blocktemp4 >> 8);
                sendbuff[12] = (byte) blocktemp4;
            }
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETALLTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            ret = CMD_LAMPSETALLTEMP_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSetALLTEMP(int blocktemp1, int blocktemp2, int blocktemp3, int blocktemp4) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 10000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 14;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETALLTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 0x0F;
            if (reverseDevice) {
                sendbuff[5] = (byte) (blocktemp4 >> 8);
                sendbuff[6] = (byte) blocktemp4;
                sendbuff[7] = (byte) (blocktemp3 >> 8);
                sendbuff[8] = (byte) blocktemp3;
                sendbuff[9] = (byte) (blocktemp2 >> 8);
                sendbuff[10] = (byte) blocktemp2;
                sendbuff[11] = (byte) (blocktemp1 >> 8);
                sendbuff[12] = (byte) blocktemp1;
            } else {
                sendbuff[5] = (byte) (blocktemp1 >> 8);
                sendbuff[6] = (byte) blocktemp1;
                sendbuff[7] = (byte) (blocktemp2 >> 8);
                sendbuff[8] = (byte) blocktemp2;
                sendbuff[9] = (byte) (blocktemp3 >> 8);
                sendbuff[10] = (byte) blocktemp3;
                sendbuff[11] = (byte) (blocktemp4 >> 8);
                sendbuff[12] = (byte) blocktemp4;
            }
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETALLTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            ret = CMD_LAMPSETALLTEMP_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }


    public Object[] cmdLampGetCapTEMP() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[4];
        int[] values = new int[4];
        byte state = 0;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 5000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPGETCAPTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 0x0F;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPGETCAPTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        if (reverseDevice) {
                            values[3] = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                            values[2] = (int) ((revbuff[8] & 0xFF) | ((revbuff[7] & 0xFF) << 8));
                            values[1] = (int) ((revbuff[10] & 0xFF) | ((revbuff[9] & 0xFF) << 8));
                            values[0] = (int) ((revbuff[12] & 0xFF) | ((revbuff[11] & 0xFF) << 8));
                            if ((((byte) revbuff[13] & (0x01 << 0)) != 0))
                                state = (byte) (state | 0x08);
                            if ((((byte) revbuff[13] & (0x01 << 1)) != 0))
                                state = (byte) (state | 0x04);
                            if ((((byte) revbuff[13] & (0x01 << 2)) != 0))
                                state = (byte) (state | 0x02);
                            if ((((byte) revbuff[13] & (0x01 << 3)) != 0))
                                state = (byte) (state | 0x01);
                            state = (byte) (state | (revbuff[13] & 0xF0));
                        } else {
                            values[0] = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                            values[1] = (int) ((revbuff[8] & 0xFF) | ((revbuff[7] & 0xFF) << 8));
                            values[2] = (int) ((revbuff[10] & 0xFF) | ((revbuff[9] & 0xFF) << 8));
                            values[3] = (int) ((revbuff[12] & 0xFF) | ((revbuff[11] & 0xFF) << 8));
                            state = revbuff[13];
                        }
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = values;
                        tempObjects[3] = state;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            LampLiuChengThread.LiuChengErr = 1;
            ret = CMD_LAMPGETCAPTEMP_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = values;
            tempObjects[3] = state;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampSetCapTEMP(int blocktemp1, int blocktemp2, int blocktemp3, int blocktemp4) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 10000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 14;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPSETCAPTEMP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) 0x0F;
            if(reverseDevice) {
                sendbuff[5] = (byte) (blocktemp4 >> 8);
                sendbuff[6] = (byte) blocktemp4;
                sendbuff[7] = (byte) (blocktemp3 >> 8);
                sendbuff[8] = (byte) blocktemp3;
                sendbuff[9] = (byte) (blocktemp2 >> 8);
                sendbuff[10] = (byte) blocktemp2;
                sendbuff[11] = (byte) (blocktemp1 >> 8);
                sendbuff[12] = (byte) blocktemp1;
            }
            else
            {
                sendbuff[5] = (byte) (blocktemp1 >> 8);
                sendbuff[6] = (byte) blocktemp1;
                sendbuff[7] = (byte) (blocktemp2 >> 8);
                sendbuff[8] = (byte) blocktemp2;
                sendbuff[9] = (byte) (blocktemp3 >> 8);
                sendbuff[10] = (byte) blocktemp3;
                sendbuff[11] = (byte) (blocktemp4 >> 8);
                sendbuff[12] = (byte) blocktemp4;
            }
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPSETCAPTEMP_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            ret = CMD_LAMPSETCAPTEMP_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    /*
    读取地址，（P1 高字节、P3 低字节）
    P4  读取数据长度
     */
    public Object[] cmdLampReadEeprom(int addr, int len) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        byte[] buff = new byte[255];
        Object[] tempObjects = new Object[3];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 500;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 9;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPREADEEPROM;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength);
            sendbuff[4] = (byte) (addr >> 16);
            sendbuff[5] = (byte) (addr >> 8);
            sendbuff[6] = (byte) addr;
            sendbuff[7] = (byte) len;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPREADEEPROM_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        buff = new byte[revlength];
                        System.arraycopy(revbuff, 4, buff, 0, revlength);
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        tempObjects[2] = buff;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            ret = CMD_LAMPREADEEPROM_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            tempObjects[2] = buff;
            return updataState(tempObjects);
        }
    }

    public Object[] cmdLampWriteEeprom(int addr, byte[] buff1, int len) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        byte[] buff = null;
        Object[] tempObjects = new Object[2];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 500;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 8 + len;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_LAMPWRITEEEPROM;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) sendlength;
            sendbuff[4] = (byte) (addr >> 16);
            sendbuff[5] = (byte) (addr >> 8);
            sendbuff[6] = (byte) addr;
            System.arraycopy(buff1, 0, sendbuff, 7, len);
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_LAMPWRITEEEPROM_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        tempObjects[1] = cmdState;
                        ret = COMMU_SUCESS;
                        tempObjects[0] = ret;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        ret = COMMU_INSERR;
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            //sendStateCodeEvent(CMD_LAMPWRITEEEPROM_ERR);
            ret = CMD_LAMPWRITEEEPROM_ERR;
            tempObjects[0] = ret;
            tempObjects[1] = cmdState;
            return updataState(tempObjects);
        }
    }

    //0x510 电机运行脉冲（扫描和绝对运动）
    //0x506 电机正常工作速度
    //0x508 电机复位工作速度
    public Object[] setIntToTwoBytes(int addr, int writeInt) {
        int value = 0;
        byte[] buff = new byte[2];
        value = writeInt;
        //buff = intToBytes2(value);
        buff[1] = (byte) ((value >> 8) & 0xFF);
        buff[0] = (byte) (value & 0xFF);
        return cmdLampWriteEeprom(addr, buff, 2);
    }

    public int getTwoBytesToInt(int addr) {
        int value = 0;
        byte[] buff = null;
        Object[] tempObjects = null;
        tempObjects = cmdLampReadEeprom(addr, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        else {
            buff = (byte[]) tempObjects[2];
            //value = Byte2TypeUtils.byteArray2int(buff);
            LogUtils.d("buff=" + buff);
            //value = bytesToInt(buff,0);
            value = (int) (buff[0] & 0xFF) | ((buff[1] & 0xFF) << 8);
            LogUtils.d("value=" + value);
            return value;
        }
    }

    public int getNormalPulse() {
        return getTwoBytesToInt(0x510);
    }

    public int getNormalSpeed() {
        return getTwoBytesToInt(0x506);
    }

    public int getResetSpeed() {
        return getTwoBytesToInt(0x508);
    }

    public Object[] process_user_cmd(byte[] cmdstr, int length) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[3];
        byte[] buff = null;
        int retlength = 0;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 500;
        synchronized (lock) {
            cmdState = -1;
            sendlength = length;

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if ((revbuff[1] | 0x80) != 0) {
                        LogUtils.d(name + " sucess");
                        retlength = revlength;
                        cmdState = revbuff[revlength - 2];
                        buff = new byte[revlength];
                        System.arraycopy(revbuff, 0, buff, 0, revlength);
                        sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        tempObjects[1] = revlength;
                        tempObjects[2] = buff;
                        return updataState(tempObjects);
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                        //emit sendStateCode(COMMU_INSERR);
                        //sendStateCodeEvent(COMMU_INSERR);
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );
            sendStateCodeEvent(COMMU_ERR);
            tempObjects[0] = ret;
            tempObjects[1] = revlength;
            tempObjects[2] = buff;
            return updataState(tempObjects);
        }
    }

    public Object[] setDeviceSerialNo(String deviceno) {
        char[] chars = new char[100];
        byte[] bytes = new byte[100];
        int len = 10;
        for (int i = deviceno.length(); i <= len; i++) {
            deviceno += " ";
        }
        deviceno.getChars(0, 10, chars, 0);
        for (int i = 0; i <= len; i++) {
            bytes[i] = (byte) chars[i];
        }

        Object[] tempObjects = null;
        tempObjects = McuSerial.getInstance().cmdLampWriteEeprom(addr_deviceno, bytes, 10);
        return updataState(tempObjects);
    }

    public String getDeviceSerialNo() {
        String temp = "";
        char[] chars = new char[100];
        Object[] tempObjects = null;
        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_deviceno, 10);
        if ((int) tempObjects[1] != 0) {
            return "";
        }
        for (int i = 0; i < 10; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        temp = String.valueOf(chars, 0, 10);
        return temp;
    }

    public void setStartupBootOrApp(int setbit) {
        setIntToFourBytes(0x009C, setbit);
    }

    public int getStartupBootOrApp() {
        return getFourBytesToInt(0x009C);
    }

////////////////////////////////////////////////////////

    public Object[] setIntToFourBytes(int addr, int writeInt) {
        int value = 0;
        byte[] buff = new byte[4];
        value = writeInt;
        //buff = Byte2TypeUtils.int2byteArray(value);
        buff = intToBytes2(value);
//        buff[0]= (byte) (value>>24);
//        buff[1]= (byte) (value>>16);
//        buff[2]= (byte) (value>>8);
//        buff[3]= (byte) value;
        return cmdLampWriteEeprom(addr, buff, 4);
    }

    public int getFourBytesToInt(int addr) {
        int value = 0;
        byte[] buff = null;
        Object[] tempObjects = null;
        tempObjects = cmdLampReadEeprom(addr, 4);
        if ((int) tempObjects[0] != 1)
            return -1;
        else {
            buff = (byte[]) tempObjects[1];
            //value = Byte2TypeUtils.byteArray2int(buff);
            LogUtils.d("buff=" + buff);
            value = bytesToInt(buff, 0);
            LogUtils.d("value=" + value);
//            value=buff[3];
//            value=(value<<8)|buff[2];
//            value=(value<<8)|buff[1];
//            value=(value<<8)|buff[0];
            return value;
        }
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset + 3] & 0xFF)
                | ((src[offset + 2] & 0xFF) << 8)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset] & 0xFF) << 24));
        return value;
    }


    public void setFloatToFourBytes(int addr, float writeInt) {
        float value = 0;
        byte[] buff = new byte[4];
        value = writeInt;
        Byte2TypeUtils.putFloat(buff, value, 0);
        cmdLampWriteEeprom(addr, buff, 4);
    }

    public float getFourBytesToFloat(int addr) {
        float value = 0;
        byte[] buff = null;
        Object[] tempObjects = null;
        tempObjects = cmdLampReadEeprom(addr, 4);
        if ((int) tempObjects[0] != 1) {
            return -1;
        } else {
            buff = (byte[]) tempObjects[1];
            value = Byte2TypeUtils.getFloat(buff, 0);
            return value;
        }
    }

    //AA A0 00 00 09 01 09 C4 83
    public Object[] cmdResetMcu() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        byte[] buff = null;
        Object[] tempObjects = new Object[1];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_RESETMCU;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if ((revbuff[1] == CMD_RESETMCU_RESP)) {
                        LogUtils.d(name + " sucess");
                        buff = new byte[revlength];
                        System.arraycopy(revbuff, 0, buff, 0, revlength);
                        cmdState = revbuff[revlength - 2];
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_RESETMCU_ERR);
            tempObjects[0] = ret;
            return tempObjects;
        }
    }


    public Object[] cmdBootInfo() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int bootVersion = 0;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_BOOTREADVERSION;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_BOOTREADVERSION_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        bootVersion = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                        tempObjects[1] = bootVersion;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_BOOTREADVERSION_ERR);
            tempObjects[0] = ret;
            tempObjects[1] = bootVersion;
            return tempObjects;
        }
    }

    public Object[] cmdBootEraseFlash() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[1];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_BOOTERASEFLASH;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_BOOTERASEFLASH_RESP) {
                        LogUtils.d(name + " sucess");
                        cmdState = revbuff[revlength - 2];
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_BOOTERASEFLASH_ERR);
            tempObjects[0] = ret;
            return tempObjects;
        }
    }

    public Object[] cmdBootProgramFlash(byte[] hexdata, int hexdatalength) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[1];

        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6 + hexdatalength;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_BOOTPROGRAMFLASH;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            System.arraycopy(hexdata, 0, sendbuff, 5, hexdatalength);
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);

            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_BOOTPROGRAMFLASH_RESP) {
                        LogUtils.d(name + " sucess");
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_BOOTPROGRAMFLASH_ERR);
            tempObjects[0] = ret;
            return tempObjects;
        }
    }

    public Object[] cmdBootCRC(int addr, int length, int mycrc) {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[2];
        int crc = 0;
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 16;
            sendbuff[0] = FRAMEHEAD;
            sendbuff[1] = CMD_BOOTCRC;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            sendbuff[5] = (byte) (addr >> 24);
            sendbuff[6] = (byte) (addr >> 16);
            sendbuff[7] = (byte) (addr >> 8);
            sendbuff[8] = (byte) addr;
            sendbuff[9] = (byte) (length >> 24);
            sendbuff[10] = (byte) (length >> 16);
            sendbuff[11] = (byte) (length >> 8);
            sendbuff[12] = (byte) length;
            sendbuff[13] = (byte) (mycrc >> 8);
            sendbuff[14] = (byte) mycrc;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);
            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_BOOTCRC_RESP) {
                        LogUtils.d(name + " sucess");
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        crc = (int) ((revbuff[6] & 0xFF) | ((revbuff[5] & 0xFF) << 8));
                        tempObjects[1] = crc;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_BOOTCRC_ERR);
            tempObjects[0] = ret;
            tempObjects[1] = crc;
            return tempObjects;
        }
    }

    public Object[] cmdJmpToApp() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Object[] tempObjects = new Object[1];
        int ret = -EBUSY, j = 0;
        int retrycnt = 3;
        int timeout = 1000;
        synchronized (lock) {
            cmdState = -1;
            sendlength = 6;
            sendbuff[0] = (byte) 0xAA;
            sendbuff[1] = CMD_JMPTOAPP;
            sendbuff[2] = 0;
            sendbuff[3] = (byte) (sendlength >> 8);
            sendbuff[4] = (byte) sendlength;
            sendbuff[sendlength - 1] = sumrc(sendbuff, sendlength - 1);
            do {
                revlength = 65535;
                revlength = sendAndRev(sendbuff, sendlength, revlength, timeout);
                revlength = cmdPrev(revbuff, revlength);
                if (revlength > 0) {
                    if (revbuff[1] == CMD_JMPTOAPP_RESP) {
                        LogUtils.d(name + " sucess");
                        //sendStateCodeEvent(COMMU_SUCESS);
                        ret = 1;
                        tempObjects[0] = ret;
                        return tempObjects;
                    } else {
                        LogUtils.d("cmd ins wrong:" + String.valueOf(revbuff[1]));
                    }
                }
                try {
                    Thread.sleep(2000);
                    LogUtils.d(name + "  " + String.valueOf(j + 1) + "  times err!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ( (++j) < retrycnt );

            sendStateCodeEvent(CMD_JMPTOAPP_ERR);
            tempObjects[0] = ret;
            return tempObjects;
        }
    }

    void sendStateCodeEvent(int codeErr) {
        StateCodeEvent se = new StateCodeEvent();
        se.setCodeState(codeErr);
        EventBus.getDefault().post(se);
    }
}
