package com.chiclaim.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class ParameterizedTypeTest {

    Map<String, Integer> map;

    public static void main(String[] args) throws Exception {

        Field f = ParameterizedTypeTest.class.getDeclaredField("map");
        System.out.println(f.getGenericType()); // java.util.Map<java.lang.String, java.lang.String>

        ParameterizedType pType = (ParameterizedType) f.getGenericType();
        System.out.println(pType.getRawType()); // interface java.util.Map

        // class java.lang.String
        // class java.lang.Integer
        for (Type type : pType.getActualTypeArguments()) {
            System.out.println(type);
        }
    }
}