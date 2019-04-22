package com.module.chiclaim.com.retrofit.custom_parameter;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/18.
 */

public interface UserApi {

    @FormUrlEncoded
    @POST("user/fetch")
    Observable<User> fetchUserInfo2(@Field("id") String key, @Field("description") String desc);

    @GET("user/fetch")
    Observable<User> fetchUserInfo(@Query("id") String key);

    //method=com.dfire.cashsoa.login
    @FormUrlEncoded
    @POST("?method=com.dfire.cashsoa.login")
    Observable<ResponseBody> login(@Field("mobile") String mobile, @Field("password") String password,
                                   @Field("device_id") String device_id, @Field("client_type") String client_type);


    @FormUrlEncoded
    @POST("?method=com.dfire.soa.cloudcash.querySystemMenuByUserId")
    Observable<ResponseBody> menuList(@Field("entity_id") String entity_id, @Field("user_id") String user_id);
}
