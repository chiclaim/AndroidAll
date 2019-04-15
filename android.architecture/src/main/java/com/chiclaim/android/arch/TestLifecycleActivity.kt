package com.chiclaim.android.arch

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity

class TestLifecycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_lifecycle)

    }

    override fun onStart() {
        super.onStart()
        checkUserStatus(object : Callback {
            override fun onDone(status: Boolean) {
                if (status) {
                    Log.e("TestLifecycleActivity", "开启位置监听")
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        Log.e("TestLifecycleActivity", "关闭位置监听")
    }

    private fun checkUserStatus(callback: Callback?) {
        Thread(Runnable {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            callback?.onDone(true)
        }).start()
    }

    internal interface Callback {
        fun onDone(status: Boolean)
    }

}
