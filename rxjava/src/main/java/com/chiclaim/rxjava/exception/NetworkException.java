package com.chiclaim.rxjava.exception;

/**
 * Created by chiclaim on 2016/02/25
 */
public class NetworkException extends RuntimeException {
    public NetworkException(String detailMessage) {
        super(detailMessage);
    }
}
