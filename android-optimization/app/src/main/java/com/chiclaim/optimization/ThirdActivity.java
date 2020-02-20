package com.chiclaim.optimization;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.text_content);
        textView.setText("ThirdActivity");

        findViewById(R.id.btn_next).setVisibility(View.GONE);

        Log.d("Optimized", "ThirdActivity onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Optimized", "ThirdActivity onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Optimized", "ThirdActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Optimized", "ThirdActivity onResume");
    }
}
