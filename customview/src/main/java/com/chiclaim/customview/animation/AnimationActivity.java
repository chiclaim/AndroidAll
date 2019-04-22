package com.chiclaim.customview.animation;

import com.chiclaim.android.base.BaseActivity;
import com.chiclaim.customview.R;

import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by chiclaim on 2016/04/02
 */
public class AnimationActivity extends BaseActivity implements ILabelInteraction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, new AnimationMainFragment()).commit();
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

    @Override
    public void setLabel(CharSequence title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

}
