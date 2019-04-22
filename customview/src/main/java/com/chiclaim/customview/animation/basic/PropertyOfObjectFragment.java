package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

import android.view.View;

/**
 * ValueAnimator's ofObject
 * Created by chiclaim on 2016/06/08
 */
public class PropertyOfObjectFragment extends BaseFragment {

    private MyPointView pointView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_property_of_obejct;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("PropertyOfObject");
        pointView = view.findViewById(R.id.point_view);
        pointView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.point_view:
                pointView.clickPoint();
                break;
        }
    }
}
