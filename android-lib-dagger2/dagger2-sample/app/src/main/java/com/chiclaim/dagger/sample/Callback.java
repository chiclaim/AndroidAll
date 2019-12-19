package com.chiclaim.dagger.sample;


public interface Callback<T> {

    void onSuccess(T data);

    void onFailed(String errorMsg);

}
