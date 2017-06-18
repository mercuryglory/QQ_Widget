package com.demo.widget.goolview.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private Context mContext;

    public PlaceView(Context context) {
        this(context, null);
    }

    public PlaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(15);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    enum Status {
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
                canvas.drawCircle(centerX, centerY, radius, paint);
                canvas.drawText("1", centerX, centerY + radius / 3, textPaint);
                break;
            case DISAPPEAR:
//                this.setBackgroundColor(Color.TRANSPARENT);
                paint.setColor(Color.TRANSPARENT);
                textPaint.setColor(Color.TRANSPARENT);
                canvas.drawCircle(centerX, centerY, radius, paint);
                canvas.drawText("1", centerX, centerY + radius / 3, textPaint);
            default:
                break;

        }

    }

    public void setStatus(Status status) {
        currentStatus = status;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                stickyTestView = new StickyTestView(mContext);
//                stickyTestView.setLayout(TextGooView.this);
                //更新粘性控件将要绘制的位置
                stickyTestView.setLayout(PlaceView.this);
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
        int[] points = new int[2];
        this.getLocationInWindow(points);
        radius                                   = this.getWidth() / 2;
        int x = points[0] + this.getWidth() / 2;
        int y = points[1] + this.getHeight() / 2;
        Log.e("mercuryonsize", points[0] + "-----" + points[1] + "-----" + this.getWidth() + "-----" +
                this.getHeight());
        if (mlistener != null) {
            mlistener.create(x, y);
        }
    }

    MeasureListener mlistener;

    public interface MeasureListener {

        void create(float centerX, float centerY);

    }

    public void setMeasureListener(MeasureListener listener) {
        mlistener = listener;
    }


    public StickyView createView(Context context) {
        stickyTestView = new StickyView(context);
        return stickyTestView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = (right - left) / 2;
        centerY = (bottom - top) / 2;
    }
}
