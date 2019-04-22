package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.ILabelInteraction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/*
 animationLayoutChanges 只能使用默认动画效果
 */

/**
 * Created by chiclaim on 2016/06/16
 */
public class AnimationLayoutChangesFragment extends BaseFragment {
    private LinearLayout layoutTransitionGroup;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_animation_layout_changes;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("AnimationLayoutChange");
        layoutTransitionGroup = view.findViewById(R.id.layoutTransitionGroup);
        view.findViewById(R.id.add_btn).setOnClickListener(this);
        view.findViewById(R.id.remove_btn).setOnClickListener(this);
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
