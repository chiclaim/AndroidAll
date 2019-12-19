package com.chiclaim.customview.animation;

import android.os.Bundle;
import android.view.View;

import com.chiclaim.customview.animation.basic.AnimationLayoutChangesFragment;
import com.chiclaim.customview.animation.basic.AnimatorFromXmlFragment;
import com.chiclaim.customview.animation.basic.AnimatorSetFragment;
import com.chiclaim.customview.animation.basic.LayoutTransitionFragment;
import com.chiclaim.customview.animation.basic.ObjectAnimatorCustomPropertyFragment;
import com.chiclaim.customview.animation.basic.ObjectAnimatorFragment;
import com.chiclaim.customview.animation.basic.ObjectAnimatorValueHolderFragment;
import com.chiclaim.customview.animation.basic.PropertyAnimInterpolatorFragment;
import com.chiclaim.customview.animation.basic.PropertyAnimVSTranslateFragment;
import com.chiclaim.customview.animation.basic.PropertyArgbEvaluateFragment;
import com.chiclaim.customview.animation.basic.PropertyCustomEvaluateFragment;
import com.chiclaim.customview.animation.basic.PropertyOfObjectFragment;
import com.chiclaim.customview.R;

import androidx.annotation.Nullable;

/**
 * Created by chiclaim on 2016/04/02
 */
public class PropertyAnimatorFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_property_animator_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_property_vs_tween_animation).setOnClickListener(this);
        view.findViewById(R.id.btn_property_interpolator).setOnClickListener(this);
        view.findViewById(R.id.btn_property_custom_evaluate).setOnClickListener(this);
        view.findViewById(R.id.btn_property_of_object).setOnClickListener(this);
        view.findViewById(R.id.btn_property_argb_evaluate).setOnClickListener(this);
        view.findViewById(R.id.btn_object_animator).setOnClickListener(this);
        view.findViewById(R.id.btn_object_animator_custom_property).setOnClickListener(this);
        view.findViewById(R.id.btn_object_animator_value_holder).setOnClickListener(this);
        view.findViewById(R.id.btn_animator_set).setOnClickListener(this);
        view.findViewById(R.id.btn_animator_from_xml).setOnClickListener(this);
        view.findViewById(R.id.btn_animation_layout_changes).setOnClickListener(this);
        view.findViewById(R.id.btn_layout_transition).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_property_vs_tween_animation:
                addFragment(new PropertyAnimVSTranslateFragment());
                break;
            case R.id.btn_property_interpolator:
                addFragment(new PropertyAnimInterpolatorFragment());
                break;
            case R.id.btn_property_custom_evaluate:
                addFragment(new PropertyCustomEvaluateFragment());
                break;
            case R.id.btn_property_of_object:
                addFragment(new PropertyOfObjectFragment());
                break;
            case R.id.btn_property_argb_evaluate:
                addFragment(new PropertyArgbEvaluateFragment());
                break;
            case R.id.btn_object_animator:
                addFragment(new ObjectAnimatorFragment());
                break;
            case R.id.btn_object_animator_custom_property:
                addFragment(new ObjectAnimatorCustomPropertyFragment());
                break;
            case R.id.btn_object_animator_value_holder:
                addFragment(new ObjectAnimatorValueHolderFragment());
                break;
            case R.id.btn_animator_set:
                addFragment(new AnimatorSetFragment());
                break;
            case R.id.btn_animator_from_xml:
                addFragment(new AnimatorFromXmlFragment());
                break;
            case R.id.btn_animation_layout_changes:
                addFragment(new AnimationLayoutChangesFragment());
                break;
            case R.id.btn_layout_transition:
                addFragment(new LayoutTransitionFragment());
                break;
        }
    }
}
