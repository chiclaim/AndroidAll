package com.chiclaim.dagger.sample.presenter.dagger;

import com.chiclaim.dagger.sample.dagger.FragmentScoped;
import com.chiclaim.dagger.sample.model.dagger.MenuBalanceRepoComponent;
import com.chiclaim.dagger.sample.view.AddMenuBalanceFragment;

import dagger.Component;

@FragmentScoped
@Component(dependencies = MenuBalanceRepoComponent.class, modules = AddMenuBalancePresenterModule.class)
public interface AddMenuBalanceComponent {

    void inject(AddMenuBalanceFragment fragment);
}
