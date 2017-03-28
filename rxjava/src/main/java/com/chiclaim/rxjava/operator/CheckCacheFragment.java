package com.chiclaim.rxjava.operator;

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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Retrieve data from Multiple Observables <br/>
 * Created by chiclaim on 2016/03/29
 */
public class CheckCacheFragment extends BaseFragment {


    TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLogs = (TextView) view.findViewById(R.id.tv_logs);
        view.findViewById(R.id.btn_operator).setOnClickListener(this);
    }

    String data[] = {null, null, "network"};
    //String data[] = {null, "disk","network"};
    //String data[] = {"memory", null,"network"};
    //String data[] = {"memory", "disk",null};
    //String data[] = {"memory", "disk","network"};

    private Observable<String> memorySource = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            String d = data[0];
            printLog(tvLogs, "", "----start check memory data. value is null? " + (d == null));
            if (d != null) {
                subscriber.onNext(d);
            }
            subscriber.onCompleted();
        }
    });


    private Observable<String> diskSource = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            String d = data[1];
            printLog(tvLogs, "", "----start check disk data. value is null? " + (d == null));
            if (d != null) {
                subscriber.onNext(d);
            }
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io());


    private Observable<String> networkSource = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            String d = data[2];
            printLog(tvLogs, "", "----start check network data. value is null? " + (d == null));
            if (d != null) {
                subscriber.onNext(d);
            }
            subscriber.onCompleted();
        }
    }).subscribeOn(Schedulers.io());



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_operator:
                tvLogs.setText("");
                Observable.concat(memorySource, diskSource, networkSource)
                        //first()-> if no data from observables will cause exception :
                        //java.util.NoSuchElementException: Sequence contains no elements
                        //takeFirst -> no exception
                        .takeFirst(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String s) {
                                return s != null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                printLog(tvLogs, "Getting data from ", s);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                printLog(tvLogs, "Error: ", throwable.getMessage());
                            }
                        });
                break;
        }
    }
}