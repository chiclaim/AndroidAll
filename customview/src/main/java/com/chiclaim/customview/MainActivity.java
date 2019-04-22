package com.chiclaim.customview;

import com.chiclaim.customview.animation.AnimationActivity;
import com.chiclaim.customview.hencoder.ui01.UI01Activity;

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

    public void recyclerViewNested(View view) {
        startActivity(new Intent(this, RecyclerViewInRecyclerView.class));
    }

    public void testEditTextInMeiZu(View view) {
        startActivity(new Intent(this, EditViewInMZActivity.class));
    }

    public void henCoder01(View view) {
        startActivity(new Intent(this, UI01Activity.class));
    }

    public void animation(View view){
        startActivity(new Intent(this, AnimationActivity.class));
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
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
