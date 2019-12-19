package com.chiclaim.dagger.sample.model;

import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;

import javax.inject.Inject;

public class MenuBalanceRepository implements IMenuBalanceSource {
    private final IMenuBalanceSource remoteSource;

    @Inject
    MenuBalanceRepository(IMenuBalanceSource remoteSource){
        this.remoteSource = remoteSource;
    }

    @Override
    public void addMenuBalance(Callback<MenuBalance> callback) {
        remoteSource.addMenuBalance(callback);
    }
}
