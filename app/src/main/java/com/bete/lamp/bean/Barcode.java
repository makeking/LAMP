package com.bete.lamp.bean;

import com.myutils.GlobalDate;
import com.utils.LogUtils;

public class Barcode {
    static char Char94Table[] = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`~!@#$%^&*()-_=+[{]};:'\"\\|,<.>/?").toCharArray();
    static byte Str94Table[] = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`~!@#$%^&*()-_=+[{]};:'\"\\|,<.>/?").getBytes();
    static int REAGENT_BAR_CODE_BYTE_NUM = 125;  //1个标志位 + 159个字节
    static int REAGENT_94BAR_CODE_NUM = 156;  //199个信息字符 + 1个标志字符‘！’
    static byte REAGENT_BAR_INFO_HEADER = 101;

    /*******************************************************************************
     * 函数名称：Convert94ToHex
     * 功能描述：将94进制的字符转换为数值
     * 输入参数：p 要转换的字符串，num 字符的个数
     * 输出参数：p 将转换后的数值再写入p中
     *******************************************************************************/
    static void Convert94ToHex(byte[] p, int index, int num) {
        int i;
        byte j;
        for (i = 0; i < num; i++) {
            for (j = 0; j < 94; j++) {
                if (p[i + index] == Str94Table[j]) {
                    p[i + index] = j;
                    break;
                }
            }
        }
    }


//    /*******************************************************************************
//     * 功能描述：获取试剂条的简要信息
//     *******************************************************************************/
//    public static BarRareData GetReagentDetailBarCode(byte[] buff) {
//        BarRareData barraredata = new BarRareData();
//        byte[] g_ucBarCodeByteBuffer = new byte[REAGENT_BAR_CODE_BYTE_NUM + 10];//
//        byte[] g_uc94BarCodeBuffer = new byte[REAGENT_94BAR_CODE_NUM];
//        //int i=0;
//        int ucNum = 0;
//        int uiValue = 0;
//        long uliValue = 0;
//
//        System.arraycopy(buff, 0, g_uc94BarCodeBuffer, 0, REAGENT_94BAR_CODE_NUM);
//
//        //hex_dump(g_uc94BarCodeBuffer,REAGENT_94BAR_CODE_NUM);
//        if (g_uc94BarCodeBuffer[0] == '!') //试剂条详细信息
//        {
//            Convert94ToHex(g_uc94BarCodeBuffer, 0, REAGENT_94BAR_CODE_NUM);
//
//            for (int i = 0; ; i++) {
//                ucNum = REAGENT_94BAR_CODE_NUM - i * 5 - 5;//(unsigned char)
//                uliValue = g_uc94BarCodeBuffer[ucNum];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 1];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 2];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 3];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 4];
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 4] = (byte) ((uliValue >> 24) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 3] = (byte) ((uliValue >> 16) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 2] = (byte) ((uliValue >> 8) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 1] = (byte) ((uliValue) & 0xFF);
//                //qDebug()<<uliValue<<endl;
//                if (ucNum <= 5) {
//                    break;
//                }
//            }
//            //memcpy(g_uc94BarCodeBuffer,buff,REAGENT_94BAR_CODE_NUM);
//            //hex_dump(g_uc94BarCodeBuffer,REAGENT_94BAR_CODE_NUM);
//            g_ucBarCodeByteBuffer[0] = REAGENT_BAR_INFO_HEADER; //标志位
//
//            LogUtils.d(g_ucBarCodeByteBuffer, REAGENT_BAR_CODE_BYTE_NUM);
//            if (sumrc(g_ucBarCodeByteBuffer, 1, REAGENT_BAR_CODE_BYTE_NUM - 2) != g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 1]) {
//                LogUtils.d("sumrc err");
//                return null;
//            }
//            int index = 1;
//            barraredata.bartype = g_ucBarCodeByteBuffer[index++]&0xff;
//            barraredata.productyear = (int) (((g_ucBarCodeByteBuffer[index++] & 0xFF) << 8) | (g_ucBarCodeByteBuffer[index++] & 0xFF));
//            barraredata.productmonth = g_ucBarCodeByteBuffer[index++];
//            barraredata.num = (int) (((g_ucBarCodeByteBuffer[index++] & 0xFF) << 8) | (g_ucBarCodeByteBuffer[index++] & 0xFF));
//            barraredata.limityear = (int) (((g_ucBarCodeByteBuffer[index++] & 0xFF) << 8) | (g_ucBarCodeByteBuffer[index++] & 0xFF));
//            barraredata.limitmonth = g_ucBarCodeByteBuffer[index++];
//            barraredata.limitday = g_ucBarCodeByteBuffer[index++];
//
//            for (int i = 0; i < 5; i++) {
//                barraredata.itemstd[i].itemtype = g_ucBarCodeByteBuffer[index + i * 2]&0xff;
//                barraredata.itemstd[i].kongweino = (g_ucBarCodeByteBuffer[index + i * 2 + 1]);
//            }
//            index = index + 10;
//            for (int i = 0; i < 10; i++) {
//                barraredata.itemqc[i].itemtype = g_ucBarCodeByteBuffer[index + i * 5]&0xff;
//                barraredata.itemqc[i].daYuShiNeng = ((g_ucBarCodeByteBuffer[index + i * 5 + 1] >> 7) & 0x01);
//                barraredata.itemqc[i].errHandle = ((g_ucBarCodeByteBuffer[index + i * 5 + 1] >> 5) & 0x03);
//                barraredata.itemqc[i].kongweino0 = (g_ucBarCodeByteBuffer[index + i * 5 + 1] & 0x1F);
//                barraredata.itemqc[i].kongweino1 = (g_ucBarCodeByteBuffer[index + i * 5 + 2] & 0x1F);
//                barraredata.itemqc[i].guangluno = (g_ucBarCodeByteBuffer[index + i * 5 + 3] >> 4);
//                barraredata.itemqc[i].refvalue = (int) ((g_ucBarCodeByteBuffer[index + i * 5 + 3] & 0x0F << 8) | (g_ucBarCodeByteBuffer[index + i * 5 + 4] & 0xFF));
//            }
//            index = index + 50;
//            for (int i = 0; i < 16; i++) {
//                barraredata.itemtest[i].itemtype = g_ucBarCodeByteBuffer[index + i * 15]&0xff;
//                barraredata.itemtest[i].K = (int) ((g_ucBarCodeByteBuffer[index + i * 15 + 1] & 0xFF << 8) | (g_ucBarCodeByteBuffer[index + i * 15 + 2] & 0xFF));
//                barraredata.itemtest[i].B = (int) ((g_ucBarCodeByteBuffer[index + i * 15 + 3] & 0xFF << 8) | (g_ucBarCodeByteBuffer[index + i * 15 + 4] & 0xFF));
//                barraredata.itemtest[i].refvalue0 = (int) ((g_ucBarCodeByteBuffer[index + i * 15 + 5] & 0xFF << 8) | (g_ucBarCodeByteBuffer[index + i * 15 + 6] & 0xFF));
//                barraredata.itemtest[i].refvalue1 = (int) ((g_ucBarCodeByteBuffer[index + i * 15 + 7] & 0xFF << 8) | (g_ucBarCodeByteBuffer[index + i * 15 + 8] & 0xFF));
//
//                barraredata.itemtest[i].fangfa = (g_ucBarCodeByteBuffer[index + i * 15 + 9] >> 3);
//                barraredata.itemtest[i].danwei = (((g_ucBarCodeByteBuffer[index + i * 15 + 9] & 0x07) << 3) | ((g_ucBarCodeByteBuffer[index + i * 15 + 10] >> 5) & 0x07));
//                barraredata.itemtest[i].kongweino = (g_ucBarCodeByteBuffer[index + i * 15 + 10] & 0x1F);
//
//                barraredata.itemtest[i].guangluno = ((g_ucBarCodeByteBuffer[index + i * 15 + 11] >> 4) & 0x0F);
//                barraredata.itemtest[i].ARG0 = (g_ucBarCodeByteBuffer[index + i * 15 + 11] & 0x0F);
//                barraredata.itemtest[i].ARG0 = ((barraredata.itemtest[i].ARG0 << 1) | ((g_ucBarCodeByteBuffer[index + i * 15 + 12] >> 7) & 0x01));
//                barraredata.itemtest[i].ARG1 = (g_ucBarCodeByteBuffer[index + i * 15 + 12] & 0x7F);
//                barraredata.itemtest[i].ARG2 = g_ucBarCodeByteBuffer[index + i * 15 + 13];
//                barraredata.itemtest[i].ARG3 = (g_ucBarCodeByteBuffer[index + i * 15 + 14]);
//            }
//            LogUtils.d("barcode right");
//            return barraredata;
//        } else {
//            LogUtils.d("barcode head not !");
//            return null; //获取失败
//        }
//    }
//
//
//    public static String setReagentDetailBarCodeToBuff(BarRareData barRareData) {//, byte[] buff
//        byte[] g_ucBarCodeByteBuffer = new byte[REAGENT_BAR_CODE_BYTE_NUM - 1];//
//        byte[] g_uc94BarCodeBuffer = new byte[REAGENT_94BAR_CODE_NUM];
//        String m_reaBoxUnencrypted = "!";
//
//        g_ucBarCodeByteBuffer[0] = (byte) barRareData.bartype;
//        g_ucBarCodeByteBuffer[1] = (byte) (barRareData.productyear >> 8);
//        g_ucBarCodeByteBuffer[2] = (byte) barRareData.productyear;
//        g_ucBarCodeByteBuffer[3] = (byte) barRareData.productmonth;
//        g_ucBarCodeByteBuffer[4] = (byte) (barRareData.num >> 8);
//        g_ucBarCodeByteBuffer[5] = (byte) barRareData.num;
//        g_ucBarCodeByteBuffer[6] = (byte) (barRareData.limityear >> 8);
//        g_ucBarCodeByteBuffer[7] = (byte) barRareData.limityear;
//        g_ucBarCodeByteBuffer[8] = (byte) barRareData.limitmonth;
//        g_ucBarCodeByteBuffer[9] = (byte) barRareData.limitday;
//        for (int i = 0; i < 5; i++) {
//            g_ucBarCodeByteBuffer[10 + i * 2] = (byte) barRareData.itemstd[i].itemtype;
//            g_ucBarCodeByteBuffer[11 + i * 2] = (byte) barRareData.itemstd[i].kongweino;
//        }
//
//        for (int i = 0; i < 10; i++) {
//            g_ucBarCodeByteBuffer[20 + i * 5] = (byte) barRareData.itemqc[i].itemtype;
//            g_ucBarCodeByteBuffer[21 + i * 5] = (byte) ((barRareData.itemqc[i].kongweino0 & 0x1F) | ((barRareData.itemqc[i].daYuShiNeng << 7) & 0x80) | ((barRareData.itemqc[i].errHandle << 5) & 0x60));
//            g_ucBarCodeByteBuffer[22 + i * 5] = (byte) barRareData.itemqc[i].kongweino1;
//            g_ucBarCodeByteBuffer[23 + i * 5] = (byte) (((barRareData.itemqc[i].guangluno << 4) & 0xF0) | ((barRareData.itemqc[i].refvalue >> 8) & 0x0F));
//            g_ucBarCodeByteBuffer[24 + i * 5] = (byte) ((barRareData.itemqc[i].refvalue) & 0xFF);
//        }
//
//        for (int i = 0; i < 16; i++) {
//            g_ucBarCodeByteBuffer[70 + i * 15] = (byte) barRareData.itemtest[i].itemtype;
//            g_ucBarCodeByteBuffer[71 + i * 15] = (byte) ((barRareData.itemtest[i].K) >> 8);
//            g_ucBarCodeByteBuffer[72 + i * 15] = (byte) barRareData.itemtest[i].K;
//            g_ucBarCodeByteBuffer[73 + i * 15] = (byte) ((barRareData.itemtest[i].B) >> 8);
//            g_ucBarCodeByteBuffer[74 + i * 15] = (byte) barRareData.itemtest[i].B;
//            g_ucBarCodeByteBuffer[75 + i * 15] = (byte) ((barRareData.itemtest[i].refvalue0) >> 8);
//            g_ucBarCodeByteBuffer[76 + i * 15] = (byte) barRareData.itemtest[i].refvalue0;
//            g_ucBarCodeByteBuffer[77 + i * 15] = (byte) ((barRareData.itemtest[i].refvalue1) >> 8);
//            g_ucBarCodeByteBuffer[78 + i * 15] = (byte) barRareData.itemtest[i].refvalue1;
//
//            g_ucBarCodeByteBuffer[79 + i * 15] = (byte) (((barRareData.itemtest[i].fangfa & 0x1F) << 3) | ((barRareData.itemtest[i].danwei >> 3) & 0x07));
//            g_ucBarCodeByteBuffer[80 + i * 15] = (byte) (((barRareData.itemtest[i].danwei & 0x07) << 5) | ((barRareData.itemtest[i].kongweino) & 0x1F));
//            g_ucBarCodeByteBuffer[81 + i * 15] = (byte) (((barRareData.itemtest[i].guangluno << 4) & 0xF0) | ((barRareData.itemtest[i].ARG0 >> 1) & 0x0F));
//            g_ucBarCodeByteBuffer[82 + i * 15] = (byte) (((barRareData.itemtest[i].ARG0 << 7) & 0x80) | ((barRareData.itemtest[i].ARG1) & 0x7F));
//            g_ucBarCodeByteBuffer[83 + i * 15] = (byte) barRareData.itemtest[i].ARG2;
//            g_ucBarCodeByteBuffer[84 + i * 15] = (byte) barRareData.itemtest[i].ARG3;
//        }
//        for (int i = 310; i < g_ucBarCodeByteBuffer.length - 1; i++) {
//            g_ucBarCodeByteBuffer[i] = 0;
//        }
//        g_ucBarCodeByteBuffer[g_ucBarCodeByteBuffer.length - 1] = sumrc(g_ucBarCodeByteBuffer, 0, g_ucBarCodeByteBuffer.length - 1);
//        LogUtils.d("g_ucBarCodeByteBuffer:" + toHexString(g_ucBarCodeByteBuffer));
//        m_reaBoxUnencrypted += ConvertToEnd(toHexString(g_ucBarCodeByteBuffer));
//        LogUtils.d("m_reaBoxUnencrypted:" + m_reaBoxUnencrypted);
//        return m_reaBoxUnencrypted;
//    }

//    /*******************************************************************************
//     * 功能描述：获取试剂条的简要信息
//     *******************************************************************************/
//    public static BarRareData GetReagentDetailBarCode(byte[] buff) {
//        BarRareData barraredata = new BarRareData();
//        byte[] g_ucBarCodeByteBuffer = new byte[REAGENT_BAR_CODE_BYTE_NUM + 10];//
//        byte[] g_uc94BarCodeBuffer = new byte[REAGENT_94BAR_CODE_NUM];
//        //int i=0;
//        int ucNum = 0;
//        int uiValue = 0;
//        long uliValue = 0;
//
//        if (buff.length < REAGENT_94BAR_CODE_NUM)
//            return null;
//        System.arraycopy(buff, 0, g_uc94BarCodeBuffer, 0, REAGENT_94BAR_CODE_NUM);
//
//        //hex_dump(g_uc94BarCodeBuffer,REAGENT_94BAR_CODE_NUM);
//        if (g_uc94BarCodeBuffer[0] == '!') //试剂条详细信息
//        {
//            Convert94ToHex(g_uc94BarCodeBuffer, 0, REAGENT_94BAR_CODE_NUM);
//
//            for (int i = 0; ; i++) {
//                ucNum = REAGENT_94BAR_CODE_NUM - i * 5 - 5;//(unsigned char)
//                uliValue = g_uc94BarCodeBuffer[ucNum];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 1];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 2];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 3];
//                uliValue = uliValue * 94 + g_uc94BarCodeBuffer[ucNum + 4];
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 4] = (byte) ((uliValue >> 24) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 3] = (byte) ((uliValue >> 16) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 2] = (byte) ((uliValue >> 8) & 0xFF);
//                g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 4 * i - 1] = (byte) ((uliValue) & 0xFF);
//                //qDebug()<<uliValue<<endl;
//                if (ucNum <= 5) {
//                    break;
//                }
//            }
//            //memcpy(g_uc94BarCodeBuffer,buff,REAGENT_94BAR_CODE_NUM);
//            //hex_dump(g_uc94BarCodeBuffer,REAGENT_94BAR_CODE_NUM);
//            g_ucBarCodeByteBuffer[0] = REAGENT_BAR_INFO_HEADER; //标志位
//
//            LogUtils.d(g_ucBarCodeByteBuffer, REAGENT_BAR_CODE_BYTE_NUM);
//            if (sumrc(g_ucBarCodeByteBuffer, 1, REAGENT_BAR_CODE_BYTE_NUM - 2) != g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM - 1]) {
//                LogUtils.d("sumrc err");
//                return null;
//            }
//            int index = 1;
//            barraredata.bartype = (int) ((g_ucBarCodeByteBuffer[index + 0] & 0xff) >> 2);
//            barraredata.productyear = (int) (((g_ucBarCodeByteBuffer[index + 0] & 0x03) << 5) | ((g_ucBarCodeByteBuffer[index + 1] & 0xff) >> 3)) + 2000;
//            barraredata.productmonth = (int) (((g_ucBarCodeByteBuffer[index + 1] & 0x07) << 1) | ((g_ucBarCodeByteBuffer[index + 2] & 0xff) >> 7));
//            barraredata.num = (int) (((g_ucBarCodeByteBuffer[index + 2] & 0x7f) << 5) | ((g_ucBarCodeByteBuffer[index + 3] & 0xff) >> 3));
//            barraredata.productday = (int) (((g_ucBarCodeByteBuffer[index + 3] & 0x07) << 2) | ((g_ucBarCodeByteBuffer[index + 4] & 0xff) >> 6));
//            try {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(barraredata.productyear, barraredata.productmonth, barraredata.productday);
//                calendar.add(Calendar.DAY_OF_YEAR, 180);
//                barraredata.limityear = calendar.get(YEAR);
//                barraredata.limitmonth = calendar.get(Calendar.MONTH);
//                barraredata.limitday = calendar.get(Calendar.DAY_OF_MONTH);
//            } catch (NumberFormatException e) {
//                LogUtils.d(e.toString());
//                e.printStackTrace();
//                barraredata.limityear = 0;
//                barraredata.limitmonth = 0;
//                barraredata.limitday = 0;
//            }
//            index = index + 5;
//            {
//                barraredata.itemqc[0].itemtype = (int) ((g_ucBarCodeByteBuffer[index + 0] & 0xFE) >> 1);
//                barraredata.itemqc[0].refvalue = (int) (((g_ucBarCodeByteBuffer[index + 0] & 0x01) << 4) | ((g_ucBarCodeByteBuffer[index + 1] & 0xF0) >> 4));
//                barraredata.itemqc[1].itemtype = (int) (((g_ucBarCodeByteBuffer[index + 1] & 0x0F) << 3) | ((g_ucBarCodeByteBuffer[index + 2] & 0xE0) >> 5));
//                barraredata.itemqc[1].refvalue = (int) (g_ucBarCodeByteBuffer[index + 2] & 0x1F);
//
//                barraredata.itemqc[2].itemtype = (int) ((g_ucBarCodeByteBuffer[index + 3] & 0xFE) >> 1);
//                barraredata.itemqc[2].refvalue = (int) (((g_ucBarCodeByteBuffer[index + 3] & 0x01) << 4) | ((g_ucBarCodeByteBuffer[index + 4] & 0xF0) >> 4));
//                barraredata.itemqc[3].itemtype = (int) (((g_ucBarCodeByteBuffer[index + 4] & 0x0F) << 3) | ((g_ucBarCodeByteBuffer[index + 5] & 0xE0) >> 5));
//                barraredata.itemqc[3].refvalue = (int) (g_ucBarCodeByteBuffer[index + 5] & 0x1F);
//
//                barraredata.itemqc[4].itemtype = (int) ((g_ucBarCodeByteBuffer[index + 6] & 0xFE) >> 1);
//                barraredata.itemqc[4].refvalue = (int) (((g_ucBarCodeByteBuffer[index + 6] & 0x01) << 4) | ((g_ucBarCodeByteBuffer[index + 7] & 0xF0) >> 4));
//                barraredata.itemqc[5].itemtype = (int) (((g_ucBarCodeByteBuffer[index + 7] & 0x0F) << 3) | ((g_ucBarCodeByteBuffer[index + 8] & 0xE0) >> 5));
//                barraredata.itemqc[5].refvalue = (int) (g_ucBarCodeByteBuffer[index + 8] & 0x1F);
//
//                barraredata.itemqc[6].itemtype = (int) ((g_ucBarCodeByteBuffer[index + 9] & 0xFE) >> 1);
//                barraredata.itemqc[6].refvalue = (int) (((g_ucBarCodeByteBuffer[index + 9] & 0x01) << 4) | ((g_ucBarCodeByteBuffer[index + 10] & 0xF0) >> 4));
//                barraredata.itemqc[7].itemtype = (int) (((g_ucBarCodeByteBuffer[index + 10] & 0x0F) << 3) | ((g_ucBarCodeByteBuffer[index + 11] & 0xE0) >> 5));
//                barraredata.itemqc[7].refvalue = (int) ((g_ucBarCodeByteBuffer[index + 11] & 0x1F));
//            }
//            index = index + 12;
//            for (int i = 0; i < 3; i++) {
//                barraredata.itemtest0[i].itemtype = (int) ((g_ucBarCodeByteBuffer[index + i * 12 + 0] & 0xff) >> 2);
//                barraredata.itemtest0[i].A[0] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 0] & 0x03) << 13) | ((g_ucBarCodeByteBuffer[index + i * 12 + 1] & 0xFF) << 5) | ((g_ucBarCodeByteBuffer[index + i * 12 + 2] & 0xF8) >> 3));
//                barraredata.itemtest0[i].A[1] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 2] & 0x07) << 12) | ((g_ucBarCodeByteBuffer[index + i * 12 + 3] & 0xFF) << 4) | ((g_ucBarCodeByteBuffer[index + i * 12 + 4] & 0xF0) >> 4));
//                barraredata.itemtest0[i].A[2] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 4] & 0x0F) << 12) | ((g_ucBarCodeByteBuffer[index + i * 12 + 5] & 0xFF) << 2) | ((g_ucBarCodeByteBuffer[index + i * 12 + 6] & 0xC0) >> 6));
//                barraredata.itemtest0[i].A[3] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 6] & 0x3F) << 8) | (g_ucBarCodeByteBuffer[index + i * 12 + 7] & 0xFF));
//                barraredata.itemtest0[i].A[4] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 8] & 0xFF) << 6) | ((g_ucBarCodeByteBuffer[index + i * 12 + 9] & 0xFC) >> 2));
//                barraredata.itemtest0[i].A[5] = (int) (((g_ucBarCodeByteBuffer[index + i * 12 + 9] & 0x03) << 11) | ((g_ucBarCodeByteBuffer[index + i * 12 + 10] & 0xFF) << 3) | ((g_ucBarCodeByteBuffer[index + i * 12 + 11] & 0xE0) >> 5));
//                barraredata.itemtest0[i].kongweino = (int) (g_ucBarCodeByteBuffer[index + i * 12 + 11] & 0x1F);
//            }
//
//            index = index + 36;
//            for (int i = 0; i < 10; i++) {
//                barraredata.itemtest[i].itemtype = (int) ((g_ucBarCodeByteBuffer[index + i * 7 + 0] & 0xff) >> 2);
//                barraredata.itemtest[i].K = (int) (((g_ucBarCodeByteBuffer[index + i * 7 + 0] & 0x03) << 13) | ((g_ucBarCodeByteBuffer[index + i * 7 + 1] & 0xFF) << 5) | ((g_ucBarCodeByteBuffer[index + i * 7 + 2] & 0xF8) >> 3));
//                barraredata.itemtest[i].B = (int) (((g_ucBarCodeByteBuffer[index + i * 7 + 2] & 0x07) << 12) | ((g_ucBarCodeByteBuffer[index + i * 7 + 3] & 0xFF) << 4) | ((g_ucBarCodeByteBuffer[index + i * 7 + 4] & 0xF0) >> 4));
//                barraredata.itemtest[i].kongweino = (int) (((g_ucBarCodeByteBuffer[index + i * 7 + 4] & 0x0f) << 1) | ((g_ucBarCodeByteBuffer[index + i * 7 + 5] & 0x80) >> 7));
//                barraredata.itemtest[i].ARG1 = (int) (g_ucBarCodeByteBuffer[index + i * 7 + 5] & 0x7F);
//                barraredata.itemtest[i].ARG3 = (int) (g_ucBarCodeByteBuffer[index + i * 7 + 6] & 0xFF);
//            }
//            index = index + 70;
//            barraredata.isi = (int) (g_ucBarCodeByteBuffer[index] & 0xFF);
//            LogUtils.d("barcode right");
//            return barraredata;
//        } else {
//            LogUtils.d("barcode head not !");
//            return null; //获取失败
//        }
//    }
//
//
//    public static String setReagentDetailBarCodeToBuff(BarRareData barRareData) {//, byte[] buff
//        byte[] g_ucBarCodeByteBuffer = new byte[REAGENT_BAR_CODE_BYTE_NUM - 1];//
//        byte[] g_uc94BarCodeBuffer = new byte[REAGENT_94BAR_CODE_NUM];
//        String m_reaBoxUnencrypted = "!";
//
//        g_ucBarCodeByteBuffer[0] = (byte) (((barRareData.bartype & 0x3f) << 2) | (((barRareData.productyear - 2000) & 0x7f) >> 5));
//        g_ucBarCodeByteBuffer[1] = (byte) ((((barRareData.productyear - 2000) & 0x1f) << 3) | ((barRareData.productmonth & 0x0f) >> 1));
//        g_ucBarCodeByteBuffer[2] = (byte) (((barRareData.productmonth & 0x01) << 7) | ((barRareData.num & 0x0fff) >> 5));
//        g_ucBarCodeByteBuffer[3] = (byte) (((barRareData.num & 0x1f) << 3) | ((barRareData.productday & 0x1f) >> 2));
//        g_ucBarCodeByteBuffer[4] = (byte) (((barRareData.productday & 0x03) << 6));
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        //for (int i = 0; i < 8; i++)
//        {
//            g_ucBarCodeByteBuffer[5 + 0] = (byte) (((barRareData.itemqc[0].itemtype & 0xEF) << 1) | ((barRareData.itemqc[0].refvalue & 0x1F) >> 4));
//            g_ucBarCodeByteBuffer[5 + 1] = (byte) (((barRareData.itemqc[0].refvalue & 0x0F) << 4) | ((barRareData.itemqc[1].itemtype & 0x0EF) >> 3));
//            g_ucBarCodeByteBuffer[5 + 2] = (byte) (((barRareData.itemqc[1].itemtype & 0x07) << 5) | ((barRareData.itemqc[1].refvalue & 0x1F)));
//
//            g_ucBarCodeByteBuffer[5 + 3] = (byte) (((barRareData.itemqc[2].itemtype & 0xEF) << 1) | ((barRareData.itemqc[2].refvalue & 0x1F) >> 4));
//            g_ucBarCodeByteBuffer[5 + 4] = (byte) (((barRareData.itemqc[2].refvalue & 0x0F) << 4) | ((barRareData.itemqc[3].itemtype & 0x0EF) >> 3));
//            g_ucBarCodeByteBuffer[5 + 5] = (byte) (((barRareData.itemqc[3].itemtype & 0x07) << 5) | ((barRareData.itemqc[3].refvalue & 0x1F)));
//
//            g_ucBarCodeByteBuffer[5 + 6] = (byte) (((barRareData.itemqc[4].itemtype & 0xEF) << 1) | ((barRareData.itemqc[4].refvalue & 0x1F) >> 4));
//            g_ucBarCodeByteBuffer[5 + 7] = (byte) (((barRareData.itemqc[4].refvalue & 0x0F) << 4) | ((barRareData.itemqc[5].itemtype & 0x0EF) >> 3));
//            g_ucBarCodeByteBuffer[5 + 8] = (byte) (((barRareData.itemqc[5].itemtype & 0x07) << 5) | ((barRareData.itemqc[5].refvalue & 0x1F)));
//
//            g_ucBarCodeByteBuffer[5 + 9] = (byte) (((barRareData.itemqc[6].itemtype & 0xEF) << 1) | ((barRareData.itemqc[6].refvalue & 0x1F) >> 4));
//            g_ucBarCodeByteBuffer[5 + 10] = (byte) (((barRareData.itemqc[6].refvalue & 0x0F) << 4) | ((barRareData.itemqc[7].itemtype & 0x0EF) >> 3));
//            g_ucBarCodeByteBuffer[5 + 11] = (byte) (((barRareData.itemqc[7].itemtype & 0x07) << 5) | ((barRareData.itemqc[7].refvalue & 0x1F)));
//            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
//
//        for (int i = 0; i < 3; i++) {
//            g_ucBarCodeByteBuffer[14 + i * 12] = (byte) (((barRareData.itemtest0[i].itemtype & 0x3f) << 2) | ((barRareData.itemtest0[i].A[0] & 0x7fff) >> 13));
//            g_ucBarCodeByteBuffer[15 + i * 12] = (byte) (((barRareData.itemtest0[i].A[0] & 0x1fff) >> 5));
//            g_ucBarCodeByteBuffer[16 + i * 12] = (byte) (((barRareData.itemtest0[i].A[0] & 0x1f) << 3) | ((barRareData.itemtest0[i].A[1] & 0x7fff) >> 12));
//            g_ucBarCodeByteBuffer[17 + i * 12] = (byte) (((barRareData.itemtest0[i].A[1] & 0xfff) >> 4));
//            g_ucBarCodeByteBuffer[18 + i * 12] = (byte) (((barRareData.itemtest0[i].A[1] & 0xf) << 4) | ((barRareData.itemtest0[i].A[2] & 0x3fff) >> 10));
//            g_ucBarCodeByteBuffer[19 + i * 12] = (byte) (((barRareData.itemtest0[i].A[2] & 0x3ff) >> 2));
//            g_ucBarCodeByteBuffer[20 + i * 12] = (byte) (((barRareData.itemtest0[i].A[2] & 0x3) << 6) | ((barRareData.itemtest0[i].A[3] & 0x3fff) >> 8));
//            g_ucBarCodeByteBuffer[21 + i * 12] = (byte) (((barRareData.itemtest0[i].A[3] & 0xff) >> 0));
//            g_ucBarCodeByteBuffer[22 + i * 12] = (byte) (((barRareData.itemtest0[i].A[4] & 0x3fff) >> 6));
//            g_ucBarCodeByteBuffer[23 + i * 12] = (byte) (((barRareData.itemtest0[i].A[4] & 0x3f) << 2) | ((barRareData.itemtest0[i].A[5] & 0x1fff) >> 11));
//            g_ucBarCodeByteBuffer[24 + i * 12] = (byte) (((barRareData.itemtest0[i].A[5] & 0x7ff) >> 3));
//            g_ucBarCodeByteBuffer[25 + i * 12] = (byte) (((barRareData.itemtest0[i].A[5] & 0x07) << 5) | ((barRareData.itemtest0[i].kongweino & 0x1f)));
//            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
//
//        for (int i = 0; i < 10; i++) {
//            g_ucBarCodeByteBuffer[50 + i * 7] = (byte) (((barRareData.itemtest[i].itemtype & 0x3f) << 2) | ((barRareData.itemtest[i].K & 0x07fff) >> 13));
//            g_ucBarCodeByteBuffer[51 + i * 7] = (byte) (((barRareData.itemtest[i].K & 0x1fff) >> 5));
//            g_ucBarCodeByteBuffer[52 + i * 7] = (byte) (((barRareData.itemtest[i].K & 0x1f) << 3) | ((barRareData.itemtest[i].B & 0x7fff) >> 12));
//            g_ucBarCodeByteBuffer[53 + i * 7] = (byte) (((barRareData.itemtest[i].B & 0xfff) >> 4));
//            g_ucBarCodeByteBuffer[54 + i * 7] = (byte) (((barRareData.itemtest[i].B & 0xf) << 4) | ((barRareData.itemtest[i].kongweino & 0x1f) >> 1));
//            g_ucBarCodeByteBuffer[55 + i * 7] = (byte) (((barRareData.itemtest[i].kongweino & 0x01) << 7) | ((barRareData.itemtest[i].ARG1 & 0x7f) >> 0));
//            g_ucBarCodeByteBuffer[56 + i * 7] = (byte) (barRareData.itemtest[i].ARG3 & 0xff);
//            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
//        g_ucBarCodeByteBuffer[120] = (byte) (barRareData.isi & 0xff);
//        for (int i = 121; i < g_ucBarCodeByteBuffer.length - 1; i++) {
//            g_ucBarCodeByteBuffer[i] = 0;
//        }
//        g_ucBarCodeByteBuffer[g_ucBarCodeByteBuffer.length - 1] = sumrc(g_ucBarCodeByteBuffer, 0, g_ucBarCodeByteBuffer.length - 1);
//        LogUtils.d("g_ucBarCodeByteBuffer:" + toHexString(g_ucBarCodeByteBuffer));
//        m_reaBoxUnencrypted += ConvertToEnd(toHexString(g_ucBarCodeByteBuffer));
//        LogUtils.d("m_reaBoxUnencrypted:" + m_reaBoxUnencrypted);
//        return m_reaBoxUnencrypted;
//    }
//
//    public static BarRareData getAllBarRareData(BarRareData temp) {
//        BarRareData barraredata = new BarRareData(temp);
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////        barraredata.productyear = barraredata.productyear + 2000;
////        Calendar limitDate = Calendar.getInstance();
////        limitDate.set(barraredata.productyear, barraredata.productmonth, barraredata.limitday);
////        limitDate.add(Calendar.DAY_OF_YEAR, 180);
////        barraredata.limityear = limitDate.get(Calendar.YEAR);
////        barraredata.limitmonth = limitDate.get(Calendar.MONTH);
////        barraredata.limitday = limitDate.get(Calendar.DAY_OF_MONTH);
//
//        for (int i = 0; i < 8; i++) {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
//        for (int i = 0; i < 3; i++) {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            barraredata.itemtest0[i].K = (int) (barraredata.itemtest0[i].A[0]);
//            barraredata.itemtest0[i].B = (int) (barraredata.itemtest0[i].A[1]);
//            barraredata.itemtest0[i].ARG1 = barraredata.itemtest0[i].A[2];
//            barraredata.itemtest0[i].ARG3 = barraredata.itemtest0[i].A[3];
//            LogUtils.d("barraredata.itemtest[i].K:" + barraredata.itemtest0[i].K);
//        }
//        for (int i = 0; i < 10; i++) {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            LogUtils.d("barraredata.itemtest[i].K:" + barraredata.itemtest[i].K);
//            barraredata.itemtest[i].K = (int) (barraredata.itemtest[i].K);
//            barraredata.itemtest[i].B = (int) (barraredata.itemtest[i].B);
//        }
//        defaultPtInr.ISI = barraredata.isi / 100.0;
//        return barraredata;
//    }


    /*******************************************************************************
     * 功能描述：获取试剂条的简要信息
     *******************************************************************************/
    public static PCRProject GetReagentDetailBarCode(byte[] buff) {
        PCRProject pcrProject = new PCRProject();
        String strbuff = new String(buff);

        LogUtils.d("strbuff:" + strbuff);
        int index = 0;
        if (!strbuff.substring(index, index + 7).trim().equals("LAMPPCR")) {
            LogUtils.d("strbuff.substring(index,index+7)：" + strbuff.substring(index, index + 7).trim());
            return null;
        }
        index = index + 7;
        if (Integer.valueOf(strbuff.substring(index, index + 1).trim()) == 0)
            pcrProject.project_type = GlobalDate.ProjectType.dingxing;
        else
            pcrProject.project_type = GlobalDate.ProjectType.dingliang;

        index = index + 1;
        pcrProject.project_name = strbuff.substring(index, index + 20).trim();
        pcrProject.project_filename = pcrProject.project_name;
        index = index + 20;
        pcrProject.project_lot = strbuff.substring(index, index + 10).trim();
        index = index + 10;
        pcrProject.project_danwei = strbuff.substring(index, index + 9).trim();
        index = index + 9;

        for (int i = 0; i < 3; i++) {
            if (Integer.valueOf(strbuff.substring(index, index + 1).trim()) == 0)
                pcrProject.pcrLiuChengCanShuItems.get(i).isExe = false;
            else
                pcrProject.pcrLiuChengCanShuItems.get(i).isExe = true;

            pcrProject.getPcrLiuChengCanShuItems().get(i).targetTemp = Double.valueOf(strbuff.substring(index + 1, index + 3).trim());
            pcrProject.getPcrLiuChengCanShuItems().get(i).holdTime = Integer.valueOf(strbuff.substring(index + 3, index + 6).trim());

            if (Integer.valueOf(strbuff.substring(index + 6, index + 7).trim()) == 0)
                pcrProject.getPcrLiuChengCanShuItems().get(i).isRead = false;
            else
                pcrProject.getPcrLiuChengCanShuItems().get(i).isRead = true;

            pcrProject.getPcrLiuChengCanShuItems().get(i).intervalTime = Integer.valueOf(strbuff.substring(index + 7, index + 10).trim());
            index = index + 10;
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        pcrProject.project_neican_tongdao = Integer.valueOf(strbuff.substring(index, index + 1).trim());
        index = index + 1;

        for (int i = 0; i < 4; i++) {
            if (Integer.valueOf(strbuff.substring(index, index + 1).trim()) == 0)
                pcrProject.project_item_ables[i] = false;
            else
                pcrProject.project_item_ables[i] = true;

            pcrProject.project_item_names[i] = strbuff.substring(index + 1, index + 7).trim();
            pcrProject.project_item_refs[i] = Double.valueOf(strbuff.substring(index + 7, index + 9).trim());
            pcrProject.project_item_rns[i] = Double.valueOf(strbuff.substring(index + 9, index + 12).trim());
            pcrProject.project_item_ks[i] = Double.valueOf(strbuff.substring(index + 12, index + 16).trim());
            pcrProject.project_item_bs[i] = Double.valueOf(strbuff.substring(index + 16, index + 20).trim());
            index = index + 20;
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        pcrProject.project_ncs[0] = Integer.valueOf(strbuff.substring(index, index + 2).trim());
        pcrProject.project_ncs[1] = Integer.valueOf(strbuff.substring(index, index + 2).trim());
        pcrProject.project_ncs[2] = Integer.valueOf(strbuff.substring(index, index + 2).trim());
        pcrProject.project_ncs[3] = Integer.valueOf(strbuff.substring(index, index + 2).trim());
        pcrProject.project_neican = Double.valueOf(strbuff.substring(index + 2, index + 4).trim());

        if (pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
            pcrProject.project_babiaos[0] = Double.valueOf(strbuff.substring(index + 6, index + 8).trim());
            pcrProject.project_babiaos[1] = Double.valueOf(strbuff.substring(index + 6, index + 8).trim());
            pcrProject.project_babiaos[2] = Double.valueOf(strbuff.substring(index + 6, index + 8).trim());
            pcrProject.project_babiaos[3] = Double.valueOf(strbuff.substring(index + 6, index + 8).trim());
        }else {
            pcrProject.project_babiaos[0] = Double.valueOf(strbuff.substring(index + 4, index + 8).trim());
            pcrProject.project_babiaos[1] = Double.valueOf(strbuff.substring(index + 4, index + 8).trim());
            pcrProject.project_babiaos[2] = Double.valueOf(strbuff.substring(index + 4, index + 8).trim());
            pcrProject.project_babiaos[3] = Double.valueOf(strbuff.substring(index + 4, index + 8).trim());
        }
        pcrProject.project_dingliang_biaozhunnongdu = Double.valueOf(strbuff.substring(index + 8, index + 12).trim());

        index = index + 12;
        LogUtils.d("index:" + index);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return pcrProject;
    }


    public static String setReagentDetailBarCodeToBuff(PCRProject pcrProject) {//, byte[] buff
        String m_reaBoxUnencrypted = "!";
        StringBuilder stringBuilder = new StringBuilder();
        String tempstr;
        int index = 0;
        int len = 0;
        for (int i = 0; i < 170; i++)
            stringBuilder.append(" ");
        tempstr = "LAMPPCR";
        len = Math.min(tempstr.length(), 7);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 7;
//////////////////////////////////////////////////////////////////////////////////////////////
        if (pcrProject.project_type == GlobalDate.ProjectType.dingxing)
            stringBuilder.replace(index, index + 1, "0");
        else
            stringBuilder.replace(index, index + 1, "1");
        index = index + 1;
/////////////////////////////////////////////////////////////////////////////////////////////
        tempstr = pcrProject.project_name;
        len = Math.min(tempstr.length(), 20);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 20;

        tempstr = pcrProject.project_lot;
        len = Math.min(tempstr.length(), 10);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 10;

        tempstr = pcrProject.project_danwei;
        len = Math.min(tempstr.length(), 9);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 9;
////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 3; i++) {
            if (pcrProject.pcrLiuChengCanShuItems.get(i).isExe)
                stringBuilder.replace(index, index + 1, "1");
            else
                stringBuilder.replace(index, index + 1, "0");
            index = index + 1;

            tempstr = String.valueOf(pcrProject.pcrLiuChengCanShuItems.get(i).targetTemp);
            len = Math.min(tempstr.length(), 2);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 2;

            tempstr = String.valueOf(pcrProject.pcrLiuChengCanShuItems.get(i).holdTime);
            len = Math.min(tempstr.length(), 3);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 3;

            if (pcrProject.pcrLiuChengCanShuItems.get(i).isRead)
                stringBuilder.replace(index, index + 1, "1");
            else
                stringBuilder.replace(index, index + 1, "0");
            index = index + 1;

            tempstr = String.valueOf(pcrProject.pcrLiuChengCanShuItems.get(i).intervalTime);
            len = Math.min(tempstr.length(), 3);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 3;
        }
////////////////////////////////////////////////////////////////////////////////////////////
        tempstr = String.valueOf(pcrProject.project_neican_tongdao);
        len = Math.min(tempstr.length(), 1);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 1;

        for (int i = 0; i < 4; i++) {
            if (pcrProject.project_item_ables[i])
                stringBuilder.replace(index, index + 1, "1");
            else
                stringBuilder.replace(index, index + 1, "0");
            index = index + 1;

            tempstr = pcrProject.project_item_names[i];
            len = Math.min(tempstr.length(), 6);
            LogUtils.d("len:"+len);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 6;

            tempstr = String.valueOf(pcrProject.project_item_refs[i]);
            len = Math.min(tempstr.length(), 2);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 2;

            tempstr = String.valueOf(pcrProject.project_item_rns[i]);
            len = Math.min(tempstr.length(), 3);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 3;

            tempstr = String.valueOf(pcrProject.project_item_ks[i]);
            len = Math.min(tempstr.length(), 4);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 4;

            tempstr = String.valueOf(pcrProject.project_item_bs[i]);
            len = Math.min(tempstr.length(), 4);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 4;
        }
//////////////////////////////////////////////////////////////////////////////////////////////
        tempstr = String.valueOf(pcrProject.project_ncs[0]);
        len = Math.min(tempstr.length(), 2);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 2;

        tempstr = String.valueOf(pcrProject.project_neican);
        len = Math.min(tempstr.length(), 2);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 2;

        if (pcrProject.project_type == GlobalDate.ProjectType.dingxing) {
            tempstr = String.valueOf(pcrProject.project_babiaos[0]);
            len = Math.min(tempstr.length(), 2);
            stringBuilder.replace(index + 2, index + 2 + len, tempstr.substring(0,len));
            index = index + 4;
        } else {
            tempstr = String.valueOf(pcrProject.project_babiaos[0]);
            len = Math.min(tempstr.length(), 4);
            stringBuilder.replace(index, index + len, tempstr.substring(0,len));
            index = index + 4;
        }

        tempstr = String.valueOf(pcrProject.project_dingliang_biaozhunnongdu);
        len = Math.min(tempstr.length(), 4);
        stringBuilder.replace(index, index + len, tempstr.substring(0,len));
        index = index + 4;

        LogUtils.d("string:" + stringBuilder.toString());
        return stringBuilder.toString();
    }


    static String ConvertToEnd(String strHexInfo) {
        StringBuilder str94Info = new StringBuilder();
        long myTemp = 0;
        int ileng = strHexInfo.length();
        int count = strHexInfo.length() % 8;
        if (count != 0) {
            String myStr = "";
            String strTemp = strHexInfo.substring(0, count);
            myTemp = HexToDec(strTemp);
            while ( myTemp != 0 ) {
                int i = (int) (myTemp % 94);
                myStr = Char94Table[i] + myStr;
                myTemp = myTemp / 94;
            }
            while ( myStr.length() < (count / 2 + 1) ) {
                myStr = "0" + myStr;
            }
            str94Info.append(myStr);
            for (int i = 0; i < strHexInfo.length() / 8; i++) {
                strTemp = strHexInfo.substring(i * 8 + count, i * 8 + +count + 8);
                myTemp = HexToDec(strTemp);
                str94Info.append(Convert94ToHex(myTemp));
                LogUtils.d("ConvertToEnd:" + strTemp);
            }
        } else {
            for (int i = 0; i < strHexInfo.length() / 8; i++) {
                String strTemp = strHexInfo.substring(i * 8, i * 8 + count + 8);
                myTemp = HexToDec(strTemp);
                str94Info.append(Convert94ToHex(myTemp));
                LogUtils.d("ConvertToEnd:" + strTemp);
            }
        }
        return str94Info.toString();
    }

    static long HexToDec(String strHex) {
        long ret = 0;
        try {
            //ret = Integer.valueOf("0x"+strHex);
            ret = Long.valueOf(strHex, 16);
        } catch (NumberFormatException e) {
            LogUtils.d(e.toString());
            e.printStackTrace();
            ret = 0;
        }
        return ret;
    }

    static String Convert94ToHex(long num) {
        String myStr = "";
        int n = 94;
        int yushu;      // 保存余数
        long shang = num; // 保存商
        while ( shang > 0 ) {
            yushu = (int) (shang % n);
            shang = shang / n;
            myStr = Char94Table[yushu] + myStr;
            LogUtils.d("Str94Table[yushu]:" + String.format("%s", Char94Table[yushu]));
        }
        while ( myStr.length() < 5 ) {
            myStr = "0" + myStr;
        }
        return myStr;
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    static byte sumrc(byte[] buf, int index, int len) {
        int i = 0;
        byte sum = 0;
        while ( i < len ) {
            sum += buf[i + index];
            i++;
        }
        return sum;
    }

///*******************************************************************************
// * 功能描述：获取试剂条的简要信息
// *******************************************************************************/
//
//    unsigned char GetReagentBriefBarCode(unsigned char *buff,struct ReagentDetailInfo *reagentboxinfo )
//    {
//        unsigned char g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM+10];
//        unsigned char *g_uc94BarCodeBuffer=buff;
//        unsigned char i=0,ucNum=0;
//        unsigned int uiValue=0,uiValue2=0;
//        unsigned long int uliValue=0;
//
//        unsigned char ucName[11];		//10位
//        unsigned char ucExpiryDate[3];	//3位，分别为年、月、日
//        unsigned char ucLotNum[3];		//3个字节 第一个字节的低7位为年，第二个字节的2~5四位为月，第二个字节的低两位和第三个字节为序列号
//        unsigned long int uliReagentKitNum; //试剂盒编码
//
//        if(g_uc94BarCodeBuffer[0] == '@') //试剂条简要信息
//        {
//            Convert94ToHex(g_uc94BarCodeBuffer,REAGENT_BRIEF_94BAR_CODE_NUM);
//            for(i=0;;i++)
//            {
//                ucNum = (unsigned char)REAGENT_BRIEF_94BAR_CODE_NUM - i*5 - 5;
//                uliValue = g_uc94BarCodeBuffer[ucNum];
//                uliValue = uliValue*94 + g_uc94BarCodeBuffer[ucNum+1];
//                uliValue = uliValue*94 + g_uc94BarCodeBuffer[ucNum+2];
//                uliValue = uliValue*94 + g_uc94BarCodeBuffer[ucNum+3];
//                uliValue = uliValue*94 + g_uc94BarCodeBuffer[ucNum+4];
//                g_ucBarCodeByteBuffer[REAGENT_BRIEF_BAR_CODE_BYTE_NUM-4*i-4]  = (uliValue>>24)&0xFF;
//                g_ucBarCodeByteBuffer[REAGENT_BRIEF_BAR_CODE_BYTE_NUM-4*i-3]  = (uliValue>>16)&0xFF;
//                g_ucBarCodeByteBuffer[REAGENT_BRIEF_BAR_CODE_BYTE_NUM-4*i-2]  = (uliValue>>8)&0xFF;
//                g_ucBarCodeByteBuffer[REAGENT_BRIEF_BAR_CODE_BYTE_NUM-4*i-1]  = (uliValue)&0xFF;
//                if(ucNum<5)
//                {
//                    break;
//                }
//            }
//            uliValue = g_uc94BarCodeBuffer[1];
//            uliValue = uliValue*94 + g_uc94BarCodeBuffer[2];
//            g_ucBarCodeByteBuffer[0] = (uliValue)&0xFF;
//            reagentboxinfo->ucType = 2;
//            memcpy(reagentboxinfo->ucName,g_ucBarCodeByteBuffer,10);    //项目名称
//            ucName[10] = '\0';
//            reagentboxinfo->ucExpiryDate[0] = (g_ucBarCodeByteBuffer[10]&0xFE)>>1;  //有效期 年
//            reagentboxinfo->ucExpiryDate[1] = ((g_ucBarCodeByteBuffer[10]&0x01)<<3) | ((g_ucBarCodeByteBuffer[11]&0xE0)>>5);  //有效期 月
//            reagentboxinfo->ucExpiryDate[2] = g_ucBarCodeByteBuffer[11]&0x1F;  //有效期 日
//            reagentboxinfo->ucLotNum[0] = (g_ucBarCodeByteBuffer[12]&0xFE)>>1;
//            uiValue= ((g_ucBarCodeByteBuffer[12]&0x01)<<3) | ((g_ucBarCodeByteBuffer[13]&0xE0)>>5);
//            uiValue2 = ((g_ucBarCodeByteBuffer[13]&0x1F)<<5) | ((g_ucBarCodeByteBuffer[14]&0xF8)>>3);
//            reagentboxinfo->ucLotNum[1] = (uiValue<<2) | (uiValue2>>8);
//            reagentboxinfo->ucLotNum[2] =  uiValue2 & 0xFF;
//            reagentboxinfo->uliReagentKitNum = g_ucBarCodeByteBuffer[14]&0x07;
//            reagentboxinfo->uliReagentKitNum = uliReagentKitNum * 65536 + (unsigned long int)g_ucBarCodeByteBuffer[15] * 256 + (unsigned long int)g_ucBarCodeByteBuffer[16];
//
//            return 0;
//        }
//        else
//        {
//            return 1;
//        }
//    }
//
//
///*******************************************************************************
// * 函数名称：GetSampleIDBarCode
// * 功能描述：获取样本号信息
// *******************************************************************************/
//    unsigned char GetSampleIDBarCode(unsigned char *buff,struct ReagentDetailInfo *sampleidinfo)
//    {
//        unsigned char g_ucBarCodeByteBuffer[REAGENT_BAR_CODE_BYTE_NUM+10];
//        unsigned char *g_uc94BarCodeBuffer=buff;
//        unsigned long int uliValue=0;
//        if((g_uc94BarCodeBuffer[0] != '@') && (g_uc94BarCodeBuffer[0] != '!'))
//        {
//            sampleidinfo->ucType=3;
//            memcpy(sampleidinfo->ucSampleID,g_uc94BarCodeBuffer,10);    //样本号
//            sampleidinfo->ucSampleID[10] = '\0';
//
//            return 0;
//        }
//        else
//        {
//            return 1;
//        }
//    }
}
