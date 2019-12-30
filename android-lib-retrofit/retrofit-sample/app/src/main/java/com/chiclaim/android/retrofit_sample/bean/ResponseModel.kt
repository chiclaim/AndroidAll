package com.chiclaim.android.retrofit_sample.bean

class ResponseModel<T> {

    var status: Int = 0

    var data: T? = null

    var errorCode: String? = null

    var message: String? = null

}