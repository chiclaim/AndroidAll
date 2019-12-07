# AndroidAll

内容涵盖绝大部分 Android 程序员所需要的技能：「设计模式」「Flutter」「ReactNative」「Kotlin」「RxJava」Dagger2」「Retrofit」「OkHttp」「ButterKnife」「JNI」「Android架构」「数据结构与算法」「自定义View」「性能优化」「Android源码分析」 等。

如果您有任何问题可以提 Issues ,本项目也欢迎各位开发者朋友来分享自己的一些想法和实践经验，欢迎 Pull Request。

- **计算机基础**
	- 协议
		- OSI模型、TCP/IP模型
		- HTTP / HTTP2 / HTTPS
	- 操作系统
		- Unix / Linux
		- Windows
	- 数据结构与算法

- **Java**
	- Java 基础
		- 面向对象思想
		- 类和接口
		- 注解与反射
		- 泛型
	- 多线程
		- 多线程通信
			- volatile / synchronized
			- await / notify / notifyAll
		- 并发库
			- Lock
			- Condition
			- Semaphore
			- CyclicBarrier
			- CountDownLatch
			- Exchanger
		- Java 并发编程
	- I / O
		- 字节流
			- InputStream
				- FileInputStream
				- FilterInputStream
					- BufferedInputStream
					- DataInputStream
					- PushbackInputStream
				- ByteArrayInputStream
				- ObjectInputStream
				- SequenceInputStream
				- PipedInputStream
			- OutputStream
				- FileOutputStream
				- FilterOutputStream
					- BufferedOutputStream
					- DataOutputStream
					- PrintStream
				- ByteArrayOutputStream
				- ObjectOutputStream
				- PipedOutputStream
		- 字符流
			- Reader
				- BufferedReader
				- InputStreamReader
					- FileReader
				- StringReader
				- PipedReader
				- CharArrayReader
				- FilterReader
					- PushbackReader
			- Writer
				- BufferedWriter
				- InputStreamWriter
					- FileWriter
				- PrintWriter
				- StringWriter
				- PipedWriter
				- CharArrayWriter
				- FilterWriter
	- 集合框架
		- List
			- ArrayList
			- LinkedList
			- Stack
			- Vector
			- TreeList
		- Set
			- HashSet
			- LinkedHashSet
			- TreeSet
		- Map
			- HashMap
			- LinkedHashMap
			- WeakHashMap
			- TreeMap
		- 并发包
			- ConcurrentHashMap
			- ConcurrentLinkedQueue
			- CopyOnWriteArrayList
			- CopyOnWriteArraySet
	- JVM 虚拟机
		- class 字节码
			- class 字节码的构成
			- class 字节码指令
			- 学习字节码对开发的指导意义
		- class 字节码执行
			- 方法调用
				- 方法调用指令
				- 方法重载解析
				- 动态分派
				- 动态类型语言的支持
			- 方法执行
				- 局部变量表
				- 操作数栈
				- 动态连接
				- 方法返回地址
			- 对象的创建
				- 对象的创建过程
				- 对象的内存布局
				- 对象的访问定位
		- 类加载
			- 类的加载时机
			- 类的加载过程
			- 类加载器
				- 双亲委派机制
				- 自定义类加载器
				- 类加载器死锁问题
				- Class.forName / ClassLoader.loadClass
		- JVM 内存区域
			- 程序计数器
			- Java 虚拟机栈
			- 本地方法栈
			- Java 堆
			- 方法区
			- 运行时常量池
		- JVM 内存模型
		- 垃圾回收
			- 什么样的对象可以被回收
			- 什么是 GC Root
			- 垃圾回收算法
				- 标记-清除算法
				- 复制算法
				- 标记整理算法
				- 分代收集算法
			- 常见的垃圾收集器
				- Serial 收集器
				- ParNew 收集器
				- Parallel Scavenge 收集器
				- Serial Old 收集器
				- CMS 收集器
				- Parallel Old 收集器
				- G1 收集器
				- ZGC
			- 垃圾回收相关的内存池
				- Eden Space	
				- Survivor Space	
				- Old Gen
				- Metaspace
				- Compressed Class Space
				- Code Cache	

- **Android 基础**
	- 四大组件
		- Activity
		- Service
		- ContentProvider
		- BroadcastReceiver
	- Activity 启动模式
		- standard
		- singleTop
		- singleTask
		- singleInstance
	- 常用的布局控件
		- RecyclerView
		- ConstraintLayout
		- WebView
		- ......
	- 数据存储
		- SharedPreferences
		- File
		- SQLite
		- Realm
	- 线程异步
		- Thread
		- AsyncTask
		- IntentService
		- 线程池
	- IPC
		- IPC 方式
			- Bundle
			- 文件共享
			- AIDL
			- Messager
			- ContentProvider
			- Socket
		- 框架
			- Hermes
			- HermesEventBus
	- 熟悉常用的开源框架

- UI
	- UI绘制流程及原理
	- 事件的传递机制
	- 自定义 View
	- 屏幕适配

- 开源库
	- RxJava
	- OkHttp
	- Retrofit
	- Router
	- EventBus
	- Glide
	- Dagger
	- LeakCanary
	- SQLite
	- ......

- **Android 虚拟机**
	- Dalvik
	- ART

- **架构**
	- MVC
	- MVP
	- MVVM
	- Clean
	- Jetpack
	- 设计模式
		- 6大设计原则
		- 模板模式
		- 观察者模式
		- 单例模式
		- 建造者模式
		- 工厂模式
		- 适配器模式
		- 装饰器模式
		- ......
	- 组件化
		- 如何处理组件之间的代码边界
		- 组件之间的 Router 路由
		- 控制反转和依赖注入
		- 如何管理拆分的 Module
	- 插件化
		- 发展历程
			- 2014 年
				- Dynamic-load-apk
			- 2015 年
				- OpenAltas
				- DroidPlugin
				- Small
			- 2016 年
				Zeus
			- 2017 年
				- Atlas
				- RePlugin
				- VirtualAPK
			- 2019 年
				- Qigsaw
				- Shadow
	- 热修复
		- Native Hook
			- Dexposed 
			- AndFix
			- HotFix
		- Java Multidex
			- QZone
			- QFix
			- Nuwa
			- RocooFix
		- Java Hook
			- Robust
			- Aceso
		- Dex Replace
			- Tinker
			- Amigo
		- 混合/优化(商业收费)
			- Sophix
	- 字节码技术
		- gradle plugin
		- 全局的 bug 修复
		- 日志统计

- **性能优化**
	- 安装包大小优化
	- 启动优化（冷启动、暖启动、热启动）
	- 耗电量优化
	- UI性能优化
		- XML 布局优化
		- 代码优化
	- 网络优化
	- 线上、线下全链路性能监控

- **NDK**
	- C/C++
	- AndroidStudio NDK 开发
	- FFmpeg

- **跨平台**
	- H5
	- Flutter
	- Weex
	- ReactNative



## 设计原则与架构

### 设计模式

- [设计模式 ~ 面向对象 6 大设计原则剖析与实战](https://chiclaim.blog.csdn.net/article/details/100566036)
- [设计模式 ~ 模板方法模式分析与实战](https://chiclaim.blog.csdn.net/article/details/100584000)
- [设计模式 ~ 观察者模式分析与实战](https://chiclaim.blog.csdn.net/article/details/100610201)
- [设计模式 ~ 单例模式分析与实战](https://chiclaim.blog.csdn.net/article/details/100639132)
- [设计模式 ~ 深入理解建造者模式与实战](https://chiclaim.blog.csdn.net/article/details/100679809)
- [设计模式 ~ 工厂模式剖析与实战](https://chiclaim.blog.csdn.net/article/details/100779270)
- [设计模式 ~ 适配器模式分析与实战](https://chiclaim.blog.csdn.net/article/details/100810001)
- [设计模式 ~ 装饰模式探究](https://chiclaim.blog.csdn.net/article/details/100850966)
- [设计模式 ~ 深入理解代理模式](https://chiclaim.blog.csdn.net/article/details/100901769)
- [设计模式 ~ 小结](https://chiclaim.blog.csdn.net/article/details/100931166)



### Android Architecture

- [Android MVP架构改造~如何重用顶层业务](https://chiclaim.blog.csdn.net/article/details/88050156)
- [二维火Android云收银模块化架构实践](https://chiclaim.blog.csdn.net/article/details/78346125)
- [Android架构—MVP架构在Android中的实践](https://chiclaim.blog.csdn.net/article/details/54783106)
- [Android-MVVM架构-Data Binding的使用](https://chiclaim.blog.csdn.net/article/details/50706329)


## 虚拟机

### JVM

- [深入理解 Java 虚拟机（一）~ class 字节码文件剖析](https://chiclaim.blog.csdn.net/article/details/101778619)
- [深入理解 Java 虚拟机（二）~ 类的加载过程剖析](https://chiclaim.blog.csdn.net/article/details/102177986)
- [深入理解 Java 虚拟机（三）~ class 字节码的执行过程剖析](https://chiclaim.blog.csdn.net/article/details/102508069)
- [深入理解 Java 虚拟机（四）~ 各种容易混淆的常量池](https://chiclaim.blog.csdn.net/article/details/102537682)
- [深入理解 Java 虚拟机（五）~ 对象的创建过程](https://chiclaim.blog.csdn.net/article/details/102573221)
- [深入理解 Java 虚拟机（六）~ Garbage Collection 剖析](https://chiclaim.blog.csdn.net/article/details/103229687)



## 数据结构与算法

- [数据结构与算法（十四）深入理解红黑树和JDK TreeMap和TreeSet源码分析](https://chiclaim.blog.csdn.net/article/details/81046088)
- [数据结构与算法（十三）平衡二叉树之AVL树](https://chiclaim.blog.csdn.net/article/details/80740418)
- [数据结构与算法（十二）并查集(Union Find)及时间复杂度分析](https://chiclaim.blog.csdn.net/article/details/80721436)
- [数据结构与算法（十一）Trie字典树](https://chiclaim.blog.csdn.net/article/details/80711441)
- [数据结构与算法（十）线段树(Segment Tree)入门](https://chiclaim.blog.csdn.net/article/details/80643017)
- [数据结构与算法（九）Set集合和BinarySearchTree的时间复杂度分析](https://chiclaim.blog.csdn.net/article/details/80628876)
- [数据结构与算法（八）二分搜索树(Binary Search Tree)](https://chiclaim.blog.csdn.net/article/details/80598727)
- [数据结构与算法（七）树和二叉树](https://chiclaim.blog.csdn.net/article/details/80574803)
- [数据结构与算法（六）二叉堆、优先队列和Java PriorityQueue](https://chiclaim.blog.csdn.net/article/details/80550279)
- [数据结构与算法（五）深入理解递归](https://chiclaim.blog.csdn.net/article/details/80536238)
- [数据结构与算法（四）队列和 Java ArrayDeque 源码剖析](https://chiclaim.blog.csdn.net/article/details/80456833)
- [数据结构与算法（三）栈和 Java Stack 源码分析](https://chiclaim.blog.csdn.net/article/details/80373290)
- [数据结构与算法（二）线性表之链式存储和LinkedList实现](https://chiclaim.blog.csdn.net/article/details/80351584)
- [数据结构与算法（一）线性表之顺序存储和 ArrayList、Vector 源码剖析](https://chiclaim.blog.csdn.net/article/details/80158343)


## Kotlin 

- [Kotlin 基础入门详解](https://chiclaim.blog.csdn.net/article/details/88624808)
- [Kotlin 操作符重载详解](https://chiclaim.blog.csdn.net/article/details/86706874)
- [从 Java 角度深入理解 Kotlin](https://chiclaim.blog.csdn.net/article/details/85575213)

## Android 框架

### RxJava 

- [RxJava create操作符的用法和源码分析](/rxjava/blogs/rxjava-create-source-analysis.md)
- [RxJava map操作符用法详解](http://blog.csdn.net/johnny901114/article/details/51531348)
- [RxJava flatMap操作符用法详解](http://blog.csdn.net/johnny901114/article/details/51532776)
- [RxJava concatMap操作符用法详解](http://blog.csdn.net/johnny901114/article/details/51533282)
- [RxJava onErrorResumeNext操作符实现app与服务器间token机制](http://blog.csdn.net/johnny901114/article/details/51533586)
- [RxJava retryWhen操作符实现错误重试机制](http://blog.csdn.net/johnny901114/article/details/51539708)
- [RxJava 使用debounce操作符优化app搜索功能](http://blog.csdn.net/johnny901114/article/details/51555203)
- [RxJava concat操作处理多数据源](http://blog.csdn.net/johnny901114/article/details/51568562)
- [RxJava zip操作符在Android中的实际使用场景](http://blog.csdn.net/johnny901114/article/details/51614927)
- [RxJava switchIfEmpty操作符实现Android检查本地缓存逻辑判断](http://blog.csdn.net/johnny901114/article/details/52585912)
- [RxJava defer操作符实现代码支持链式调用](http://blog.csdn.net/johnny901114/article/details/52597643)
- [combineLatest操作符的高级使用](http://blog.csdn.net/johnny901114/article/details/61191723)
- [RxJava导致Fragment Activity内存泄漏问题](http://blog.csdn.net/johnny901114/article/details/67640594)
- [interval、takeWhile操作符实现获取验证码功能](http://blog.csdn.net/johnny901114/article/details/79037306)
- [RxJava线程的自由切换](http://blog.csdn.net/johnny901114/article/details/80032801)


### Dagger2

- [Android Dagger2（二）源码分析-对象是如何被注入的](https://chiclaim.blog.csdn.net/article/details/58231038)
- [Android Dagger2（一） Dagger使用详解](https://chiclaim.blog.csdn.net/article/details/58225116)

### ButterKnife

- [Android开发之手把手教你写ButterKnife框架（三）](https://chiclaim.blog.csdn.net/article/details/52672188)
- [Android开发之手把手教你写ButterKnife框架（二）](https://chiclaim.blog.csdn.net/article/details/52664112)
- [Android开发之手把手教你写ButterKnife框架（一）](https://chiclaim.blog.csdn.net/article/details/52662376)

## Android WebView

- [Android WebView文件上传及自定义文件选择器](https://chiclaim.blog.csdn.net/article/details/50981391)

## 跨平台开发

### Flutter 

- [（一）Flutter学习之Dart变量和类型系统](https://chiclaim.blog.csdn.net/article/details/94364411)
- [（二）Flutter学习之Dart展开操作符 和 Control Flow Collections
](https://chiclaim.blog.csdn.net/article/details/94617048)
- [（三）Flutter学习之Dart函数](https://chiclaim.blog.csdn.net/article/details/94861914)
- [（四）Flutter学习之Dart操作符、控制流和异常处理](https://chiclaim.blog.csdn.net/article/details/95072693)
- [（五）Flutter学习之Dart面向对象](https://chiclaim.blog.csdn.net/article/details/96826144)
- [（六）Flutter学习之Dart异步操作详解](https://chiclaim.blog.csdn.net/article/details/97099388)
- [（七）Flutter学习之开发环境搭建](https://chiclaim.blog.csdn.net/article/details/97561397)
- [（八）Flutter 和 Native 之间的通信详解](https://chiclaim.blog.csdn.net/article/details/97621045)
- [（九）Android 项目集成 Flutter 模块](https://chiclaim.blog.csdn.net/article/details/100045683)
- [（十）Flutter FutureBuilder 优雅构建异步UI](https://chiclaim.blog.csdn.net/article/details/100063636)
- 更新中...



