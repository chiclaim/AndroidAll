package com.chiclaim.jetpack

import android.content.Intent
import android.os.Bundle
import com.chiclaim.jetpack.basic.LifecycleDemoActivity
import com.chiclaim.jetpack.basic.LiveDataDemoActivity
import com.chiclaim.jetpack.basic.ViewModelDemoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_lifecycle_demo.setOnClickListener {
            startActivity(Intent(this, LifecycleDemoActivity::class.java))
        }

        btn_live_data_demo.setOnClickListener {
            startActivity(Intent(this, LiveDataDemoActivity::class.java))
        }

        btn_view_model_demo.setOnClickListener {
            startActivity(Intent(this, ViewModelDemoActivity::class.java))
        }
    }

}