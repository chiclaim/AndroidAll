package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import com.chiclaim.android.retrofit_sample.helper.startTaskAsync
import com.chiclaim.android.retrofit_sample.helper.uiJob
import kotlinx.android.synthetic.main.activity_content_layout.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException


class DynamicBaseUrlActivity : BaseActivity() {

    companion object {
        const val API_URL1 = "https://www.pepsi.com/"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_layout)

        uiJob {
            val hostSelectionInterceptor = HostSelectionInterceptor()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(hostSelectionInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(API_URL1)
                .callFactory(okHttpClient)
                .build()

            val pop = retrofit.create(Pop::class.java)

            try {
                showLoading()

                val task1 = startTaskAsync(this) {

                    val response1: retrofit2.Response<ResponseBody> = pop.robots().execute()

                    response1.raw().request().url().toString() + "--> \n\n" + response1.body()?.string()

                }

                val result1 = task1.await()


                val task2 = startTaskAsync(this) {

                    hostSelectionInterceptor.setHost("www.github.com")

                    val response2: retrofit2.Response<ResponseBody> = pop.robots().execute()

                    "\n\n" + response2.raw().request().url().toString() + "--> \n\n" + response2.body()?.string()
                }

                val result2 = task2.await()

                text_content.append(result1)
                text_content.append(result2)

            } catch (e: Exception) {
                e.printStackTrace()
                text_content.append(e.message)
            } finally {
                dismissLoading()
            }

        }


    }


    interface Pop {
        @GET("robots.txt")
        fun robots(): Call<ResponseBody>
    }


    private class HostSelectionInterceptor : Interceptor {
        @Volatile
        private var host: String? = null

        fun setHost(host: String?) {
            this.host = host
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val host = host
            if (host != null) {
                val newUrl = request.url().newBuilder()
                    .host(host)
                    .build()
                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
            return chain.proceed(request)
        }
    }
}