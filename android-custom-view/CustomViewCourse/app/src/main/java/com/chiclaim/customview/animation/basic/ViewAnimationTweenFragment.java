package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * alpha,scale,translate,rotate
 * Created by chiclaim on 2016/04/02
 */
public class ViewAnimationTweenFragment extends BaseFragment {

    ImageView imageView;
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_basic_animation_layout;
    }


    private static final int[] BTN_IDS = {R.id.btn_alpha, R.id.btn_scale,
            R.id.btn_translate, R.id.btn_rotate};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLabel("Tween Animation");
        imageView = view.findViewById(R.id.imageview);
        tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.ll_title).setOnClickListener(this);

        for (int id : BTN_IDS) {
            view.findViewById(id).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_alpha:
                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.basic_alpha);
                imageView.startAnimation(a);
                break;
            case R.id.btn_scale:
                Animation a1 = AnimationUtils.loadAnimation(getActivity(), R.anim.basic_scale);
                imageView.startAnimation(a1);
                break;
            case R.id.btn_translate:
                Animation a2 = AnimationUtils.loadAnimation(getActivity(), R.anim.basic_translate);
                imageView.startAnimation(a2);
                break;
            case R.id.btn_rotate:
                Animation a3 = AnimationUtils.loadAnimation(getActivity(), R.anim.basic_rotate);
                imageView.startAnimation(a3);
                break;
            case R.id.ll_title:
                Animation a4 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_bottom);
                tvTitle.startAnimation(a4);
                break;
        }
    }
}
