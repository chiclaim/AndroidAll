package com.chiclaim.dagger.sample.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chiclaim.dagger.sample.ComponentManager;
import com.chiclaim.dagger.sample.R;
import com.chiclaim.dagger.sample.bean.MenuBalance;
import com.chiclaim.dagger.sample.presenter.AddMenuBalancePresenter;
import com.chiclaim.dagger.sample.presenter.dagger.AddMenuBalancePresenterModule;
import com.chiclaim.dagger.sample.presenter.dagger.DaggerAddMenuBalanceComponent;

import javax.inject.Inject;


public class AddMenuBalanceFragment extends Fragment implements IAddMenuBalanceView {

    private ProgressDialog dialog;
    private TextView tvMenuBalanceList;

    @Inject
    AddMenuBalancePresenter presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerAddMenuBalanceComponent.builder()
                .menuBalanceRepoComponent(ComponentManager.getInstance().getMenuBalanceRepoComponent())
                .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule("Chiclaim","湘菜", this))
                .build()
                .inject(this);//完成注入
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_menu_balance_layout, container, false);
        tvMenuBalanceList = (TextView) view.findViewById(R.id.tv_menu_balance_list);
        view.findViewById(R.id.btn_add_menu_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                presenter.addMenuBalance();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void addMenuBalanceSuccess(MenuBalance menuBalance) {
        hideLoading();
        tvMenuBalanceList.append(menuBalance.getMenuId() + "--" + menuBalance.getMenuName() + "\n");
        showToast("Added successfully");
    }

    @Override
    public void addMenuBalanceFailure(String errorMsg) {
        hideLoading();
        showToast(errorMsg);
    }

    void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("请稍后...");
        }
        dialog.show();

    }

    void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
