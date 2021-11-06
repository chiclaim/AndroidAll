

##  Package Manager(pm)

1. 帮助命令：

```
adb shell pm -h
```

2. 打印所有包名：

```
adb shell pm list packages
```

3. 打印所有禁用的包名（disable）：

```
adb shell pm list packages -d
```

4. 打印所有可用包名（enable）：

```
adb shell pm list packages -e
```

5. 打印系统所有包名：

```
adb shell pm list packages -s
```

6. 打印所有第三方包名：

```
adb shell pm list packages -3
```

7. 查看所有包名及对应的 `UID`：

```
adb shell pm list packages -U
```

> UID 是安装 APK 时，系统赋予的，是不变的，除非卸载重装；一个 pid 对应一个进程，每次打开时系统都会赋予不同的 pid，

8. 查看包名和 versionCode：

```
adb shell pm list packages --show-versioncode
```

9. 可以在命令后面加上要过滤的包名：

```
adb shell pm list packages -U $package
// output:
package:com.xxx.yy uid:10318
```





## Activity Manager(am)

帮助命令：

```
adb shell am -h
```

启动 activity：

```
adb shell am start $package/$entryActivity
// 还可以传递参数：
传递 String 参数可以使用 -e/es  
传递 int 参数可以使用 -ei
更多类型，可以通过 adb shell am -h 查看。
格式：
adb shell am start $package/$entryActivity -e keyName keyValue
String value = getIntent().getStringExtra($keyName)
```

模拟触发内存级别回调：
```
格式：
adb shell am send-trim-memory [--user <USER_ID>] <PROCESS>
          [HIDDEN|RUNNING_MODERATE|BACKGROUND|RUNNING_LOW|MODERATE|RUNNING_CRITICAL|COMPLETE]
例如：
adb shell am send-trim-memory zmsoft.rest.phone RUNNING_LOW
```



## Window Manager(wm)

帮助命令：

```
adb shell wm -h
```

设置是否锁定屏幕朝向：

```
adb shell wm set-user-rotation [free|lock]
```

设备的分辨率：

```
adb shell wm size
```

屏幕密度：

```
adb shell wm density
```



## Monkey

启动某个 App：

```
adb shell monkey -p $package -c android.intent.category.LAUNCHER 1
```



## dumpsys

1. 帮助文档：

```
adb shell dumpsys -h
```


2. 列出 dumpsys 支持的所有系统服务：

```
adb shell dumpsys -l 
```


3. 服务的帮助文本和可选项：

```
adb shell dumpsys $service -h
 // 如电池的服务帮助文本:
adb shell dumpsys battery -h
```

4. 常用的服务有：

- battery 电池信息：
	```
  adb shell dumpsys battery
  ```
- window
	```
	// 获取当前获取焦点的 window 名字
	adb shell dumpsys window | findStr mCurrentFocus
	```
	
- activity
	```
	// 获取某个 package 的 activity 栈：
	adb shell dumpsys activity activities | findStr $package | findStr Hist
	```
	
- package
	```
	查看某个包名的版本信息：
	adb shell dumpsys package $packName | findStr version
	```

- meminfo
	```
	// 帮助文档
	adb shell dumpsys meminfo -h
	// dump 内存信息
	adb shell dumpsys meminfo [pkg/pid] 
	
	// 会打印出内存信息、View数量、Activity对象数、使用到的数据库文件等等：
	
	Applications Memory Usage (in Kilobytes):
	Uptime: 2545251558 Realtime: 4365916078

	MEMINFO in pid 9285 [zmsoft.rest.phone]
	                   Pss  Private  Private  SwapPss      Rss     Heap     Heap     Heap
	                 Total    Dirty    Clean    Dirty    Total     Size    Alloc     Free
	                ------   ------   ------   ------   ------   ------   ------   ------
	  Native Heap    20353    13920     6364    21648    20812    61284    58434     2849
	  Dalvik Heap    10358     9680      228       73    12440    12763     6382     6381
	 Dalvik Other     8355     4320     1840      206    10800                           
	        Stack     1320     1268       52      676     1324                           
	       Ashmem      158      156        0        0      176                           
	      Gfx dev     1152     1152        0        0     1156                           
	    Other dev       44       28       16        0      412                           
	     .so mmap    20287      548    15320       22    45480                           
	    .jar mmap     1786        0      104        0    40832                           
	    .apk mmap    26327      152    24104        0    38632                           
	    .ttf mmap      129        0       56        0      480                           
	    .dex mmap    34704     1172    23144      800    54744                           
	    .oat mmap      163        0        0        0     2360                           
	    .art mmap     5116     3540      608     1975    11644                           
	   Other mmap     2555       36     1488        0     7860                           
	   EGL mtrack    37228    37228        0        0    37228                           
	    GL mtrack    11408    11408        0        0    11408                           
	      Unknown      984      176      788     4334     1112                           
	        TOTAL   212161    84784    74112    29734   212161    74047    64816     9230
	 
	 App Summary
	                       Pss(KB)                        Rss(KB)
	                        ------                         ------
	           Java Heap:    13828                          24084
	         Native Heap:    13920                          20812
	                Code:    64620                         186960
	               Stack:     1268                           1324
	            Graphics:    49788                          49792
	       Private Other:    15472
	              System:    53265
	             Unknown:                                   15928
	 
	           TOTAL PSS:   212161            TOTAL RSS:   298900       TOTAL SWAP PSS:    29734
	
	 Objects
	               Views:      351         ViewRootImpl:        4
	         AppContexts:       12           Activities:        4
	              Assets:       24        AssetManagers:        0
	       Local Binders:      101        Proxy Binders:       60
	       Parcel memory:       23         Parcel count:       99
	    Death Recipients:        8      OpenSSL Sockets:        4
	            WebViews:        0
	
	DATABASES
	      pgsz     dbsz   Lookaside(b)          cache  Dbname
	         4      400            109      120/72/15  /data/user/0/zmsoft.rest.phone/databases/bugly_db_
	         4       32             51       230/62/5  /data/user/0/zmsoft.rest.phone/databases/sensorsdata
	         4       76             61      6042/62/5  /data/user/0/zmsoft.rest.phone/databases/analysys.data
	
	```



## Process Status(ps)



1. 帮助命令：

   ```
   adb shell ps --help
   ```

2. 显示某个用户下的所有进程：

   ```
   // 安装 App 后，系统会分配一个 UID，给该 App，那么就可以统计某个 App 运行时启动的所有进程：
   adb shell ps -u u0_a318
   
   // 例如根据命令查询微信的 UID 为 u0_a227
   adb shell ps -u u0_a227
   
   在微信登陆界面，关于微信进程有：
   USER            PID   PPID     VSZ    RSS WCHAN            ADDR S NAME
   u0_a227        6899    803 7207372 148716 0                   0 S com.tencent.mm
   u0_a227        7490    803 18116368 85828 0                   0 S com.tencent.mm:push
   ```

3. 根据进程id查看进程：

   ```
   adb shell ps -p $pid
   ```

4. 根据父进程id查询子进程：

   ```
   adb shell ps -P $ppid
   ```

5. 查看线程：

   ```
   // 查看设备上所有的线程
   adb shell ps -T
   // 查看 UID 所有的线程（可以用于查看某个应用开启了多少个线程）：
   adb shell ps -T | findStr $UID
   ```

   

## 无线连接



```
// 将手机连上电脑 USB，然后执行如下命令
adb tcpip 5555
// 保证手机连上 WiFi
adb connect $phone_ip

```

