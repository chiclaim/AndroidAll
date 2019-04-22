package com.chiclaim.android.arch.live_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chiclaim.android.arch.Callback
import com.chiclaim.android.arch.bean.User
import com.chiclaim.android.arch.source.UserRemoteSource

class UserViewModel() : ViewModel() {


    init {
        println("trigger")
        UserRemoteSource.loadUserInfo(object : Callback<User> {
            override fun onDone(data: User) {
                TODO("not implemented")
            }

        })
    }


    private val userLiveData by lazy {
        MutableLiveData<User>()
    }

    fun getUser(): LiveData<User> {
        return userLiveData
    }

    fun doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
        //userLiveData.value =
    }

}