package com.chiclaim.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class RectView extends View {

    private Paint paint = new Paint();
    private int width, height;

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getSize(widthMeasureSpec);
        height = getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int getSize(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                return 0;

            case MeasureSpec.EXACTLY: {
                return size;
            }
        }
        return 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        canvas.drawRect(0, 0, width, height, paint);
    }
}
