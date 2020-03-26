package com.chiclaim.optimization.systrace;

import android.os.Bundle;
import android.os.Debug;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.TraceCompat;

import com.chiclaim.optimization.helper.InitHelper;


/*

Systrace 工具是结合 Android 系统内核数据生成 HTML 文档

2，API 18 以上使用，推荐使用 TraceCompat

1，轻量级、开销小

2，直观反映CPU利用率

3，cputime 和 walltime 的区别？

walltime 是代码代码执行的时间

cputime 是代码消耗cpu的时间（重点指标）

举例：锁竞争

比如进入个方法 A 后需要获取一把锁，但是这把锁被其他线程持有，尽管 A 方法代码本身不耗时，但是线程一直在等待，
总的下来 A 方法的执行时间也较长，也就是 walltime 比较大，但是 cpu花在上面的时间却很少。

 */

public class SystraceTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TraceCompat.beginSection("TraceInit");

        InitHelper.initStetho(this);
        InitHelper.initWeex(this);
        InitHelper.initJPush(this);
        InitHelper.initFresco(this);
        InitHelper.initAMap(this);
        InitHelper.initUmeng(this);
        InitHelper.initBugly(this);

        TraceCompat.endSection();


    }


}
