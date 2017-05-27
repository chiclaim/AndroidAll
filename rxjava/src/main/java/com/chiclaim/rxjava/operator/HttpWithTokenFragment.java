package com.chiclaim.rxjava.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;
import com.chiclaim.rxjava.api.ApiServiceFactory;
import com.chiclaim.rxjava.api.NetErrorType;
import com.chiclaim.rxjava.api.UserApi;
import com.chiclaim.rxjava.exception.AccessDenyException;
import com.chiclaim.rxjava.model.AuthToken;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chiclaim on 2016/03/24
 */
public class HttpWithTokenFragment extends BaseFragment {

    private final UserApi userApi = ApiServiceFactory.createService(UserApi.class);
    private TextView tvLogs;
    private boolean loading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_http_token, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLogs = (TextView) view.findViewById(R.id.tv_logs);
        view.findViewById(R.id.btn_request).setOnClickListener(this);
        requestUserInfo();
    }


    public Observable<AuthToken> createTokenObvervable() {
        return Observable.create(new Observable.OnSubscribe<AuthToken>() {
            @Override
            public void call(Subscriber<? super AuthToken> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        appendText(tvLogs, "God!!! Token is out of date. \nstart refresh token......");
                        observer.onNext(userApi.refreshToken());
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private <T> Func1<Throwable, ? extends Observable<? extends T>> refreshTokenAndRetry(final Observable<T> toBeResumed) {
        return new Func1<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(Throwable throwable) {
                throwable.printStackTrace();
                // Here check if the error thrown really is a 401
                if (isHttp401Error(throwable)) {
                    return createTokenObvervable().flatMap(new Func1<AuthToken, Observable<? extends T>>() {
                        @Override
                        public Observable<? extends T> call(AuthToken token) {
                            appendText(tvLogs, "refresh token success,token's validity is 10s\nResume last request");
                            return toBeResumed;
                        }
                    });
                }
                // re-throw this error because it's not recoverable from here
                return Observable.error(throwable);
            }

            public boolean isHttp401Error(Throwable throwable) {
                return throwable instanceof AccessDenyException;
            }

        };
    }

    private void requestUserInfo() {
        appendText(tvLogs, "start to request user info");
        loading = true;
        Observable<Response> observable = userApi.getUserInfo();
        observable.onErrorResumeNext(refreshTokenAndRetry(observable))//also use retryWhen to implement it
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        loading = false;
                        appendText(tvLogs, "task completed-----");
                        //hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //hideLoadingDialog();
                        t.printStackTrace();
                        loading = false;

                        appendText(tvLogs, t.getClass().getName() + "\n" + t.getMessage());
                        NetErrorType.ErrorType error = NetErrorType.getErrorType(t);
                        appendText(tvLogs, error.msg);
                    }

                    public void onNext(Response response) {
                        String content = new String(((TypedByteArray) response.getBody()).getBytes());
                        appendText(tvLogs, "receiver data: " + content);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_request:
                if (!loading) {
                    tvLogs.append("\n\n--------------------------------");
                    requestUserInfo();
                }
                break;
        }
    }
}
