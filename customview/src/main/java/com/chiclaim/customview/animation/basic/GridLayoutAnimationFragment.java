package com.chiclaim.customview.animation.basic;

import com.chiclaim.customview.R;
import com.chiclaim.customview.animation.BaseFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/*

<gridLayoutAnimation
    android:animation="@anim/slide_in_left"
    android:directionPriority="none"
    android:direction="left_to_right"
    android:columnDelay="60%"
    android:rowDelay="75%"/>

android:directionPriority row(行优先),column(列优先),none(同时进行)
android:direction 各个item的动画方向，取值如下，可以通过“|”连接多个属性值。
                - left_to_right：列，从左向右开始动画
                - right_to_left ：列，从右向左开始动画
                - top_to_bottom：行，从上向下开始动画
                - bottom_to_top：行，从下向上开始动画

android:columnDelay 和layoutAnimation的delay类似,不赘述了
android:rowDelay    和layoutAnimation的delay类似,不赘述了

 */

/**
 * Created by chiclaim on 2016/06/15
 */
public class GridLayoutAnimationFragment extends BaseFragment {

    private GridAdapter mGridAdapter;
    private List<String> dataList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grid_layout_animation;
    }


    @Override
    protected void initViews(View view) {
        super.initViews(view);

        setLabel("GridLayoutAnimation");

        GridView grid = view.findViewById(R.id.grid_view);
        dataList.addAll(getData());
        mGridAdapter = new GridAdapter();
        grid.setAdapter(mGridAdapter);


        Button addData = view.findViewById(R.id.btn_start);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private List<String> getData() {

        List<String> data = new ArrayList<>();
        for (int i = 1;i<35;i++){
            data.add("DATA "+i);
        }
        return data;
    }


    private void addData(){
        dataList.addAll(dataList);
        mGridAdapter.notifyDataSetChanged();
    }


    public class GridAdapter extends BaseAdapter {
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView i = new TextView(getActivity());
            i.setText(dataList.get(position));
            i.setLayoutParams(new GridView.LayoutParams(
                    GridView.LayoutParams.WRAP_CONTENT,
                    GridView.LayoutParams.WRAP_CONTENT));
            return i;
        }

        public final int getCount() {
            return dataList.size();
        }

        public final Object getItem(int position) {
            return null;
        }

        public final long getItemId(int position) {
            return position;
        }
    }
}
