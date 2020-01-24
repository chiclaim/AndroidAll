package com.chiclaim.reflection;


import com.chiclaim.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ReflectionTest {


    private static class BaseSub1 extends Base<String> {
    }

    private static class BaseSub2 extends Base<StringBuilder> {
    }

    private static class BaseSub3 extends Base<StringBuffer> {
    }

    void test(@NotNull List<String> list) {

    }


    private static class Base<T> {
        @SuppressWarnings("unchecked")
        Class<T> getTypeParameterClass() {
            Type type = getClass().getGenericSuperclass();
            ParameterizedType paramType = (ParameterizedType) type;
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

    private static void getMethodParameterAnnotation() throws Exception {
        Method method = ReflectionTest.class.getDeclaredMethod("test", List.class);
        Annotation[][] ass = method.getParameterAnnotations();
        for (Annotation[] as : ass) {
            for (Annotation annotation : as) {
                System.out.println(annotation); // @com.chiclaim.annotation.NotNull()
            }
        }
    }


    public static void main(String[] args) throws Exception {

        getSuperClassTypeArgument();

        getMethodParameterAnnotation();
    }

}
