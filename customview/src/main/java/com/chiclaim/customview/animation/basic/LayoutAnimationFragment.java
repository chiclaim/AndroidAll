package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiclaim on 2016/06/15
 */
public class LayoutAnimationFragment extends BaseFragment {

    private ListView listView;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_animation;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        setLabel("Layout Animation");

        listView =  view.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, getData(0));
        listView.setAdapter(mAdapter);
        view.findViewById(R.id.btn_start_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addAll(getData(mAdapter.getCount()));
            }
        });

        setAnimationByCode();

    }

    private List<String> getData(int offset) {
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            data.add("测试数据" + (i + offset));
        }
        return data;
    }


    private void setAnimationByCode(){
        Animation animation= AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        //得到一个LayoutAnimationController对象；
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        //设置控件显示的顺序；
        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        //设置控件显示间隔时间；
        controller.setDelay(0.5f);
        //为ListView设置LayoutAnimationController属性；
        listView.setLayoutAnimation(controller);
        listView.startLayoutAnimation();
    }
}

/*
    <layoutAnimation
        android:animation="@anim/slide_in_left"
        android:animationOrder="normal"
        android:delay="0.5"

    android:animation 为viewGroup item指定的动画
    android:animationOrder 只有三个选项:normal(正序)、reverse(倒序)、random(随机)
    android:delay 一个item的动画会在上一个item动画完成后延时单次动画时长的n(上面例子n=0.5)倍时间开始，
                  即延时duration*n(上面的例子:0.5*200)毫秒后开始

*/