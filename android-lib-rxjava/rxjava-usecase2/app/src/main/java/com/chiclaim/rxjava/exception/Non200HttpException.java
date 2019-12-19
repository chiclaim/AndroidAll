package com.chiclaim.rxjava.exception;

/**
 * A non-200 HTTP status code was received from the server <br/><br/>
 * <b>Created by chiclaim on 2016/02/26</b>
 */
public class Non200HttpException extends RuntimeException {
    public Non200HttpException(String detailMessage) {
        super(detailMessage);
    }
}
