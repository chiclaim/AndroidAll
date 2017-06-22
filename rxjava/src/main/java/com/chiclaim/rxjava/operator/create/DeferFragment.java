package com.chiclaim.rxjava.operator.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/6/22.
 */

public class DeferFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_defer_operator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RxUtils.deferObservable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Log.d("DeferFragment", "defer callable call invoked");
                return null;
            }
        });

        RxUtils.createObservable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Log.d("DeferFragment", "create callable call invoked");
                return null;
            }
        });
    }
}
