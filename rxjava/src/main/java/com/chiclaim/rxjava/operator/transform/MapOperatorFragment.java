package com.chiclaim.rxjava.operator.transform;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Demonstrate map operator of RxJava <br/>
 * Created by chiclaim on 2016/03/23
 */
public class MapOperatorFragment extends BaseFragment {

    private TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_operator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_operator).setOnClickListener(this);
        tvLogs = (TextView) view.findViewById(R.id.tv_logs);
        tvLogs.setText("Click Button to test 'map operator'");

        view.findViewById(R.id.btn_operator).setOnClickListener(this);
        view.findViewById(R.id.btn_operator_map_ips).setOnClickListener(this);

    }


    private String getIPByUrl(String str) throws MalformedURLException, UnknownHostException {
        URL urls = new URL(str);
        String host = urls.getHost();
        String address = InetAddress.getByName(host).toString();
        int b = address.indexOf("/");
        return address.substring(b + 1);

    }


    private Observable<String> processUrlsIpByMap() {
        return Observable.just(
                "http://www.baidu.com/",//invalid url
                "http://www.google.com/",
                "https://www.bing.com/")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            // if occur a exception how to notify to subscriber? you can use flatMap
                            return s + " : " + getIPByUrl(s);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void observableMapIps() {
        processUrlsIpByMap().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                printLog(tvLogs, "Consume Data: ", s);
            }
        });
    }

    private void observableMapIpList() {
        processUrlsIpByMap().toList().subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> s) {
                printLog(tvLogs, "Consume Data: ", s.toString());
            }
        });
    }


    private void observableMap() {
        Observable.from(new String[]{"This", "is", "RxJava"})
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        printLog(tvLogs, "Transform Data toUpperCase: ", s);
                        return s.toUpperCase();
                    }
                })
                .toList()
                .map(new Func1<List<String>, List<String>>() {
                    @Override
                    public List<String> call(List<String> strings) {
                        printLog(tvLogs, "Transform Data Reverse List: ", strings.toString());
                        Collections.reverse(strings);
                        return strings;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> s) {
                        printLog(tvLogs, "Consume Data ", s.toString());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_operator:
                tvLogs.setText("");
                observableMap();
                break;
            case R.id.btn_operator_map_ips:
                tvLogs.setText("");
                observableMapIps();
                //observableMapIpList();
                break;
        }
    }
}
