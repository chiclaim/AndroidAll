package com.chiclaim.dagger.sample.model.dagger;

import com.chiclaim.dagger.sample.dagger.ApplicationModule;
import com.chiclaim.dagger.sample.model.MenuBalanceRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MenuBalanceRepoModule.class, ApplicationModule.class})
public interface MenuBalanceRepoComponent {

    MenuBalanceRepository getMenuBalanceRepository();
}
