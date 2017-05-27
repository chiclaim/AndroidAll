package com.chiclaim.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiclaim.rxjava.operator.CheckCacheFragment;
import com.chiclaim.rxjava.operator.CombineLatestFragment;
import com.chiclaim.rxjava.operator.HttpWithTokenFragment;
import com.chiclaim.rxjava.operator.ObservableDependencyFragment;
import com.chiclaim.rxjava.operator.RxJavaLeakFragment;
import com.chiclaim.rxjava.operator.SearchDebounceFragment;
import com.chiclaim.rxjava.operator.TryWhenFragment;
import com.chiclaim.rxjava.operator.create.CreateOperatorFragment;
import com.chiclaim.rxjava.operator.transform.ConcatFlatMapFragment;
import com.chiclaim.rxjava.operator.transform.FlatMapOperatorFragment;
import com.chiclaim.rxjava.operator.transform.MapOperatorFragment;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_create_just_from).setOnClickListener(this);
        view.findViewById(R.id.btn_map).setOnClickListener(this);
        view.findViewById(R.id.btn_flat_map).setOnClickListener(this);
        view.findViewById(R.id.btn_token).setOnClickListener(this);
        view.findViewById(R.id.btn_search_debounce).setOnClickListener(this);
        view.findViewById(R.id.btn_flat_concat_map).setOnClickListener(this);
        view.findViewById(R.id.btn_observable_dependence_on_other_observable).setOnClickListener(this);
        view.findViewById(R.id.btn_multiple_observables).setOnClickListener(this);
        view.findViewById(R.id.btn_retry_when_http_error).setOnClickListener(this);
        view.findViewById(R.id.btn_combine_latest).setOnClickListener(this);
        view.findViewById(R.id.btn_use_rxjava_in_right_way).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_leak).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_just_from:
                addFragment(new CreateOperatorFragment());
                break;
            case R.id.btn_map:
                addFragment(new MapOperatorFragment());
                break;
            case R.id.btn_flat_map:
                addFragment(new FlatMapOperatorFragment());
                break;
            case R.id.btn_token:
                addFragment(new HttpWithTokenFragment());
                break;
            case R.id.btn_search_debounce:
                addFragment(new SearchDebounceFragment());
                break;
            case R.id.btn_flat_concat_map:
                addFragment(new ConcatFlatMapFragment());
                break;
            case R.id.btn_observable_dependence_on_other_observable:
                addFragment(new ObservableDependencyFragment());
                break;
            case R.id.btn_multiple_observables:
                addFragment(new CheckCacheFragment());
                break;
            case R.id.btn_retry_when_http_error:
                addFragment(new TryWhenFragment());
                break;
            case R.id.btn_combine_latest:
                addFragment(new CombineLatestFragment());
                break;
            case R.id.btn_rxjava_leak:
                addFragment(new RxJavaLeakFragment());
                break;
            case R.id.btn_use_rxjava_in_right_way:
                UseRxJavaRightWayActivity.launch(getActivity());
                break;

        }
    }
}