package com.mercury.swipelayout.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Mercury on 2016/8/12.
 * 侧滑删除的布局，可以放在listview或recyclerview中使用
 */
public class SwipeLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private ViewGroup      mFrontLayout;      //前布局
    private ViewGroup      mBackLayout;       //后布局
    private int            mWidth;      //该控件显示在屏幕内的宽度
    private int            mHeight;     //该控件显示在屏幕内的高度
    private int            mRange;      //拖拽范围,也就是后布局的宽度

    boolean isOpen = false;   //控件默认的状态(后布局是否显示),关闭
    private Status status = Status.Close;

    public enum Status {
        Open,
        Close,
        Swiping
    }

    //拖动行为中的状态监听
    OnSwipeListener mOnSwipeListener;

    public interface OnSwipeListener {
        void onClose(SwipeLayout layout);

        void onOpen(SwipeLayout layout);

        void onStartOpen(SwipeLayout layout);
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        mOnSwipeListener = onSwipeListener;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //是否要捕捉滑动事件
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //第二个参数left是当前滑动的view的左端滑动到的位置的x坐标
            if (child == mFrontLayout) {
                //当前滑动发生在前布局，控制其拖拽范围
                if (left < -mRange) {
                    left = -mRange;
                } else if (left > 0) {
                    left = 0;
                }
            } else if (child == mBackLayout) {
                //当前滑动发生在后布局
                if (left < mWidth - mRange) {
                    left = mWidth - mRange;
                } else if (left > mWidth) {
                    left = mWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mFrontLayout) {
                //如果前布局位置发生变化,传递给后布局
//                int i = mBackLayout.getLeft();
                ViewCompat.offsetLeftAndRight(mBackLayout,dx);
            } else if (changedView == mBackLayout) {
                ViewCompat.offsetLeftAndRight(mFrontLayout, dx);
            }

            //随着状态变化需要更新的
            dispatchChangeEvent();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0 && mFrontLayout.getLeft() < -mRange / 2.0) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }
        }
    };

    private void dispatchChangeEvent() {
        Status lastStatus = status;
        //更新控件的状态
        status = updateStatus();

        //状态发生变化时,才执行监听回调
        if (lastStatus != status && mOnSwipeListener != null) {
            if (status == Status.Close) {
                mOnSwipeListener.onClose(this);
            } else if (status == Status.Open) {
                mOnSwipeListener.onOpen(this);
            } else {
                if (lastStatus == Status.Close) {
                    //最新状态是滑动中,且上一个状态是关闭
                    mOnSwipeListener.onStartOpen(this);
                }
            }
        }
    }

    private Status updateStatus() {
        int left = mFrontLayout.getLeft();
        Log.e("status", left + "");
        if (left == -mRange) {
            return Status.Open;
        } else if (left == 0) {
            return Status.Close;
        } else {
            return Status.Swiping;
        }

    }

    public void open() {
        open(true);
    }

    public void open(boolean isSmooth) {
        if (isSmooth) {
            //触发平滑动画
            if (mDragHelper.smoothSlideViewTo(mFrontLayout, -mRange, 0)) {
                //drawChild->child.draw->computeScroll
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            isOpen = true;
            layoutContent(isOpen);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        if (isSmooth) {
            //触发平滑动画
            if (mDragHelper.smoothSlideViewTo(mFrontLayout, 0, 0)) {
                //drawChild->child.draw->computeScroll
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {

            isOpen = false;
            layoutContent(isOpen);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //渲染完毕后得到控件中的前布局和后布局，指定在布局中从上至下为前布局和侧滑出的后布局
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mFrontLayout = (ViewGroup) getChildAt(0);
        mBackLayout = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定子控件的位置

        layoutContent(isOpen);
    }


    /**
     * 根据控件的开关状态布局内容
     * @param isOpen
     */
    private void layoutContent(boolean isOpen) {
        //摆放 前布局
        Rect frontRect = computeFrontRect(isOpen);
        mFrontLayout.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);

        //摆放 后布局
        Rect backRect = computeBackRect(frontRect);
        mBackLayout.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        //将指定控件前置
        bringChildToFront(mFrontLayout);
    }

    //计算前布局矩形区域
    private Rect computeFrontRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, 0 + mHeight);
    }

    //计算后布局矩形区域
    private Rect computeBackRect(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, 0 + mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRange = mBackLayout.getMeasuredWidth();
    }
}
