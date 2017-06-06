package com.mercury.goolview.ui;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.mercury.goolview.util.GeometryUtil;
import com.mercury.goolview.util.Utils;

/**
 * Created by Mercury on 2016/8/12.
 */
public class GooView extends View {

    private Paint paint;
    private int statusBarHeight;
    private float mTempStickRadius;
    private boolean isOutOfRange=false;       //是否超出范围
    private boolean isDisappear=false;       //是否超出范围
    private Paint textPaint;

    public GooView(Context context) {
        this(context,null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(15f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    PointF mDragCenter = new PointF(80f, 80f);          //拖拽圆圆心
    float mDragRadius = 14f;            //拖拽圆半径
    PointF mStickCenter = new PointF(150f, 150f);       //固定圆圆心
    float mStickRadius = 10f;           //固定圆半径

    PointF[] mDragPoints = new PointF[]{        //拖拽圆的两个附着点
            new PointF(50f, 250f),      //点2
            new PointF(50f, 350f)       //点3
    };

    PointF[] mStickPoints = new PointF[]{
            new PointF(250f, 250f),     //点1
            new PointF(250f, 350f)      //点4
    };

    PointF mControlPoint = new PointF(150f, 300f);          //控制点

    @Override
    protected void onDraw(Canvas canvas) {
        //计算变量
        computePoints();

        canvas.save();
        //向上平移状态栏的高度
        canvas.translate(0, -statusBarHeight);

        //绘制内容
        drawContent(canvas);

        canvas.restore();
        canvas.drawCircle(0,0,5f,paint);

    }

    private void computePoints() {
        //3,计算固定圆的半径
        mTempStickRadius = computeStickRadius();

        //1,一个控制点坐标
        mControlPoint= GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);
        //2,四个附着点坐标
        Double lineK = null;
        double yOffset = mStickCenter.y - mDragCenter.y;
        double xOffset = mStickCenter.x - mDragCenter.x;

        if (xOffset != 0) {

            lineK = yOffset / xOffset;
        }
        mDragPoints=GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius,lineK);
        mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter, mTempStickRadius, lineK);


    }

    FloatEvaluator floatEvaluator = new FloatEvaluator();
    float farestDistance = 80f;
    //计算临时的固定圆半径
    private float computeStickRadius() {
        //距离:0.0->80f
        //半径:10f->4f
        float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
        d = Math.min(d, farestDistance);
        float percent = d / farestDistance;
        return floatEvaluator.evaluate(percent, mStickRadius, mStickRadius * 0.4f);
    }

    private void drawContent(Canvas canvas) {
        //绘制最大范围
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, paint);
        paint.setStyle(Paint.Style.FILL);

        if (!isDisappear) {
            if (!isOutOfRange) {

                //绘制连接部分
                Path path = new Path();
                path.moveTo(mStickPoints[0].x,mStickPoints[0].y);
                path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x, mDragPoints[0].y);
                path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
                path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
                canvas.drawPath(path,paint);

                //绘制固定圆
                canvas.drawCircle(mStickCenter.x,mStickCenter.y,mTempStickRadius,paint);
            }
            //绘制拖拽圆
            canvas.drawCircle(mDragCenter.x,mDragCenter.y,mDragRadius,paint);
            canvas.drawText("66",mDragCenter.x,mDragCenter.y+mDragRadius/3.0f, textPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDisappear = false;
                isOutOfRange = false;
                x = event.getRawX();
                y = event.getRawY();
                updateDragCenter(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                updateDragCenter(x, y);

                float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                if (distance > farestDistance) {
                    isOutOfRange = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isOutOfRange) {
                    float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                    if (d > farestDistance) {
                        isDisappear = true;
                        invalidate();
                    } else {
                        updateDragCenter(mStickCenter.x, mStickCenter.y);
                    }
                } else {
                    final PointF start = new PointF(mDragCenter.x, mDragCenter.y);
                    ValueAnimator animator = ValueAnimator.ofFloat(100f);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float fraction = animation.getAnimatedFraction();
                            PointF p = GeometryUtil.getPointByPercent(start, mStickCenter, fraction);
                            updateDragCenter(p.x, p.y);
                        }
                    });
                    animator.setInterpolator(new OvershootInterpolator(2));
                    animator.setDuration(500);
                    animator.start();
                }
                break;

        }
        return true;
    }

    private void updateDragCenter(float x, float y) {
        mDragCenter.set(x, y);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        statusBarHeight = Utils.getStatusBarHeight(this);
    }
}
