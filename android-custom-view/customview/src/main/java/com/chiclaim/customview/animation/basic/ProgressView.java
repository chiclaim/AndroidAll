package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.RotateAnimation;

import androidx.appcompat.widget.AppCompatImageView;


public class ProgressView extends AppCompatImageView {
    private static final int FRAME_COUNT = 12;
    private static final int DURATION = 1000;
    private RotateAnimation rotateAnimation;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAnimation(attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAnimation(attrs);
    }


    private void setAnimation(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        int frameCount = a.getInt(R.styleable.ProgressView_frameCount, 0);
        int duration = a.getInt(R.styleable.ProgressView_duration, 0);
        if (frameCount == 0) {
            Log.w("ProgressView", "should set `frameCount` attr. default is " + FRAME_COUNT);
            frameCount = FRAME_COUNT;
        }
        if (duration == 0) {
            Log.w("ProgressView", "should set `duration` attr. default is " + DURATION);
            duration = DURATION;
        }
        a.recycle();
        startAnimation(frameCount, duration);
    }

    public void startAnimation() {
        startAnimation(FRAME_COUNT, DURATION);
    }

    public void startAnimation(final int frameCount, int duration) {
        if (rotateAnimation == null) {
            //create rotation animation
            rotateAnimation = new RotateAnimation(0, 360,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setInterpolator(new android.view.animation.Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    return (float) Math.floor(input * frameCount) / frameCount;
                }
            });
        }
        startAnimation(rotateAnimation);
    }

    public void cancelAnimation() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
    }

    public void toggleAnimation() {
        if (rotateAnimation != null) {
            if (rotateAnimation.hasStarted() && !rotateAnimation.hasEnded()) {
                rotateAnimation.cancel();
            } else {
                startAnimation();
            }
        }
    }
}