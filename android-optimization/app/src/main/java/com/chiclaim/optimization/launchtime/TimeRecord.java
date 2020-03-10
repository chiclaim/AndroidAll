package com.chiclaim.optimization.launchtime;

import android.util.Log;

public class TimeRecord {

    private static long startTime;

    public static void startRecord() {
        startTime = System.currentTimeMillis();
    }

    public static void stopRecord(String tag) {
        Log.e("TimeRecord", tag + ":" + (System.currentTimeMillis() - startTime));
    }


}
