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

    public native String invokeObjMethod();

    public native User createObj();

    public native void updateIntArray(int[] array);

    public String getIntArrayFromJNI() {
        int[] arr = getIntArray();
        StringBuilder builder = new StringBuilder();
        builder.append("从 Native 获取 int[]：");
        for (int i : arr) {
            builder.append(i).append(",");
        }
        return builder.toString();
    }

    // 私有字段也可以被 Native 访问
    public int number = 5;

    // 私有方法也可以被 Native 调用
    private String methodForJNI(int value) {
        return "本方法被 Native 调用，参数为：" + value;
    }


}
