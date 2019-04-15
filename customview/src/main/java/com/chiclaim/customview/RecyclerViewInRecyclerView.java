package com.chiclaim.customview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description：RecyclerView的Item又是一个RecyclerView
 * <br/>
 * Created by kumu on 2017/4/6.
 */

//http://stackoverflow.com/questions/32011995/how-to-have-a-listview-recyclerview-inside-a-parent-recyclerview
//https://stackoverflow.com/questions/33218397/recyclerview-inside-recyclerview-not-smoothly
//https://github.com/drakeet/MultiType/issues/67   嵌套后，自定滑到了第一个子RecyclerView，而不是最外层的RecyclerView

public class RecyclerViewInRecyclerView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_in_recyclerview);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);

        //recyclerView.setScrollingTouchSlop();
        recyclerView.startNestedScroll(0);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Title " + i);
        }
        recyclerView.setAdapter(new MyAdapter(this, list));
    }

    private static class ChildRecyclerAdapter extends RecyclerView.Adapter {
        List<String> subList;

        ChildRecyclerAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChildHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ChildHolder childHolder = (ChildHolder) holder;
            childHolder.tvSubTitle.setText(subList.get(position));
            Log.d("ChildRecyclerAdapter", position + "");
        }

        @Override
        public int getItemCount() {
            return subList.size();
        }

        class ChildHolder extends RecyclerView.ViewHolder {
            TextView tvSubTitle;

            public ChildHolder(View itemView) {
                super(itemView);
                tvSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            }
        }
    }


    private static class MyAdapter extends RecyclerView.Adapter {
        LayoutInflater inflater;
        List<String> list;
        List<String> subList;

        //ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter();

        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

        MyAdapter(Context context, List<String> list) {
            inflater = LayoutInflater.from(context);
            this.list = list;
            if (subList == null) {
                subList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    subList.add("Sub Title " + i);
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TitleHolder(inflater.inflate(R.layout.item_title_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.bind(position, list.get(position));
            Log.e("TitleHolder", "==outer onBindViewHolder " + position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class TitleHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;
            RecyclerView recyclerView;


            TitleHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_child);

                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);


                //layoutManager.setAutoMeasureEnabled(true);

                RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(layoutManager);
            }

            void bind(int position, String title) {
                tvTitle.setText(title);
                recyclerView.setRecycledViewPool(recycledViewPool);
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    ChildRecyclerAdapter childAdapter = (ChildRecyclerAdapter) adapter;
                    childAdapter.subList = subList;
                    //recyclerView.swapAdapter(childAdapter, false);
                    childAdapter.notifyDataSetChanged();
                    Log.e("TitleHolder", "!=null " + position);
                } else {
                    ChildRecyclerAdapter childAdapter = new ChildRecyclerAdapter();
                    childAdapter.subList = subList;
                    recyclerView.setAdapter(childAdapter);
                    //recyclerView.swapAdapter(childAdapter, false);
                    Log.e("TitleHolder", "==null " + position);
                }
                Log.e("TitleHolder", "===" + recyclerView.getRecycledViewPool());


                //嵌套RecyclerView，
                //对于上面的方案，如果是竖向嵌套，还是有点卡顿，特别是子列表数据比较多的时候。主要原因，显示某个Item的时候，该Item的子列表会全部会渲染，而不是显示几条渲染几条。

                //如果横向嵌套估计会好一点，因为横向的不用全部显示。 @TODO 待测试



            }
        }
    }
}
