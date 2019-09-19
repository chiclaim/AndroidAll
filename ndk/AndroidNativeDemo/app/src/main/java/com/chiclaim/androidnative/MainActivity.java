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
        tv2.append("在 Native 中修改 number 的值 ");
        tv2.append(jniHolder.updateObjProperty() + "");
        tv2.append("\n");
        tv2.append(jniHolder.invokeObjMethod());

        /*User user = jniHolder.createObj();
        tv2.append("\n");
        tv2.append("Native 创建 Java 对象并返回：" + user.getUsername());*/

        int[] array = {1, 2, 3, 4, 5};
        jniHolder.updateIntArray(array);

        tv2.append("\n");
        tv2.append(array[0] + "");
    }


}
