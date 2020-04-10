package com.chiclaim.customview.animation;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.basic.GridLayoutAnimationFragment;
import com.chiclaim.customview.animation.basic.LayoutAnimationFragment;
import com.chiclaim.customview.animation.basic.ViewAnimationFrameFragment;
import com.chiclaim.customview.animation.basic.ViewAnimationTweenFragment;

import android.view.View;


/**
 * Created by chiclaim on 2016/04/02
 */
public class ViewAnimationFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_animation_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        view.findViewById(R.id.btn_animation_basic).setOnClickListener(this);
        view.findViewById(R.id.btn_animation_frame).setOnClickListener(this);
        view.findViewById(R.id.btn_layout_animation).setOnClickListener(this);
        view.findViewById(R.id.btn_grid_layout_animation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_animation_basic:
                addFragment(new ViewAnimationTweenFragment());
                break;
            case R.id.btn_animation_frame:
                addFragment(new ViewAnimationFrameFragment());
                break;
            case R.id.btn_layout_animation:
                addFragment(new LayoutAnimationFragment());
                break;
            case R.id.btn_grid_layout_animation:
                addFragment(new GridLayoutAnimationFragment());
                break;
        }
    }
}
