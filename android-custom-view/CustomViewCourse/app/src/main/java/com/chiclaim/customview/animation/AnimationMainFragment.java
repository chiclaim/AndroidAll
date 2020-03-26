package com.chiclaim.customview.animation;

import com.google.android.material.tabs.TabLayout;

import com.chiclaim.customview.R;

import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by chiclaim on 2016/04/02
 */
public class AnimationMainFragment extends BaseFragment {

    private List<String> tabs;
    private List<? extends Fragment> fragments;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_animation_layout;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);


        initTabTitles();
    }

    private void initTabTitles() {
        fragments = Arrays.asList(new ViewAnimationFragment(), new PropertyAnimatorFragment());
        tabs = Arrays.asList("View Animation", "Property Animator");

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }
        };

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onResume() {
        super.onResume();
        setLabel("Android Animation");
    }


}
