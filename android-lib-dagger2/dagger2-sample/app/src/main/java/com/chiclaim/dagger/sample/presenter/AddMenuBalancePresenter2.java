package com.chiclaim.dagger.sample.presenter;


import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;
import com.chiclaim.dagger.sample.model.MenuBalanceRepository2;
import com.chiclaim.dagger.sample.view.IAddMenuBalanceView;

import javax.inject.Inject;

public class AddMenuBalancePresenter2 implements IAddMenuBalancePresenter {

    private final IAddMenuBalanceView mView;

    private final MenuBalanceRepository2 mMenuBalanceRepository;


    @Inject
    AddMenuBalancePresenter2(IAddMenuBalanceView view, MenuBalanceRepository2 mMenuBalanceRepository) {
        this.mView = view;
        this.mMenuBalanceRepository = mMenuBalanceRepository;
    }

    @Override
    public void addMenuBalance() {
        //Log.d("AddMenuBalancePresenter", "add menu in category " + mCategoryName + " by " + mUsername);
        mMenuBalanceRepository.addMenuBalance(new Callback<MenuBalance>() {
            @Override
            public void onSuccess(MenuBalance data) {
                mView.addMenuBalanceSuccess(data);
            }

            @Override
            public void onFailed(String errorMsg) {
                mView.addMenuBalanceFailure(errorMsg);
            }
        });
    }
}
