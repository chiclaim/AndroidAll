package com.chiclaim.jetpack.basic

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.chiclaim.jetpack.BaseActivity
import com.chiclaim.jetpack.R
import com.chiclaim.jetpack.viewmodel.LiveDataViewModel
import com.chiclaim.jetpack.viewmodel.LiveDataViewModelFactory
import kotlinx.android.synthetic.main.activity_viewmodel_demo_layout.*

class LiveDataDemoActivity : BaseActivity() {

    private val myViewModel: LiveDataViewModel by viewModels {
        LiveDataViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewmodel_demo_layout)

        myViewModel.liveData.observe(this, Observer<Int> {
            text_number.text = "${myViewModel.liveData.value}"
        })

        btn_plus.setOnClickListener {
            myViewModel.plus(1)
        }
    }


}