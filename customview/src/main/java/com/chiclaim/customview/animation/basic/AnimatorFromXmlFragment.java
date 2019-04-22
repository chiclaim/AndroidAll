package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.TextView;

/**
 * 关于动画资源 android官方文档:
 * <br/>
 * <a href="https://developer.android.com/guide/topics/resources/animation-resource.html">https://developer.android.com/guide/topics/resources/animation-resource.html</a>
 * <br/>
 * Created by chiclaim on 2016/06/13
 */
public class AnimatorFromXmlFragment extends BaseFragment {


    private TextView tvTarget;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_animator_from_xml;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("AnimatorFromXml");
        view.findViewById(R.id.btn_start_1).setOnClickListener(this);
        view.findViewById(R.id.btn_start_2).setOnClickListener(this);
        view.findViewById(R.id.btn_start_3).setOnClickListener(this);
        tvTarget = (TextView) view.findViewById(R.id.tv_target_animation_a);
    }


    int left;
    int top;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (left == 0) {
            left = tvTarget.getLeft();
        }
        if (top == 0) {
            top = tvTarget.getTop();
        }
        switch (v.getId()) {
            case R.id.btn_start_1:
                ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater
                        .loadAnimator(getActivity(), R.animator.value_animtor);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        tvTarget.layout(
                                value + left,
                                value + top,
                                value + left + tvTarget.getWidth(),
                                value + top + tvTarget.getHeight());
                    }
                });
                valueAnimator.start();
                break;
            case R.id.btn_start_2:
                ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater
                        .loadAnimator(getActivity(), R.animator.object_animtor);
                objectAnimator.setTarget(tvTarget);
                objectAnimator.start();
                break;

            case R.id.btn_start_3:
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater
                        .loadAnimator(getActivity(), R.animator.animtor_set);
                animatorSet.setTarget(tvTarget);
                animatorSet.start();
                break;
        }
    }
}
/*

    <animator
        android:duration="int"
        android:valueFrom="float | int | color"
        android:valueTo="float | int | color"
        android:startOffset="int"
        android:repeatCount="int"
        android:repeatMode=["repeat" | "reverse"]
        android:valueType=["intType" | "floatType"]/>

        //动画xml属性与java代码的关系   [可以查看官方文档]
        animator = ValueAnimator
        android:duration = valueAnimator.setDuration(long) 默认300ms
        android:startOffset = valueAnimator.setStartDelay()
        android:valueFrom = ValueAnimator.ofXXX(valueFrom,)
        android:valueTo = ValueAnimator.ofXXX(,valueTo)
        android:repeatCount = valueAnimator.setRepeatCount()
        android:repeatMode = valueAnimator.setRepeatMode()
        android:valueType = ofXXX 如果是颜色不需要设置该属性,系统会自动处理,
                                  只支持2个:"intType" | "floatType" 默认为floatType


    <objectAnimator
        android:propertyName="string"
        android:duration="int"
        android:valueFrom="float | int | color"
        android:valueTo="float | int | color"
        android:startOffset="int"
        android:repeatCount="int"
        android:repeatMode=["repeat" | "reverse"]
        android:valueType=["intType" | "floatType"]
        android:interpolator=["@android:interpolator/XXX"]/>

        //动画xml属性与java代码的关系   [可以查看官方文档]
        android:propertyName 动画操作的属性. 如 ObjectAnimator.ofFloat(target,"alpha")
        android:interpolator = animator.setInterpolator()
        其他属性同上面



    <set
      android:ordering=["together" | "sequentially"]
      android:interpolator="@android:anim/accelerate_interpolator">

        <objectAnimator/>

        <animator/>

        <set>
            ...
        </set>
    </set>

    //动画xml属性与java代码的关系   [可以查看官方文档]
    set = AnimatorSet
    android:ordering=["together" | "sequentially"]
    sequentially = animatorSet.playSequentially
    together = animatorSet.playTogether
    android:interpolator = animatorSet.setInterpolator()


*/