



## adb 工具相关



1. 为了快速定位当前界面是哪个Activity，可以使用如下命令进行查看：

`adb shell dumpsys window | findStr mCurrentFocus`



2. 为了详细展示当前 App 的 stack 信息，检查是否有残留的 Activity 没有关闭，可以使用如下命令：

`adb shell dumpsys activity activities | findStr your_app_package | findStr Hist`



3. 为了查看某个库是哪个组件依赖进来的，我们可以通过打印项目的依赖树来分析：

`gradlew app:dependencies > dependencies.txt`



4. 做内存优化的时候，会根据不同观点当前内存处于什么等级，来做相应的处理，我们可以通过命令来模拟内存不同的状态，进而测试我们的代码：

```
格式：
adb shell am send-trim-memory [--user <USER_ID>] <PROCESS>
          [HIDDEN|RUNNING_MODERATE|BACKGROUND|RUNNING_LOW|MODERATE|RUNNING_CRITICAL|COMPLETE]
例如：
adb shell am send-trim-memory zmsoft.rest.phone RUNNING_LOW
```



5. 查看线程：

```
// 查看设备上所有的线程
adb shell ps -T
// 查看 UID 所有的线程（可以用于查看某个应用开启了多少个线程）：
adb shell ps -T | findStr $UID
```



6. 我们也可以通过命令来查看设备的一些信息，如序列号、分辨率、密度等：

- 设备的分辨率：`adb shell wm size`，如果多设备可以指定某个序列号，设置如：`adb -s 4d3bb8fe shell wm density`
- 设备的屏幕密度：`adb shell wm density`
- 设备序列号：`adb devices`



7. 还可以通过命令来设置朝向：

`adb shell wm set-user-rotation [free|lock]`



8. 不同的 Android 品牌截屏快捷键可能不一样，可以通过命令截屏：

`adb shell screencap /sdcard/screen.png`



9. Wi-Fi IP address

`adb shell ip addr show wlan0`



更多关于 adb 命令可以查看：[adb.md](adb.md)



## apk 编辑工具

[apk-editor-studio](https://github.com/kefir500/apk-editor-studio)





## 连接远程服务器



- [WinSCP](https://winscp.net/eng/download.php)：传输文件
- [PuTTY](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html)：远程连接





