package com.chiclaim.annotation.com.chiclaim.reflection;


import com.chiclaim.annotation.com.chiclaim.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionTest {

    public void test(@NotNull List<String> list) {

    }


    public static void main(String[] args) throws NoSuchMethodException {
        Method method = ReflectionTest.class.getDeclaredMethod("test", List.class);
        Annotation[][] ass = method.getParameterAnnotations();
        for (Annotation[] as : ass) {
            for (Annotation annotation : as) {
                System.out.println(annotation); // @com.chiclaim.annotation.com.chiclaim.annotation.NotNull()
            }
        }
    }

}
