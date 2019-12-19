package com.chiclaim.customview.animation.basic;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * Created by chiclaim on 2016/06/12
 */
public class PropertyArgbEvaluateFragment extends BaseFragment {
    private View viewHello;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_perperty_argb;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("PropertyArgbEvaluate");

        view.findViewById(R.id.btn_change_color).setOnClickListener(this);
        viewHello = view.findViewById(R.id.tv_hello);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_change_color:
                //api 21
                //ValueAnimator valueAnimator = ValueAnimator.ofArgb(0xffffff00,0xff0000ff);
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0xffffff00, 0xff0000ff);
                valueAnimator.setDuration(4000);
                valueAnimator.setEvaluator(new ArgbEvaluator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        viewHello.setBackgroundColor(value);
                    }
                });
                valueAnimator.start();
                break;
        }
    }
}
