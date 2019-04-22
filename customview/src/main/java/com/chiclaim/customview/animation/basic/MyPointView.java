package com.chiclaim.customview.animation.basic;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by chiclaim on 2016/06/12
 */
public class MyPointView extends View {

    private Paint paint = new Paint();
    private Point mCurPoint;


    public MyPointView(Context context) {
        super(context);
    }

    public MyPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //if (mCurPoint != null) {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(300, 300, mCurPoint == null ? 100 : mCurPoint.getRadius(), paint);
        //}
    }

    public void clickPoint() {
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), new Point(20), new Point(200));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }

    /**
     * 用于ObjectAnimator自定义属性[ObjectAnimator通过反射调用]
     * @param radius
     */
    private void setRadius(int radius) {
        if (mCurPoint == null) {
            mCurPoint = new Point(radius);
        } else {
            mCurPoint.setRadius(radius);
        }
        invalidate();
    }

    private int getRadius(){
        return 20;
    }


    public static class Point {
        private int radius;

        public Point(int radius) {
            this.radius = radius;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }
    }

    public static class PointEvaluator implements TypeEvaluator<Point> {


        private Point point;

        public PointEvaluator() {
            point = new Point(0);
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            int start = startValue.getRadius();
            int end = endValue.getRadius();
            int result = (int) (start + (end - start) * fraction);
            point.setRadius(result);
            return point;
            //return new Point(result); 避免创建不必要的对象
        }
    }

}
