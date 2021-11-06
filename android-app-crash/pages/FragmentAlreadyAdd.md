## 使用 Fragment 需要注意事项



### 避免Fragment重复添加，导致页面重影

在内存重建（旋转屏幕、用户动态设置本App的权限），需要判断 `Bundle savedInstanceState`

```
if (savedInstanceState != null) {
    myFragment = getSupportFragmentManager().findFragmentById(R.id.container);
}

if (myFragment == null) {
    myFragment = new MyFragment();
}
```

### 如何避免 java.lang.IllegalStateException: Fragment already added 异常

异常的原因在于一个fragment实例多 add 多次。

```
FragmentTransaction ft = fragmentManager.beginTransaction();
if (fragment.isAdded()) {
    ft.show(fragment);
} else {
    //如果同一个fragment实例被add多会闪退:Fragment already added
    ft.add(frameId, fragment);
}
ft.commitAllowingStateLoss();
```



### 在极端情况下哪怕判断了 isAdded 方法也会出现 Fragment already added 异常

可以使用 replace 方法代替 add 方法：

```
FragmentTransaction ft = fragmentManager.beginTransaction();
if (fragment.isAdded()) {
    ft.show(fragment);
} else {
    ft.replace(frameId, fragment);
}
ft.commitAllowingStateLoss();
```