package com.chiclaim.dagger.sample.presenter.dagger;

import com.chiclaim.dagger.sample.dagger.Category;
import com.chiclaim.dagger.sample.dagger.Username;
import com.chiclaim.dagger.sample.view.IAddMenuBalanceView;

import dagger.Module;
import dagger.Provides;

@Module
public class AddMenuBalancePresenterModule {

    private final IAddMenuBalanceView mView;
    private final String mUsername;
    private final String mCategoryName;

    public AddMenuBalancePresenterModule(String username, String categoryName, IAddMenuBalanceView view) {
        mUsername = username;
        mCategoryName = categoryName;
        mView = view;
    }

    @Provides
    IAddMenuBalanceView provideAddMenuBalanceView() {
        return mView;
    }

    @Provides
    @Category
    String provideCategoryName() {
        return mCategoryName;
    }

    @Provides
    @Username
    String provideUsername() {
        return mUsername;
    }
}
