package com.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wang.zhonghao on 2017/7/25.
 */

public class TriangleView extends View {

    private Paint paint;
    private Paint textPaint;
    private String text;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        text = "红";
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24f);
//        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        int status = getStatusBarHeight(getContext());
//        canvas.translate(0, -status);
        int[] points = new int[2];
        getLocationInWindow(points);

        int left = points[0];
        int top = points[1];

        int width = this.getWidth();
        int height = this.getHeight();

        int left1 = this.getLeft();
        int right1 = this.getRight();


        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.close();
        canvas.drawPath(path, paint);
        canvas.drawText(text, this.getWidth() / 2, this.getHeight() / 3, textPaint);
    }

    public int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = 60;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = 60;
        }
        setMeasuredDimension(width, height);
    }

    public void makeRed() {
        text = "红";
        paint.setColor(Color.RED);
        invalidate();
    }

    public void makeYellow() {
        text = "黄";
        paint.setColor(Color.YELLOW);
        invalidate();
    }
}
