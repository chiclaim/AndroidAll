package com.chiclaim.rxjava.operator.transform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * flatMap compare with concatMap  <br/>
 * Created by chiclaim on 2016/03/28
 */
public class ConcatFlatMapFragment extends BaseFragment {

    TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_concat_flat_map_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvLogs = (TextView) view.findViewById(R.id.tv_logs);

        view.findViewById(R.id.btn_concat_one_thread).setOnClickListener(this);
        view.findViewById(R.id.btn_concat_multiple_threads).setOnClickListener(this);
        view.findViewById(R.id.btn_flat_one_thread).setOnClickListener(this);
        view.findViewById(R.id.btn_flat_multiple_threads).setOnClickListener(this);
    }


    private String getIPByUrl(String str) throws MalformedURLException, UnknownHostException {
        URL urls = new URL(str);
        String host = urls.getHost();
        String address = InetAddress.getByName(host).toString();
        int b = address.indexOf("/");
        return address.substring(b + 1);

    }


    private synchronized Observable<String> createIpObservableMultiThread(final String url) {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            String ip = getIPByUrl(url);
                            printLog(tvLogs, "Emit Data -> ", url + "->" + ip);
                            subscriber.onNext(ip);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            //subscriber.onError(e);
                            subscriber.onNext(null);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                            //subscriber.onError(e);
                            subscriber.onNext(null);
                        }
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
        //.subscribeOn(Schedulers.io()) 注意该方法在这里调用和放在使用该Observable的地方调 产生不同的影响
        //把注释去掉会使用不同的线程去执行,放在放在使用该Observable的地方调会共用一个线程去执行
    }

    private synchronized Observable<String> createIpObservableOneThread(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String ip = getIPByUrl(url);
                    printLog(tvLogs, "Emit Data -> ", url + "->" + ip);
                    subscriber.onNext(ip);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    //subscriber.onError(e);
                    subscriber.onNext(null);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    //subscriber.onError(e);
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        });
    }


    List<String> urls = Arrays.asList(
            "http://www.baidu.com/",
            "http://www.google.com/",
            "https://www.bing.com/");

    public Observable<String> createUrlObservable() {
        return Observable.from(urls);
    }

    private Observable<String> processUrlIpByConcatMapOneThread() {
        return createUrlObservable()
                .concatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return createIpObservableOneThread(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Observable<String> processUrlIpByConcatMapMultipleThread() {
        return createUrlObservable()
                .concatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return createIpObservableMultiThread(s);
                    }
                }).filter(new Func1<String, Boolean>() {
                    //filter data [if result is null or empty ,it will be ignored]
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);
                    }
                })
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<String> processUrlIpByFlatMapMultipleThread() {
        return createUrlObservable()
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        //Log.d("call", getMainText("call"));
                        return createIpObservableMultiThread(s);
                    }
                }).filter(new Func1<String, Boolean>() {
                    //filter data [if result is null or empty ,it will be ignored]
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);
                    }
                })
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Observable<String> processUrlIpByFlatMapOneThread() {
        return createUrlObservable()
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return createIpObservableOneThread(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private void subscribe(Observable<String> observable) {
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                printLog(tvLogs, "Consume Data <- ", s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                printErrorLog(tvLogs, "throwable call()", throwable.getMessage());
            }
        });
    }


    private void resetLogs() {
        tvLogs.setText("Original Stream Order:");
        for (String url : urls) {
            tvLogs.append("\n");
            tvLogs.append(url);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // concat();
        switch (v.getId()) {
            case R.id.btn_concat_one_thread:
                resetLogs();
                subscribe(processUrlIpByConcatMapOneThread());
                break;
            case R.id.btn_concat_multiple_threads:
                resetLogs();
                subscribe(processUrlIpByConcatMapMultipleThread());
                break;
            case R.id.btn_flat_one_thread:
                resetLogs();
                subscribe(processUrlIpByFlatMapOneThread());
                break;
            case R.id.btn_flat_multiple_threads:
                resetLogs();
                subscribe(processUrlIpByFlatMapMultipleThread());
                break;
        }
    }
}
