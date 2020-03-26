package com.chiclaim.customview.hencoder.ui01;

import com.chiclaim.customview.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class ColorView extends View {
    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        // TODO: 2017/8/3  drawColor by mode /  drawRGB  / drawARGB
        //canvas.drawColor(color,mode);
        //canvas.drawRGB();
        //canvas.drawARGB();
    }
}
