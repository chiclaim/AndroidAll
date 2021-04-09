package com.chiclaim.array;

public class ArraySize {

    public static void main(String[] args) {
        int[][] x = {
                {1,2,3},
                {},
                {3,4,5},
                {6,7}
        };
        System.out.println(x.length);    // 4
        System.out.println(x[1].length); // 0
        System.out.println(x[3].length); // 2
    }

}
