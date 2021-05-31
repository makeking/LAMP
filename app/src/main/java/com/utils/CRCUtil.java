package com.utils;
/**
 * CRC-CCITT 算法校验类
 *
 * @author amadowang
 * @version [版本号, Aug 29, 2011]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * @date 2012-10-24
 */
public class CRCUtil {
    public static int CRC_16_XMODEM(byte[] bytes, int offset,int length) {
        int crc = 0x0000; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = offset; index < offset + length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        // 输出String字样的16进制
        //String strCrc = Integer.toHexString(crc).toUpperCase();
        return crc;
    }
}
