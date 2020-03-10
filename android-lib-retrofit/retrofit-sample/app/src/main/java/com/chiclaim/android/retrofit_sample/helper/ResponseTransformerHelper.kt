package com.chiclaim.android.retrofit_sample.helper

import com.chiclaim.android.retrofit_sample.exception.ApiException
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class ResponseTransformerHelper {

    companion object {
        fun <T> transformResult(): ObservableTransformer<ResponseModel<T>?, T> {
            return ObservableTransformer { upstream ->
                upstream.flatMap { responseModel: ResponseModel<T>? ->
                    responseModel?.let {
                        if (it.status != 0) {
                            Observable.error(ApiException(it.errorCode, it.message))
                        } else {
                            Observable.just(it.data)
                        }

                    }
                }
            }
        }
    }
}
