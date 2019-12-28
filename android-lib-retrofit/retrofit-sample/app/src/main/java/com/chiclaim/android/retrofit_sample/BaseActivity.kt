package com.chiclaim.android.retrofit_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showBackMenu()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTasks()
    }

    open fun showBackMenu() = true


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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