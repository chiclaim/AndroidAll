package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/*
本类只要演示了上传动画、声音动画、loading动画

loading动画实现方式:
    1>使用Frame动画可以实现,因为没有找到那么多图片所以本类中没有实现.可以参考上传动画;
    2>使用animated-rotate实现,但是效果不佳,不能定义frameCount.
      白色部分(最白的那一帧)始终在一个位置,不会走动.[仔细看下第一个loading]
      而且旋转一圈后,还有细微的停顿感
    3>使用RotationAnimation实现, 自定义interpolator. 效果也不错

    总结: 最差的是第一种方式, 需要的图片比较多. 实际项目中可以使用方式3,一张图片搞定,效果也不错!
 */

/**
 * Created by chiclaim on 2016/06/14
 */
public class ViewAnimationFrameFragment extends BaseFragment {

    private ImageView ivUpload, ivLoading, ivLoading2, ivSound;
    private ProgressView ivLoading3;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_animation_frame;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("Frame Animation");
        view.findViewById(R.id.btn_start_upload).setOnClickListener(this);
        view.findViewById(R.id.btn_start_sound).setOnClickListener(this);
        view.findViewById(R.id.btn_start_loading).setOnClickListener(this);
        view.findViewById(R.id.btn_start_loading2).setOnClickListener(this);
        ivUpload = view.findViewById(R.id.iv_upload);
        ivSound = view.findViewById(R.id.iv_sound);
        ivLoading = view.findViewById(R.id.iv_loading);
        ivLoading2 = view.findViewById(R.id.iv_loading2);
        ivLoading3 = view.findViewById(R.id.iv_loading3);
        ivLoading3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_upload:
                ivUpload.setBackgroundResource(R.drawable.frame_animation_upload);
                AnimationDrawable uploadAnimation = (AnimationDrawable) ivUpload.getBackground();
                uploadAnimation.start();
                break;
            case R.id.btn_start_sound:
                ivSound.setBackgroundResource(R.drawable.frame_animation_sound);
                AnimationDrawable uploadAnimation2 = (AnimationDrawable) ivSound.getBackground();
                uploadAnimation2.start();
                break;
            case R.id.btn_start_loading:
                ivLoading.setBackgroundResource(R.drawable.rotation_animation1);
                Animatable uploadAnimation1 = (Animatable) ivLoading.getBackground();
                uploadAnimation1.start();
                break;

            case R.id.btn_start_loading2:
                Animation a = AnimationUtils.loadAnimation(getContext(),
                        R.anim.rotation_animation2);
                a.setInterpolator(new android.view.animation.Interpolator() {
                    //总共有多少帧(具体根据图片来设置)
                    private final int frameCount = 12;

                    @Override
                    public float getInterpolation(float input) {
                        return (float) Math.floor(input * frameCount) / frameCount;
                    }
                });
                a.setDuration(1500);
                ivLoading2.startAnimation(a);
                break;
            case R.id.iv_loading3:
                ivLoading3.toggleAnimation();
                break;
        }
    }


}
