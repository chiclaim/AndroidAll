package com.chiclaim.jetpack

import android.app.Application
import android.content.Context
import android.util.Log

class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.e("MyApplication", "MyApplication attachBaseContext()")

    }

    override fun onCreate() {
        super.onCreate()
        Log.e("MyApplication", "MyApplication onCreate()")
    }


}