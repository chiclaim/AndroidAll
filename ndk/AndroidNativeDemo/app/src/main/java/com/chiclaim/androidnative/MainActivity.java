package com.chiclaim.androidnative;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.androidnative.jni.JNIHolder;

public class MainActivity extends AppCompatActivity {

    private JNIHolder jniHolder = new JNIHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv2 = findViewById(R.id.sample_text2);
        tv2.setText(jniHolder.stringFromJNI());
        tv2.append("\n");
        tv2.append(JNIHolder.stringFromJNI2());
        tv2.append("\n");
        tv2.append(jniHolder.getIntArrayFromJNI());
        tv2.append("\n");
        tv2.append("在 Native 中将 number 从 ");
        tv2.append(jniHolder.number + " ");
        tv2.append("改成");
        tv2.append(jniHolder.updateObjProperty() + "");
    }


}
