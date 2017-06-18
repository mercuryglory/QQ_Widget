package com.demo.widget.goolview.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

import com.demo.widget.goolview.util.GeometryUtil;
import com.demo.widget.goolview.util.Utils;

/**
 * Created by Mercury on 2016/8/12.
 * 粘性控件
 * 真正可以拖动显示效果的，应该是全局的
 */
public class StickyView extends View {

    private Paint paint;        //绘制控件圆形的画笔
    private int   statusBarHeight;    //状态栏高度
    private float mTempStickRadius;
    private boolean isOutOfRange = false;       //是否超出范围
    private boolean isDisappear  = false;       //控件是否不可见
    private Paint textPaint;    //绘制文字的画笔
    private String text = "1";  //控件内显示的文本

    private boolean DEBUG = false;

    private Context mContext;

    private WindowManager              mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private PlaceView                  mTextGooView;

    private float centerX;
    private float centerY;

    private Handler handler = new Handler();

    public void setLayout(PlaceView textGooView) {
        mTextGooView = textGooView;
        int[] points = new int[2];
        mTextGooView.getLocationInWindow(points);
        //根据占位红点的位置得到自身应该绘制的位置
        centerX = points[0] + mTextGooView.getWidth() / 2;
        centerY= points[1] + mTextGooView.getHeight() / 2;
        Log.e("mercurytest", centerX + "-----" + centerY);

        mDragCenter  = new PointF(centerX, centerY);        //拖拽圆圆心初始值（随手势变化）
        mStickCenter = new PointF(centerX, centerY);       //固定圆圆心

        mDragPoints = new PointF[]{        //拖拽圆的两个切点初始值
                new PointF(centerX, centerY),      //点2
                new PointF(centerX, centerY)       //点3
        };

        mStickPoints = new PointF[]{       //固定圆的两个切点初始值
                new PointF(centerX, centerY),     //点1
                new PointF(centerX, centerY)      //点4
        };

        mControlPoint  = new PointF(centerX, centerY);
        mDragRadius = mTextGooView.getWidth() / 2;
        mStickRadius = mTextGooView.getWidth() / 2;
    }


    public StickyView(Context context) {
        this(context, null);
    }

    public StickyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSLUCENT;

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(15f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);


    }

    PointF mDragCenter;         //拖拽圆圆心初始值（随手势变化）
    float  mDragRadius;                            //拖拽圆半径
    PointF mStickCenter;        //固定圆圆心
    float  mStickRadius;                           //固定圆半径（随手势变化）

    PointF[] mDragPoints;

    PointF[] mStickPoints;

    PointF mControlPoint ;          //控制点
    float  farestDistance = 100f;     //边界值，控制拖拽圆的拖拽范围

    @Override
    protected void onDraw(Canvas canvas) {

        //计算绘制图形所需的变量
        computePoints();

        //        canvas.save();
        //向上平移状态栏的高度
        canvas.translate(0, -statusBarHeight);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Log.e("canvas", width + "-----" + height);
        Rect rect = canvas.getClipBounds();
//        rect.left = 0;
//        rect.top = 0;
//        rect.right = 480;
//        rect.bottom = 800;
//        canvas.clipRect(0,0,50,50);
        Log.e("left", rect.left+"");
        Log.e("top", rect.top+"");
        Log.e("right", rect.right+"");
        Log.e("bottom", rect.bottom+"");

        //绘制内容
        drawContent(canvas);
        //        canvas.restore();

    }

    private void computePoints() {
        //1,计算固定圆的实时半径
        mTempStickRadius = computeStickRadius();

        //2,计算控制点坐标，为了绘制Path 贝塞尔曲线
        mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);

        //3,计算四个切点坐标，为了绘制固定圆和拖拽圆之间的连接部分
        Double lineK = null;
        double yOffset = mStickCenter.y - mDragCenter.y;
        double xOffset = mStickCenter.x - mDragCenter.x;

        if (xOffset != 0) {
            lineK = yOffset / xOffset;
        }
        //得到拖拽圆的两个切点坐标
        mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
        //得到固定圆的两个切点坐标
        mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter, mTempStickRadius, lineK);


    }

    FloatEvaluator floatEvaluator = new FloatEvaluator();

    //计算随手势变化的固定圆半径
    private float computeStickRadius() {
        //距离:0.0->80f  ——  半径:15f->4f
        //计算固定圆和拖拽圆圆心之间的距离，最大不超过给定的范围值
        float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
        d = Math.min(d, farestDistance);
        float percent = d / farestDistance;
        //根据距离百分比平滑算出实时的固定圆半径
        return floatEvaluator.evaluate(percent, mStickRadius, mStickRadius * 0.4f);
    }

    private void drawContent(Canvas canvas) {
        //绘制最大范围的一个圆，只是为了显示效果更直观，实际使用中不用绘制
        if (DEBUG) {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, paint);
        }
        paint.setStyle(Paint.Style.FILL);

        if (!isDisappear) {
            //如果还没有放手并且拖拽圆还在边界范围内
            if (!isOutOfRange) {

                //绘制连接部分 贝塞尔曲线
                Path path = new Path();
                path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
                path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x, mDragPoints[0].y);
                path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
                path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
                canvas.drawPath(path, paint);

                Log.e("stickRadius", mTempStickRadius+"");
                //绘制固定圆
                canvas.drawCircle(mStickCenter.x, mStickCenter.y, mTempStickRadius, paint);
                Log.e("back", mStickCenter.x + "-----" + mStickCenter.y);
            }
            //绘制拖拽圆
            canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, paint);
            //绘制拖拽圆中的文字
            canvas.drawText(text, mDragCenter.x, mDragCenter.y + mDragRadius / 3.0f, textPaint);
        }

    }

    public void setTextNumber(String textNumber) {
        text = textNumber;
    }

    public void backToLayout() {
//        isDisappear = false;
        isOutOfRange = false;
        mDragCenter = new PointF(centerX, centerY);        //拖拽圆圆心初始值（随手势变化）
//        mStickRadius = 10f;
        invalidate();
        mTextGooView.setStatus(PlaceView.Status.NORMAL);
    }

    float x;
    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOutOfRange && isDisappear) {
            Log.e("status", "彻底消失");
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                Log.e("down", event.getRawX() + "------" + event.getRawY());
                Log.e("stickview", mStickRadius + "");
                //按下的时候将该控件添加到悬浮窗口，并且占位的红点应该消失
                removePoint();
                mWindowManager.addView(StickyView.this, mLayoutParams);
                        mTextGooView.setStatus(PlaceView.Status.DISAPPEAR);

                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                //更新拖拽圆圆心的坐标
                updateDragCenter(x, y);
                Log.e("moveCenter", mStickCenter.x + "------" + mStickCenter.y);

                float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                if (distance > farestDistance) {
                    isOutOfRange = true;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("parent_up", mDragCenter.x + "-----" + mDragCenter.y);

                //只要在拖拽的过程中拖拽圆超出范围,而不管是不是抬手的时候超出了
                if (isOutOfRange) {
                    float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                    if (d > farestDistance) {
                        Log.e("status", "真的超出了");
                        isDisappear = true;
                        mTextGooView.setStatus(PlaceView.Status.DISAPPEAR);
                        removePoint();
                    } else {
                        Log.e("status", "还好回去了");
                        // updateDragCenter(mStickCenter.x, mStickCenter.y);
                        backToLayout();
                        removePoint();
                    }
                } else {
                    //拖拽圆和固定圆之间的连线还是可见的情况下放手，回弹效果
                    final PointF start = new PointF(mDragCenter.x, mDragCenter.y);
                    final PointF end = new PointF(mStickCenter.x, mStickCenter.y);
                    ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
                    animator.setInterpolator(new OvershootInterpolator(5.0f));
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            //动画进度值
                            float fraction = animation.getAnimatedFraction();
                            //动画过程中拖拽圆圆心的实时坐标
                            PointF p = GeometryUtil.getPointByPercent(start, end, fraction);
                            Log.e("fraction", p.x + "-----" + p.y);
                            updateDragCenter(p.x, p.y);
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isOutOfRange = false;
                            mTextGooView.setStatus(PlaceView.Status.NORMAL);
                            removePoint();
                        }
                    });
                    //默认动画时长就是300ms
                    animator.setDuration(300);
                    animator.start();
                    Log.e("status", "根本就没有超出");

                }

                break;

        }
        return true;
    }

    private void updateDragCenter(float x, float y) {
        mDragCenter.set(x, y);
        invalidate();
    }

    public void removePoint() {
        if (getParent() != null) {
            mWindowManager.removeViewImmediate(StickyView.this);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context
                .WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenWidth1 = dm.widthPixels;
        int screenHeight1 = dm.heightPixels;

        statusBarHeight = Utils.getStatusBarHeight(this);
    }


}

