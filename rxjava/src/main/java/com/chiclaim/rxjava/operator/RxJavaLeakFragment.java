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

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/28.
 */

public class RxJavaLeakFragment extends BaseFragment {
    OtherApi otherApi;
    Subscription subscription;

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
                if (otherApi == null) {
                    otherApi = ApiServiceFactory.createService(OtherApi.class);
                }
                subscription = otherApi.testTimeout("10000")
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
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            Log.d("RxJavaLeakFragment", "subscription.unsubscribe()");
        }
    }
}
