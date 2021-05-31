package com.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by $ Ping.sir on 2018/8/14.
 * 二维码工具类
 */

public class QRCodeUtil {
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 314;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     * @param content 二维码内容
     * @return 返回二维码图片
     * @throws WriterException
     * @throws IOException
     * BufferedImage
     */
    public static Bitmap createImage(String content) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
//        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = null;
        Bitmap image = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width*height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(y,x)){
                        pixels[y*width +x] = 0xff000000;
                    }else {
                        pixels[y*width + x] = 0xffffffff;
                    }
//                    image.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            image = Bitmap.createBitmap(pixels,width,height,Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 插入图片
//        QRCodeUtil.insertImage(image, logoImgPath, needCompress);
        return image;
    }

    /**
     * 生成图片  加上title的图片
     * @param content
     * @param title
     * @return
     */
    public static Bitmap createImage(String content,String title, int QR_WIDTH, int QR_HEIGHT, int backgroundColor, int codeColor) {
        int titleTextSize = 30;
        int contentTextSize = 22;
        int textColor = Color.BLACK;
        int paddingTop = 22;
        int paddingMiddle = 20;
        int paddingBottom = 22;
        int width = 0;
        int height = 0;
        //画二维码
//        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
//        BitMatrix bitMatrix;
//        Bitmap image = null;
//        try {
//            bitMatrix = new MultiFormatWriter().encode(content+ "", BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            bitMatrix = deleteWhite(bitMatrix);
//            width = bitMatrix.getWidth();
//            height = bitMatrix.getHeight();
////            QR_WIDTH = width;
////            QR_HEIGHT = height;
//            image = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    image.setPixel(x, y, bitMatrix.get(x, y) ? backgroundColor : codeColor);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Bitmap image = null;
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            //Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); //容错率
//            hints.put(MARGIN, -1);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content + "", BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            //调用去除白边方法
            bitMatrix = deleteWhite(bitMatrix);
            width = bitMatrix.getWidth();
            height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * height + x] = codeColor;
                    } else {
                        pixels[y * width + x] = backgroundColor;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            image.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //最终生成的图片
        Bitmap result = Bitmap.createBitmap(QR_WIDTH,QR_HEIGHT+paddingTop+titleTextSize,Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Canvas canvas = new Canvas(result);

        //先画一整块白色矩形块
        canvas.drawRect(0,0,QR_WIDTH,QR_HEIGHT+paddingTop+titleTextSize,paint);

        //画title文字
        Rect bounds = new Rect();
        paint.setColor(textColor);
        paint.setTextSize(titleTextSize);
        //获取文字的字宽高，以便将文字与图片中心对齐
        paint.getTextBounds(title,0,title.length(),bounds);
        canvas.drawText(title,QR_WIDTH/2-bounds.width()/2,paddingTop+bounds.height()/2,paint);

        //画白色矩形块
        int qrTop = paddingTop+titleTextSize+paddingMiddle;//二维码的顶部高度
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(image,(QR_WIDTH-width)/2,qrTop,paint);//(QR_WIDTH-width)/2

//        //画文字
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(contentTextSize);
//        int lineTextCount = (int)((QR_WIDTH-50)/contentTextSize);
//        int line = (int)(Math.ceil(Double.valueOf(content.length())/Double.valueOf(lineTextCount)));
//        int textTop = qrTop+QR_HEIGHT+paddingBottom;//地址的顶部高度

//        for (int i = 0 ; i < line ; i++){
//            String s;
//            if (i == line-1){
//                s = content.substring(i*lineTextCount,content.length());
//            }else {
//                s = content.substring(i * lineTextCount,(i+1)*lineTextCount);
//            }
//            paint.getTextBounds(content,0,s.length(),bounds);
//
//            canvas.drawText(s,QR_WIDTH/2-bounds.width()/2,textTop+i*contentTextSize+i*5+bounds.height()/2,paint);
//        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return result;
    }
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
