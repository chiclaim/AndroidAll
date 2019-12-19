package com.chiclaim.dagger.sample.bean;

/**
 * Description：
 * <br/>
 * Created by Chiclaim on 2017/2/27.
 */

public class MenuBalance {
    private String menuId;
    private String menuName;

    public MenuBalance(String menuId, String menuName) {
        this.menuId = menuId;
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
