package com.chiclaim.customview.animation.basic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chiclaim.customview.animation.BaseFragment;
import com.chiclaim.customview.R;

/**
 * Created by chiclaim on 2016/06/13
 */
public class AnimatorSetFragment extends BaseFragment {

    private View viewTargetA, viewTargetB;


    private Button mMenuButton;
    private Button mItemButton1;
    private Button mItemButton2;
    private Button mItemButton3;
    private Button mItemButton4;
    private Button mItemButton5;
    private boolean mIsMenuOpen = false;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_animator_set_layout;
    }


    @Override
    protected void initViews(View view) {
        super.initViews(view);
        setLabel("AnimatorSet");
        view.findViewById(R.id.btn_start_1).setOnClickListener(this);
        view.findViewById(R.id.btn_start_2).setOnClickListener(this);
        view.findViewById(R.id.btn_start_3).setOnClickListener(this);
        view.findViewById(R.id.btn_start_4).setOnClickListener(this);

        viewTargetA = view.findViewById(R.id.tv_target_animation_a);
        viewTargetB = view.findViewById(R.id.tv_target_animation_b);


        mMenuButton = (Button) view.findViewById(R.id.menu);
        mMenuButton.setOnClickListener(this);

        mItemButton1 = (Button) view.findViewById(R.id.item1);
        mItemButton1.setOnClickListener(this);

        mItemButton2 = (Button) view.findViewById(R.id.item2);
        mItemButton2.setOnClickListener(this);

        mItemButton3 = (Button) view.findViewById(R.id.item3);
        mItemButton3.setOnClickListener(this);

        mItemButton4 = (Button) view.findViewById(R.id.item4);
        mItemButton4.setOnClickListener(this);

        mItemButton5 = (Button) view.findViewById(R.id.item5);
        mItemButton5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_1:
                start(true);
                break;
            case R.id.btn_start_2:
                start(false);
                break;
            case R.id.btn_start_3:
                start(false, true);
                break;
            case R.id.btn_start_4:
                startByBuilder();
                break;
            case R.id.menu:
                if (!mIsMenuOpen) {
                    mIsMenuOpen = true;
                    doAnimateOpen(mItemButton1, 0, 5, 300);
                    doAnimateOpen(mItemButton2, 1, 5, 300);
                    doAnimateOpen(mItemButton3, 2, 5, 300);
                    doAnimateOpen(mItemButton4, 3, 5, 300);
                    doAnimateOpen(mItemButton5, 4, 5, 300);
                } else {
                    mIsMenuOpen = false;
                    doAnimateClose(mItemButton1, 0, 5, 300);
                    doAnimateClose(mItemButton2, 1, 5, 300);
                    doAnimateClose(mItemButton3, 2, 5, 300);
                    doAnimateClose(mItemButton4, 3, 5, 300);
                    doAnimateClose(mItemButton5, 4, 5, 300);
                }
                break;
            case R.id.item1:
                Toast.makeText(getActivity(),"menu1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(getActivity(),"menu2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item3:
                Toast.makeText(getActivity(),"menu3",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item4:
                Toast.makeText(getActivity(),"menu4",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item5:
                Toast.makeText(getActivity(),"menu5",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void doAnimateOpen(View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));

        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
        //动画周期为500ms
        set.setDuration(300).start();
    }

    private void doAnimateClose(final View view, int index, int total,
                                int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.PI * index / ((total - 1) * 2);
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));

        set.setDuration(300).start();

        //avoid menuItem cover the menu
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    private void startByBuilder() {
        ObjectAnimator objectAnimatorA = ObjectAnimator.ofFloat(viewTargetA,
                "translationY", 0, 300, 0);
        ObjectAnimator colorAnimatorA = ObjectAnimator.ofInt(viewTargetA,
                "backgroundColor", 0xffffffff, 0xffff00ff, 0xffffff00);

        ObjectAnimator objectAnimatorB = ObjectAnimator.ofFloat(viewTargetB,
                "translationY", 0, 300, 0);

        //===============================================================
        //先同时执行objectAnimator1和colorAnimator,完毕后再执行objectAnimator2
        //builder方法解释:
        //with(animator) 和前面的动画一起执行
        //before(animator) 前面的动画在参数animator动画执行之前执行
        //after(animator) 前面的动画在参数animator动画执行完之执行
        //如何理解 :  方法名(参数),从右往左读. 重点是参数,前面的动画到底是在参数之前还是之后执行 通过方法名就知道了.
        //===============================================================

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorA)
                .with(colorAnimatorA)
                .before(objectAnimatorB);

        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    private void start(boolean sequentially) {
        start(sequentially, false);
    }

    private void start(boolean sequentially, boolean infinite) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(viewTargetA,
                "translationY", 0, 300, 0);
        if (infinite) {
            objectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        }

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(viewTargetB,
                "translationY", 0, 300, 0);
        if (infinite) {
            objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        if (sequentially) {
            //有先后顺序执行动画
            animatorSet.playSequentially(objectAnimator1, objectAnimator2);
        } else {
            //同时执行所有动画
            animatorSet.playTogether(objectAnimator1, objectAnimator2);
        }
        //设置AnimatorSet里每个动画的时长, 就算里面的动画设置了自己的动画时长,也会被该方法覆盖.
        //如果要为每个动画单独设置时长,就把下面的方法注释掉
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
}
