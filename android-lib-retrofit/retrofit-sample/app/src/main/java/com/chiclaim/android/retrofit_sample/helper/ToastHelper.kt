package com.chiclaim.android.retrofit_sample.helper

import android.content.Context
import android.widget.Toast

class ToastHelper {

    companion object {
        fun showToast(context: Context, mssage: CharSequence) {
            Toast.makeText(context, ",", Toast.LENGTH_SHORT).show()
        }
    }
}