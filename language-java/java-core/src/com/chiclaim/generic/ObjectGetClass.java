package com.chiclaim.generic;

/**
 * @author chiclaim
 */
public class ObjectGetClass {

    public static void main(String[] args) {
        Object obj = new Object();
        classOf(obj.getClass());
    }

    static <T> Class<? extends T> classOf(Class<? extends T> clazz) {
      return clazz;
    }

    static <T> void classOf2(T obj) {
        classOf(obj.getClass());
    }

}
