package com.chiclaim.rxjava.api;

import android.util.Log;

import com.chiclaim.rxjava.exception.AccessDenyException;
import com.chiclaim.rxjava.exception.ConversionException;
import com.chiclaim.rxjava.exception.NetworkException;
import com.chiclaim.rxjava.exception.UnKnowException;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by chiclaim on 2016/01/26
 */
public class ApiServiceFactory {

    //server source code please see:
    //    https://github.com/chiclaim/android_mvvm_server
    private static final String BASE_URL = "http://10.1.67.34:8080/android_mvvm_server";
//    private static final String BASE_URL = "http://192.168.2.106:8080/AndroidMvvmServer";
    //http://10.1.67.34:8080/android_mvvm_server

    private static RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Authorization", "test");
        }
    };

    private static class NetWorkErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError error) {
            retrofit.client.Response r = error.getResponse();
            if (r != null && r.getStatus() == 401) {
                Log.e("ErrorHandler", "---------> access deny code=401");
                return new AccessDenyException(error.getMessage());
            } else if (error.getKind() == RetrofitError.Kind.NETWORK) {
                Log.e("ErrorHandler", "---------> An IOException occurred while communicating to the server");
                return new NetworkException(error.getMessage());
            } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                Log.e("ErrorHandler", "---------> A non-200 HTTP status code was received from the server");
                //return new Non200HttpException(cause.getMessage());
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION) {
                Log.e("ErrorHandler", "---------> An exception was thrown while (de)serializing a body");
                return new ConversionException(error.getMessage());
            } else if (error.getKind() == RetrofitError.Kind.UNEXPECTED) {
                Log.e("ErrorHandler", "---------> An internal error occurred while attempting to execute a request. " +
                        "It is best practice to re-throw this exception so your application crashes.");
                return new UnKnowException(error.getMessage());
            }
            return error.getCause();
        }
    }

    private static RestAdapter restAdapter = new RestAdapter
            .Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(BASE_URL)
            .setErrorHandler(new NetWorkErrorHandler())
            .setRequestInterceptor(requestInterceptor)
            .build();


    public static <S> S createService(Class<S> serviceClazz) {
        return restAdapter.create(serviceClazz);
    }
}
