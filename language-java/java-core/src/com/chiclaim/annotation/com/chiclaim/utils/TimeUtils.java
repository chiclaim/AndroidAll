package com.chiclaim.annotation.com.chiclaim.utils;

import java.util.Calendar;

public class TimeUtils {

    /**
     * 获取"今天"的开始和结束的时间戳
     *
     * @return long[], long[0] = start, long[1] = end
     * @author kumu
     */
    private static long[] getTodayBeginEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long start = calendar.getTimeInMillis();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long end = calendar.getTimeInMillis();
        return new long[]{start, end};
    }


    /**
     * 获取"昨天"的开始和结束的时间戳
     *
     * @return long[], long[0] = start, long[1] = end
     * @author kumu
     */
    public static long[] getYesterdayBeginEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
        long start = calendar.getTimeInMillis();

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long end = calendar.getTimeInMillis();
        return new long[]{start, end};
    }


    private static void printDiff(long[] s) {
        System.out.println(s[0]);
        System.out.println(s[1]);

        System.out.println((s[1] - s[0]) / 1000 / 60 / 60 + " Hours");
    }

    public static void main(String[] args) {
        long[] s = getTodayBeginEnd();
        printDiff(s);

        long[] s2 = getYesterdayBeginEnd();
        printDiff(s2);

    }
}

