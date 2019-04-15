package sample.module.chiclaim.com.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/2.
 */

public class DrawerLayoutActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_right);
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void addSlide(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit,
                R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        transaction.add(R.id.frame_drawer, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void removeSlide() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

}
