package com.chiclaim.coroutine

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_basic_coroutine_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasicCoroutineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_coroutine_layout)
    }


    fun launch(view: View) {
        text_content.text = "start task with launch"
        val job = launch(Dispatchers.Default) {
            sleep()
            println("sleep 2 seconds by launch with thread: ${Thread.currentThread().name}")
        }
        text_content.text = "isActive: ${job.isActive}, isCompleted ${job.isCompleted}"
    }

    fun async(view: View) {
        text_content.text = "start task with async"
        launch {
            val deferred = async(Dispatchers.Default) {
                sleep()
                "the value from async with thread: ${Thread.currentThread().name}"
            }
            val result = deferred.await()
            text_content.text = result
        }
    }

    fun withContext(view: View) {
        text_content.text = "start task with withContext"
        launch {
            val result = withContext(Dispatchers.Default) {
                sleep()
                "the value from withContext with thread: ${Thread.currentThread().name}"
            }
            println(Thread.currentThread().name)
            text_content.text = result
        }
    }

    private fun sleep() {
        Thread.sleep(2000)
    }
}