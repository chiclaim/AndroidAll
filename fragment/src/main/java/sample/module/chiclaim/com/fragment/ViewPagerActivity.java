package sample.module.chiclaim.com.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

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

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        int moreWidth = getResources().getDisplayMetrics().widthPixels / 4;
        int moreHeight = (int) (getResources().getDisplayMetrics().density * 36 + 0.5);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        View view = findViewById(R.id.text_other);
        RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(moreWidth, moreHeight);
        rll.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view.setLayoutParams(rll);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3, false);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fs;
        private List<String> titles = Arrays.asList("Title1", "Title2", "Title3", "Title4");

        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fs = Arrays.asList(TabFragment.get("Content1"), TabFragment.get("Content2"),
                    TabFragment.get("Content3"), TabFragment.get("Content4"));
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
