package com.chiclaim.jetpack.basic


import android.os.Bundle
import androidx.activity.viewModels
import com.chiclaim.jetpack.BaseActivity
import com.chiclaim.jetpack.R
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
        ViewModel原理分析：
        在 Activity 销毁前执行，保存 onRetainNonConfigurationInstance 返回的数据
        然后重新创建 Activity 的时候通过 attach 方法将保存的数据赋值给 Activity 的成员变量 mLastNonConfigurationInstances
        最后通过 getLastNonConfigurationInstance 获取 mLastNonConfigurationInstances 里的数据
        所以，屏幕旋转、重新设置系统语言等配置发生变化，也能保持数据不丢失。
     */

    // attach()
    // onRetainNonConfigurationInstance()
    // getLastNonConfigurationInstance()
}