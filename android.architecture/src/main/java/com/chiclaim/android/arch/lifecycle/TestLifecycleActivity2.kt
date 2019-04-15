package com.chiclaim.android.arch.lifecycle

import android.os.Bundle
import com.chiclaim.android.arch.R
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_lifecycle.*

class TestLifecycleActivity2 : AppCompatActivity() {

    companion object {
        const val CONSUME_TIME = 2000L
    }

    private lateinit var locationListener: MyLocationListener2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_lifecycle)

        text_test_lifecycle.text = "press back in $CONSUME_TIME milliseconds"



        locationListener = MyLocationListener2(this, lifecycle) { location ->
            println("update UI")
        }

        lifecycle.addObserver(locationListener)

        checkUserStatus(object : Callback {
            override fun onDone(status: Boolean) {
                if (status) {
                    locationListener.enable()
                }
            }
        })
    }

    private fun checkUserStatus(callback: Callback?) {
        Thread(Runnable {
            try {
                Thread.sleep(CONSUME_TIME)
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
