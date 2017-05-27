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
import com.chiclaim.rxjava.api.UserApi;
import com.chiclaim.rxjava.model.User;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Description：CombineLatest操作符高级用法
 * <br/>
 * Created by kumu on 2017/3/9.
 */

public class CombineLatestFragment extends BaseFragment {

    UserApi userApi;
    TextView tvLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_combine_latest, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLogs = (TextView) view.findViewById(R.id.tv_logs);

        userApi = ApiServiceFactory.createService(UserApi.class);
        userApi.fetchUserInfo(null)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        printLog(tvLogs, "----fetch a user---- \n", getUserString(user));
                        return fetchFriendsInfo(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        printLog(tvLogs, "----process his friends by id---- \n", getUserString(user));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private Observable<User> fetchFriendsInfo(User user) {

        Observable<User> observableUser = Observable.just(user);

        Observable<List<User>> observableUsers = Observable
                .from(user.getFriends())
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        return userApi.fetchUserInfo(user.getId() + "");
                    }
                })
                .toList();

        return Observable.combineLatest(observableUser, observableUsers, new Func2<User, List<User>, User>() {
            @Override
            public User call(User user, List<User> users) {
                user.setFriends(users);
                return user;
            }
        });
    }

    private String getUserString(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("name:").append(user.getUsername()).append(", email:").append(user.getEmail())
                .append(" \n\tHis friends:");
        for (User friend : user.getFriends()) {
            sb.append("\n\t\t").append("id:").append(friend.getId())
                    .append(", name:").append(friend.getUsername()).append(", email:").append(friend.getEmail());
        }
        return sb.toString();
    }

}
