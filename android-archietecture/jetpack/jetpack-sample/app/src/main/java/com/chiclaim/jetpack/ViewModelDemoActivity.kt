package com.chiclaim.jetpack


import android.os.Bundle
import androidx.activity.viewModels
import com.chiclaim.jetpack.viewmodel.MyViewModel
import com.chiclaim.jetpack.viewmodel.MyViewModelFactory
import kotlinx.android.synthetic.main.activity_viewmodel_demo_layout.*


class ViewModelDemoActivity : BaseActivity() {

    private val myViewModel: MyViewModel by viewModels {
        MyViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewmodel_demo_layout)

        text_number.text = "${myViewModel.number}"

        btn_plus.setOnClickListener {
            text_number.text = "${++myViewModel.number}"
        }
    }

    /*

         屏幕旋转也能保持数据不丢失，原因在于 ViewModelStore 是由 androidx.activity.ComponentActivity 提供的
         只要 Activity 没有被销毁，数据都是存在的。
     */
}