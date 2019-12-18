package com.chiclaim.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chiclaim.butterknife.MyButterKnife;
import com.chiclaim.processor.annotation.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.view)
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyButterKnife.bind(this);

        Toast.makeText(this, textView + "--textView", Toast.LENGTH_LONG).show();
        Log.d("MainActivity", textView + "," + view);

        textView.setText("initialed by my butter knife");
    }
}
