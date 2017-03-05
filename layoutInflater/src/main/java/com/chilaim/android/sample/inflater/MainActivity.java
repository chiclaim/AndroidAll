package com.chilaim.android.sample.inflater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;
    LinearLayout viewGroup;

    Button btnAddView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewGroup = (LinearLayout) findViewById(R.id.activity_main);
        inflater = LayoutInflater.from(this);

        btnAddView = (Button) findViewById(R.id.btn_add_view);
    }

    public void addView(View view) {
        add();
    }


    private void add() {
        // inflater.inflate(R.layout.inflater,viewGroup);
        // View view = inflater.inflate(R.layout.inflater,viewGroup,false);
        View view = inflater.inflate(R.layout.inflater, null);
        //view.setLayoutParams();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            Log.e("MainActivity", "layoutParams is null");
        } else {
            Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
        }
        viewGroup.addView(view);


        //--------------------------
        //viewGroup.addView(view,new ViewGroup.LayoutParams(-2,-2)); 相当于下面两行代码
        // view.setLayoutParams(new ViewGroup.LayoutParams(-2,-2));
        //viewGroup.addView(view)
        //--------------------------

        //viewGroup.addView(view,new ViewGroup.LayoutParams(-2,-2));

    }
}
