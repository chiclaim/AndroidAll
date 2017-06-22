package com.chiclaim.rxjava.operator.create;

public interface Callable<T> {
    T call() throws Exception;
}