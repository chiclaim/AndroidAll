package com.chiclaim.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * @author chiclaim
 */
public class ReflectMember {

    public static void main(String[] args) throws NoSuchFieldException {
        // getFields 获取所有public字段
        for (Field field : Bean.class.getFields()) {
            //System.out.println(field);
        }
        // getDeclaredFields 获取所有字段，包括私有
        for (Field field : Bean.class.getDeclaredFields()) {
            //System.out.println(field);
        }

        // getFields(name) 根据字段名称获取public字段
        System.out.println(Bean.class.getField("name"));
        // getDeclaredField(name) 根据字段名称获取字段(包括私有)
        System.out.println(Bean.class.getDeclaredField("age"));

        // Method 也是类似
    }
}

class Bean{


    private int age;
    public String name;


}
