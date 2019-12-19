package com.chiclaim.dagger.sample.model;

import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/2/27.
 */

public interface IMenuBalanceSource {
    void addMenuBalance(Callback<MenuBalance> callback);
}
