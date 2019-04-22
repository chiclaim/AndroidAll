package com.chiclaim.android.arch

interface Callback<T> {
    fun onDone(data: T)
}