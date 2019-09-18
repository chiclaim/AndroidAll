package com.chiclaim.androidnative.jni;

/**
 * desc:
 * <p>
 * Created by kumu@2dfire.com on 2019/9/18.
 */
public class JNIHolder {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native static String stringFromJNI2();


}
