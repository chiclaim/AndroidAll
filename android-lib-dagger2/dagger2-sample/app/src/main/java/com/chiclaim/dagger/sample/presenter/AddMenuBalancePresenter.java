package com.chiclaim.dagger.sample.presenter;


import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;
import com.chiclaim.dagger.sample.model.MenuBalanceRepository;
import com.chiclaim.dagger.sample.view.IAddMenuBalanceView;

import javax.inject.Inject;

public class AddMenuBalancePresenter implements IAddMenuBalancePresenter {

    private final IAddMenuBalanceView mView;

    private final MenuBalanceRepository mMenuBalanceRepository;

//    private final String mCategoryName;
//    private final String mUsername;

//    @Inject
//    AddMenuBalancePresenter(@Username String username, @Category String categoryName, IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
//        this.mUsername = username;
//        this.mCategoryName = categoryName;
//        this.mView = view;
//        this.mMenuBalanceRepository = mMenuBalanceRepository;
//    }

    @Inject
    AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
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
