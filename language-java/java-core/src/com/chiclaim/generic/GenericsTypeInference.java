package com.chiclaim.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 关于泛型的类型推导
 * @author chiclaim
 */
public class GenericsTypeInference {


    static <T> T pick(T a1, T a2) { return a2; }
    Collection<String> s = pick(new HashSet<>(), new ArrayList<>());

}
