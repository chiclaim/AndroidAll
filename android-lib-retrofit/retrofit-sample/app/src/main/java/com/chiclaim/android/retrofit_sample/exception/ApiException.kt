package com.chiclaim.android.retrofit_sample.exception

class ApiException(val errorCode: String?, errorMessage: String?) : Exception(errorMessage)