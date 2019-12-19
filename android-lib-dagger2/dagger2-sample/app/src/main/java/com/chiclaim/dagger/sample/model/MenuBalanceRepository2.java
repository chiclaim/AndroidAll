package com.chiclaim.dagger.sample.model;

import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;

import javax.inject.Inject;

public class MenuBalanceRepository2 implements IMenuBalanceSource {

    @Inject
    MenuBalanceRemoteSource2 remoteSource;

    @Inject
    MenuBalanceRepository2(){
    }

    @Override
    public void addMenuBalance(Callback<MenuBalance> callback) {
        remoteSource.addMenuBalance(callback);
    }
}
