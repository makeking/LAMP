package com.bete.lamp.customWidget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bete.lamp.R;

/**
 * TODO: document your custom view class.
 */
public class ProgressSeek extends View {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    /**
     * 进度条的宽度
     */
    private int view_width;

    /**
     * 画布的宽度
     */
    private int view_base_width;

    /**
     * 控件的宽度
     */
    private int view_edge_width;

    /**
     * 进度
     */
    private int progress =50;

    private Canvas cacheCanvas;

    /**
     * 背景颜色的画笔
     */
    private Paint backgroundPaint;
    /**
     * 进度条的画笔
     */
    private Paint progressPaint;

    /**
     * 进度末端的图
     */
    private Bitmap bitmap;

    private int bitmapWidth;
    private int bitmapHeight;

    private Context context;


    //渐变色开始
    private static final int DEFAULT_START_COLOR = Color.parseColor("#4F607D");

    //渐变色结束
    private static final int DEFAULT_END_COLOR = Color.parseColor("#E5E5EA");

    /**
     * 缓存图片
     */
    private Bitmap cacheBitmap;

    public ProgressSeek(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public ProgressSeek(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public ProgressSeek(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int finalWidth = getMeasuredLength(widthMeasureSpec, true);
        int finalHeight = getMeasuredLength(heightMeasureSpec, false);
        setMeasuredDimension(finalWidth, finalHeight);
    }

    private int getMeasuredLength(int length, boolean isWidth) {
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            size = isWidth ? padding + DEFAULT_WIDTH  : DEFAULT_HEIGHT + padding;//提供一个默认的值
            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }
        return size;
    }

    private void init(AttributeSet attrs, int defStyle) {
//        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressSeek, defStyle, 0);
//
//        mExampleString = a.getString(R.styleable.ProgressSeek_exampleString);
//        mExampleColor = a.getColor(R.styleable.ProgressSeek_exampleColor,mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(R.styleable.ProgressSeek_exampleDimension,mExampleDimension);
//
//        if (a.hasValue(R.styleable.ProgressSeek_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(R.styleable.ProgressSeek_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }
//        a.recycle();
//
//        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
//        invalidateTextPaintAndMeasurements();
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lamp_selfcheck_process_thumb);
//        bitmap = getRoundedCornerBitmap(Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_8888),360);
//        bitmapWidth = bitmap.getWidth();
//        bitmapHeight = bitmap.getHeight();

        backgroundPaint = new Paint();
//        backgroundPaint.setStrokeWidth(bitmapWidth);
//        backgroundPaint.setColor(Color.parseColor("#cc0000"));
//        backgroundPaint.setDither(true);
//        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
//        progressPaint.setStrokeWidth(bitmapWidth);
//        progressPaint.setDither(true);
//        progressPaint.setAntiAlias(true);


//        DisplayMetrics d = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(d);
//        view_base_width = d.widthPixels;
        // Update TextPaint and text measurements from attributes
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
        final Paint paint = new Paint();
        paint.setStrokeWidth(bitmap.getWidth()/2);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }


    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        // TODO: consider storing these as member variables to reduce
//        // allocations per draw cycle.
//        int paddingLeft = getPaddingLeft();
//        int paddingTop = getPaddingTop();
//        int paddingRight = getPaddingRight();
//        int paddingBottom = getPaddingBottom();
//
//        int contentWidth = getWidth() - paddingLeft - paddingRight;
//        int contentHeight = getHeight() - paddingTop - paddingBottom;
//
//        // Draw the text.
//        canvas.drawText(mExampleString,paddingLeft + (contentWidth - mTextWidth) / 2,paddingTop + (contentHeight + mTextHeight) / 2,mTextPaint);
//
//        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }


        bitmap = getRoundedCornerBitmap(Bitmap.createBitmap((this.getHeight()-getPaddingTop()-getPaddingBottom())/2, (this.getHeight()-getPaddingTop()-getPaddingBottom())/2, Bitmap.Config.ARGB_8888),360);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();

        backgroundPaint.setStrokeWidth(2*bitmapWidth);
        backgroundPaint.setColor(Color.parseColor("#356A86"));
        backgroundPaint.setDither(true);
        backgroundPaint.setAntiAlias(true);

        progressPaint.setStrokeWidth(bitmapWidth);
        progressPaint.setDither(true);
        progressPaint.setAntiAlias(true);

        Paint bmpPaint = new Paint();
        //将cacheBitmap绘制到该View组件
        if (cacheBitmap != null) {
            canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        }
        view_edge_width = this.getWidth()-getPaddingRight()-getPaddingLeft()-bitmapWidth;
//        Log.e("打出来看看控件的宽度:", view_edge_width + "  "+this.getWidth() + " "+ getPaddingRight() +" " + this.getHeight()+ "  "+getPaddingTop() + " " + bitmapWidth + " "+ bitmapHeight);
//        setProgress(progress);

        //        if (view_width == 0) {//第一上来
//           /*DisplayMetrics d = new DisplayMetrics();
//            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(d);
//            view_width = d.widthPixels*progress/100;*/
//            view_width = (view_base_width-getPaddingRight()-getPaddingLeft()) * progress / 100;
//        } else {
        view_width = view_edge_width * progress / 100;
//        }
        if (cacheBitmap != null) {
            if (!cacheBitmap.isRecycled()) {
                cacheBitmap.recycle();
                cacheBitmap = null;
            }
            cacheCanvas = null;
        }

        cacheBitmap = Bitmap.createBitmap(this.getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        if (cacheCanvas == null) {
            cacheCanvas = new Canvas();
            cacheCanvas.setBitmap(cacheBitmap);
        }

        /**
         * 画背景
         */
        RectF r = new RectF();
        r.left = getPaddingLeft();
        r.top = getPaddingTop();
        r.right = getWidth()-getPaddingRight();//+getPaddingLeft()+getPaddingRight()
        r.bottom = getHeight()-getPaddingBottom();
        cacheCanvas.drawRoundRect(r, bitmapWidth, bitmapHeight, backgroundPaint);

        if (progress > 0) {
            LinearGradient lg = new LinearGradient(bitmapWidth, 0, view_width, bitmapWidth, DEFAULT_START_COLOR, DEFAULT_END_COLOR, Shader.TileMode.CLAMP);
            progressPaint.setShader(lg);
            RectF r1 = new RectF();
            r.left = getPaddingLeft()+bitmapWidth/2;
            r.top = getPaddingTop()+bitmapHeight/2;
            r.right = view_width+getPaddingLeft()+bitmapWidth/2;
            r.bottom = getHeight()-getPaddingBottom()-bitmapHeight/2;
            cacheCanvas.drawRoundRect(r, bitmapWidth/2, bitmapHeight/2, progressPaint);
            cacheCanvas.drawBitmap(bitmap, view_width+getPaddingLeft()-bitmapHeight/2, getPaddingLeft()+bitmapWidth/2+1, new Paint());
        }
    }

    public int getProgress() {
        return progress;
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
