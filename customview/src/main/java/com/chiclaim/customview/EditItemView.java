package com.chiclaim.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/30.
 */

public class EditItemView extends LinearLayout {

    public static final int CLICK_LEFT = 1;
    public static final int CLICK_RIGHT = 2;
    public static final int CLICK_MIDDLE = 3;

    TextView tvMiddle;

    OnEditViewClick onEditViewClick;

    public EditItemView(Context context) {
        super(context);
        init(context, null);
    }

    public EditItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnEditViewClick(OnEditViewClick onEditViewClick) {
        this.onEditViewClick = onEditViewClick;
    }

    public void setMiddleText(String text) {
        tvMiddle.setText(text);
    }

    public void setMiddleText(@StringRes int textRes) {
        tvMiddle.setText(textRes);
    }


    //ImageView  TextView ImageView


    private void init(Context context, AttributeSet attrs) {
        int leftBorderColor = -1, rightBorderColor = -1, middleTextColor = -1,
                leftImageRes = -1, rightImageRes = -1, middleTextSize = -1;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditItemView);

            leftBorderColor = typedArray.getColor(R.styleable.EditItemView_left_border_color, -1);
            leftImageRes = typedArray.getResourceId(R.styleable.EditItemView_left_image, -1);

            rightBorderColor = typedArray.getColor(R.styleable.EditItemView_left_border_color, -1);
            rightImageRes = typedArray.getResourceId(R.styleable.EditItemView_right_image, -1);

            //String middleText = typedArray.getString(R.styleable.EditItemView_middle_text);
            middleTextColor = typedArray.getColor(R.styleable.EditItemView_middle_text_color, -1);
            middleTextSize = typedArray.getDimensionPixelSize(R.styleable.EditItemView_middle_text_size, -1);

            typedArray.recycle();
        }

        setOrientation(LinearLayout.HORIZONTAL);

        ImageView leftImage = new ImageView(getContext());
        leftImage.setImageResource(leftImageRes);
        addView(leftImage);
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditViewClick != null) {
                    onEditViewClick.onClick(CLICK_LEFT);
                }
            }
        });

        tvMiddle = new TextView(getContext());
        //textView.setText(middleText);
        if (middleTextSize != -1)
            tvMiddle.setTextSize(middleTextSize);
        if (middleTextColor != -1)
            tvMiddle.setTextColor(middleTextColor);
        addView(tvMiddle);

        tvMiddle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditViewClick != null) {
                    onEditViewClick.onClick(CLICK_MIDDLE);
                }
            }
        });

        ImageView rightImage = new ImageView(getContext());
        rightImage.setImageResource(rightImageRes);
        addView(rightImage);

        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditViewClick != null) {
                    onEditViewClick.onClick(CLICK_RIGHT);
                }
            }
        });
    }

    interface OnEditViewClick {
        void onClick(int which);
    }

}
