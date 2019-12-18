package com.chiclaim.butterknife;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * 关于Processor用到的工具方法
 * Created by chiclaim on 2016/09/23
 */

public class ProcessorUtils {


    public static <T> T findViewByCast(View source, @IdRes int id, Class<T> cls) {
        View view = source.findViewById(id);
        return castView(view, id, cls);
    }

    private static <T> T castView(View view, @IdRes int id, Class<T> cls) {
        try {
            return cls.cast(view);
        } catch (ClassCastException e) {
            //提示使用者类型转换异常
            throw new IllegalStateException(view.getClass().getName() + "不能强转成" + cls.getName());
        }
    }

}
