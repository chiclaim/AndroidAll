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

    @Override
    public void setLabel(CharSequence title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

}
