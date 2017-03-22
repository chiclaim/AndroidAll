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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sample.module.chiclaim.com.fragment.R.layout.activity_main);
    }

    public void fragmentLifecycle(View view) {
        startActivity(new Intent(this, FragmentLifecycleActivity.class));
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
}
