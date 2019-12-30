package com.chiclaim.android.retrofit_sample.bean

class User(private val username: String?, private val mobile: String?) {

    override fun toString(): String {
        return "User(username=$username, mobile=$mobile)"
    }

}