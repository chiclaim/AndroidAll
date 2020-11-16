# Android 常见闪退

汇聚一些常见且容易出现的闪退错误，帮助大家在错误出现之前规避掉。不要等用户使用的过程中出现闪退，再来修复，防患于未然。下面汇聚的异常列表，也可以帮助我们编写相关插件，检测危险代码提供依据。希望大家一起来维护它。

## java.lang.IllegalStateException

|  错误描述   | 闪退原因  | 解决方案|
|  ----  | ----  | ----  |
| Not allowed to start service Intent { }: app is in background uid  | Android 8.0 + 不允许后台启动 Service | 可以使用 JobIntentService |
| Only fullscreen opaque activities can request orientation | 透明的Dialog 样式的Activity 在Android 8.0 版本的系统 不允许设置朝向 | 在代码中或者Manifest中去掉朝向相关的代码 |
| Fragment not attached to a context | Fragment页面已经关闭，但Fragment里的定时任务或网络请求还在运行，任务完成后进行UI操作（内存泄漏） | 通过架构层面解决内存泄漏问题，将View层和Model解耦；View 层发起的定时任务或网络请求页面关闭是要取消。 |
| Fragment already added | 相同的 Fragment 重复添加到 Activity | [Solution](pages/FragmentAlreadyAdd.md) |



## java.lang.NullPointException

| 错误描述                                                    | 闪退原因                         | 解决方案                                                     |
| ----------------------------------------------------------- | -------------------------------- | ------------------------------------------------------------ |
| Attempt to invoke virtual method on a null object reference | 使用空对象调用其成员方法或者属性 | 1，尽量使用 Kotin 能解决绝大部分的空指针；2，通过聚检测代码，如 findbugs、lint等工具。 |





