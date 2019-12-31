package com.chiclaim.android.retrofit_sample

import android.os.Bundle
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

class CustomAdapterCallActivity : BaseActivity() {


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
        ): UnwrapCall<ResponseModel<User>>
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

//        normalCall()
        customCall()
    }

    private fun normalCall() {
        val call = userService.register("chiclaim", "110")
        showLoading()
        call.enqueue(object : retrofit2.Callback<ResponseModel<User>> {

            override fun onFailure(call: Call<ResponseModel<User>>, t: Throwable) {
                dismissLoading()
                ToastHelper.showToast(applicationContext, t.message)
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
            }
        })
    }


    // ======================================================== //

    interface Callback<T> {
        fun onSuccess(data: T?)
        fun onError(error: ApiException)
    }

    interface UnwrapCall<T> {
        fun cancel()
        fun request(callback: Callback<T>)
        fun clone(): UnwrapCall<T>

        // Left as an exercise for the reader...
        // TODO MyResponse<T> execute() throws MyHttpException;
    }

    private class UnwrapCallImpl<T>(private val call: Call<T>, private val callbackExecutor: Executor?) :
        UnwrapCall<T> {

        override fun cancel() {
            call.cancel()
        }

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

        override fun clone(): UnwrapCall<T> {
            return UnwrapCallImpl(call.clone(), callbackExecutor)
        }
    }

    private class MyCallAdapter<R>(private val responseType: Type, private val executor: Executor?) :
        CallAdapter<R, UnwrapCall<R>> {

        override fun adapt(call: Call<R>): UnwrapCall<R> {
            return UnwrapCallImpl(call, executor)
        }

        override fun responseType(): Type {
            return responseType
        }

    }

    class MyCallAdapterFactory : CallAdapter.Factory() {
        override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

            if (getRawType(returnType) != UnwrapCall::class.java) return null

            check(returnType is ParameterizedType) { "UnwrapCall must have generic type (e.g., UnwrapCall<ResponseBody>)" }
            val responseType = getParameterUpperBound(0, returnType)
            val executor = retrofit.callbackExecutor()

            println("getParameterUpperBound -> $responseType , executor -> $executor")

            return MyCallAdapter<Any>(responseType, executor)
        }

    }


}