package com.chiclaim.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Description：圆角View
 * <br/>
 * Created by kumu on 2017/8/3.
 */

/*
        // EXACTLY(精确值) -> match_parent/某个特定的值
        // AT_MOST(最大值) -> wrap_content
        // UNSPECIFIED
 */
public class RoundRectView extends View {

    private Paint paint = new Paint();
    private int height, width;

    public RoundRectView(Context context) {
        super(context);
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getSize(widthMeasureSpec);
        height = getSize(heightMeasureSpec);

        // 外层布局为 LinearLayout，则自定义 View 会调用 2 次 onMeasure
        // 外层布局为 RelativeLayout，则自定义 View 会调用 3 次 onMeasure
        Log.d("RoundRectView", "onMeasure width=" + width + ", height=" + height);

        setMeasuredDimension(width, height);
    }

    private int getSize(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        Log.d("RoundRectView", "mode=" + mode);

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
        Log.d("RoundRectView", "onDraw");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(android.R.color.holo_green_dark));
            canvas.drawRoundRect(0, 0, width, height, 50, 50, paint);
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        Log.d("RoundRectView", "layout");
    }
}
