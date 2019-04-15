package com.chiclaim.android.arch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chiclaim.android.arch.lifecycle.TestLifecycleActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun testLifecycle(view: View) {
        startActivity(Intent(this, TestLifecycleActivity::class.java))
    }
}