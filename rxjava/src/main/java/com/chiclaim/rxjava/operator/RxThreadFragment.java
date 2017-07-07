package com.chiclaim.rxjava.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;
import com.chiclaim.rxjava.operator.create.Callable;
import com.chiclaim.rxjava.operator.create.RxUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.chiclaim.rxjava.operator.create.RxUtils.deferObservable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/7/7.
 */

public class RxThreadFragment extends BaseFragment {


    private void rxThread1() {
        deferObservable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.e("RxThreadFragment", "Callable thread name : " + Thread.currentThread().getName());
                return "123456789";
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String string) {
                Log.e("RxThreadFragment", "Func1--1 thread name : " + Thread.currentThread().getName());
                String reverse = new StringBuilder(string).reverse().toString();
                return Observable.just(reverse);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                Log.e("RxThreadFragment", "Func1--2 thread name : " + Thread.currentThread().getName());
                String reverse = new StringBuilder(s).reverse().toString();
                return Observable.just(reverse);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxThreadFragment", "Action1 thread name : " + Thread.currentThread().getName());
                Log.e("RxThreadFragment", s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        //subscribeOn切换线程只有第一次调用的时候起作用，后面调用subscribeOn无效，都是使用第一次设置的
    }


    private void rxThread2() {
        final Observable<String> observable1 = RxUtils.deferObservable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.e("RxThreadFragment", "observable1 thread name : " + Thread.currentThread().getName());
                return "observable1 Schedulers.io()";
            }
        }).subscribeOn(Schedulers.io());

        final Observable<String> observable2 = RxUtils.deferObservable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.e("RxThreadFragment", "observable2 thread name : " + Thread.currentThread().getName());
                return "observable2 AndroidSchedulers.mainThread()";
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

        RxUtils.deferObservable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "test thread";
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                Log.e("RxThreadFragment", "flatMap1 thread name : " + Thread.currentThread().getName());
                return observable1;
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                Log.e("RxThreadFragment", "flatMap2 thread name : " + Thread.currentThread().getName());
                return observable2;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxThreadFragment", "subscribe Action1 thread name : " + Thread.currentThread().getName());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

//        flatMap1 thread name : main
//        observable1 thread name : RxIoScheduler-2
//        flatMap2 thread name : RxIoScheduler-2
//        observable2 thread name : main
//        subscribe Action1 thread name : main

        //当前observable默认使用上一个observable的线程
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rx_thread_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_rx_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxThread1();
            }
        });

        view.findViewById(R.id.btn_observable_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxThread2();
            }
        });

    }
}
