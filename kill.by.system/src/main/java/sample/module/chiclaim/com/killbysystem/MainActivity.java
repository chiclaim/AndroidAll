package sample.module.chiclaim.com.killbysystem;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Fragment myFragment;


    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e("Kill", "MainActivity onAttachFragment : " + fragment);
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e("Kill", "MainActivity onAttachFragment : " + fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Kill", "MainActivity onCreate : " + " state: " + savedInstanceState);

        if (savedInstanceState != null) {
            myFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        }

        if (myFragment == null) {
            myFragment = new MyFragment();
        }

        showFragment(getSupportFragmentManager(), myFragment, R.id.container);
        //getSupportFragmentManager().beginTransaction().add(R.id.container, myFragment).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Kill", "MainActivity onSaveInstanceState : " + System.currentTimeMillis());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("Kill", "MainActivity onRestoreInstanceState : " + System.currentTimeMillis());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.e("Kill", "MainActivity onStart : " + System.currentTimeMillis());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Kill", "MainActivity onRestart : " + System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("Kill", "MainActivity onResume : " + System.currentTimeMillis());
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private static void showFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        Log.e("Kill", "MainActivity fragment.isAdded() : " + fragment.isAdded() + ", " + fragment);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(frameId, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Kill", "MainActivity onDestroy : " + System.currentTimeMillis());
    }
}
