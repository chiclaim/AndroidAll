package com.chiclaim.jetpack.repository

import androidx.lifecycle.LiveData
import com.chiclaim.jetpack.bean.Contributor
import retrofit2.http.GET
import retrofit2.http.Path


interface Github {

    @GET("/repos/{owner}/{repo}/contributors")
    fun contributors(
            @Path("owner") owner: String?,
            @Path("repo") repo: String?): LiveData<List<Contributor?>?>?
}