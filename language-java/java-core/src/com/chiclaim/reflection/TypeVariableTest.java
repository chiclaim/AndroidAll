package com.chiclaim.reflection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;


public class TypeVariableTest<K extends Comparable & Serializable, V> {
    K key;
    V value;

    public static void main(String[] args) throws Exception {
        // 获取字段的类型
        Field fk = TypeVariableTest.class.getDeclaredField("key");
        Field fv = TypeVariableTest.class.getDeclaredField("value");
        TypeVariable<?> keyType = (TypeVariable) fk.getGenericType();
        TypeVariable<?> valueType = (TypeVariable) fv.getGenericType();

        System.out.println(keyType.getName()); // K
        System.out.println(valueType.getName()); // V

        // getGenericDeclaration 方法
        System.out.println(keyType.getGenericDeclaration()); // class com.chiclaim.reflection.TypeVariableTest
        System.out.println(valueType.getGenericDeclaration()); // class com.chiclaim.reflection.TypeVariableTest

        // getBounds 方法

        // K 的上界，有两个：
        //interface java.lang.Comparable
        //interface java.io.Serializable
        for (Type bound : keyType.getBounds()) {
            System.out.println("K 的上界:"+bound);
        }

        // V 的上界，没有明确声明，默认为Object
        for (Type bound : valueType.getBounds()) {
            System.out.println("V 的上界:"+bound);
        }
    }
}