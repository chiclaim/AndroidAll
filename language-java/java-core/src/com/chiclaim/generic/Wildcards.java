package com.chiclaim.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 泛型通配符
 *
 * @author chiclaim
 */
public class Wildcards{

    public static <T> void main(String[] args) {
        // 通配符: ?
        // new ArrayList<?>(); 不能用于创建泛型类对象
        // genericMethod(?);  不能用于调用泛型方法
        // interface MyList<T> extends List<?> 不能用于 super class

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3, 4.4, 5.5);
        //printList(integers); // 编译出错
        //printList(doubles);  // 编译出错

        printList2(integers);
        printList2(doubles);

        List<? extends String> list = new ArrayList<>();

        List<Object> s2 = Arrays.asList(new Object(),new Object());
        List<Serializable> s1 = Arrays.asList("11",22);
        //printList3(s1);
        //printList3(s2);

        List<? extends Number> extendsNumber = new ArrayList<>();
        List<? extends Integer> extendsInteger = new ArrayList<>();
        // List<? extends Number> 是 List<? extends Integer> 的父类
        extendsNumber = extendsInteger;

        List<? super Number> superNumber = new ArrayList<>();
        List<? super Integer> superInteger = new ArrayList<>();
        //List<? super Integer> 是 List<? super Number> 的父类
        superInteger = superNumber;


        //printClass(s2);

    }

    // 打印数字方法
    public static void printList(List<Number> list) {
        for (Number number : list) {
            System.out.println(number);
        }
    }


    /**
     * 上界通配符
     * @param list
     */
    public static void printList2(List<? extends Number> list) {
        for (Number number : list) {
            System.out.println(number);
        }
    }

    public static void updateNumberList2(List<? extends Number> list){
        Number number = list.get(1);
        //list.set(0,number); // 编译报错
    }




    /**
     * 下界通配符
     * @param list
     */
    public static void printNumberList3(List<? super Number> list) {
        for (int i = 0; i < list.size(); i++) {
            // //编译报错
            //Number number = list.get(i);
        }
    }


    /**
     * 下界通配符
     * @param list
     */
    public static void updateNumberList3(List<? super Number> list) {
        list.set(0,2.2);
    }

    void foo(List<?> i) {
        fooHelper(i);

        Class<?> aClass = this.getClass();
    }

    private <T> void fooHelper(List<T> l) {
        l.set(0, l.get(0));


    }

    public List<?> getList(){
        return null;
    }

    public final native Class<?> getClass2();


    public static void printClass(Object obj){
        System.out.println(obj.getClass());

        List<?> list = new ArrayList<>();

    }
}

