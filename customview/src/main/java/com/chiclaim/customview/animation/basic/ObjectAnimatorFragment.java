package com.chiclaim.customview.animation.basic;

import android.animation.ObjectAnimator;
import android.view.View;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * ObjectAnimator流程:<br/>
 * ofXXX() -> interpolator -> evaluate -> setter
 *
 * Created by chiclaim on 2016/06/12
 */
public class ObjectAnimatorFragment extends BaseFragment {

    private View viewTarget;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_object_animator_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("ObjectAnimator");
        view.findViewById(R.id.btn_change_transparent).setOnClickListener(this);
        view.findViewById(R.id.btn_change_x).setOnClickListener(this);
        view.findViewById(R.id.btn_change_y).setOnClickListener(this);
        view.findViewById(R.id.btn_change_rotation_z).setOnClickListener(this);
        view.findViewById(R.id.btn_change_rotation_x).setOnClickListener(this);
        view.findViewById(R.id.btn_change_rotation_y).setOnClickListener(this);
        viewTarget = view.findViewById(R.id.view_target);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_change_transparent:
                ObjectAnimator objectAnimator = ObjectAnimator.
                        ofFloat(viewTarget, "alpha", 1, 0, 1);
                objectAnimator.setDuration(3000);
                objectAnimator.start();
                break;
            case R.id.btn_change_x:
                ObjectAnimator objectAnimator2 = ObjectAnimator.
                        ofFloat(viewTarget, "translationX", 0, 300, -300, 0);
                objectAnimator2.setDuration(3000);
                objectAnimator2.start();
                break;
            case R.id.btn_change_y:
                ObjectAnimator objectAnimator3 = ObjectAnimator.
                        ofFloat(viewTarget, "translationY", 0, 300, -300, 0);
                objectAnimator3.setDuration(3000);
                objectAnimator3.start();
                break;
            case R.id.btn_change_rotation_z:
                ObjectAnimator objectAnimator4 = ObjectAnimator.
                        ofFloat(viewTarget, "rotation", 0, 180, 90, 0);
                objectAnimator4.setDuration(3000);
                objectAnimator4.start();
                break;
            case R.id.btn_change_rotation_x:
                ObjectAnimator objectAnimator5 = ObjectAnimator.
                        ofFloat(viewTarget, "rotationX", 0, 180, 90, 0);
                objectAnimator5.setDuration(3000);
                objectAnimator5.start();
                break;
            case R.id.btn_change_rotation_y:
                ObjectAnimator objectAnimator6 = ObjectAnimator.
                        ofFloat(viewTarget, "rotationY", 0, 180, 90, 0);
                objectAnimator6.setDuration(3000);
                objectAnimator6.start();
                break;
        }
    }
}
