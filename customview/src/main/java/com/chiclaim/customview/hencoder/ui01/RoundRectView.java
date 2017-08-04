package com.chiclaim.customview.hencoder.ui01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class RoundRectView extends View {

    private Paint paint = new Paint();

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int rectWidth = 400;
            int margin = (getResources().getDisplayMetrics().widthPixels - rectWidth) / 2;
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(android.R.color.black));
            //水平居中
            //canvas.drawRoundRect(left, top, right, bottom, rx, ry, paint);
            //rx, ry  是圆角的横向半径和纵向半径
            canvas.drawRoundRect(margin, 100, margin + rectWidth, 100 + rectWidth, 50, 50, paint);
        }
    }
}
