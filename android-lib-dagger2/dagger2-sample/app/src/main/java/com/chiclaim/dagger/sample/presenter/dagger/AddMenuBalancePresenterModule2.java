package com.chiclaim.dagger.sample.presenter.dagger;

import com.chiclaim.dagger.sample.view.IAddMenuBalanceView;

import dagger.Module;
import dagger.Provides;

@Module
public class AddMenuBalancePresenterModule2 {

    private final IAddMenuBalanceView mView;

    public AddMenuBalancePresenterModule2(IAddMenuBalanceView view) {
        mView = view;
    }

    @Provides
    IAddMenuBalanceView provideAddMenuBalanceView() {
        return mView;
    }
}
