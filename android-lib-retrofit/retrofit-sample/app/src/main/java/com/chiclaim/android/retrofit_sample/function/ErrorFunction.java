package com.chiclaim.android.retrofit_sample.function;

import com.chiclaim.android.retrofit_sample.bean.ResponseModel;
import com.chiclaim.android.retrofit_sample.helper.ExceptionHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


public class ErrorFunction implements Function<Throwable, ObservableSource<ResponseModel<?>>> {

    @Override
    public ObservableSource<ResponseModel<?>> apply(Throwable throwable) {
        return Observable.error(ExceptionHelper.transformException(throwable));
    }

}