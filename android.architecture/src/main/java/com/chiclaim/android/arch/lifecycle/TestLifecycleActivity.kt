package com.chiclaim.android.arch.lifecycle

import com.chiclaim.android.arch.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_lifecycle.*

class TestLifecycleActivity : AppCompatActivity() {

    companion object {
        const val CONSUME_TIME = 2000L
    }

    private var locationListener: MyLocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_lifecycle)
        locationListener = MyLocationListener()
    }

    override fun onStart() {
        super.onStart()
        text_test_lifecycle.text = "press back in $CONSUME_TIME milliseconds"
        checkUserStatus(object : Callback {
            override fun onDone(status: Boolean) {
                if (status) {
                    locationListener?.start()
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        locationListener?.stop()
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
