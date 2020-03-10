package com.chiclaim.bugs;

import java.util.Random;

public class RandomTest {

    private static final char[] LOWER_CASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();


    public static void main(String[] args) {
        Random random = new Random();
        // 如果 random.nextInt() 等于 Integer.MIN_VALUE 将会 ArrayIndexOutOfBoundsException
        char c = LOWER_CASE[Math.abs(random.nextInt()) % LOWER_CASE.length];
        System.out.println(c);
        System.out.println(Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE); // true
        System.out.println((Integer.MIN_VALUE)); // -2147483648
        System.out.println((Integer.MAX_VALUE)); //  2147483647
    }

}
