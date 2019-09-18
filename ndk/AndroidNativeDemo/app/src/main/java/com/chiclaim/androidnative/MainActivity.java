package com.chiclaim.androidnative;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.androidnative.jni.JNIHolder;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(new JNIHolder().stringFromJNI());

        TextView tv2 = findViewById(R.id.sample_text2);
        tv2.setText(JNIHolder.stringFromJNI2());
    }


}
