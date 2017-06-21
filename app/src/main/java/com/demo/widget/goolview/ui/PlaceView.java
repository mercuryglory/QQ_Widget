package com.demo.widget.goolview.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 创建者:    wang.zhonghao
 * 创建时间:  2017/6/16
 * 描述:      固定的红点 在布局中使用，起占位的作用
 */
public class PlaceView extends TextView {

    private StickyView stickyTestView;
    Paint paint;
    Paint textPaint;

    float centerX;
    float centerY;
    float radius;
    int itemHeight;

    String text;

    public PlaceView(Context context) {
        this(context, null);
    }

    public PlaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(18f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    public enum Status {
        NORMAL,DISAPPEAR
    }

    Status currentStatus = Status.NORMAL;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (currentStatus) {
            case NORMAL:
//                this.setBackgroundResource(R.drawable.red_bg);
                paint.setColor(Color.RED);
                textPaint.setColor(Color.WHITE);
                Log.e("placeview", radius + "");
                canvas.drawCircle(centerX, centerY, radius, paint);
                canvas.drawText(text, centerX, centerY + radius / 4, textPaint);
                break;
            case DISAPPEAR:
//                this.setBackgroundColor(Color.TRANSPARENT);
                paint.setColor(Color.TRANSPARENT);
                textPaint.setColor(Color.TRANSPARENT);
                canvas.drawCircle(centerX, centerY, radius, paint);
                canvas.drawText(text, centerX, centerY + radius / 4, textPaint);
            default:
                break;

        }

    }

    public void setStatus(Status status) {
        currentStatus = status;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //将自身的位置参数传递给粘性控件
                stickyTestView.setLayout(PlaceView.this, text);
                Log.e("mercury", "down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("mercury", "move");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("mercury", "up");
                break;

            default:
                break;

        }
        stickyTestView.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = this.getWidth() / 2;

    }

    public StickyView createView(Context context, int height) {
        //得到listview条目的高度，重新计算布局
        itemHeight = height;
        requestLayout();
        stickyTestView = new StickyView(context);
        return stickyTestView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Rect bounds = new Rect();
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            textPaint.getTextBounds(this.getText().toString(), 0, this.getText().length(), bounds);
//            width = bounds.width() + 50 + getPaddingLeft() + getPaddingRight();
            width = itemHeight/3;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            textPaint.getTextBounds(this.getText().toString(), 0, this.getText().length(), bounds);
//            height = bounds.width() + 50 + getPaddingTop() + getPaddingBottom();
            height = itemHeight/3;
        }
        Log.e("measure", width + "-----" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = (right - left) / 2;
        centerY = (bottom - top) / 2;
    }
}
