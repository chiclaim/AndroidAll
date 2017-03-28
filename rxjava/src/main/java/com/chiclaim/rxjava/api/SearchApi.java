package com.chiclaim.rxjava.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by chiclaim on 2016/02/26
 */
public interface SearchApi {

    @GET("/search")
    Observable<List<String>> search(@Query("key") String key);

    @GET("/hosts")
    Observable<List<String>> hosts();
}
