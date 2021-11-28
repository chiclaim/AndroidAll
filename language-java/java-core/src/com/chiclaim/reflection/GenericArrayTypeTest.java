package com.chiclaim.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.util.List;

/**
 * @author chiclaim
 */
public class GenericArrayTypeTest {
    List<String>[] lists;

    public static void main(String[] args) throws Exception {
        Field f = GenericArrayTypeTest.class.getDeclaredField("lists");
        GenericArrayType genericType = (GenericArrayType) f.getGenericType();
        System.out.println(genericType.getGenericComponentType());
    }
}
