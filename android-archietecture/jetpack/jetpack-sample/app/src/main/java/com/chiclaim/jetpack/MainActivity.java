package com.chiclaim.jetpack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.jetpack.basic.ViewModelDemoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, LiveDataDemoActivity.class));
        startActivity(new Intent(this, ViewModelDemoActivity.class));

    }
}
