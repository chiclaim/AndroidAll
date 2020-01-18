package com.chiclaim.android.retrofit_sample.helper

import com.chiclaim.android.retrofit_sample.exception.ApiException
import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class ResponseTransformerHelper {

    companion object {
        fun <T> transformResult(): ObservableTransformer<ResponseModel<T>?, T> {
            return ObservableTransformer { upstream ->
                upstream.flatMap { responseModel ->
                    if (responseModel.status != 0) {
                        Observable.error(ApiException(responseModel.errorCode, responseModel.message))
                    } else {
                        Observable.just(responseModel.data)
                    }
                }
            }
        }
    }
}
