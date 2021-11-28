package com.chiclaim.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.List;

/**
 * @author chiclaim
 */
public class WildcardTypeTest {

    private List<? extends Number> a; // 上限
    private List<? super String> b; //下限

    public static void main(String[] args) throws NoSuchFieldException {
        Field fieldA = WildcardTypeTest.class.getDeclaredField("a");
        Field fieldB = WildcardTypeTest.class.getDeclaredField("b");

        // 先拿到范型类型
        ParameterizedType pTypeA = (ParameterizedType) fieldA.getGenericType();
        ParameterizedType pTypeB = (ParameterizedType) fieldB.getGenericType();

        // 再从范型里拿到通配符类型
        WildcardType wTypeA = (WildcardType) pTypeA.getActualTypeArguments()[0];
        WildcardType wTypeB = (WildcardType) pTypeB.getActualTypeArguments()[0];

        //获取上、下界类型
        System.out.println(wTypeA.getUpperBounds()[0]); // class java.lang.Number
        System.out.println(wTypeB.getLowerBounds()[0]); // class java.lang.String

        // 打印结果为: ? extends java.lang.Number
        System.out.println(wTypeA);
    }
}

