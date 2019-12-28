package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.bean.User
import com.chiclaim.android.retrofit_sample.helper.ToastHelper
import kotlinx.android.synthetic.main.activity_content_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class CustomCustomCallActivity : BaseActivity() {


    interface UserService {
        @POST("register")
        @FormUrlEncoded
        fun register(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Call<ResponseModel<User>>
    }

    private val userService by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(FileUploadActivity.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content_layout)

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

}