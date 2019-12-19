package base;

import java.util.Arrays;
import java.util.List;

//int[] 当做 泛型
class A extends FunctionVarargs<int[]>{

}

public class FunctionVarargs<E> {

    private static void println(Object data) {
        System.out.println(data);
    }


    public static <T> List<T> listOf(T... items) {
        System.out.println(items.getClass());
        return Arrays.asList(items);
    }

    public static void testArrays() {
        int[] intArr = {1, 2, 3};
        List list = Arrays.asList(intArr);
        println(list.size());
    }

    public static void testArrays2() {
        Integer[] intArr = {1, 2, 3};
        List list = Arrays.asList(intArr);
        println(list.size());
    }

    public static <T> void testGeneric(T[] data) {
        int[][] intArray = {{1},{2}};
        //T[] t = intArray; 非法
    }


    public static void main(String[] args) {

        //listOf(1, 2, 3, 4, 5);
        testArrays();
        testArrays2();

        int[] intArr = {1, 2};

        //把 二维数组 传递 给 参数 是 一维泛型数组 的函数
        int[][] intArray = {{1},{2}};
        testGeneric(intArray);

    }

}
