package com.chiclaim.coroutine.basic;

/**
 * desc: Java daemon thread 测试
 * <p>
 * Created by Chiclaim on 2019/01/13
 */

public class DaemonThreadTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    System.out.println("Sleeping .." + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //daemon thread是优先级最低的线程
        // jvm不会等待daemon thread执行完再shutdown，而是daemon thread随着jvm关闭而关闭
        thread.setDaemon(true);
        thread.start();
    }
}
