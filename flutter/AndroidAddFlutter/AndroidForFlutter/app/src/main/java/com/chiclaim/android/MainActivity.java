package com.chiclaim.android;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.flutter.facade.Flutter;

import android.os.PersistableBundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment firstFragment, secondFragment, thirdFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void showFragment(Fragment fragment, String tag) {
        if (!fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_layout, fragment, tag);
            ft.commit();
        } else if (!fragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(fragment);
            ft.commit();
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null && fragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }
    }

    private Fragment createFragment(String argument) {
        Fragment fragment = new NativeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", argument);
        fragment.setArguments(bundle);
        return fragment;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (firstFragment == null) {
                        firstFragment = createFragment("Android Page 1");
                    }
                    showFragment(firstFragment, "first");
                    hideFragment(secondFragment);
                    hideFragment(thirdFragment);
                    return true;
                case R.id.navigation_dashboard:
                    if (secondFragment == null) {
                        secondFragment = createFragment("Android Page 2");
                    }
                    showFragment(secondFragment, "second");
                    hideFragment(firstFragment);
                    hideFragment(thirdFragment);

                    return true;
                case R.id.navigation_notifications:
                    if (thirdFragment == null) {
                        thirdFragment = Flutter.createFragment(null);
                    }
                    showFragment(thirdFragment, "flutter");
                    hideFragment(firstFragment);
                    hideFragment(secondFragment);
                    return true;
            }
            return false;
        }
    };

}
