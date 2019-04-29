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


    fun sequential(view: View) {
        text_content.text = "start tasks with sequential"
        launch {
            val result = withContext(Dispatchers.Default) {
                sleep()
                "the value1 from task1 with thread: ${Thread.currentThread().name}"
            }
            text_content.apply {
                append("\n")
                append(result)
            }

            val result2 = withContext(Dispatchers.Default) {
                sleep()
                "the value2 from task2 with thread: ${Thread.currentThread().name}"
            }
            text_content.apply {
                append("\n")
                append(result2)
            }
        }
    }

    fun parallel(view: View) {
        text_content.text = "start tasks with parallel"
        launch {
            val deferred = async(Dispatchers.Default) {
                launch(Dispatchers.Main) {
                    text_content.apply {
                        append("\n")
                        append("start task1...")
                    }
                }

                sleep()
                "the value1 from task1 with thread: ${Thread.currentThread().name}"
            }

            val deferred2 = async(Dispatchers.Default) {
                launch(Dispatchers.Main) {
                    text_content.apply {
                        append("\n")
                        append("start task2...")
                    }
                }
                sleep()
                "the value2 from task2 with thread: ${Thread.currentThread().name}"
            }
            text_content.append("\n" + deferred.await() + "\n" + deferred2.await())
        }
    }


    private fun sleep() {
        Thread.sleep(2000)
    }
}