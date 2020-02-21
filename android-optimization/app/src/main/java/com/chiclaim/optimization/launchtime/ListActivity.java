package com.chiclaim.optimization.launchtime;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiclaim.optimization.ListAdapter;
import com.chiclaim.optimization.R;

import java.util.ArrayList;
import java.util.List;

//  onWindowFocusChanged VS OnPreDrawListener

/*

    TimeRecord: onCreate
    TimeRecord: onResume
    TimeRecord: FirstItemDraw:186
    TimeRecord: onWindowFocusChanged:246
    TimeRecord: onWindowFocusChanged hasFocus -> true

    // press back menu

    TimeRecord: onWindowFocusChanged:5116
    TimeRecord: onWindowFocusChanged hasFocus -> false

    // 焦点发生变化都会调用 onWindowFocusChanged （界面展示、关闭、按下 HOME 键等）


    onWindowFocusChanged 作为启动时间的结束位置，是不包括网络请求的时间的
    OnPreDrawListener 列表第一条展示的时间位置结束位置，如果列表数据是从网络拉去的，那么是整个时间是包括网络请求的时间的

*/

public class ListActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);

        for (int i = 0; i < 100; i++) {
            list.add("Item" + i);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(list);
        recyclerView.setAdapter(adapter);

        Log.e("TimeRecord", "onCreate");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 首帧时间
        TimeRecord.stopRecord("onWindowFocusChanged");

        Log.e("TimeRecord", "onWindowFocusChanged hasFocus -> " + hasFocus);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TimeRecord", "onResume");
    }

}
