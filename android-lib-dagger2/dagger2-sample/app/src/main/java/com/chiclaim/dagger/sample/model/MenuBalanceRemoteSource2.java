package com.chiclaim.dagger.sample.model;

import android.os.AsyncTask;

import com.chiclaim.dagger.sample.Callback;
import com.chiclaim.dagger.sample.bean.MenuBalance;

import javax.inject.Inject;

/**
 * 远程数据源 如HTTP请求
 */
public class MenuBalanceRemoteSource2 implements IMenuBalanceSource {

    @Inject
    MenuBalanceRemoteSource2(){

    }

    @Override
    public void addMenuBalance(final Callback<MenuBalance> callback) {
        //模拟网络请求
        new AsyncTask<Void, Void, MenuBalance>() {

            @Override
            protected MenuBalance doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
                return new MenuBalance("1001", "东坡肉");
            }

            @Override
            protected void onPostExecute(MenuBalance menuBalance) {
                super.onPostExecute(menuBalance);
                callback.onSuccess(menuBalance);
            }
        }.execute();
    }
}
