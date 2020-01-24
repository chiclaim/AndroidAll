package com.chiclaim.annotation;

import java.util.ArrayList;
import java.util.List;

public class AnnotationTest  {


    @Deprecated
    public void test() {
        // do something
    }

    @SuppressWarnings("unchecked")
    public void testWarning() {
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
