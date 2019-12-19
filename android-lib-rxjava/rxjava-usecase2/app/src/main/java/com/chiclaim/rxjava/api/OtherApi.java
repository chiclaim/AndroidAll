package com.chiclaim.rxjava.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/28.
 */

public interface OtherApi {

    @GET("/timeout")
    Observable<Response> testTimeout(@Query("timeout") String timeout);
}
