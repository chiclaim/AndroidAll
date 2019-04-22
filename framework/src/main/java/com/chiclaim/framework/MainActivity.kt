package com.chiclaim.framework

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val start = System.currentTimeMillis()
        println("start: $start")

        val handler = Handler()
        val message = Message.obtain(handler) {
            val end = System.currentTimeMillis()
            println("end: $end")
            println("diff:" + (end - start))
        }
        handler.sendMessage(message)
    }
}