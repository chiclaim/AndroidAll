package com.chiclaim.dagger.sample;

import android.app.Application;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/2/27.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ComponentManager.getInstance().init(this);
    }

}
