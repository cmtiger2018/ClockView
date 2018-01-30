package com.cmtiger.clockview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cmtiger.clockview.R;

import java.util.Calendar;

/**
 * Created by yuxingxing on 2018/1/22.
 */

public class ClockView extends View {

    //表盘边距
    private int mPadding;

    //秒针宽度及颜色
    private float mSecondPointWidth;
    private float mSecondPointColor;

    //分针宽度及颜色
    private float mMinutePointWidth;
    private float mMinutePointColor;

    //时针宽度及颜色
    private float mHourPointWidth;
    private float mHourPointColor;

    //表盘刻度长线及短线颜色
    private int mColorLong;
    private int mColorShort;

    //表盘刻度长线及短线颜色
    private float mWidthLong;
    private float mWidthShort;

    private int mTextSize;

    private Paint mPaint;

    private int mCircleWith = 4;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        obtainStyleAttr(attrs);
        initView();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true); //抗抖动

    }

    private void obtainStyleAttr(AttributeSet attrs) {
        TypedArray arr = null;

        try {

            arr = getContext().obtainStyledAttributes(attrs, R.styleable.ClockBoard);
            mHourPointWidth = arr.getDimension(R.styleable.ClockBoard_cb_hour_pointer_width, dip2px(5));
            mMinutePointWidth = arr.getDimension(R.styleable.ClockBoard_cb_minute_pointer_width, dip2px(5));
            mSecondPointWidth = arr.getDimension(R.styleable.ClockBoard_cb_second_pointer_width, dip2px(5));

            mHourPointColor = arr.getDimension(R.styleable.ClockBoard_cb_hour_pointer_color, Color.BLACK);
            mMinutePointColor = arr.getDimension(R.styleable.ClockBoard_cb_minute_pointer_color, Color.BLACK);
            mSecondPointColor = arr.getDimension(R.styleable.ClockBoard_cb_second_pointer_color, Color.RED);

            mWidthLong = (int) arr.getDimension(R.styleable.ClockBoard_cb_width_long, dip2px(8));
            mWidthShort = (int) arr.getDimension(R.styleable.ClockBoard_cb_width_short, dip2px(5));
            mColorLong =(int) arr.getDimension(R.styleable.ClockBoard_cb_color_long, Color.BLACK);
            mColorShort =(int) arr.getDimension(R.styleable.ClockBoard_cb_color_short, Color.BLACK);



        } catch (Exception e) {

        } finally {
            if (arr != null) {
                arr.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 1000;
        int height = 1000;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec); //获取Mode
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取Mode
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //获取宽度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);////获取高度



        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {

            try {
                throw new RuntimeException("参数错误");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Log.e("yxx", widthMode+"----true");
            //widthMode ----AT_MOST
        } else {

            if (widthMode == MeasureSpec.EXACTLY) {

                width = Math.min(widthSize, width);
            }

            if (heightMode == MeasureSpec.EXACTLY) {

                height = Math.min(heightSize, width);
            }


            Log.e("yxx", widthMode+"----false");
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mWidth = Math.min(getWidth(), getHeight());
        int mHeight = Math.max(getWidth(), getHeight());


        canvas.save();


        drawCirCle(canvas, mWidth, mHeight);
        drawScale(canvas);

        drawSecond(canvas);
        canvas.restore();

    }

    /**
     * 绘制外圆
     * @param canvas
     * @param mWidth
     * @param mHeight
     */
    private void drawCirCle(Canvas canvas, int mWidth, int mHeight) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mCircleWith, mPaint);
    }

    /**
     * 绘制刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas) {

        canvas.save();

        mPaint.setStrokeWidth(dip2px(1));

        int lineWidth = 0;

        for (int i=0 ;i<60; i++) {

            if (i % 5 == 0) { //整点 画长线
                mPaint.setStrokeWidth(dip2px(1.5f));
                mPaint.setColor(Color.BLACK);
                lineWidth = 40;

                int text = (i / 5) == 0 ? 12 : i / 5;
                drawText(canvas, String.valueOf(text));
            } else {// 非整点 画短线

                lineWidth = 30;
                mPaint.setColor(Color.BLUE);
                mPaint.setStrokeWidth(dip2px(1));

            }

            canvas.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 2, getWidth() / 2, getHeight()/ 2- getWidth() /2 + lineWidth , mPaint);
            canvas.rotate(6, getWidth() / 2, getHeight() / 2);
        }

        canvas.restore();
    }

    private void drawText() {

    }

    /**
     * 绘制文字
     * @param canvas
     * @param text
     */
    private void drawText(Canvas canvas, String text) {

        mTextSize = 24;
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);


        Rect textBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textBound);

        int textTop = textBound.bottom - textBound.top;
        int textWidth = textBound.right - textBound.left;

        canvas.save();

        int startX = getWidth() / 2 - textWidth / 2;
        int startY = getHeight() / 2 - getWidth() / 2 + dip2px(35);

        canvas.drawText(text, startX, startY, mPaint);
        canvas.rotate(360 / 12, getWidth() / 2, getHeight() / 2);
        canvas.restore();

    }

    /**
     * 绘制秒针
     */
    private void drawSecond(Canvas canvas) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(dip2px(2));

        int angleSecond = second * 360 / 60;

        canvas.save();

        canvas.rotate(angleSecond);
        canvas.drawLine(getWidth() / 2, getWidth()/2, getWidth()/2 ,getHeight() / 2 - getWidth() / 2, mPaint);

        canvas.restore();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 绘制分针
     */
    private void drawMinute() {

    }

    /**
     * 绘制时针
     */
    private void drawHour() {

    }


    private int px2dip(float pxValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int dip2px(float pxValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }


}
