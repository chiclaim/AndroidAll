package com.chiclaim.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.chiclaim.customview.R;

public class StatusView extends View {

    private Paint paint = new Paint();

    private int width, height;
    private int outCircleBorder;
    private int innerCircleBorder;
    private int circlesGap;
    private int circleColor;
    private int textColor;
    private int textAngle;
    private String rawText;
    private float textMargin;
    private int normalTextLength;

    private StringBuilder stringBuilder;
    private StaticLayout textLayout;
    private String showText;


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
        rawText = typedArray.getString(R.styleable.StatusView_text);
        textAngle = typedArray.getInt(R.styleable.StatusView_text_angle, 25);
        normalTextLength = typedArray.getInt(R.styleable.StatusView_line_normal_text_length, 3);
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

    private String getShowText(int step) {
        if (!TextUtils.isEmpty(showText)) {
            return showText;
        }
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder(rawText.length() + rawText.length() / step);
        }
        stringBuilder.setLength(0);
        for (int i = 0; i < rawText.length(); i += step) {
            stringBuilder.append(rawText, i, Math.min(i + step, rawText.length()));
            if (i + step >= rawText.length()) {
                continue;
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
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

        // draw outside circle
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(outCircleBorder);
        canvas.drawCircle(width / 2f, height / 2f, (width - 2 * outCircleBorder) / 2.0f, paint);

        // draw inside circle
        paint.setStrokeWidth(innerCircleBorder);
        float innerCircleRadius = (width - 2 * outCircleBorder - 2 * innerCircleBorder - 2 * circlesGap) / 2.0f;
        canvas.drawCircle(width / 2f, height / 2f, innerCircleRadius, paint);

        canvas.save();

        // draw text
        if (!TextUtils.isEmpty(rawText)) {
            float textSize = (innerCircleRadius * 2 - textMargin * 2) / normalTextLength;
            float marginOffset = 0;
            if (rawText.length() > normalTextLength) {
                marginOffset = textSize / 2;
                showText = getShowText(normalTextLength - 1);
            } else {
                showText = rawText;
            }

            if (textLayout == null) {
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(textSize);
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setColor(textColor);
                textPaint.setAntiAlias(true);
                textPaint.setFakeBoldText(true);
                textLayout = new StaticLayout(showText, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }

            canvas.rotate(textAngle, width / 2f, height / 2f);
            canvas.translate(
                    outCircleBorder + innerCircleBorder + circlesGap + textMargin + marginOffset,
                    height / 2f - textLayout.getHeight() / 2.f);

            textLayout.draw(canvas);

            canvas.restore();

        }


        // test text is center
//        paint.setStrokeWidth((float) 20.0);         //线宽
//        paint.setColor(getResources().getColor(android.R.color.holo_red_light));
//        canvas.drawPoint(width / 2f, height / 2f, paint);
    }
}
