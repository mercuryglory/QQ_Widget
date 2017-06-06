package com.mercury.parallex.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Mercury on 2016/8/12.
 */
public class ParallaxListView extends ListView {
    private ImageView iv_image;
    private int drawableHeight;     //图片原始高度
    private int originalHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setParallaxImage(ImageView iv_image) {
        this.iv_image = iv_image;
        Drawable drawable = this.iv_image.getDrawable();

        //获取图片原本的高度
        drawableHeight = drawable.getIntrinsicHeight();

        originalHeight = iv_image.getHeight();
        int measuredHeight = iv_image.getMeasuredHeight();
        System.out.println("measuredHeight: " + measuredHeight + " originalheight: " + originalHeight);
    }

    /**
     * 任何view,滑动到边缘,继续滑动时,此方法会被触发
     * @param deltaY  竖直方向的瞬时偏移量,从顶端往下拉-,速度越快绝对值越大
     * @param scrollY 竖直方向滚动量
     * @param scrollRangeY 超出滚动后,恢复时的滚动位置
     * @param maxOverScrollY  最大超出滚动距离
     * @param isTouchEvent 是否触摸滑动到边缘,true 触摸 false 惯性
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
            scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
            isTouchEvent) {
        //将下拉的瞬时偏移量累加给头布局
        if (deltaY < 0 && isTouchEvent) {
            int newHeight = (int) (iv_image.getHeight() + Math.abs(deltaY/3.0f));
            if (newHeight <= drawableHeight) {

                updateImageHeight(newHeight);
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    private void updateImageHeight(int newHeight) {
        iv_image.getLayoutParams().height = newHeight;
        iv_image.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //抬起手指,弹回去
            int startHeight = iv_image.getHeight();
            int endHeight = originalHeight;
            System.out.println(startHeight);

            ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    int newHeight = (int) animation.getAnimatedValue();
//                    System.out.println("fraction: " + fraction + " value: " + newHeight);

                    updateImageHeight(newHeight);
                }
            });
            animator.setInterpolator(new OvershootInterpolator(4));

            animator.setDuration(500);
            animator.start();
        }

        return super.onTouchEvent(ev);
    }
}
