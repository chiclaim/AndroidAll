package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.bean.User
import com.chiclaim.android.retrofit_sample.call_adapter.SubscribeOnCallAdapterFactory
import com.chiclaim.android.retrofit_sample.helper.ResponseTransformerHelper
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
            @Field("username") username: String?,
            @Field("mobile") mobile: String
        ): Observable<ResponseModel<User>?>

        @POST("register")
        @FormUrlEncoded
        fun registerByRxJava2(
            @Field("username") username: String?,
            @Field("mobile") mobile: String
        ): Observable<Response<ResponseModel<User>?>>
    }

    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_RXJAVA = 2
        const val TYPE_RXJAVA_BODY = 3
        const val TYPE_RXJAVA_RESPONSE = 4
    }

    private val userService by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(FileUploadActivity.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // 自定义 CallAdapter，统一设置被观察者的执行线程，以及统一封装错误处理
            .addCallAdapterFactory(SubscribeOnCallAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content_layout)

//        rxJavaCall()
//        rxJavaCall2()
        rxJavaCall3()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, TYPE_NORMAL, 1, "Without RxJava")
        menu.add(1, TYPE_RXJAVA, 2, "WithRxJava")
        menu.add(1, TYPE_RXJAVA_BODY, 3, "Observable<Body>")
        menu.add(1, TYPE_RXJAVA_RESPONSE, 4, "Observable<Response>")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            TYPE_NORMAL -> normalCall()
            TYPE_RXJAVA -> rxJavaCall()
            TYPE_RXJAVA_BODY -> rxJavaCall2()
            TYPE_RXJAVA_RESPONSE -> rxJavaCall3()
        }
        return super.onOptionsItemSelected(item)
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
                    text_content.text = "response code = ${response.code()}\nmessage = ${repsModel?.message}"
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
            .subscribe({ responseModel: ResponseModel<User>? ->
                dismissLoading()
                text_content.text = (responseModel?.message)
                text_content.append("\n")
                text_content.append(responseModel?.data.toString())
            }, {
                dismissLoading()
                text_content.text = "register failed -> ${it.message}"
            }).apply {
                compositeDisposable.add(this)
            }
    }

    /**
     * http code = 240,205 抛出 java.lang.NullPointerException: Null is not a valid element
     *
     * 不用指定被观察者所在的执行线程
     *
     */
    private fun rxJavaCall2() {
        showLoading()
        userService.registerByRxJava("chiclaim", "110")
            .observeOn(AndroidSchedulers.mainThread())
            .compose(ResponseTransformerHelper.transformResult())
            .subscribe({ user: User? ->
                dismissLoading()
                text_content.text = ("\n${user.toString()}")
            }, {
                dismissLoading()
                text_content.text = "register failed -> ${it.message}"
            }).apply {
                compositeDisposable.add(this)
            }
    }


    /**
     * http code = 240,205 不会抛出 java.lang.NullPointerException: Null is not a valid element
     */
    private fun rxJavaCall3() {
        showLoading()
        userService.registerByRxJava2("chiclaim", "110")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: Response<ResponseModel<User>?> ->
                dismissLoading()
                val body = response.body()
                if (body != null) {
                    text_content.text = ("response code = ${response.code()}\n${body.data?.toString()}")
                } else {
                    text_content.text = ("response code = ${response.code()}\n response body is null")
                }
            }, {
                dismissLoading()
                text_content.text = "register failed -> ${it.message}"
            }).apply {
                compositeDisposable.add(this)
            }
    }


}