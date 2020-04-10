package com.chiclaim.customview.animation.basic;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;
;
/**
 * PropertyAnimator's Evaluate
 * Created by chiclaim on 2016/06/07
 */
public class PropertyCustomEvaluateFragment extends BaseFragment {

    private TextView textView;
    private ValueAnimator valueAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_property_custom_evaluate_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("PropertyCustomEvaluate");
        view.findViewById(R.id.btn_start_by_property).setOnClickListener(this);

        textView = (TextView) view.findViewById(R.id.text_view);
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


    private int left;
    private int top;


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_start_by_property:
                valueAnimator = ValueAnimator.ofObject(new CharacterEvaluate(), 'A', 'Z');
                valueAnimator.setDuration(5000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        char value = (char) animation.getAnimatedValue();
                        Log.d("PropertyAnimator", value + "");
                        textView.setText(String.valueOf(value));
                    }
                });
                valueAnimator.start();

                break;
            case R.id.text_view:
                Toast.makeText(getActivity(), "hello world", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // 计算公式: result = x0 + t * (x1 - x0)
    // 0,1,2,3,4,5,6,7,8,9,10
    // start+(end-start)*progress
    // 0 + 10 * 0.5 = 5

    public static class CharacterEvaluate implements TypeEvaluator<Character> {
        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            return (char) ((startValue + (int) ((endValue - startValue) * fraction)));
        }
    }
}
