package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by chiclaim on 2016/06/16
 */
public class LayoutTransitionFragment extends BaseFragment {

    private LinearLayout layoutTransitionGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_animation_layout_transition;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("LayoutTransition");
        layoutTransitionGroup = view.findViewById(R.id.layoutTransitionGroup);
        view.findViewById(R.id.add_btn).setOnClickListener(this);
        view.findViewById(R.id.remove_btn).setOnClickListener(this);

        //layoutTransitionGroup.setLayoutTransition(createTransition());
        layoutTransitionGroup.setLayoutTransition(createTransitionChange());
    }

    private LayoutTransition createTransition() {
        LayoutTransition mTransitioner = new LayoutTransition();
        //入场动画:view在这个容器中消失时触发的动画
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 0f, 360f, 0f);
        mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);

        //出场动画:view显示时的动画
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotation", 0f, 90f, 0f);
        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        return mTransitioner;
    }


    private LayoutTransition createTransitionChange() {
        LayoutTransition mTransitioner = new LayoutTransition();
        //入场动画:view在这个容器中消失时触发的动画
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 0f, 360f, 0f);
        mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);

        //出场动画:view显示时的动画
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotation", 0f, 90f, 0f);
        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 100, 0);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 0);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 0);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 0);


        Animator changeAppearAnimator = ObjectAnimator.ofPropertyValuesHolder(layoutTransitionGroup,
                pvhLeft, pvhTop, pvhRight, pvhBottom);
        mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeAppearAnimator);

        /*
            1, LayoutTransition.CHANGE_APPEARING/CHANGE_DISAPPEARING 必须配合PropertyValuesHolder使用才能有效果(使用ObjectAnimator无效).
         */

        return mTransitioner;
    }


    private int i;

    private void addButtonView() {
        i++;
        Button button = new Button(getActivity());
        button.setText("button" + i);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        layoutTransitionGroup.addView(button, 0);
    }

    private void removeButtonView() {
        if (i > 0) {
            layoutTransitionGroup.removeViewAt(0);
        }
        i--;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            addButtonView();
        }
        if (v.getId() == R.id.remove_btn) {
            removeButtonView();
        }

    }
}
