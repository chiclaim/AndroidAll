package com.chiclaim.rxjava.api;


import com.chiclaim.rxjava.model.AuthToken;
import com.chiclaim.rxjava.model.User;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface UserApi {

    @GET("/token")
    AuthToken refreshToken();

    @GET("/userinfo")
    Observable<Response> getUserInfo();


    @GET("/userinfo?noToken=1")
    Observable<Response> getUserInfoNoToken();

    //wrong path
    @GET("/userinfo1")
    Observable<Response> getUserInfo1();

    @GET("/user/fetch")
    Observable<User> fetchUserInfo(@Query("id") String key);

}