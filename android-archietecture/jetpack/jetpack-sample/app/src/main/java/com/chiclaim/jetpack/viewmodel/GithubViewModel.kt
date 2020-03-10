package com.chiclaim.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiclaim.jetpack.bean.Contributor
import com.chiclaim.jetpack.calladapter.LiveDataCallAdapterFactory
import com.chiclaim.jetpack.repository.Github
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubViewModel : ViewModel() {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    private var githubService: Github = retrofit.create(Github::class.java)

    fun getContributors(owner: String?, repo: String?): LiveData<List<Contributor?>?>? {
        return githubService.contributors(owner, repo)

    }

}