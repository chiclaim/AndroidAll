package com.chiclaim.android.arch.lifecycle

import android.os.Bundle
import com.chiclaim.android.arch.R
import androidx.appcompat.app.AppCompatActivity
import com.chiclaim.android.arch.Callback
import kotlinx.android.synthetic.main.activity_test_lifecycle.*


class TestLifecycleActivity2 : AppCompatActivity() {

/*

AppCompatActivity 实现了 LifecycleOwner 接口
    AppCompatActivity <- FragmentActivity <- ComponentActivity <- LifecycleOwner

MyLocationListener2 实现了  LifecycleObserver 接口

LifecycleOwner 可以无缝地和 LifecycleObserver 一起结合使用

通过 addObserver 将两者关联起来

lifecycle.addObserver(locationListener)



 */
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

        checkUserStatus(object : Callback<Boolean> {
            override fun onDone(data: Boolean) {
                if (data) {
                    locationListener.enable()
                }
            }
        })
    }

    private fun checkUserStatus(callback: Callback<Boolean>?) {
        Thread(Runnable {
            try {
                Thread.sleep(CONSUME_TIME)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            callback?.onDone(true)
        }).start()
    }

}
