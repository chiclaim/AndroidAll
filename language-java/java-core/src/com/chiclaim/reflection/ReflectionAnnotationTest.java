package com.chiclaim.reflection;

import com.chiclaim.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射（注解）
 * @author chiclaim
 */
public class ReflectionAnnotationTest {
    private void methodWithAnnotation(@NotNull List<String> list) {

    }

    private static void getMethodParameterAnnotation() throws Exception {
        Method method = ReflectionAnnotationTest.class.getDeclaredMethod("methodWithAnnotation", List.class);

        Annotation[][] ass = method.getParameterAnnotations();
        for (Annotation[] as : ass) {
            for (Annotation annotation : as) {
                System.out.println(annotation); // @com.chiclaim.annotation.NotNull()
            }
        }
    }

    public static void main(String[] args) throws Exception {

        getMethodParameterAnnotation();
    }


}
