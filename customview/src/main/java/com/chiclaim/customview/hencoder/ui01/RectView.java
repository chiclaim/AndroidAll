package com.chiclaim.customview.hencoder.ui01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class RectView extends View {

    private Paint paint = new Paint();

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int rectWidth = 400;
        int margin = (getResources().getDisplayMetrics().widthPixels - rectWidth) / 2;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.black));
        //canvas.drawRect(100, 100, 500, 500, paint);
        //水平居中
        canvas.drawRect(margin, 100, margin + rectWidth, 100 + rectWidth, paint);


    }
}
