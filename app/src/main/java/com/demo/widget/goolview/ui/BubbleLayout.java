package com.demo.widget.goolview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 创建者:    wang.zhonghao
 * 创建时间:  2017/6/19
 * 描述:      ${TODO}
 */
public class BubbleLayout extends FrameLayout {
    private float centerX;
    private float centerY;
    private boolean isMeasure;


    public BubbleLayout(Context context) {
        super(context);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View child = getChildAt(0);
        if (child != null && child.getVisibility() != GONE) {
            child.layout((int) centerX - 34, (int) centerY - 34, (int) centerX + 34, (int)
                    centerY + 34);
        }

    }



    public void setCenter(float x, float y) {
        centerX = x;
        centerY = y;
        isMeasure = true;
//        requestLayout();
    }
}
