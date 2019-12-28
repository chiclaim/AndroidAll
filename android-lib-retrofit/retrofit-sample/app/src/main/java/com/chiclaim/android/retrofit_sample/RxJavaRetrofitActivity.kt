package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.bean.User
import com.chiclaim.android.retrofit_sample.helper.ToastHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_content_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class RxJavaRetrofitActivity : BaseActivity() {


    private interface UserService {
        @POST("register")
        @FormUrlEncoded
        fun register(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Call<ResponseModel<User>>

        @POST("register")
        @FormUrlEncoded
        fun registerByRxJava(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Observable<ResponseModel<User>>
    }

    private val userService by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(FileUploadActivity.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content_layout)

        rxJavaCall()

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    /**
     * 常规的执行网络请求
     */
    private fun normalCall() {
        val call = userService.register("chiclaim", "110")
        showLoading()
        call.enqueue(object : Callback<ResponseModel<User>> {

            override fun onFailure(call: Call<ResponseModel<User>>, t: Throwable) {
                dismissLoading()
                ToastHelper.showToast(applicationContext, t.message.toString())
            }

            override fun onResponse(call: Call<ResponseModel<User>>, response: Response<ResponseModel<User>>) {
                dismissLoading()
                val repsModel = response.body()
                if (response.code() == 200) {
                    text_content.text = (repsModel?.message)
                    text_content.append("\n")
                    text_content.append(repsModel?.data.toString())
                } else {
                    text_content.text = "response code ${response.code()}\n${repsModel?.message}"
                }
            }

        })
    }

    /**
     * 使用 RxJava 处理网络请求任务
     */
    private fun rxJavaCall() {
        showLoading()
        userService.registerByRxJava("chiclaim", "110")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dismissLoading()
                text_content.text = (it.message)
                text_content.append("\n")
                text_content.append(it.data.toString())
            }, {
                dismissLoading()
                text_content.text = "register failed -> ${it.message}"
            }).apply {
                compositeDisposable.add(this)
            }
    }

}