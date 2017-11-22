package com.chiclaim.rxjava.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;
import com.chiclaim.rxjava.operator.create.Callable;
import com.chiclaim.rxjava.operator.create.RxUtils;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description：如果调用了void onError(Throwable e)方法，那么onNext和onCompleted都不会执行。会在onError调用之前，把Subscription取消注册。
 5. 整个事件流不管是正常结束（onComplete）还是出现了异常（onError），Subscription都会被取消注册（unsubscribe）。
 但是，由于我们可能执行一些耗时操作，界面又被关闭了，所以还需要把subscription取消注册
 * <br/>
 * Created by kumu on 2017/11/22.
 */

public class UnSubscriptionFragment extends BaseFragment {

    rx.Subscription subscription, subscription2;
    TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unsubscribe_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLogs = (TextView) view.findViewById(R.id.tv_logs);
        subscription = RxUtils.deferObservable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(10000);
                return "Hello World";
            }
        }).subscribeOn(Schedulers.io()).onTerminateDetach().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                printLog(tvLogs, "", "receive data :" + o);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                printLog(tvLogs, "", "error :" + throwable.getMessage());
            }
        });


        subscription2 = RxUtils.deferObservable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "Hello World".substring(0, 100);
            }
        }).subscribeOn(Schedulers.io()).onTerminateDetach().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                printLog(tvLogs, "", "receive data :" + o);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                printLog(tvLogs, "", throwable.getMessage());
            }
        });

        view.findViewById(R.id.btn_operator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscription.isUnsubscribed()) {
                    printLog(tvLogs, "", "----subscription unSubscribed successfully");
                    subscription.unsubscribe();
                } else {
                    printLog(tvLogs, "", "----subscription already was unSubscribed ");
                }
            }
        });

        view.findViewById(R.id.btn_operator2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscription2.isUnsubscribed()) {
                    printLog(tvLogs, "", "----subscription2 unSubscribed successfully");
                    subscription2.unsubscribe();
                } else {
                    printLog(tvLogs, "", "----subscription2 already was unSubscribed ");
                }
            }
        });
    }
}
