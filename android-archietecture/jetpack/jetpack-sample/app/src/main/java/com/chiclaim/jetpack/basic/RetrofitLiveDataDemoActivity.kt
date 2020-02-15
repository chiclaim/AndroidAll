package com.chiclaim.jetpack.basic

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.chiclaim.jetpack.BaseActivity
import com.chiclaim.jetpack.R
import com.chiclaim.jetpack.bean.Contributor
import com.chiclaim.jetpack.viewmodel.GithubViewModel
import com.chiclaim.jetpack.viewmodel.GithubViewModelFactory
import kotlinx.android.synthetic.main.activity_lifecycle_layout.*

class RetrofitLiveDataDemoActivity : BaseActivity() {


    private val myViewModel: GithubViewModel by viewModels {
        GithubViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_livedata_layout)

        text_log.text = "Square's retrofit contributors:\n"

        myViewModel.getContributors("square", "retrofit")?.observe(this, Observer {
            it?.forEach { contributor: Contributor? ->
                text_log.append("\nname: ${contributor?.login} - count: ${contributor?.contributions}")
            }
        })
    }


}