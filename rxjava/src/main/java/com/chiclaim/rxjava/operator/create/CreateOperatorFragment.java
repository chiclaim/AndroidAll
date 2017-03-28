package com.chiclaim.rxjava.operator.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chiclaim on 2016/03/23
 */
public class CreateOperatorFragment extends BaseFragment {


    private TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_operator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_create).setOnClickListener(this);
        view.findViewById(R.id.btn_just).setOnClickListener(this);
        view.findViewById(R.id.btn_from).setOnClickListener(this);

        tvLogs = (TextView) view.findViewById(R.id.tv_logs);
        tvLogs.setText("Click Button to test 'map operator'");

    }


    private void observableCreate() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            printLog(tvLogs, "Emit Data:", i + "");
                            subscriber.onNext("" + i);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //showToast(s);
                printLog(tvLogs, "Consume Data:", s);
            }
        });
    }


    private void observableJust() {
        Observable.just("hello", "world").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                printLog(tvLogs, "Consume Data : ", s);
            }
        });
    }


    private void observableFrom() {
        Observable<String> sentenceObservable = Observable.from(new String[]{"This", "is", "RxJava"});
        sentenceObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                printLog(tvLogs, "Consume Data : ", s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_create:
                tvLogs.setText("");
                observableCreate();
                break;
            case R.id.btn_just:
                tvLogs.setText("");
                observableJust();
                break;
            case R.id.btn_from:
                tvLogs.setText("");
                observableFrom();
                break;
        }
    }
}
