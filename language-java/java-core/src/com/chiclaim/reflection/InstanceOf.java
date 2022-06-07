package com.chiclaim.reflection;


/**
 * @author chiclaim
 */
public class InstanceOf {

    public static void main(String[] args) {

        System.out.println(Integer.valueOf(1) instanceof Number); // true
        System.out.println(int.class.isInstance(11)); // false [int is not Integer]
        System.out.println(Number.class.isInstance(11)); // true
        System.out.println(Number.class.isAssignableFrom(Integer.class)); // true
    }

}
