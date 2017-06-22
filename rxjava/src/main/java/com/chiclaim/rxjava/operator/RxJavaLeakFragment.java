package com.chiclaim.rxjava.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;
import com.chiclaim.rxjava.api.ApiServiceFactory;
import com.chiclaim.rxjava.api.OtherApi;

import java.util.concurrent.TimeUnit;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/28.
 */

public class RxJavaLeakFragment extends BaseFragment {
    OtherApi otherApi;
    Subscription retrofitSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rxjava_leak_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_request_netword_and_pop).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_request_netword_and_pop:
                //doRetrofit();
                timerOperator();
                break;
        }
    }

    private Subscription timerSubscription;

    private void timerOperator() {
        Log.e("RxJavaLeakFragment", "start test timer: " + ",current time:" + System.currentTimeMillis());
        timerSubscription = Observable.timer(5000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        Log.e("RxJavaLeakFragment", "fragment instance: " + RxJavaLeakFragment.this + ", call parameter:" + aLong + ",current time:" + System.currentTimeMillis());
                        return null;
                    }
                }).subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.e("RxJavaLeakFragment", "fragment instance: " + RxJavaLeakFragment.this + ",current time:" + System.currentTimeMillis());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        //@TODO 只要timerSubscription.unsubscribe();执行，就不会内存泄漏？？
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timerSubscription != null) {
            timerSubscription.unsubscribe();
        }
    }

    public void doRetrofit() {
        if (otherApi == null) {
            otherApi = ApiServiceFactory.createService(OtherApi.class);
        }
        retrofitSubscription = otherApi.testTimeout("10000")
                .onTerminateDetach()
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        String content = new String(((TypedByteArray) response.getBody()).getBytes());
                        Log.d("RxJavaLeakFragment", RxJavaLeakFragment.this + ":" + content);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (retrofitSubscription != null && !retrofitSubscription.isUnsubscribed()) {
            retrofitSubscription.unsubscribe();
            Log.d("RxJavaLeakFragment", "retrofitSubscription.unsubscribe()");
        }
    }
}
