package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.bean.User
import kotlinx.android.synthetic.main.activity_content_layout.*
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class RetrofitWithCoroutineActivity : BaseActivity() {

    companion object {
        const val RETURN_DATA = 1
        const val RETURN_RESPONSE = 2
    }

    interface UserService {
        @POST("register")
        @FormUrlEncoded
        suspend fun register(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): ResponseModel<User>?


        @POST("register")
        @FormUrlEncoded
        suspend fun register2(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Response<ResponseModel<User>>
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
        request2()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, RETURN_DATA, 1, "ReturnData")
        menu.add(1, RETURN_RESPONSE, 2, "ReturnResponse")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            RETURN_DATA -> {
                request1()
            }
            RETURN_RESPONSE -> {
                request2()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun request1() {
        launch {
            try {
                showLoading()
                val repsModel = userService.register("chiclaim", "110")
                dismissLoading()
                text_content.text = "ResponseModel -> ${repsModel?.message}"
                text_content.append("\n")
                text_content.append(repsModel?.data.toString())

            } catch (e: Exception) {
                dismissLoading()
                e.printStackTrace()
                text_content.append("$e")
            }
        }
    }

    private fun request2() {
        launch {
            try {
                val response = userService.register2("chiclaim", "110")
                dismissLoading()
                text_content.text = "Response -> code = ${response.code()}, ${response.body()?.data.toString()}"
            } catch (e: Exception) {
                dismissLoading()
                e.printStackTrace()
                text_content.append("$e")
            }

        }

    }


}