package com.chiclaim.jetpack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LiveDataViewModel : ViewModel() {

    val liveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            value = 0
        }
    }


    fun plus(num: Int) {
        liveData.value = liveData.value?.plus(num)
    }
}