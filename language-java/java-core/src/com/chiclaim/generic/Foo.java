package com.chiclaim.generic;

/**
 * Generic type 定义
 *
 * @author chiclaim
 */
// T is type parameter
public class Foo<T> {


    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    // 也可以定义多个 type parameter
    static class Foo2<T1, T2> {

    }
}
