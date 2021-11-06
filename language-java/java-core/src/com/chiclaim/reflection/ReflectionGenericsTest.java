package com.chiclaim.reflection;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通过反射获取泛型信息
 */
public class ReflectionGenericsTest {


    private static class BaseSub1 extends Base<String> {
    }

    private static class BaseSub2 extends Base<StringBuilder> {
    }

    private static class BaseSub3 extends Base<StringBuffer> {
    }



    private static class Base<T> {
        @SuppressWarnings("unchecked")
        Class<T> getTypeParameterClass() {
            Type type = getClass().getGenericSuperclass();
            ParameterizedType paramType = (ParameterizedType) type;
            // Base<java.lang.String>
            System.out.println("------ParameterizedType:" + paramType);
            // ReflectionTest
            System.out.println("------ParameterizedType.getOwnerType:" + paramType.getOwnerType());
            // ReflectionTest$Base
            System.out.println("------ParameterizedType.getOwnerType:" + paramType.getRawType());
            return (Class<T>) paramType.getActualTypeArguments()[0];
        }
    }


    private static void getSuperClassTypeArgument() throws Exception {
        Object object0 = new BaseSub1().getTypeParameterClass().newInstance();
        Object object1 = new BaseSub2().getTypeParameterClass().newInstance();
        Object object2 = new BaseSub3().getTypeParameterClass().newInstance();
        System.out.println(object0.getClass().getSimpleName()); // String
        System.out.println(object1.getClass().getSimpleName()); // StringBuilder
        System.out.println(object2.getClass().getSimpleName()); // StringBuffer
    }



    public static void main(String[] args) throws Exception {

        getSuperClassTypeArgument();

    }

}
