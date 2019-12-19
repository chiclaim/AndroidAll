
package com.chiclaim.rxjava.operator.create;

public class ServerException extends Exception {

    private String errorCode;
    private String message;

    public ServerException(String errorCode, String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
