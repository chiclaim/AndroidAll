package com.chiclaim.customview.compat;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Description：解决魅族手机EditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);第一个字符显示不全的问题
 * <br/>
 * Created by kumu on 2017/7/11.
 */

public class MZEditText extends AppCompatEditText {

    private int mExtraWidth = 12;
    private boolean mDisableExtraWidth;

    public MZEditText(Context context) {
        super(context);
    }

    public MZEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MZEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setExtraWidth(int extraWidth) {
        mExtraWidth = extraWidth;
    }

    /**
     * 不需要额外的宽度
     */
    public void removeExtraWidth() {
        mDisableExtraWidth = true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mDisableExtraWidth) {
            setMeasuredDimension(getMeasuredWidth() + mExtraWidth, getMeasuredHeight());
        }
    }
}
