package com.chiclaim.androidnative.jni;

/**
 * desc:
 * <p>
 * Created by kumu@2dfire.com on 2019/9/18.
 */
public class JNIHolder {

    static {
        System.loadLibrary("native-lib");
    }


    public native String stringFromJNI();

    public native static String stringFromJNI2();

    public native int[] getIntArray();

    public native int updateObjProperty();

    public String getIntArrayFromJNI() {
        int[] arr = getIntArray();
        StringBuilder builder = new StringBuilder();
        builder.append("从 Native 获取 int[]：");
        for (int i : arr) {
            builder.append(i).append(",");
        }
        return builder.toString();
    }

    public int number = 5;



}
