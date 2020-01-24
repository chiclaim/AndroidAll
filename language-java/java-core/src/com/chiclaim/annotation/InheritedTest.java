package com.chiclaim.annotation;

public class InheritedTest extends BaseClass {

    public static void main(String[] args) {
        System.out.println(InheritedTest.class.isAnnotationPresent(BaseAnnotation.class));
    }
}

@BaseAnnotation
class BaseClass {

}
