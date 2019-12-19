package com.chiclaim.dagger.sample.presenter.dagger;

import com.chiclaim.dagger.sample.dagger.FragmentScoped;
import com.chiclaim.dagger.sample.view.AddMenuBalanceFragment2;

import dagger.Component;

@FragmentScoped
@Component(modules = AddMenuBalancePresenterModule2.class)
public interface AddMenuBalanceComponent2 {

    void inject(AddMenuBalanceFragment2 fragment);
}
