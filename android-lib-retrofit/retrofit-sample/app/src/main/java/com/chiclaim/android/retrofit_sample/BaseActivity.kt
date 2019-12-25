package com.chiclaim.android.retrofit_sample

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {


    private val job = Job()

    private var loadingDialog: LoadingDialog? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun cancelTasks() {
        coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTasks()
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.show(supportFragmentManager, "loadingDialog")
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }
}