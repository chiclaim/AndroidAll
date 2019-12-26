package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import com.chiclaim.android.retrofit_sample.helper.startTaskAsync
import com.chiclaim.android.retrofit_sample.helper.uiJob
import kotlinx.android.synthetic.main.activity_content_layout.*
import okio.Buffer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class RetrofitPostActivity : BaseActivity() {

    interface UserSource {
        @POST("users/new")
        fun createUser(@Body user: User): Call<User>

        @FormUrlEncoded
        @POST("users/new")
        fun createUser2(@Field("username") username: String, @Field("email") email: String): Call<User>


    }

    class User(var username: String?, var email: String?)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_layout)


        uiJob {

            val task = startTaskAsync(this) {

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(SimpleServiceActivity.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val userSource = retrofit.create(UserSource::class.java)

                val result1 = processCall(userSource.createUser(User("chiclaim", "chiclaim@gmail.com")))
                val result2 = processCall(userSource.createUser2("chiclaim", "chiclaim@gmail.com"))



                "$result1 \n\n $result2"

            }

            try {
                showLoading()

                val result = task.await()
                text_content.append(result)

            } catch (e: Throwable) {
                e.printStackTrace()
                text_content.append(e.message)
            } finally {
                dismissLoading()
            }

        }

    }

    private fun processCall(call: Call<User>): String {

        val buffer = Buffer()
        call.request().body()?.writeTo(buffer)
        val requestBody = buffer.readUtf8()

        val response = call.execute()

        return " post param -> $requestBody \n\n response code -> ${response.code()} \n error message -> ${response.errorBody()?.string()}"
    }


}