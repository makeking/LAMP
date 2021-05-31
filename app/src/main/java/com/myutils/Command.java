package com.myutils;

public class Command {
    /* INTERFACE CMD ID */
    public static final byte FRAMEHEAD = 0x5E;
    public static final byte CMD_HELLO = 0x01;
    public static final byte CMD_HELLO_RESP = (byte) (CMD_HELLO|0x80);


////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
    /*lamp*/
    public static final byte CMD_LAMPSETMPPC = 0x0D;
    public static final byte CMD_LAMPLEDRUN = 0x08;
    public static final byte CMD_LAMPSETPCRMOTOR = 0x07;
    public static final byte CMD_LAMPREADEEPROM = (byte) 0x11;
    public static final byte CMD_LAMPWRITEEEPROM = (byte) 0x12;
    public static final byte CMD_LAMPGETTEMP = (byte) 0x09;
    public static final byte CMD_LAMPSETPCRTEMP = 0x06;
    public static final byte CMD_LAMPREADPCRFENG = (byte) 0x0C;
    public static final byte CMD_LAMPREADAD = (byte) 0x17;
    public static final byte CMD_LAMPGETALLTEMP = (byte) 0x70;
    public static final byte CMD_LAMPSETALLTEMP = (byte) 0x71;
    public static final byte CMD_LAMPGETCAPTEMP = (byte) 0x72;
    public static final byte CMD_LAMPSETCAPTEMP = (byte) 0x73;
    /*lamp*/
    public static final byte CMD_LAMPSETMPPC_RESP = (byte) (CMD_LAMPSETMPPC|0x80);
    public static final byte CMD_LAMPLEDRUN_RESP = (byte) (CMD_LAMPLEDRUN|0x80);
    public static final byte CMD_LAMPREADEEPROM_RESP = (byte) (CMD_LAMPREADEEPROM|0x80);
    public static final byte CMD_LAMPWRITEEEPROM_RESP = (byte) (CMD_LAMPWRITEEEPROM|0x80);
    public static final byte CMD_LAMPGETTEMP_RESP = (byte) (CMD_LAMPGETTEMP|0x80);
    public static final byte CMD_SETPCRTEMP_RESP = (byte) (CMD_LAMPSETPCRTEMP|0x80);
    public static final byte CMD_LAMPSETPCRMOTOR_RESP = (byte) (CMD_LAMPSETPCRMOTOR|0x80);
    public static final byte CMD_LAMPSETPCRTEMP_RESP = (byte) (CMD_LAMPSETPCRTEMP|0x80);
    public static final byte CMD_LAMPREADPCRFENG_RESP = (byte) (CMD_LAMPREADPCRFENG|0x80);
    public static final byte CMD_LAMPREADAD_RESP = (byte) (CMD_LAMPREADAD|0x80);
    public static final byte CMD_LAMPGETALLTEMP_RESP = (byte) (CMD_LAMPGETALLTEMP|0x80);
    public static final byte CMD_LAMPSETALLTEMP_RESP = (byte) (CMD_LAMPSETALLTEMP|0x80);
    public static final byte CMD_LAMPGETCAPTEMP_RESP = (byte) (CMD_LAMPGETCAPTEMP|0x80);
    public static final byte CMD_LAMPSETCAPTEMP_RESP = (byte) (CMD_LAMPSETCAPTEMP|0x80);
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
    /*boot*/
    public static final byte CMD_RESETMCU = (byte)0xf6;
    public static final byte CMD_BOOTREADVERSION = (byte) 0xf8;
    public static final byte CMD_BOOTERASEFLASH = (byte) 0Xf9;
    public static final byte CMD_BOOTPROGRAMFLASH = (byte)0xfa;
    public static final byte CMD_BOOTCRC = (byte) 0xFb;
    public static final byte CMD_JMPTOAPP = (byte) 0xfc;
    /*boot*/
    public static final byte CMD_RESETMCU_RESP = (byte)(CMD_RESETMCU|0x80);
    public static final byte CMD_BOOTREADVERSION_RESP = (byte)(CMD_BOOTREADVERSION|0x80);
    public static final byte CMD_BOOTERASEFLASH_RESP = (byte)(CMD_BOOTERASEFLASH|0x80);
    public static final byte CMD_BOOTPROGRAMFLASH_RESP = (byte)(CMD_BOOTPROGRAMFLASH|0x80);
    public static final byte CMD_BOOTCRC_RESP = (byte)(CMD_BOOTCRC|0x80);
    public static final byte CMD_JMPTOAPP_RESP = (byte)(CMD_JMPTOAPP|0x80);
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
    public static final byte PRINTER_SCAN_SWITCH = 9;
    public static final byte POWER_SWITCH = 10;
    public static final byte COMMU_SWITCH = 11;
}