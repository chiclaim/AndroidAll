package com.chiclaim.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author chiclaim
 */
public class CreateObject {

    public static void main(String[] args) throws CloneNotSupportedException {
        cloneTest();
    }

    private static void getConstructors() throws NoSuchMethodException {
        // getConstructors() 获得类的所有public构造函数
        for (Constructor<?> constructor : Dog.class.getConstructors()) {
            //System.out.println(constructor);
        }
        // getDeclaredConstructors 获取类的所有构造方法，包括private
        for (Constructor<?> constructor : Dog.class.getDeclaredConstructors()) {
            //System.out.println(constructor);
        }
        // getConstructors(Class...) 获得类的所有特定参数的public构造函数(不包括父类)
        System.out.println(Dog.class.getConstructor(int.class));

        // getDeclaredConstructor(Class...) 获得类的所有特定参数的构造函数(包括private，但不能获取父类)
        System.out.println(Dog.class.getDeclaredConstructor(String.class));

    }

    private static void newInstanceByClass() throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        String str = String.class.newInstance();
        System.out.println("----" + str);

        str = String.class.getConstructor(String.class).newInstance("this is my string");
        System.out.println("----" + str);

    }


    private static void cloneTest() throws CloneNotSupportedException {
        System.out.println(new Dog(22).clone());
    }
}

class Animal {
    public Animal(int age) {

    }

    public Animal(String name) {

    }
}

class Dog extends Animal implements Cloneable{

    public Dog(int age) {
        super(age);
        System.out.println("Dog(int age)");

    }

    private Dog(String name) {
        super("Dog");
        System.out.println("Dog(String name)");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
