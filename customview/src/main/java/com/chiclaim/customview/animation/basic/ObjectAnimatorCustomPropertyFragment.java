package com.chiclaim.customview.animation.basic;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * ObjectAnimator 自定义属性
 * Created by chiclaim on 2016/06/12
 */
public class ObjectAnimatorCustomPropertyFragment extends BaseFragment {
    private MyPointView viewPoint;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_object_animator_custom_property;
    }


    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("ObjectAnimatorCustomProperty");
        viewPoint = (MyPointView) view.findViewById(R.id.point_view);
        viewPoint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.point_view:
                //ofInt方法后面的可变参数如果只传递一个参数
                //在没有定义getRadius方法的情况下会出现如下警告:
                //W/PropertyValuesHolder: Method getRadius() with type null not found on target class MyPointView
                //ObjectAnimator objectAnimator = ObjectAnimator.ofInt(viewPoint,"radius",200);

                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(viewPoint,"radius",20,200);
                objectAnimator.setDuration(1000);
                objectAnimator.setInterpolator(new BounceInterpolator());
                objectAnimator.start();
                break;
        }
    }
}
