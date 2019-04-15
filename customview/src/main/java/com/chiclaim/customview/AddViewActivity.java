package com.chiclaim.customview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/4/11.
 */

public class AddViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        LinearLayout ll = (LinearLayout) findViewById(R.id.content);
        for (int i = 0; i < 10; i++) {
            TextView textView1 = new TextView(this);
            textView1.setText("text" + i);
            ll.addView(textView1);
            int index = ll.indexOfChild(textView1);
            Log.e("AddView", index + "=====");
        }


        TextView textView = new TextView(this);
        textView.setText("new text 0");
        ll.addView(textView, 0);


        TextView textView2 = new TextView(this);
        textView2.setText("new text 00");
        ll.addView(textView2, 0);
    }


}
