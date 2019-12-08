# 最全的 Android 技术栈

内容涵盖绝大部分 Android 程序员所需要的技能：「[设计模式](#设计模式)」「[Flutter](#跨平台开发)」「[ReactNative](#跨平台开发)」「[Kotlin](#Kotlin)」「[RxJava](#RxJava)」「[Dagger2](#Dagger2)」「[Retrofit](#Android框架)」「[OkHttp](#Android框架)」「[ButterKnife](#ButterKnife)」「[Router](#NDK开发)」「[NDK](#NDK开发)」「[Android架构](#Android架构)」「[数据结构与算法](#数据结构与算法)」「自定义View」「[性能优化](#Android性能优化)」「Android源码分析」 「[经典书籍](#经典书籍)」等。

**我编写了一份详细的 Android 技术栈思维导图，由于 GitHub 图片国内展示不稳定，所以下面使用的树形结构目录。你可以点击查看详细的 [思维导图](https://img-blog.csdnimg.cn/20191208200439182.jpg)**

如果您有任何问题可以提 Issues ,本项目也欢迎各位开发者朋友来分享自己的一些想法和实践经验，欢迎 Pull Request。


- **[计算机基础](#计算机技术)**
	- 协议
		- OSI模型、TCP/IP模型
		- HTTP / HTTP2 / HTTPS
	- 操作系统
		- Unix / Linux
		- Windows
	- [数据结构与算法](#数据结构与算法)

- **[Java](#Java)**
	- Java 基础
		- 面向对象思想
		- 类和接口
		- 注解与反射
		- 泛型
	- [多线程](#Java多线程)
		- [多线程通信](#Java多线程)
			- [volatile / synchronized](https://blog.csdn.net/johnny901114/article/details/8695668)
			- [await / notify / notifyAll](https://blog.csdn.net/johnny901114/article/details/8695668)
		- [线程池](#Java多线程)
			- [线程池入门 Callable 和 Future](https://blog.csdn.net/johnny901114/article/details/8695693)
			- [线程池各参数的意义](https://blog.csdn.net/johnny901114/article/details/8332088)
		- [并发库](#Java多线程)
			- [Lock](https://blog.csdn.net/johnny901114/article/details/8695708)
			- [Condition](https://blog.csdn.net/johnny901114/article/details/8695708)
			- [Semaphore](https://blog.csdn.net/johnny901114/article/details/8695717)
			- [CyclicBarrier](https://blog.csdn.net/johnny901114/article/details/8695723)
			- [CountDownLatch](https://blog.csdn.net/johnny901114/article/details/8695726)
			- [Exchanger](https://blog.csdn.net/johnny901114/article/details/8696019)
			- [ArrayBlockingQueue](https://blog.csdn.net/johnny901114/article/details/8696026)
		- Java 并发编程
	- [I/O](#JavaIO)
		- [字节流](#JavaIO)
			- [InputStream](#JavaIO)
				- [FileInputStream](#JavaIO)
				- [FilterInputStream](#JavaIO)
					- [BufferedInputStream](#JavaIO)
					- [DataInputStream](#JavaIO)
					- [PushbackInputStream](#JavaIO)
				- [ByteArrayInputStream](#JavaIO)
				- [ObjectInputStream](#JavaIO)
				- [SequenceInputStream](#JavaIO)
				- [PipedInputStream](#JavaIO)
			- [OutputStream](#JavaIO)
				- [FileOutputStream](#JavaIO)
				- [FilterOutputStream](#JavaIO)
					- [BufferedOutputStream](#JavaIO)
					- [DataOutputStream](#JavaIO)
					- [PrintStream](#JavaIO)
				- [ByteArrayOutputStream](#JavaIO)
				- [ObjectOutputStream](#JavaIO)
				- [PipedOutputStream](#JavaIO)
		- [字符流](#JavaIO)
			- [Reader](#JavaIO)
				- [BufferedReader](#JavaIO)
				- [InputStreamReader](#JavaIO)
					- [FileReader](#JavaIO)
				- [StringReader](#JavaIO)
				- [PipedReader](#JavaIO)
				- [CharArrayReader](#JavaIO)
				- [FilterReader](#JavaIO)
					- [PushbackReader](#JavaIO)
			- [Writer](#JavaIO)
				- [BufferedWriter](#JavaIO)
				- [InputStreamWriter](#JavaIO)
					- [FileWriter](#JavaIO)
				- [PrintWriter](#JavaIO)
				- [StringWriter](#JavaIO)
				- [PipedWriter](#JavaIO)
				- [CharArrayWriter](#JavaIO)
				- [FilterWriter](#JavaIO)
	- [集合框架](#Java集合框架)
		- [List](#Java集合框架)
			- [ArrayList](https://blog.csdn.net/johnny901114/article/details/80158343)
			- [LinkedList](https://blog.csdn.net/johnny901114/article/details/80351584)
			- [Stack](https://blog.csdn.net/johnny901114/article/details/80373290)
			- [Vector](https://blog.csdn.net/johnny901114/article/details/80158343)
			- [TreeList](#Java集合框架)
		- [Set](#Java集合框架)
			- HashSet
			- LinkedHashSet
			- [TreeSet](https://blog.csdn.net/johnny901114/article/details/81046088)
		- [Map](#Java集合框架)
			- HashMap
			- LinkedHashMap
			- WeakHashMap
			- [TreeMap](https://blog.csdn.net/johnny901114/article/details/81046088)
		- [Queue](#Java集合框架)
		    - [PriorityQueue](https://chiclaim.blog.csdn.net/article/details/80550279)
		    - [ArrayDeque](https://blog.csdn.net/johnny901114/article/details/80456833)
		- [并发包](#Java集合框架)
			- ConcurrentHashMap
			- CopyOnWriteArrayList
			- CopyOnWriteArraySet
			- [ArrayBlockingQueue](https://blog.csdn.net/johnny901114/article/details/8696026)
			- LinkedBlockingDeque
			- LinkedBlockingQueue
			- ConcurrentLinkedQueue
			- DelayQueue
	- [JVM 虚拟机](#Java虚拟机)
		- [class 字节码](https://chiclaim.blog.csdn.net/article/details/101778619)
			- [class 字节码的构成](#Java虚拟机)
			- [class 字节码指令](#Java虚拟机)
			- [学习字节码对开发的指导意义](#Java虚拟机)
		- [class 字节码执行](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [方法调用](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [方法调用指令](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [方法重载解析](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [动态分派](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [动态类型语言的支持](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [方法执行](#Java虚拟机)
				- [局部变量表](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [操作数栈](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [动态连接](https://chiclaim.blog.csdn.net/article/details/102508069)
				- [方法返回地址](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [对象的创建](https://chiclaim.blog.csdn.net/article/details/102573221)
				- [对象的创建过程](https://chiclaim.blog.csdn.net/article/details/102573221)
				- [对象的内存布局](https://chiclaim.blog.csdn.net/article/details/102573221)
				- [对象的访问定位](https://chiclaim.blog.csdn.net/article/details/102573221)
		- [类加载](https://chiclaim.blog.csdn.net/article/details/102177986)
			- [类的加载时机](https://chiclaim.blog.csdn.net/article/details/102177986)
			- [类的加载过程](https://chiclaim.blog.csdn.net/article/details/102177986)
			- [类加载器](https://chiclaim.blog.csdn.net/article/details/102177986)
				- [双亲委派机制](https://chiclaim.blog.csdn.net/article/details/102177986)
				- [自定义类加载器](https://chiclaim.blog.csdn.net/article/details/102177986)
				- [类加载器死锁问题](https://chiclaim.blog.csdn.net/article/details/102177986)
				- [Class.forName VS ClassLoader.loadClass](https://chiclaim.blog.csdn.net/article/details/102177986)
		- [JVM 内存区域](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [程序计数器](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [Java 虚拟机栈](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [本地方法栈](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [Java 堆](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [方法区](https://chiclaim.blog.csdn.net/article/details/102508069)
			- [运行时常量池](https://chiclaim.blog.csdn.net/article/details/102508069)
		- [垃圾回收](https://blog.csdn.net/johnny901114/article/details/103229687)
			- [什么样的对象可以被回收](https://blog.csdn.net/johnny901114/article/details/103229687)
			- [什么是 GC Root](https://blog.csdn.net/johnny901114/article/details/103229687)
			- [垃圾回收算法](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [标记-清除算法](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [复制算法](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [标记整理算法](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [分代收集算法](https://blog.csdn.net/johnny901114/article/details/103229687)
			- [常见的垃圾收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Serial 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [ParNew 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Parallel Scavenge 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Serial Old 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [CMS 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Parallel Old 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [G1 收集器](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [ZGC](https://blog.csdn.net/johnny901114/article/details/103229687)
			- [垃圾回收相关的内存池](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Eden Space](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Survivor Space](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Old Gen](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Metaspace](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Compressed Class Space](https://blog.csdn.net/johnny901114/article/details/103229687)
				- [Code Cache](https://blog.csdn.net/johnny901114/article/details/103229687)
		- JVM 内存模型

- **[Kotlin](#Kotlin)**
	- [Kotlin数据类型、访问修饰符](#Kotlin)
	- [Kotlin 中的 Class 和 Interface](#Kotlin)
	- [Lambda 表达式](#Kotlin)
		- [定义 Lambda 表达式](#Kotlin)
		- [Member Reference](#Kotlin)
		- [常用函数 let、with、run、apply 分析](#Kotlin)
		- [Lambda 原理分析](#Kotlin)
	- [高阶函数](#Kotlin)
		- [高阶函数的定义](#Kotlin)
		- [高阶函数的原理分析](#Kotlin)
		- [高阶函数的优化](#Kotlin)
	- [Kotlin 泛型](#Kotlin)
		- [Java 泛型：不变、协变、逆变](#Kotlin)
		- [Kotlin 中的协变、逆变](#Kotlin)
		- [Kotlin 泛型擦除和具体化](#Kotlin)
	- [Kotlin 集合](#Kotlin)
		- [Kotlin 集合创建方式有哪些](#Kotlin)
		- [Kotlin 集合的常用的函数](#Kotlin)
		- [Kotlin 集合 Sequence 原理](#Kotlin)
	- [Kolint 操作符重载](#Kotlin)
		- [算术操作符重载](#Kotlin)
		- [比较操作符重载](#Kotlin)
		- [[集合相关的操作符重载](#Kotlin)
		- [区间操作符重载](#Kotlin)
		- [解构操作符重载](#Kotlin)
		- [属性委托](#Kotlin)
	- [Koltin 和 Java 交互的一些问题](#Kotlin)
	

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

- [开源库](#Android框架)
	- [RxJava](#Android框架)
	- OkHttp
	- Retrofit
	- [Router](#Android框架)
	- EventBus
	- Glide
	- [Dagger](#Android框架)
	- LeakCanary
	- SQLite
	- ......

- **Android 虚拟机**
	- Dalvik
	- ART

- **[架构](#Android架构)**
	- [程序架构](#Android架构)
		- MVC
		- [MVP](#Android架构)
		- [MVVM](#Android架构)
		- Clean
		- Jetpack
	- [设计模式](#设计模式)
		- [6大设计原则](https://blog.csdn.net/johnny901114/article/details/100566036)
		- [模板模式](https://chiclaim.blog.csdn.net/article/details/100584000)
		- [观察者模式](https://chiclaim.blog.csdn.net/article/details/100610201)
		- [单例模式](https://chiclaim.blog.csdn.net/article/details/100639132)
		- [建造者模式](https://chiclaim.blog.csdn.net/article/details/100679809)
		- [工厂模式](https://chiclaim.blog.csdn.net/article/details/100779270)
		- [适配器模式](https://chiclaim.blog.csdn.net/article/details/100810001)
		- [装饰器模式](https://chiclaim.blog.csdn.net/article/details/100850966)
		- [代理模式](https://chiclaim.blog.csdn.net/article/details/100901769)
		- ......
	- [组件化](https://blog.csdn.net/johnny901114/article/details/78346125)
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
				- Zeus
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

- **[NDK](#NDK开发)**
	- C/C++
	- [AndroidStudio NDK 开发](#NDK开发)
	- FFmpeg

- **[跨平台](#跨平台开发)**
	- H5
	- [Flutter](#Flutter)
	- Weex
	- ReactNative


---

## 计算机技术


### 数据结构与算法

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


## Java

### JavaIO

- [Java I/O 流操作（一）System Properties Runtime 类](https://blog.csdn.net/johnny901114/article/details/8710381)
- [Java I/O 流操作（二）字节流与缓冲流](https://blog.csdn.net/johnny901114/article/details/8710403)
- [Java I/O 流操作（三）File 文件操作、PrintWriter、SequenceInputStream](https://blog.csdn.net/johnny901114/article/details/8710433)
- [Java I/O 流操作（四）对象的序列化](https://blog.csdn.net/johnny901114/article/details/8710341)

### Java多线程
- [Java 多线程（一）线程间的互斥和同步通信](https://blog.csdn.net/johnny901114/article/details/8695668)
- [Java 多线程（二）同步线程分组问题](https://blog.csdn.net/johnny901114/article/details/7854666)
- [Java 多线程（三）线程池入门 Callable 和 Future](https://blog.csdn.net/johnny901114/article/details/8695693)
- [Java 多线程（四）ThreadPoolExecutor 线程池各参数的意义](https://blog.csdn.net/johnny901114/article/details/8332088)
- [Java 多线程（五）Lock 和 Condition 实现线程同步通信](https://blog.csdn.net/johnny901114/article/details/8695708)
- [Java 多线程（六）Semaphore 实现信号灯](https://blog.csdn.net/johnny901114/article/details/8695717)
- [Java 多线程（七）CyclicBarrier 同步的工具类](https://blog.csdn.net/johnny901114/article/details/8695723)
- [Java 多线程（八）CountDownLatch 同步工具类](https://blog.csdn.net/johnny901114/article/details/8695726)
- [Java 多线程（九）Exchanger 同步工具类](https://blog.csdn.net/johnny901114/article/details/8696019)
- [Java 多线程（十）ArrayBlockingQueue 阻塞队列](https://blog.csdn.net/johnny901114/article/details/8696026)
- [Java 多线程（十一）JDK 同步集合](https://blog.csdn.net/johnny901114/article/details/8696032)

### Java集合框架

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
- [ArrayBlockingQueue 阻塞队列](https://blog.csdn.net/johnny901114/article/details/8696026)

### Java虚拟机

- [深入理解 Java 虚拟机（一）~ class 字节码文件剖析](https://chiclaim.blog.csdn.net/article/details/101778619)
- [深入理解 Java 虚拟机（二）~ 类的加载过程剖析](https://chiclaim.blog.csdn.net/article/details/102177986)
- [深入理解 Java 虚拟机（三）~ class 字节码的执行过程剖析](https://chiclaim.blog.csdn.net/article/details/102508069)
- [深入理解 Java 虚拟机（四）~ 各种容易混淆的常量池](https://chiclaim.blog.csdn.net/article/details/102537682)
- [深入理解 Java 虚拟机（五）~ 对象的创建过程](https://chiclaim.blog.csdn.net/article/details/102573221)
- [深入理解 Java 虚拟机（六）~ Garbage Collection 剖析](https://chiclaim.blog.csdn.net/article/details/103229687)


## Kotlin 

- [Kotlin 基础入门详解](https://chiclaim.blog.csdn.net/article/details/88624808)
- [Kotlin 操作符重载详解](https://chiclaim.blog.csdn.net/article/details/86706874)
- [从 Java 角度深入理解 Kotlin](https://chiclaim.blog.csdn.net/article/details/85575213)


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



### Android架构

- [Android MVP架构改造~如何重用顶层业务](https://chiclaim.blog.csdn.net/article/details/88050156)
- [二维火Android云收银模块化架构实践](https://chiclaim.blog.csdn.net/article/details/78346125)
- [Android架构—MVP架构在Android中的实践](https://chiclaim.blog.csdn.net/article/details/54783106)
- [Android-MVVM架构-Data Binding的使用](https://chiclaim.blog.csdn.net/article/details/50706329)
- [使用 repo 管理 Android 组件化项目](https://blog.csdn.net/johnny901114/article/details/103387569)

### Android性能优化

- [Android 性能优化—内存篇](https://blog.csdn.net/johnny901114/article/details/54377370)

## NDK开发

- [Android NDK ~ 基础入门指南](https://blog.csdn.net/johnny901114/article/details/101112607)
- [Android NDK ~ Java 和 Native 交互](https://blog.csdn.net/johnny901114/article/details/101124117)

## Android框架

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


## 经典书籍

- [《Android开发艺术探索》](#经典书籍)
- [《Android源码设计模式》](#经典书籍)
- [《Android自定义控件开发入门与实战》](#经典书籍)
- [《Android软件安全与逆向分析》](#经典书籍)
- [《Android群英传》](#经典书籍)
- [《Java编程思想》](#经典书籍)
- [《Java核心技术：卷I基础知识》](#经典书籍)
- [《Java8实战》](#经典书籍)
- [《重构:改善既有代码的设计》](#经典书籍)
- [《Java并发编程实战》](#经典书籍)
- [《深入理解Java虚拟机》](#经典书籍)
- [《Effective Java 第二版》](#经典书籍)
- [《Java与模式》](#经典书籍)
- [《代码整洁之道》](#经典书籍)
- [《剑指Offer名企面试官精讲典型编程题》](#经典书籍)
- [《阿里巴巴-深入探索 Android 热修复技术原理》](#经典书籍)
- [《Kotlin In Action》](#经典书籍)
- [《HTTP权威指南》](#经典书籍)
- 陆续整理网盘开放中...




