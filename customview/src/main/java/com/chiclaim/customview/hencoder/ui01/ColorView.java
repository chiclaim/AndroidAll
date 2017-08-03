package com.chiclaim.customview.hencoder.ui01;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.chiclaim.customview.R;

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
        //canvas.drawColor(color,mode);
    }
}
