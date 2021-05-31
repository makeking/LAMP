package com.bete.lamp.thread;

import android.os.SystemClock;

import com.bete.lamp.message.SelfCheckProcessValueEvent;
import com.bete.lamp.message.StateCodeEvent;
import com.utils.Byte2TypeUtils;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import static android.system.OsConstants.EBUSY;
import static com.myutils.GlobalDate.*;
import static com.myutils.GlobalDate.DefaultMinCheckTime;
import static com.myutils.GlobalDate.MinCheckTime;
import static com.myutils.GlobalDate.g_device_gangnum;
import static com.myutils.GlobalDate.g_device_guang_ables;
import static com.myutils.GlobalDate.g_guang_name1s;
import static com.myutils.GlobalDate.g_guang_name2s;
import static com.myutils.GlobalDate.g_guang_name3s;
import static com.myutils.GlobalDate.g_guang_name4s;
import static com.myutils.GlobalDate.xinhaohigh;
import static java.lang.StrictMath.abs;

public class SelfCheckThread extends Thread {
    public static boolean[] selfwenkongflag = new boolean[]{true,true,true,true};
    public static boolean[] selfguangluflag = new boolean[]{true,true,true,true};
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;
//    McuSerial mcuSerial = McuSerial.getInstance();

    int process_i = 0;

    public SelfCheckThread() {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
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

    public void run() {
        int ret = -EBUSY, stop = 0;
        startTimeThread();
        while ( !mFinished ) {
            synchronized (mPauseLock) {
                while ( mPaused ) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (DEVICE) {
                ret = checkTongXin();
                if (ret != 0) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(101);
                        process_i = 101;
                        return;
                    }
                } else {
                    while ( process_i != 25 ) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i);
                        process_i = 26;
                    }
                }
                ret = checkYunDong();
                if (ret != 0) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(102);
                        process_i = 102;
                        return;
                    }
                } else {
                    while ( process_i != 50 ) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i);
                        process_i = 51;
                    }
                }
                ret = checkGuangLu();
//            mcuSerial.cmdSetPCRMOTOR(0x19, 0, 0, 0);
                if (ret != 0) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(103);
                        process_i = 103;
                        return;
                    }
                } else {
                    while ( process_i != 75 ) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i);
                        process_i = 76;
                    }
                }
                if ((selfwenjiangvalue > 0) && (selfwenjiangvalue < 120)) {
                    ret = checkWenKong();
                } else {
                    ret = checkWenKongOld();
                }
                if (ret != 0) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(104);
                        process_i = 104;
                        return;
                    }
                } else {
                    while ( process_i != 100 ) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i);
                    }
                }
            } else {
                while ( process_i != 25 ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (lock) {
                    sendSelfCheckProcessValueEvent(process_i);
                    process_i = 26;
                }

                while ( process_i != 50 ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (lock) {
                    sendSelfCheckProcessValueEvent(process_i);
                    process_i = 51;
                }

                while ( process_i != 75 ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (lock) {
                    sendSelfCheckProcessValueEvent(process_i);
                    process_i = 76;
                }

                while ( process_i != 100 ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (lock) {
                    sendSelfCheckProcessValueEvent(process_i);
                    process_i = 100;
                }
            }
            return;
        }
    }


    int readEeprom() {
        char[] chars = new char[100];
        Object[] tempObjects = null;
        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_wenshengspeed, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        wenshengspeed = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("wenshengspeed:" + wenshengspeed);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_wenjiangspeed, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        wenjiangspeed = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("wenjiangspeed:" + wenjiangspeed);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_hardwareversion, 16);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < 16; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        devicehardwareversion = String.valueOf(chars, 0, 16);
        LogUtils.d("devicehardwareversion:" + devicehardwareversion);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_deviceoutdate, 16);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < 16; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        deviceoutdate = String.valueOf(chars, 0, 16);
        LogUtils.d("deviceoutdate:" + deviceoutdate);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_deviceno, 16);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < 16; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        deviceno = String.valueOf(chars, 0, 16);
        LogUtils.d("DeviceSerialNo:" + deviceno);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_hardversion, 16);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < 16; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        devicehardversion = String.valueOf(chars, 0, 16);
        LogUtils.d("devicehardversion:" + devicehardversion);


        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_jiaozhun, 64);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                g_jiaozhun[i][j] = Byte2TypeUtils.getFloat(((byte[]) tempObjects[2]), 4 * (4 * i + j));
            }

        tempObjects =McuSerial.getInstance().cmdLampReadEeprom(addr_minchecktime, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        MinCheckTime = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        if((MinCheckTime>50)||(MinCheckTime<0))
        {
            MinCheckTime = DefaultMinCheckTime;
        }

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_gangnumable, 6);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        int guangnum = 0;
        for (int i = 0; i < GUANGNUM; i++) {
            if (((byte[]) tempObjects[2])[i] == 0x31) {
                guangnum++;
                g_device_guang_ables[i] = true;
            } else {
                g_device_guang_ables[i] = false;
            }
        }
        g_device_gangnum = guangnum;

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_gangnames, addr_gangnames_len);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < addr_gangnames_len; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        String guang1 = String.valueOf(chars, 0, addr_gangnames_len);

        String[] name_array1 = guang1.split(",");
        if (name_array1.length > 0) {
            g_guang_name1s.clear();
            for (int j = 0; j < name_array1.length; j++) {
                g_guang_name1s.add(name_array1[j].replace(" ", ""));
            }
        }

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_gangnames+addr_gangnames_len*1, addr_gangnames_len);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < addr_gangnames_len; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }

        String guang2 = String.valueOf(chars, 0, addr_gangnames_len);
        String[] name_array2 = guang2.split(",");
        if (name_array2.length > 0) {
            g_guang_name2s.clear();
            for (int j = 0; j < name_array2.length; j++) {
                g_guang_name2s.add(name_array2[j].replace(" ", ""));
            }
        }

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_gangnames+addr_gangnames_len*2, addr_gangnames_len);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < addr_gangnames_len; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        String guang3 = String.valueOf(chars, 0, addr_gangnames_len);
        String[] name_array3 = guang3.split(",");
        if (name_array3.length > 0) {
            g_guang_name3s.clear();
            for (int j = 0; j < name_array3.length; j++) {
                g_guang_name3s.add(name_array3[j].replace(" ", ""));
            }
        }


        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_gangnames+addr_gangnames_len*3, addr_gangnames_len);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        for (int i = 0; i < addr_gangnames_len; i++) {
            chars[i] = (char) ((byte[]) tempObjects[2])[i];
        }
        String guang4 = String.valueOf(chars, 0, addr_gangnames_len);
        String[] name_array4 = guang4.split(",");
        if (name_array4.length > 0) {
            g_guang_name4s.clear();
            for (int j = 0; j < name_array4.length; j++) {
                g_guang_name4s.add(name_array4[j].replace(" ", ""));
            }
        }

        return 0;
    }

    int getEeprom() {
        Object[] tempObjects = null;
        for (int i = 0; i < 4; i++) {
            tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_xinhaoabs[i], 2);
            if ((int) tempObjects[1] != 0) {
                return (int) tempObjects[0];
            }
            xinhaoabs[i] = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
            LogUtils.d("xinhaoabs[" + i + "]:" + xinhaoabs[i]);

            tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_xinhaohigh[i], 2);
            if ((int) tempObjects[1] != 0) {
                return (int) tempObjects[0];
            }
            xinhaohigh[i] = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
            LogUtils.d("xinhaohigh[" + i + "]:" + xinhaohigh[i]);

            tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_xinhaolow[i], 2);
            if ((int) tempObjects[1] != 0) {
                return (int) tempObjects[0];
            }
            xinhaolow[i] = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
            LogUtils.d("xinhaolow[" + i + "]:" + xinhaolow[i]);
        }

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_wenduabs, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        wenduabs = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("wenduabs:" + wenduabs);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_wenduwaittime, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        wenduwaittime = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("wenduwaittime:" + wenduwaittime);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_wenduself, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        wenduself = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("wenduself:" + wenduself);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenduthres, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenduthres = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenduthres:" + selfwenduthres);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenshengtime, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenshengtime = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenshengtime:" + selfwenshengtime);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenshengvalue, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenshengvalue = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenshengvalue:" + selfwenshengvalue);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenshengabs, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenshengabs = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenshengabs:" + selfwenshengabs);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenjiangtime, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenjiangtime = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenjiangtime:" + selfwenjiangtime);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenjiangvalue, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenjiangvalue = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenjiangvalue:" + selfwenjiangvalue);

        tempObjects = McuSerial.getInstance().cmdLampReadEeprom(addr_selfwenjiangabs, 2);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        selfwenjiangabs = (int) (((((byte[]) tempObjects[2])[1] & 0xff) << 8) | (((byte[]) tempObjects[2])[0] & 0xff));
        LogUtils.d("selfwenjiangabs:" + selfwenjiangabs);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return 0;
    }

    int checkTongXin()//int no,int value,int wucha
    {
        int ret = -1;
        int count_i = 0;
        Object[] tempObjects = null;

        tempObjects = McuSerial.getInstance().cmdHello();
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];
        ret = getEeprom();
        if (ret == 0)
            ret = readEeprom();
        return ret;
    }

    int checkYunDong()//int no,int value,int wucha
    {
        Object[] tempObjects = null;
        int count_i = 0;
        sendSelfCheckProcessValueEvent(process_i);
        tempObjects = McuSerial.getInstance().cmdLampSetPCRMOTOR(0x13, 14000, 1, g_ResetSpeed);//复位
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];
        return 0;
    }

    int checkGuangLuOld()//int no,int value,int wucha
    {
        int ret = 0;
        int count_i = 0;
        byte[] buff = new byte[8 * 2 + 6];
        int guandengxinhao[] = new int[4];
        int value = 0;
        Object[] tempObjects = null;
        tempObjects = McuSerial.getInstance().cmdLampSetPCRMOTOR(0x10, 6000, 1, 10000);//复位
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];

        tempObjects = McuSerial.getInstance().cmdLampSelfLed(false);
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];
        tempObjects = McuSerial.getInstance().cmdLampReadAD();
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];
        int templength = (int) (tempObjects[2]);
        if (templength > 8 * 2 + 6)
            templength = 8 * 2 + 6;
        System.arraycopy((byte[]) (tempObjects[3]), 0, buff, 0, templength);

        for (int z = 0; z < 4; z++) {
            guandengxinhao[z] = (int) (((buff[4 + 2 * z] & 0xFF) << 8) | ((buff[5 + 2 * z] & 0xFF)));
            LogUtils.v("guandengxinhao[" + z + "]:" + guandengxinhao[z]);
            if (guandengxinhao[z] > xinhaolow[z])
                return -1;
        }

        tempObjects = McuSerial.getInstance().cmdLampSelfLed(true);
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];
        tempObjects = McuSerial.getInstance().cmdLampReadAD();
        McuSerial.getInstance().cmdLampSelfLed(false);
        if ((int) tempObjects[1] != 0) {
            return (int) tempObjects[0];
        }
        templength = (int) (tempObjects[2]);
        if (templength > 8 * 2 + 6)
            templength = 8 * 2 + 6;
        System.arraycopy((byte[]) (tempObjects[3]), 0, buff, 0, templength);
        for (int z = 0; z < 4; z++) {
            value = (int) (((buff[4 + 2 * z] & 0xFF) << 8) | ((buff[5 + 2 * z] & 0xFF)));
            LogUtils.v("value[" + z + "]:" + value);
            if (abs(value - guandengxinhao[z]) < xinhaoabs[z]) {
                return -1;
            }
        }

        tempObjects = McuSerial.getInstance().cmdLampSetPCRMOTOR(0x13, 14000, 1, g_ResetSpeed);//复位
        if ((int) tempObjects[1] != 0)
            return (int) tempObjects[0];

        return ret;
    }

    int checkGuangLu()//int no,int value,int wucha
    {
        int ret = 0;
        int count_i = 0;
        byte[] buff = new byte[BLOCKNUM * BLOCKSIZE * 2 + 6];
        int sum = 0, pingjun = 0;
        Object[] tempObjects = null;
        tempObjects = McuSerial.getInstance().cmdLampSetPCRMOTOR(0x15, g_NormalPulse, 1, g_NormalSpeed);
        if ((int) tempObjects[1] != 0) {
        }
        for (int j = 0; j < 4; j++) {
            if (j == 0)
                tempObjects = McuSerial.getInstance().cmdLampReadPCRFeng(1);
            else if (j == 1)
                tempObjects = McuSerial.getInstance().cmdLampReadPCRFeng(3);
            else if (j == 2)
                tempObjects = McuSerial.getInstance().cmdLampReadPCRFeng(5);
            else
                tempObjects = McuSerial.getInstance().cmdLampReadPCRFeng(2);
            if ((int) tempObjects[1] != 0) {
            }
            int templength = (int) (tempObjects[2]);
            if (templength > BLOCKNUM * BLOCKSIZE * 2 + 6)
                templength = BLOCKNUM * BLOCKSIZE * 2 + 6;
            System.arraycopy((byte[]) (tempObjects[3]), 0, buff, 0, templength);
            sum = 0;
            pingjun = 0;
            for (int z = 0; z < BLOCKNUM * BLOCKSIZE; z++) {
                sum += (int) ((buff[4 + 2 * z] & 0xFF) | ((buff[5 + 2 * z] & 0xFF) << 8));
            }
            pingjun = sum / (BLOCKNUM * BLOCKSIZE);
            LogUtils.v("pingjun:" + pingjun);
            if ((pingjun < xinhaolow[j]) || (pingjun > xinhaohigh[j])) {
                ret = -1;
                break;
            }
        }
        tempObjects = McuSerial.getInstance().cmdLampSetPCRMOTOR(0x13, g_NormalPulse, 0, g_NormalSpeed);
        if ((int) tempObjects[1] != 0) {
        }
        return ret;
    }

    int checkWenKongOld()//int value,int wuca,int waittimeout,int holdtimeout
    {
        int i = 0;
        int count_i = 0;
        int value1[] = new int[4];
        Object[] tempObjects = null;

        tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduself, wenduself, wenduself, wenduself);
        if ((int) tempObjects[1] != 0)
            return -1;
        int waittime = (wenduwaittime > 60) ? 60 : wenduwaittime;
        i = 0;
        do {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while ( (++i) < waittime );//wenduwaittime
        LogUtils.v("wenduabs:" + wenduabs);
        tempObjects = McuSerial.getInstance().cmdLampGetALLTEMP();
        if ((int) tempObjects[1] != 0) {
            tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
            return -1;
        }
        for (int z = 0; z < 4; z++)
            if (abs(((int[]) tempObjects[2])[z] - wenduself) > wenduabs) {
                LogUtils.d("tempValue:" + ((int[]) tempObjects[2])[z]);
                //tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal,wenduselffinal,wenduselffinal,wenduselffinal);
                tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
                return -1;
            }
        //tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal,wenduselffinal,wenduselffinal,wenduselffinal);
        tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
        if ((int) tempObjects[1] != 0)
            return -1;
        return 0;
    }

    int checkWenKong()//int value,int wuca,int waittimeout,int holdtimeout
    {
        int i = 0;
        int count_i = 0;
        int[] value_origns = new int[BLOCKNUM];
        int[] value_settings = new int[BLOCKNUM];
        int[] value_absmins = new int[BLOCKNUM];
        int[] value_absmaxs = new int[BLOCKNUM];
        boolean[] wenkong = new boolean[]{true,true,true,true};
        int waittingtime = 0;
        int valuetemp, value3;
        Object[] tempObjects = null;

        tempObjects = McuSerial.getInstance().cmdLampGetALLTEMP();
        if ((int) tempObjects[1] != 0) {
            McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
            return (int) tempObjects[0];
        }
        for (int z = 0; z < BLOCKNUM; z++) {
            value_origns[z] = ((int[]) tempObjects[2])[z];
            wenkong[z] = false;
            //降温
            if (value_origns[z] > selfwenduthres * 100) {
                value_settings[z] = value_origns[z] - selfwenjiangvalue * 100;
                value_absmins[z] = value_settings[z] - selfwenjiangabs*100;
                value_absmaxs[z] = value_settings[z] + selfwenjiangabs*100;
                waittingtime = Math.max(waittingtime, selfwenjiangtime);
            } else//升温
            {
                value_settings[z] = value_origns[z] + selfwenshengvalue * 100;
                value_absmins[z] = value_settings[z] - selfwenshengabs*100;
                value_absmaxs[z] = value_settings[z] + selfwenshengabs*100;
                waittingtime = Math.max(waittingtime, selfwenshengtime);
            }
        }

        tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(value_settings[0], value_settings[1], value_settings[2], value_settings[3]);
        if ((int) tempObjects[1] != 0) {
            McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
            return (int) tempObjects[0];
        }
        i = 0;
        do {
            SystemClock.sleep(1000);
            tempObjects = McuSerial.getInstance().cmdLampGetALLTEMP();
            if ((int) tempObjects[1] != 0) {
                McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
                return (int) tempObjects[1];
            }
            for (int z = 0; z < BLOCKNUM; z++) {
                if ((((int[]) tempObjects[2])[z] > value_absmins[z]) && ((((int[]) tempObjects[2])[z]) < value_absmaxs[z])) {
                    wenkong[z] = true;
                }
            }
        } while ( (++i) < waittingtime );
        tempObjects = McuSerial.getInstance().cmdLampSetALLTEMP(wenduselffinal, wenduselffinal, wenduselffinal, wenduselffinal);
        selfwenkongflag[0]=wenkong[0];
        selfwenkongflag[1]=wenkong[1];
        selfwenkongflag[2]=wenkong[2];
        selfwenkongflag[3]=wenkong[3];
        if (wenkong[0] || wenkong[1] || wenkong[2] || wenkong[3]) {
            if ((int) tempObjects[1] != 0)
                return (int) tempObjects[0];
            return 0;
        } else {
            return -1;
        }
    }

    void sendSelfCheckProcessValueEvent(int processValue) {
        SelfCheckProcessValueEvent se = new SelfCheckProcessValueEvent();
        se.setProcessValue(processValue);
        EventBus.getDefault().post(se);
    }

    void sendStateCodeEvent(int codeErr) {
        StateCodeEvent se = new StateCodeEvent();
        se.setCodeState(codeErr);
        EventBus.getDefault().post(se);
    }

    private TimeThread timeThread;

    void startTimeThread() {
        if (timeThread != null)
            timeThread.myStop();
        timeThread = new TimeThread();
        timeThread.start();
    }

    static private Object lock = new Object();

    private class TimeThread extends Thread {
        private boolean mFinished = false;
        private boolean myPause = false;

        public void myStop() {
            mFinished = true;
        }

        public void myPause() {
            myPause = true;
        }

        @Override
        public void run() {
            while ( process_i < 100 ) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (process_i < 25) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i++);
                    }
                } else if ((process_i > 25) && (process_i < 50)) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i++);
                    }
                } else if ((process_i > 50) && (process_i < 75)) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i++);
                    }
                } else if ((process_i > 75) && (process_i < 100)) {
                    synchronized (lock) {
                        sendSelfCheckProcessValueEvent(process_i++);
                    }
                } else {
                    continue;
                }
            }
        }
    }
}
