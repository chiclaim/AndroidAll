package com.chiclaim.viewbindingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chiclaim.viewbindingdemo.databinding.ViewLayoutBinding

/**
 * desc: ViewBindingDemo
 *
 * Created by kumu@2dfire.com on 2020/3/9.
 */
class ViewBindingDemoActivity : AppCompatActivity() {

    private lateinit var binding: ViewLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textContent.text = "setText By ViewBinding"

    }
}