package com.chiclaim.optimization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chiclaim.optimization.launchtime.ListActivity;
import com.chiclaim.optimization.launchtime.TimeRecord;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Optimized", "MainActivity onCreate");
        TextView textView = findViewById(R.id.text_content);
        textView.setText("MainActivity");
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThirdActivity.class));
            }
        });

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                TimeRecord.startRecord();
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
