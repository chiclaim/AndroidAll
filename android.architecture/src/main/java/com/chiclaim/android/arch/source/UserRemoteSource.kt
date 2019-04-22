package com.chiclaim.android.arch.source

import com.chiclaim.android.arch.Callback
import com.chiclaim.android.arch.bean.User

object UserRemoteSource {


    fun loadUserInfo(callback: Callback<User>?) {
        Thread {
            Thread.sleep(2000)
            callback?.onDone(User("Chiclaim", "HangZhou"))

        }.start()
    }
}