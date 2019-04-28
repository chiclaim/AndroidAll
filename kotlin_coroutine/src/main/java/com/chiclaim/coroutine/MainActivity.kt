package com.chiclaim.coroutine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun coroutineBasic(view: View) {
        startActivity(Intent(this, BasicCoroutineActivity::class.java))
    }
}
