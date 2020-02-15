package com.chiclaim.jetpack.basic

import android.os.Bundle
import android.util.Log
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

        Log.d("LiveDataDemoActivity","onCreate $this")


        myViewModel.liveData.observe(this, Observer<Int> {
            text_number.text = "$it"

            Log.d("LiveDataDemoActivity","${lifecycle.currentState.name} observer callback $this")
        })

        btn_plus.setOnClickListener {
            myViewModel.plus(1)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LiveDataDemoActivity","onStart $this")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LiveDataDemoActivity","onResume $this")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LiveDataDemoActivity","onPause $this")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LiveDataDemoActivity","onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LiveDataDemoActivity","onDestroy $this")
    }





}