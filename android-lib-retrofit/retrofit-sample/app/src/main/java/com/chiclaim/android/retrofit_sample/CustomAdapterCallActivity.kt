package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.bean.User
import com.chiclaim.android.retrofit_sample.exception.ApiException
import com.chiclaim.android.retrofit_sample.helper.ExceptionHelper
import com.chiclaim.android.retrofit_sample.helper.ToastHelper
import kotlinx.android.synthetic.main.activity_content_layout.*
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 * 自定义 CallAdapter，统一封装对错误的处理
 *
 * -----思考：-----
 * 为何 normalCall 普通的方式，请求的回调会默认在主线程中执行？
 * 下面自定义的 CallAdapter 的网络请求回调在会后台线程执行？
 * (已从源码中找到答案)
 *
 */
class CustomAdapterCallActivity : BaseActivity() {

    companion object {
        const val MENU_ID_NORMAL = 1
        const val MENU_ID_CUSTOM = 2
    }

    interface UserService {
        @POST("register")
        @FormUrlEncoded
        fun register(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Call<ResponseModel<User>>


        @POST("register")
        @FormUrlEncoded
        fun register2(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): MyCall<ResponseModel<User>>
    }

    private val userService by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(FileUploadActivity.API_URL)
            .addCallAdapterFactory(MyCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content_layout)

        customCall()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, MENU_ID_NORMAL, 1, "Normal")
        menu.add(1, MENU_ID_CUSTOM, 2, "Custom")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_ID_NORMAL -> {
                normalCall()
            }
            MENU_ID_CUSTOM -> {
                customCall()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun normalCall() {
        val call = userService.register("chiclaim", "110")
        showLoading()
        call.enqueue(object : retrofit2.Callback<ResponseModel<User>> {

            override fun onFailure(call: Call<ResponseModel<User>>, t: Throwable) {
                dismissLoading()
                ToastHelper.showToast(applicationContext, t.message)
                text_content.text = t.message
            }

            override fun onResponse(call: Call<ResponseModel<User>>, response: Response<ResponseModel<User>>) {
                dismissLoading()
                val repsModel = response.body()
                val code = response.code()
                if (code in 200..299) {
                    text_content.text = (repsModel?.message)
                    text_content.append("\n")
                    text_content.append(repsModel?.data.toString())
                } else {
                    text_content.text = "response code ${response.code()}\n${repsModel?.message}"
                }
            }

        })
    }

    private fun customCall() {

        val call = userService.register2("chiclaim", "110")

        showLoading()

        call.request(object : Callback<ResponseModel<User>> {

            override fun onSuccess(data: ResponseModel<User>?) {
                dismissLoading()
                text_content.text = (data?.message)
                text_content.append("\n")
                text_content.append(data?.data.toString())
            }

            override fun onError(error: ApiException) {
                dismissLoading()
                ToastHelper.showToast(applicationContext, error.message)
                text_content.text = error.message
            }
        })
    }


    // ======================================================== //

    interface Callback<T> {
        fun onSuccess(data: T?)
        fun onError(error: ApiException)
    }

    interface MyCall<T> {
        fun cancel()
        fun request(callback: Callback<T>)
        fun clone(): MyCall<T>

        // Left as an exercise for the reader...
        // TODO MyResponse<T> execute() throws MyHttpException;
    }

    private class MyCallImpl<T>(private val call: Call<T>, private val callbackExecutor: Executor?) :
        MyCall<T> {

        override fun cancel() {
            call.cancel()
        }

        // 思考：
        // 为何 normalCall 普通的方式，请求的回调会默认在主线程中执行？
        // 下面自定义的 CallAdapter 的网络请求回调在会后台线程执行？
        // 已从源码中找到答案
        override fun request(callback: Callback<T>) {
            call.enqueue(object : retrofit2.Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    fun processFailure() {
                        callback.onError(ExceptionHelper.transformException(t))
                    }

                    if (callbackExecutor != null) {
                        callbackExecutor.execute {
                            processFailure()
                        }
                    } else {
                        processFailure()
                    }

                }

                override fun onResponse(call: Call<T>, response: Response<T>) {

                    fun processResponse() {
                        val code = response.code()
                        if (code in 200..299) {
                            callback.onSuccess(response.body())
                        } else {
                            callback.onError(ApiException("---", "$code ${response.message()}"))
                        }
                    }

                    if (callbackExecutor != null) {
                        callbackExecutor.execute {
                            processResponse()
                        }
                    } else {
                        processResponse()
                    }
                }
            })
        }

        override fun clone(): MyCall<T> {
            return MyCallImpl(call.clone(), callbackExecutor)
        }
    }

    private class MyCallAdapter<R>(private val responseType: Type, private val executor: Executor?) :
        CallAdapter<R, MyCall<R>> {

        override fun adapt(call: Call<R>): MyCall<R> {
            return MyCallImpl(call, executor)
        }

        override fun responseType(): Type {
            return responseType
        }

    }

    class MyCallAdapterFactory : CallAdapter.Factory() {
        override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

            if (getRawType(returnType) != MyCall::class.java) return null

            check(returnType is ParameterizedType) { "MyCall must have generic type (e.g., UnwrapCall<ResponseBody>)" }
            val responseType = getParameterUpperBound(0, returnType)
            val executor = retrofit.callbackExecutor()

            println("getParameterUpperBound -> $responseType , executor -> $executor")

            return MyCallAdapter<Any>(responseType, executor)
        }

    }


}