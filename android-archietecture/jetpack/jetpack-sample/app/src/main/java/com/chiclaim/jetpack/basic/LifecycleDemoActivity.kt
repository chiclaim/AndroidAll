package com.chiclaim.jetpack.basic

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.chiclaim.jetpack.BaseActivity

/*
主要基于观察者模式

1，ReportFragment.injectIfNeededIn()
2，LifecycleRegistry 核心类

 */
class LifecycleDemoActivity : BaseActivity() {

    private lateinit var myLocationListener: MyLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        myLocationListener = MyLocationListener(this, lifecycle) { location ->
            // update UI
        }

        Util.checkUserStatus { result ->
            if (result) {
                myLocationListener.enable()
            }
        }

        lifecycle.addObserver(myLocationListener)
    }
}

internal class MyLocationListener(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: (Location) -> Unit
) : LifecycleObserver {

    private var enabled = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            // connect
            Log.e("MyLocationListener", "start connect")
        }
    }

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // connect if not connected
            start()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // disconnect if connected
        Log.e("MyLocationListener", "disconnect")

    }
}

internal class Util {
    companion object {
        fun checkUserStatus(callback: (Boolean) -> Unit) {
            Handler().postDelayed({
                callback(true)
            }, 3000)
        }
    }

}