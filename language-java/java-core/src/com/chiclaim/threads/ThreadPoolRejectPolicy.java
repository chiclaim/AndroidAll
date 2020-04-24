package com.chiclaim.threads;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池的拒绝策略（案例分析）：
 *
 * AbortPolicy
 * DiscardPolicy
 * DiscardOldestPolicy
 * CallerRunsPolicy
 */
public class ThreadPoolRejectPolicy {


    private static void testAbortPolicy() {

        // corePoolSize = 1
        // maximumPoolSize = 1

        ThreadPoolExecutor es = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1)); // 队列容量为 1

        // 不设置拒绝策略的话，默认是 defaultHandler = AbortPolicy，抛出异常 RejectedExecutionException

        // 为什么容量都是 1，提交 2 个任务为什么不抛出异常？

        // 通过源码分析知道，如果当前正在的执行的任务数量，小于核心线程数 corePoolSize，那么直接加入容器 HashSet<Worker>
        // 不会判断队列容器
        int taskCount = 2;  // 改成 3 会抛出异常
        AtomicInteger index = new AtomicInteger(0);
        try {
            for (int i = 0; i < taskCount; i++) {
                execute(es, index.incrementAndGet(), 0);
            }
        } finally {
            es.shutdown();
        }

        /*
        // 输出结果
        execute task 1 start
            workers size 1
            queue size 1
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@36d05613
            queue:com.chiclaim.threads.ThreadPoolRejectPolicy$1@73d2cead
        execute task 1 end

        execute task 2 start
            workers size 1
            queue size 0
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@73d2cead
            queue:null
        execute task 2 end
         */
    }

    private static void testDiscardPolicy() {

        ThreadPoolExecutor es = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1)); // 队列容量为 1
        es.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());


        int taskCount = 3;
        AtomicInteger index = new AtomicInteger(0);
        try {
            for (int i = 0; i < taskCount; i++) {
                execute(es, index.incrementAndGet(), 0);
            }
        } finally {
            es.shutdown();
        }

        /*
        输出结果：

        execute task 1 start
            workers size 1
            queue size 1
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@25a9dc61
            queue:com.chiclaim.threads.ThreadPoolRejectPolicy$1@60ab46b0
        execute task 1 end

        execute task 2 start
            workers size 1
            queue size 0
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@60ab46b0
            queue:null
        execute task 2 end

        使用 DiscardPolicy 策略，超出最大值，直接抛弃，什么都不做。
         */
    }

    private static void testDiscardPolicyOldest() {

        ThreadPoolExecutor es = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1)); // 队列容量为 1
        es.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());


        int taskCount = 3;
        AtomicInteger index = new AtomicInteger(0);
        try {
            for (int i = 0; i < taskCount; i++) {
                execute(es, index.incrementAndGet(), 30000);
            }

        } finally {
            es.shutdown();
        }

        /*
        输出结果：

        execute task 1 start
            workers size 1
            queue size 1
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@25a9dc61
            queue:com.chiclaim.threads.ThreadPoolRejectPolicy$1@60ab46b0
        execute task 1 end

        execute task 3 start
            workers size 1
            queue size 0
            this:com.chiclaim.threads.ThreadPoolRejectPolicy$1@60ab46b0
            queue:null
        execute task 3 end

         为什么最老的是第二个提交的呢？
         因为第一次提交任务将任务添加到了 workers 集合中，不是 队列中 workQueue
         第二次提交将任务添加到 workQueue 中
         第三次试图添加到 workQueue 中，但是容量不够，添加失败，然后执行reject，也就是把 workQueue 的第一个元素删除（也就是第二个任务）
         */
    }

    //
    private static void testCallerRunsPolicy() {

        ThreadPoolExecutor es = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1)); // 队列容量为 1
        es.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // CallerRunsPolicy 表示拒绝策略为 不交给线程池执行，使用调用方法所在的线程执行。

        int taskCount = 3;
        AtomicInteger index = new AtomicInteger(0);
        try {
            for (int i = 0; i < taskCount; i++) {
                execute(es, index.incrementAndGet(), 0);
            }
        } finally {
            es.shutdown();
        }

        /*
         部分输出：
            execute task 3 start in main
         */
    }


    private static void execute(ThreadPoolExecutor es, int index, long sleep) {
        Runnable runnable = new MyRunnable(es, index, sleep);
        System.out.println("try execute task " + runnable);
        es.execute(runnable);

    }

    private static void printWorkers(ThreadPoolExecutor es) {
        try {
            Field field = ThreadPoolExecutor.class.getDeclaredField("workers");
            field.setAccessible(true);
            HashSet works = (HashSet) field.get(es);
            int size = works.size();
            System.out.println("\tworkers size " + size);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static class MyRunnable implements Runnable {
        ThreadPoolExecutor es;
        int index;
        long sleep;

        MyRunnable(ThreadPoolExecutor es, int index, long sleep) {
            this.es = es;
            this.index = index;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            System.out.println("execute task " + (index) + " start in " + Thread.currentThread().getName());

            printWorkers(es);
            System.out.println("\tqueue size " + es.getQueue().size());
            System.out.println("\tthis:" + this);
            Runnable run = es.getQueue().peek();
            System.out.println("\tqueue:" + run);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("execute task " + (index) + " end\n");
        }

    }


    public static void main(String[] args) {
//        testAbortPolicy();
//        testDiscardPolicy();
//        testDiscardPolicyOldest();
        testCallerRunsPolicy();
    }
}
