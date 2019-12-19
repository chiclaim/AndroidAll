package com.module.chiclaim.com.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private float initialXValue;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(event) && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return (IsSwipeAllowed(event))
                && super.onInterceptTouchEvent(event);
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0) {
                    // swipe from left to right detected
                    //return false;
                } else if (diffX < 0 && getCurrentItem() == 3) {
                    // swipe from right to left detected
                    return false;
                }
                //Log.e("CustomViewPager", diffX + "==============");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

}