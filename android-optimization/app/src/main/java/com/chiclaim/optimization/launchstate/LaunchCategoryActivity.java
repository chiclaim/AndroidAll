package com.chiclaim.optimization.launchstate;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.optimization.R;

public class LaunchCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_category);

        Log.d("Optimized", "LaunchCategoryActivity onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Optimized", "LaunchCategoryActivity onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Optimized", "LaunchCategoryActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Optimized", "LaunchCategoryActivity onResume");
    }
}
