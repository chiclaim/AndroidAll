package com.chiclaim.customview.hencoder.ui01;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiclaim.customview.R;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/8/3.
 */

public class DrawCircleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw_circle_layout, container, false);
    }


}
