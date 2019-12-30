package com.chiclaim.android.retrofit_sample.call_adapter

import com.chiclaim.android.retrofit_sample.bean.ResponseModel
import com.chiclaim.android.retrofit_sample.function.ErrorFunction
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class SubscribeOnCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        if (getRawType(returnType) != Observable::class.java) {
            return null // Ignore non-Observable types.
        }

        // Look up the next call adapter which would otherwise be used if this one was not present.
        val delegate =
            retrofit.nextCallAdapter(this, returnType, annotations) as CallAdapter<Any, Observable<ResponseModel<*>>>

        println(delegate.responseType())

        return object : CallAdapter<Any, Any> {
            override fun adapt(call: Call<Any>): Any {
                val o: Observable<ResponseModel<*>> = delegate.adapt(call)
                return o.subscribeOn(Schedulers.io()).onErrorResumeNext(ErrorFunction())
            }

            override fun responseType(): Type {
                return delegate.responseType()
            }
        }
    }

}
