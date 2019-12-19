package com.chiclaim.dagger.sample.view;

import com.chiclaim.dagger.sample.bean.MenuBalance;

public interface IAddMenuBalanceView {
    void addMenuBalanceSuccess(MenuBalance menuBalance);

    void addMenuBalanceFailure(String errorMsg);
}