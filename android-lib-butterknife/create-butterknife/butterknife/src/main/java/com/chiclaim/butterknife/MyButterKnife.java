package com.chiclaim.butterknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chiclaim on 2016/09/22
 */

public class MyButterKnife {


    public static void bind(Activity activity) {
        //获取activity的decorView
        View view = activity.getWindow().getDecorView();
        String qualifiedName = activity.getClass().getName();

        //找到该activity对应的Bind类
        String generateClass = qualifiedName + "_ViewBinding";
        try {
            //然后调用Bind类的构造方法,从而完成activity里view的初始化
            Class.forName(generateClass)
                    .getConstructor(activity.getClass(), View.class).newInstance(activity, view);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


}
