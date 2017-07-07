package sample.module.chiclaim.com.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/7/6.
 */

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);


        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fs;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fs = Arrays.asList(TabFragment.get("Content1"), TabFragment.get("Content2"), TabFragment.get("Content3"));
        }

        @Override
        public int getCount() {
            return fs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
}
