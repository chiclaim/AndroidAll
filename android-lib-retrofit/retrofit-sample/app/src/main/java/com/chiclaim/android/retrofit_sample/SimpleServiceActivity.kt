package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import com.chiclaim.android.retrofit_sample.bean.Contributor
import com.chiclaim.android.retrofit_sample.source.GitHub
import kotlinx.android.synthetic.main.activity_simple_service_layout.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class SimpleServiceActivity : BaseActivity() {

    companion object {
        const val API_URL = "https://api.github.com"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_service_layout)

        uiJob {

            val task = startTaskAsync(this) {

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val github = retrofit.create(GitHub::class.java)

                val call: Call<List<Contributor>> = github.contributors("square", "retrofit")

                call.execute().body()

            }

            try {
                showLoading()

                val contributors = task.await()

                if (contributors != null) {
                    for (contributor in contributors) {
                        text_content.append("contributor: " + contributor.login + ", count: " + contributor.contributions + "\n")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                dismissLoading()
            }

        }


    }
}