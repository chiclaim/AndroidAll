package com.chiclaim.customview;

import com.chiclaim.customview.view.ExtendViewActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goExtendViews(View view) {
        startActivity(new Intent(this, ExtendViewActivity.class));
    }


    private long currentBackPressedTime;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (System.currentTimeMillis() - currentBackPressedTime > 2000) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, R.string.tip_app_exit, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
