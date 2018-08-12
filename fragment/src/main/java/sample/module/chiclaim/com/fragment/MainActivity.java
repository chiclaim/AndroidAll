package sample.module.chiclaim.com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Fragment mFragmentAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sample.module.chiclaim.com.fragment.R.layout.activity_main);
    }

    public void fragmentLifecycle(View view) {
        startActivity(new Intent(this, FragmentLifecycleActivity.class));
    }

    public void fragmentForResult(View view) {
        startActivity(new Intent(this, ActivityTestFragmentResult.class));
    }


    private void showFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        //Log.e("Lifecycle", "MainActivity fragment.isAdded() : " + fragment.isAdded() + ", " + fragment);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(frameId, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void goDrawerLayout(View view) {
        startActivity(new Intent(this, DrawerLayoutActivity.class));
    }

    public void goViewPager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    public void fragmentAnimation(View view) {
        if (mFragmentAnimation == null) {
            mFragmentAnimation = new FragmentAnimation();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_expand, R.anim.fragment_collapse);
        if (mFragmentAnimation.isVisible()) {
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
            transaction.hide(mFragmentAnimation);
        } else {
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            if (mFragmentAnimation.isAdded()) {
                transaction.show(mFragmentAnimation);
            } else {
                transaction.add(R.id.fragment_container, mFragmentAnimation);
            }
        }
        transaction.commit();
    }
}
