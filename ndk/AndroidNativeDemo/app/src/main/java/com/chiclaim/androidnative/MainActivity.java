package com.chiclaim.androidnative;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.androidnative.jni.JNIHolder;
import com.chiclaim.androidnative.jni.User;

public class MainActivity extends AppCompatActivity {

    private JNIHolder jniHolder = new JNIHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] supportedAIs = Build.SUPPORTED_ABIS;
            for (String abi : supportedAIs) {
                Log.d("MainActivity", "----abi:" + abi);
            }
        }


        TextView tv2 = findViewById(R.id.sample_text2);
        tv2.setText(jniHolder.stringFromJNI());
        tv2.append("\n");
        tv2.append(JNIHolder.stringFromJNI2());
        tv2.append("\n");
        tv2.append(jniHolder.getIntArrayFromJNI());
        tv2.append("\n");
        tv2.append("在 Native 中将 number = " + jniHolder.number + " 的值改为 ");
        tv2.append(jniHolder.updateObjProperty() + "");
        tv2.append("\n");
        tv2.append(jniHolder.invokeObjMethod());

        User user = jniHolder.createObj();
        tv2.append("\n");
        tv2.append("Native 创建 Java 对象并返回：" + user.getUsername());

        int[] array = {1, 2, 3, 4, 5};
        jniHolder.updateIntArray(array);

        tv2.append("\n");
        tv2.append("Native 修改 Java int数组：" + array[0]);


        tv2.append("\n");
        tv2.append("判断对象是否相等：" + jniHolder.equals(user, user));

        tv2.append("\n");
        tv2.append("判断对象是否相等2：" + jniHolder.equals(user, new User("Chiclaim")));

    }


}
