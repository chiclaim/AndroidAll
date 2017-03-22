package sample.module.chiclaim.com.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FragmentLifecycleActivity extends AppCompatActivity {

    private FragmentLifecycle fragmentLifecycle;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        log("Activity onAttachFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("Activity onCreate, savedInstanceState=" + savedInstanceState);
        setContentView(R.layout.activity_fragment_container);


        if (savedInstanceState != null) {
            fragmentLifecycle = (FragmentLifecycle) getSupportFragmentManager().findFragmentById(R.id.container);
        }

        if (fragmentLifecycle == null) {
            fragmentLifecycle = new FragmentLifecycle();
        }

        showFragment(getSupportFragmentManager(), fragmentLifecycle, R.id.container);
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("Activity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("Activity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("Activity onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("Activity onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        log("Activity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("Activity onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        log("Activity onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        log("Activity onRestoreInstanceState");
    }

    public void log(String log) {
        Log.d("Lifecycle", log);
    }

    private void showFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        //Log.e("Lifecycle", "FragmentLifecycle fragment.isAdded() : " + fragment.isAdded() + ", " + fragment);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(frameId, fragment);
        }
        ft.commitAllowingStateLoss();
    }
}
