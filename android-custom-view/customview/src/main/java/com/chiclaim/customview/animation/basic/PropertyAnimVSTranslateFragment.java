package com.chiclaim.customview.animation.basic;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * 对比PropertyAnimator和TranslateAnimation
 * Created by chiclaim on 2016/06/07
 */
public class PropertyAnimVSTranslateFragment extends BaseFragment {

    private View textView;
    private ValueAnimator valueAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_property_vs_translate_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("Property VS Translation");
        view.findViewById(R.id.btn_start_animation_a).setOnClickListener(this);
        view.findViewById(R.id.btn_start_by_property).setOnClickListener(this);

        textView = view.findViewById(R.id.text_view);
        textView.setOnClickListener(this);

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
            case R.id.btn_start_animation_a:

                TranslateAnimation animation = new TranslateAnimation(
                        Animation.ABSOLUTE, 0,
                        Animation.ABSOLUTE, 400,
                        Animation.ABSOLUTE, 0,
                        Animation.ABSOLUTE, 400);
                animation.setFillAfter(true);
                animation.setDuration(1000);
                textView.startAnimation(animation);
                break;
            case R.id.btn_start_by_property:

                valueAnimator = ValueAnimator.ofInt(0, 400);
                valueAnimator.setDuration(1000);

                //valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                //valueAnimator.setRepeatMode(ValueAnimator.REVERSE);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int i = (int) animation.getAnimatedValue();
                        //left top right bottom
                        //left增加,top增加 right增加,bottom增加
                        textView.layout(
                                i + left,
                                i + top,
                                i + left + textView.getWidth(),
                                i + top + textView.getHeight());
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
