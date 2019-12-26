package com.chiclaim.android.retrofit_sample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_simple_service.setOnClickListener {
            startActivity(Intent(this, SimpleServiceActivity::class.java))
        }

        btn_dynamic_base_url.setOnClickListener {
            startActivity(Intent(this, DynamicBaseUrlActivity::class.java))
        }

        btn_retrofit_post.setOnClickListener {
            startActivity(Intent(this, RetrofitPostActivity::class.java))
        }

        btn_file_upload.setOnClickListener {
            startActivity(Intent(this, FileUploadActivity::class.java))

        }
    }
}
