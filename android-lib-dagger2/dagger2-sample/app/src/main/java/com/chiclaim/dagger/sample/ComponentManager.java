package com.chiclaim.dagger.sample;

import android.content.Context;

import com.chiclaim.dagger.sample.dagger.ApplicationModule;
import com.chiclaim.dagger.sample.model.dagger.DaggerMenuBalanceRepoComponent;
import com.chiclaim.dagger.sample.model.dagger.MenuBalanceRepoComponent;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/2/27.
 */

public class ComponentManager {

    private static ComponentManager componentManager;

    private MenuBalanceRepoComponent mMenuBalanceRepoComponent;


    private ComponentManager() {

    }

    public static ComponentManager getInstance() {
        if (componentManager == null) {
            componentManager = new ComponentManager();
        }
        return componentManager;
    }

    /**
     * 初始化组件
     *
     * @param context
     */
    public void init(Context context) {
        if (mMenuBalanceRepoComponent == null) {
            initMenuBalanceRepoComponent(context);
        }
    }

    private void initMenuBalanceRepoComponent(Context application) {
        mMenuBalanceRepoComponent = DaggerMenuBalanceRepoComponent.builder()
                .applicationModule(new ApplicationModule(application.getApplicationContext()))
                .build();
    }

    public MenuBalanceRepoComponent getMenuBalanceRepoComponent() {
        return mMenuBalanceRepoComponent;
    }
}


