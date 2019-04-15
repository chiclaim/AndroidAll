package com.chiclaim.customview.hencoder.ui01;

import com.chiclaim.customview.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class UI01Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ui01_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.container);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            switch (position) {
                case 0:
                    ColorView colorView = new ColorView(getContext());
                    FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400);
                    colorView.setLayoutParams(ll);
                    container.addView(colorView);
                    break;
                case 1:
                    CircleView circleView = new CircleView(getContext());
                    container.addView(circleView);
                    break;
                case 2:
                    RectView rectView = new RectView(getContext());
                    container.addView(rectView);
                    break;
                case 3:
                    RoundRectView roundRectView = new RoundRectView(getContext());
                    container.addView(roundRectView);
                    break;
            }
        }
    }

    public static Fragment newInstance(Bundle bundle) {
        UI01Fragment fragment = new UI01Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
