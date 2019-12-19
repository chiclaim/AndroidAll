package com.chiclaim.customview.animation.basic;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * Created by chiclaim on 2016/06/13
 */
public class ObjectAnimatorValueHolderFragment extends BaseFragment {

    private TextView tvTargetA, tvTargetB;

    private ImageView ivTargetC;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_value_holder_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("ObjectAnimatorValueHolder");
        tvTargetA = view.findViewById(R.id.tv_target_animation_a);
        tvTargetB = view.findViewById(R.id.tv_target_animation_b);
        ivTargetC = view.findViewById(R.id.iv_key_frame);

        view.findViewById(R.id.btn_start_animation_a).setOnClickListener(this);
        view.findViewById(R.id.btn_start_animation_b).setOnClickListener(this);
        view.findViewById(R.id.btn_start_animation_c).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_animation_a:

                PropertyValuesHolder colorHolder = PropertyValuesHolder.ofInt("backgroundColor",
                        0xffffffff, 0xffff00ff, 0xffffff00, 0xffffffff);
                PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("rotation", 180, -90, 90, 0);

                ObjectAnimator
                        .ofPropertyValuesHolder(tvTargetA, colorHolder, rotationHolder)
                        .setDuration(3000)
                        .start();
                break;

            case R.id.btn_start_animation_b:
                PropertyValuesHolder valuesHolder = PropertyValuesHolder
                        .ofObject("char", new CharacterEvaluate(), 'A', 'Z');

                ObjectAnimator
                        .ofPropertyValuesHolder(tvTargetB, valuesHolder)
                        .setDuration(4000)
                        .start();
                break;
            case R.id.btn_start_animation_c:
                ObjectAnimator.ofPropertyValuesHolder(ivTargetC,
                        getRotationValuesHolder(),
                        getScaleXValuesHolder(), getScaleYValuesHolder())
                        .setDuration(800)
                        .start();
                break;
        }
    }

    private PropertyValuesHolder getRotationValuesHolder() {
        Keyframe frame0 = Keyframe.ofFloat(0f, 0);
        Keyframe frame1 = Keyframe.ofFloat(0.1f, -10f);
        Keyframe frame2 = Keyframe.ofFloat(0.2f, 10f);
        Keyframe frame3 = Keyframe.ofFloat(0.3f, -10f);
        Keyframe frame4 = Keyframe.ofFloat(0.4f, 10f);
        Keyframe frame5 = Keyframe.ofFloat(0.5f, -10f);
        Keyframe frame6 = Keyframe.ofFloat(0.6f, 10f);
        Keyframe frame7 = Keyframe.ofFloat(0.7f, -10f);
        Keyframe frame8 = Keyframe.ofFloat(0.8f, 10f);
        Keyframe frame9 = Keyframe.ofFloat(0.9f, -10f);
        Keyframe frame10 = Keyframe.ofFloat(1, 0);

        return PropertyValuesHolder.ofKeyframe("rotation",
                frame0, frame1, frame2, frame3, frame4,
                frame5, frame6, frame7, frame8, frame9, frame10);
    }

    private PropertyValuesHolder getScaleXValuesHolder() {
        Keyframe frame0 = Keyframe.ofFloat(0f, 1);
        Keyframe frame1 = Keyframe.ofFloat(0.1f, 1.1f);
        Keyframe frame2 = Keyframe.ofFloat(0.2f, 1.1f);
        Keyframe frame3 = Keyframe.ofFloat(0.3f, 1.1f);
        Keyframe frame4 = Keyframe.ofFloat(0.4f, 1.1f);
        Keyframe frame5 = Keyframe.ofFloat(0.5f, 1.1f);
        Keyframe frame6 = Keyframe.ofFloat(0.6f, 1.1f);
        Keyframe frame7 = Keyframe.ofFloat(0.7f, 1.1f);
        Keyframe frame8 = Keyframe.ofFloat(0.8f, 1.1f);
        Keyframe frame9 = Keyframe.ofFloat(0.9f, 1.1f);
        Keyframe frame10 = Keyframe.ofFloat(1, 1);

        return PropertyValuesHolder.ofKeyframe("scaleX",
                frame0, frame1, frame2, frame3, frame4,
                frame5, frame6, frame7, frame8, frame9, frame10);
    }

    private PropertyValuesHolder getScaleYValuesHolder() {
        Keyframe frame0 = Keyframe.ofFloat(0f, 1);
        Keyframe frame1 = Keyframe.ofFloat(0.1f, 1.1f);
        Keyframe frame2 = Keyframe.ofFloat(0.2f, 1.1f);
        Keyframe frame3 = Keyframe.ofFloat(0.3f, 1.1f);
        Keyframe frame4 = Keyframe.ofFloat(0.4f, 1.1f);
        Keyframe frame5 = Keyframe.ofFloat(0.5f, 1.1f);
        Keyframe frame6 = Keyframe.ofFloat(0.6f, 1.1f);
        Keyframe frame7 = Keyframe.ofFloat(0.7f, 1.1f);
        Keyframe frame8 = Keyframe.ofFloat(0.8f, 1.1f);
        Keyframe frame9 = Keyframe.ofFloat(0.9f, 1.1f);
        Keyframe frame10 = Keyframe.ofFloat(1, 1);

        return PropertyValuesHolder.ofKeyframe("scaleY",
                frame0, frame1, frame2, frame3, frame4,
                frame5, frame6, frame7, frame8, frame9, frame10);
    }


    public static class CharacterEvaluate implements TypeEvaluator<Character> {
        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            return (char) ((startValue + (int) ((endValue - startValue) * fraction)));
        }
    }


}
