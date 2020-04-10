package com.chiclaim.customview.animation.basic;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;

/**
 * PropertyAnimator插值器
 * Created by chiclaim on 2016/06/07
 */
public class PropertyAnimInterpolatorFragment extends BaseFragment {

    View textView;
    private ValueAnimator valueAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_property_animation_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        setLabel("PropertyAnimInterpolator");

        view.findViewById(R.id.btn_start_by_property).setOnClickListener(this);

        textView = view.findViewById(R.id.text_view);
        textView.setOnClickListener(this);

        //save beginning position
        textView.post(new Runnable() {
            @Override
            public void run() {
                if (left == 0) {
                    left = textView.getLeft();
                }
                if (top == 0) {
                    top = textView.getTop();
                }
            }
        });
    }


    int left;
    int top;


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_start_by_property:

                valueAnimator = ValueAnimator.ofInt(0, 400);
                valueAnimator.setDuration(1000);
                valueAnimator.setInterpolator(new BounceInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        Log.d("PropertyAnimator", value + "");
                        //left top right bottom
                        //让其上下运动 left不变,top增加 right不变,bottom增加
                        textView.layout(
                                left,
                                value + top,
                                left + textView.getWidth(),
                                value + top + textView.getHeight());
                    }
                });
                valueAnimator.start();

                break;
            case R.id.text_view:
                Toast.makeText(getActivity(), "hello world", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.removeAllListeners();
        }
    }
}
