package com.chiclaim.array;

import java.util.Arrays;

public class ArrayUtils {


    private static void arrayAppend() {

        byte[] content = new byte[]{1, 2, 3, 4};
        byte[] append = new byte[]{5, 6, 7};

        byte[] newArray = new byte[content.length + append.length];
        System.arraycopy(content, 0, newArray, 0, content.length);
        System.arraycopy(append, 0, newArray, content.length, append.length);

        System.out.println(Arrays.toString(newArray));
    }

    public static void main(String[] args) {

        arrayAppend();


    }

}
