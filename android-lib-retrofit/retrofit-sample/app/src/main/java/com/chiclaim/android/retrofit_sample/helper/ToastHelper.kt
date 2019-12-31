package com.chiclaim.android.retrofit_sample.helper

import android.content.Context
import android.widget.Toast

class ToastHelper {

    companion object {
        fun showToast(context: Context, message: CharSequence?) {
            message ?: return
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}