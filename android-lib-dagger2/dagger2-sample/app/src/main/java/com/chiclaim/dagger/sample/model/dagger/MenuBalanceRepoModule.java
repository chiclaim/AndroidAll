package com.chiclaim.dagger.sample.model.dagger;

import com.chiclaim.dagger.sample.model.IMenuBalanceSource;
import com.chiclaim.dagger.sample.model.MenuBalanceRemoteSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MenuBalanceRepoModule {

    @Singleton
    @Provides
        //@Remote
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
}
