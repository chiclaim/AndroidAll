package com.chilaim.android.sample.inflater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = (LinearLayout) findViewById(R.id.activity_main);
        inflater = LayoutInflater.from(this);

    }

    public void addView01(View view) {
        add01();
    }

    public void addView02(View view) {
        add02();
    }

    public void addView03(View view) {
        add03();
    }

    /**
     * inflater.inflate(layoutId, null);
     */
    private void add01() {
        View view = inflater.inflate(R.layout.inflater01, null);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            Log.e("MainActivity", "layoutParams is null");
        } else {
            Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
        }

        root.addView(view);

        //--------------------------
        //viewGroup.addView(view,new ViewGroup.LayoutParams(-2,-2));
        //相当于下面两行代码
        // view.setLayoutParams(new ViewGroup.LayoutParams(-2,-2));
        //viewGroup.addView(view)
        //--------------------------

        //viewGroup.addView(view,new ViewGroup.LayoutParams(-2,-2));

    }


    /**
     * inflater.inflate(layoutId, root, false);
     */
    private void add02() {
        View view = inflater.inflate(R.layout.inflater01, root, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            Log.e("MainActivity", "layoutParams is null");
        } else {
            Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
        }
        root.addView(view);
    }

    /**
     * inflater.inflate(layoutId, root, true);
     */
    private void add03() {
        View view = inflater.inflate(R.layout.inflater01, root, true);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            Log.e("MainActivity", "layoutParams is null");
        } else {
            Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
        }
    }
}
