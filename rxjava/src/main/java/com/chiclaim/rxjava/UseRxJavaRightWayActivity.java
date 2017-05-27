package com.chiclaim.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chiclaim.rxjava.api.ApiServiceFactory;
import com.chiclaim.rxjava.api.UserApi;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chiclaim on 2016/03/31
 */
public class UseRxJavaRightWayActivity extends AppCompatActivity {

    final UserApi userApi = ApiServiceFactory.createService(UserApi.class);

    CompositeSubscription compositeSubscription = new CompositeSubscription();
    Subscription subscriptionForUser;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_rxjava_right_way);
        tvContent = (TextView) findViewById(R.id.tv_content);
        subscriptionForUser = userApi.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        String content = new String(((TypedByteArray) response.getBody()).getBytes());
                        tvContent.setText("receiver data : " + content);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        tvContent.setText("receiver error : " + throwable.getMessage());
                    }
                });

        compositeSubscription.add(subscriptionForUser);

    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, UseRxJavaRightWayActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //if (!subscriptionForUser.isUnsubscribed()) {
        //    subscriptionForUser.unsubscribe();
        //}


        //avoid leak activity/fragment
        if (!compositeSubscription.isUnsubscribed()) {
            //调用compositeSubscription.unsubscribe()后 compositeSubscription 就不可用了.需要重新创建
            compositeSubscription.unsubscribe();
        }
    }
}
