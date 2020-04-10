package com.chiclaim.customview.view;

import com.chiclaim.customview.R;

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

public class CircleView extends View {

    Paint paint = new Paint();

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("CircleView", "onDraw");

        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(100, 100, 80, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawCircle(100, 280, 80, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(280, 100, 80, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(getResources().getColor(android.R.color.black));
        canvas.drawCircle(280, 280, 80, paint);

        //注意，画某个圆之前一定要重新设置paint相关属性，不要沿用上一个设置的paint
    }
}
