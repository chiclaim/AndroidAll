package com.chiclaim.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：RecyclerView的Item又是一个RecyclerView
 * <br/>
 * Created by kumu on 2017/4/6.
 */

//http://stackoverflow.com/questions/32011995/how-to-have-a-listview-recyclerview-inside-a-parent-recyclerview

public class RecyclerViewInRecyclerView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_in_recyclerview);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Title " + i);
        }
        recyclerView.setAdapter(new MyAdapter(this, list));
    }


    private static class MyAdapter extends RecyclerView.Adapter {
        LayoutInflater inflater;
        List<String> list;

        MyAdapter(Context context, List<String> list) {
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TitleHolder(inflater.inflate(R.layout.item_title_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.bind(position, list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class TitleHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;
            RecyclerView recyclerView;

            public TitleHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_child);
            }

            public void bind(int position, String title) {
                tvTitle.setText(title);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                List<String> titles = new ArrayList<>();
                for (int i = 0; i < position + 1; i++) {
                    titles.add("Sub Title " + i);
                }
                recyclerView.setAdapter(new ChildRecyclerAdapter(titles));
            }

            class ChildRecyclerAdapter extends RecyclerView.Adapter {
                List<String> subList;

                ChildRecyclerAdapter(List<String> subList) {
                    this.subList = subList;
                }

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new ChildHolder(inflater.inflate(R.layout.item_child_layout, parent, false));
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    ChildHolder childHolder = (ChildHolder) holder;
                    childHolder.tvSubTitle.setText(subList.get(position));
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


        }
    }
}
