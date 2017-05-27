package com.chiclaim.rxjava.api;


import com.chiclaim.rxjava.exception.ConversionException;

/**
 * Created by chiclaim on 2016/02/26
 */
public class NetErrorType {

    public static final int TYPE_ERROR_TIME_OUT = 1;
    public static final int TYPE_ERROR_UNKNOW_HOST = 2;
    public static final int TYPE_ERROR_CONNECT = 3;
    public static final int TYPE_ERROR_CONVERSION = 6;
    public static final int TYPE_ERROR_UNKNOW = 20;

    public static class ErrorType {
        public int type;
        public String msg;

        public ErrorType(int type, String msg) {
            this.type = type;
            this.msg = msg;
        }
    }

    public static ErrorType getErrorType(Throwable t) {
        if (t instanceof java.net.SocketTimeoutException) {
            return new ErrorType(TYPE_ERROR_TIME_OUT, "连接超时");
        } else if (t instanceof java.net.UnknownHostException) {
            return new ErrorType(TYPE_ERROR_UNKNOW_HOST, "网络不可用");
        } else if (t instanceof java.net.ConnectException) {
            return new ErrorType(TYPE_ERROR_CONNECT, "网络不可用");
        } else if (t instanceof ConversionException) {
            return new ErrorType(TYPE_ERROR_CONVERSION, "JSON解析失败");
        }
        return new ErrorType(TYPE_ERROR_UNKNOW, "未知错误");
    }
}
