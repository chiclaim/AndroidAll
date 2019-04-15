package com.chiclaim.rxjava.operator;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chiclaim.rxjava.BaseFragment;
import com.chiclaim.rxjava.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Description：
 * <br/>
 * Created by kumu on 2018/1/11.
 */

public class FetchPhoneCodeFragment extends BaseFragment {


    private static final long INTERVAL_TIME = 50;

    private Button mBtn;
    private Subscription mSubscription;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fetch_phone_code, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtn = (Button) view.findViewById(R.id.btn_fetch_phone_code);
        fetchPhoneCode();
    }

    /**
     * 获取验证的逻辑：
     * <p>
     * 1，防止按钮在短时间内点击多次
     * <p>
     * 2，调用获取验证码接口
     * <p>
     * 3，验证码获取成功开始倒计时，此时按钮不可被点击
     * <p>
     * 4，倒计时完毕后，点击可以再次被点击
     */

    public void fetchPhoneCode() {
        mSubscription = RxView.clicks(mBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .flatMap(new Func1<Void, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Void aVoid) {
                        //弹出loading框（省略）
                        //调用获取验证码接口 (省略)
                        return Observable.just(true);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        //验证码获取成功，把按钮置为不可点
                        mBtn.setEnabled(false);
                        //关闭loading框（省略）
                        return intervalButton();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.e("FetchPhoneCodeFragment", "subscribe call");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        //关闭loading框（省略）
                        //出错后把按钮设置为可用
                        mBtn.setEnabled(true);
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public Observable<Boolean> intervalButton() {
        //如果使用Observable.interval(interval, TimeUnit)，会默认延迟interval执行
        return Observable.interval(0, 1000, TimeUnit.MILLISECONDS)

                .takeWhile(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong <= INTERVAL_TIME;
                    }
                })

//                .filter(new Func1<Long, Boolean>() {
//                    @Override
//                    public Boolean call(Long aLong) {
//                        return aLong <= INTERVAL_TIME;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Long, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Long aLong) {
                        long diff = INTERVAL_TIME - aLong;
                        Log.e("FetchPhoneCodeFragment", "thread name:" + Thread.currentThread().getName() + ",aLong:" + aLong + ",diff:" + diff);
                        mBtn.setText(diff + "s");
                        //倒计时完毕后，按钮可以再次被点击，重新设置按钮文案
                        if (diff == 0) {
                            mBtn.setEnabled(true);
                            mBtn.setText("获取验证码");
                        }
                        return Observable.just(true);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
