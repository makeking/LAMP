package com.myutils;

import static com.myutils.Command.CMD_BOOTCRC;
import static com.myutils.Command.CMD_BOOTERASEFLASH;
import static com.myutils.Command.CMD_BOOTPROGRAMFLASH;
import static com.myutils.Command.CMD_BOOTREADVERSION;
import static com.myutils.Command.CMD_HELLO;
import static com.myutils.Command.CMD_JMPTOAPP;
import static com.myutils.Command.CMD_LAMPGETALLTEMP;
import static com.myutils.Command.CMD_LAMPGETCAPTEMP;
import static com.myutils.Command.CMD_LAMPGETTEMP;
import static com.myutils.Command.CMD_LAMPLEDRUN;
import static com.myutils.Command.CMD_LAMPREADAD;
import static com.myutils.Command.CMD_LAMPREADEEPROM;
import static com.myutils.Command.CMD_LAMPREADPCRFENG;
import static com.myutils.Command.CMD_LAMPSETALLTEMP;
import static com.myutils.Command.CMD_LAMPSETCAPTEMP;
import static com.myutils.Command.CMD_LAMPSETMPPC;
import static com.myutils.Command.CMD_LAMPSETPCRMOTOR;
import static com.myutils.Command.CMD_LAMPSETPCRTEMP;
import static com.myutils.Command.CMD_LAMPWRITEEEPROM;
import static com.myutils.Command.CMD_RESETMCU;

public class StateCode {
    public static final int CMD_LAMPSETMPPC_ERR =  0x10006|((int) CMD_LAMPSETMPPC<<8);
    public static final int CMD_LAMPLEDRUN_ERR =  0x10006|((int) CMD_LAMPLEDRUN<<8);
    public static final int CMD_LAMPSETPCRMOTOR_ERR =  0x10006|((int) CMD_LAMPSETPCRMOTOR<<8);
    public static final int CMD_LAMPREADEEPROM_ERR =  0x10006|((int)CMD_LAMPREADEEPROM<<8);
    public static final int CMD_LAMPWRITEEEPROM_ERR =  0x10006|((int) CMD_LAMPWRITEEEPROM<<8);
    public static final int CMD_LAMPGETTEMP_ERR =  0x10006|((int)CMD_LAMPGETTEMP<<8);
    public static final int CMD_LAMPSETPCRTEMP_ERR =  0x10006|((int) CMD_LAMPSETPCRTEMP<<8);
    public static final int CMD_LAMPREADPCRFENG_ERR =  0x10006|((int) CMD_LAMPREADPCRFENG<<8);
    public static final int CMD_LAMPREADAD_ERR =  0x10006|((int) CMD_LAMPREADAD<<8);
    public static final int CMD_LAMPGETALLTEMP_ERR =  0x10006|((int) CMD_LAMPGETALLTEMP<<8);
    public static final int CMD_LAMPSETALLTEMP_ERR =  0x10006|((int) CMD_LAMPSETALLTEMP<<8);
    public static final int CMD_LAMPGETCAPTEMP_ERR =  0x10006|((int) CMD_LAMPGETCAPTEMP<<8);
    public static final int CMD_LAMPSETCAPTEMP_ERR =  0x10006|((int) CMD_LAMPSETCAPTEMP<<8);

    public static final int COMMU_SUCESS    =   0x10000;
    public static final int COMMU_TIMEOUT   =   0x10001;
    public static final int COMMU_LENLT     =   0x10002;
    public static final int COMMU_BCCERR    =   0x10003;
    public static final int COMMU_INSERR    =   0x10004;
    public static final int COMMU_LENNOMATCH =  0x10005;
    public static final int COMMU_ERR =  0x10006;
    public static final int CMD_HELLO_ERR =  0x10006|((int)CMD_HELLO<<8);

    public static final int CMD_RESETMCU_ERR = 0x10006|((int)CMD_RESETMCU<<8);
    public static final int CMD_BOOTREADVERSION_ERR = 0x10006|((int)CMD_BOOTREADVERSION<<8);
    public static final int CMD_BOOTERASEFLASH_ERR = 0x10006|((int)CMD_BOOTERASEFLASH<<8);
    public static final int CMD_BOOTPROGRAMFLASH_ERR = 0x10006|((int)CMD_BOOTPROGRAMFLASH<<8);
    public static final int CMD_BOOTCRC_ERR = 0x10006|((int)CMD_BOOTCRC<<8);
    public static final int CMD_JMPTOAPP_ERR = 0x10006|((int)CMD_JMPTOAPP<<8);

    public static final int PRINT_SUCESS    =   0x20000;
    public static final int PRINT_TIMEOUT   =   0x20001;
    public static final int PRINT_LENLT     =   0x20002;
    public static final int PRINT_BCCERR    =   0x20003;
    public static final int PRINT_INSERR    =   0x20004;
    public static final int PRINT_LENNOMATCH =  0x20005;
    public static final int SCAN_SUCESS     =   0x30000;
    public static final int SCAN_BCCERR     =   0x30001;
    public static final int SCAN_BARTYPEERR =   0x30002;
    public static final int SCAN_ITEMREPEAT =   0x30003;
    public static final int SCAN_ITEMONIMPORT = 0x30004;
    public static final int SCAN_KONGWEIERR =   0x30005;
    public static final int SCAN_LIMITERR   =   0x30006;
    public static final int SCAN_TIMEERR    =   0x30007;
    public static final int SCAN_RIQIERR    =   0x30008;

    public static final int TEMP_SUCESS     =   0x40000;
    public static final int TEMP_ERR        =   0x40001;
    public static final int TEMP_OUTMAXERR  =   0x40002;
    public static final int TEMP_OUTMINERR  =   0x40003;
    public static final int TEMP_GETFAIL    =   0x40004;
    public static final int TEMP_ARRIVEHIGHERR= 0x40005;
    public static final int TEMP_ARRIVELOWERR=  0x40006;
    public static final int TEMP_HOLDHIGHERR=   0x40007;
    public static final int TEMP_HOLDLOWERR =   0x40008;

    public static final int MOTOR_SUCESS    =   0x50000;
    public static final int MOTOR_ERR       =   0x50001;
    public static final int MOTOR_ARRIVEHIGHERR=    0x50002;
    public static final int MOTOR_ARRIVELOWERR= 0x50003;
    public static final int MOTOR_HOLDHIGHERR=  0x50004;
    public static final int MOTOR_HOLDLOWERR=   0x50005;
    public static final int MOTOR_DIRERR    =   0x50006;

    public static final int QC_SUCESS       =   0x70000;
    public static final int QC_ERR          =   0x70001;

    public static final int LED_SUCESS      =   0x60000;
    public static final int LED_ERR         =   0x60001;
    public static final int LED_HIGHERR     =   0x60002;
    public static final int LED_LOWERR      =   0x60003;

    public static final int ZHIKONG_PAN_OK    =   0x80001;
    public static final int ZHIKONG_SAMPLE_OK    =   0x80002;
    public static final int ZHIKONG_XISIYE_OK    =   0x80003;
    public static final int ZHIKONG_HUNHEYE_OK    =   0x80004;
    public static final int ZHIKONG_SHIJIQIU_OK    =   0x80005;
    public static final int ZHIKONG_RONGXUE_OK    =   0x80006;
    public static final int ZHIKONG_ZHIXUE_OK    =   0x80007;
    public static final int ZHIKONG_HUANGDAN_OK    =   0x80008;

    public static final int ZHIKONG_PAN_ERR    =   0x81001;
    public static final int ZHIKONG_SAMPLE_ERR    =   0x81002;
    public static final int ZHIKONG_XISIYE_ERR    =   0x81003;
    public static final int ZHIKONG_HUNHEYE_ERR    =   0x81004;
    public static final int ZHIKONG_SHIJIQIU_ERR    =   0x81005;
    public static final int ZHIKONG_RONGXUE_ERR    =   0x81006;
    public static final int ZHIKONG_ZHIXUE_ERR    =   0x81007;
    public static final int ZHIKONG_HUANGDAN_ERR    =   0x81008;


    public static final int SHIYAN_LEDSTATE_ERR    =   0x91009;
    public static final int SHIYAN_SETMPPC_ERR     =    0x9100A;
    public static final int SHIYAN_GETTEMP_ERR     =    0x9100B;
    public static final int SHIYAN_PCRMOTOR_ERR     =    0x9100C;
    public static final int SHIYAN_READPCRFENG_ERR     =    0x9100D;


}
