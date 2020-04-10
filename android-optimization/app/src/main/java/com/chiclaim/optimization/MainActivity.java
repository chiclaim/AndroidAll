package com.chiclaim.optimization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chiclaim.optimization.launchstate.LaunchCategoryActivity;
import com.chiclaim.optimization.launchtime.ListActivity;
import com.chiclaim.optimization.launchtime.TimeRecord;
import com.chiclaim.optimization.systrace.SystraceTestActivity;
import com.chiclaim.optimization.traceview.TraceViewTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Optimized", "MainActivity onCreate");

        findViewById(R.id.btn_launch_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LaunchCategoryActivity.class));
            }
        });

        findViewById(R.id.time_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                TimeRecord.startRecord();
            }
        });

        findViewById(R.id.trace_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TraceViewTestActivity.class));
            }
        });

        findViewById(R.id.systrace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SystraceTestActivity.class));
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Optimized", "MainActivity onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Optimized", "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Optimized", "MainActivity onResume");
    }
}
