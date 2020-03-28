package com.chiclaim.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.chiclaim.customview.R;

public class StatusView extends View {

    private Paint paint = new Paint();
    private Rect rect = new Rect();

    private int width, height;
    private int outCircleBorder;
    private int innerCircleBorder;
    private int circlesGap;
    private int circleColor;
    private int textColor;
    private String text;
    private float textMargin = 10;


    public StatusView(Context context) {
        super(context);
        init(null);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatusView);
        outCircleBorder = typedArray.getDimensionPixelOffset(R.styleable.StatusView_outside_circle_border_width, 0);
        innerCircleBorder = typedArray.getDimensionPixelOffset(R.styleable.StatusView_inside_circle_border_width, 0);
        circlesGap = typedArray.getDimensionPixelOffset(R.styleable.StatusView_circles_gap, 0);
        circleColor = typedArray.getColor(R.styleable.StatusView_circle_color, 0);
        text = typedArray.getString(R.styleable.StatusView_text);
        textColor = typedArray.getColor(R.styleable.StatusView_textColor, 0);
        if (textColor == 0) {
            textColor = circleColor;
        }
        textMargin = typedArray.getDimensionPixelOffset(R.styleable.StatusView_text_margin, 10);
        typedArray.recycle();
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getSize(widthMeasureSpec);
        height = getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAntiAlias(true);
        //paint.setColor(Color.parseColor("#ff9900"));
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(outCircleBorder);
        canvas.drawCircle(width / 2f, height / 2f, (width - 2 * outCircleBorder) / 2.0f, paint);

        paint.setStrokeWidth(innerCircleBorder);
        float innerCircleRadius = (width - 2 * outCircleBorder - 2 * innerCircleBorder - 2 * circlesGap) / 2.0f;
        canvas.drawCircle(width / 2f, height / 2f, innerCircleRadius, paint);

        float rawTextSize = (innerCircleRadius * 2 - textMargin * 2) / 3;

        paint.setTextSize(rawTextSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);

        paint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();

//        Paint.FontMetrics fm = paint.getFontMetrics();
//        float fontHeight = fm.bottom - fm.top + fm.leading;
//
//        Log.d("StatusView", "textWidth=" + textWidth + ",textHeight=" + textHeight
//                + ",fontHeight=" + fontHeight
//                + ",rawTextSize=" + rawTextSize);

        canvas.save();

        canvas.rotate(25, width / 2f, height / 2f);

        canvas.drawText(
                text,
                outCircleBorder + innerCircleBorder + circlesGap + textMargin,
                height / 2f + textHeight / 2f,
                paint);


        // test text is center
        //paint.setStrokeWidth((float) 20.0);         //线宽
        //paint.setColor(getResources().getColor(android.R.color.holo_red_light));
        //canvas.drawPoint(width / 2f, height / 2f, paint);
    }
}
