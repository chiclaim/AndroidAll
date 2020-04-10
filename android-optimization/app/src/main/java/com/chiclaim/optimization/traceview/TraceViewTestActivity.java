package com.chiclaim.optimization.traceview;

import android.os.Bundle;
import android.os.Debug;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.optimization.helper.InitHelper;


/*

可以展现执行时间，调用栈等。

信息全面，包含所有线程

1，THREAD(N) 可以看出线程的总数

2，top down 可以清楚的看出方法的调用栈及耗时时间，Wall Clock Time 和 Thread Time 的区别，Wall Clock Time 是方法执行的时间，Thread Time 是 CPU 在方法上的时间，它要比 Wall Clock Time 要小。

3，call chart 可以看出调用栈信息，方法的执行顺序及时间点


可以通过代码埋点的方式进行打点统计。缺点：

1，运行时开销大，程序整体变慢

2，可能带偏优化方向

3，traceview VS cpu profiler

traceView 文件生成路径：mnt/sdcard/Android/data/package-name/files/

 */

public class TraceViewTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.startMethodTracing("myTraceView");

        InitHelper.initStetho(this);
        InitHelper.initWeex(this);
        InitHelper.initJPush(this);
        InitHelper.initFresco(this);
        InitHelper.initAMap(this);
        InitHelper.initUmeng(this);
        InitHelper.initBugly(this);

        Debug.stopMethodTracing();



    }



}
