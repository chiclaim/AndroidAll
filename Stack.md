## 细化 Android 技术栈

```
├─ Computer Sience
│  ├─ Protocol
│  │  └─ OSI、TCP/IP Model
│  ├─ Operating System
│  │  ├─ Unix、Linux
│  │  └─ Windows
├─ Java
│  ├─ Java Basic
│  │  ├─ OOP
│  │  ├─ Class/Interface
│  │  ├─ Annotation/Reflection
│  │  └─ Generic
│  ├─ 多线程
│  │  ├─ 多线程通信
│  │  │  ├─ volatile/synchronized
│  │  │  └─ await/notify/notifyAll
│  │  ├─ Thread Pool
│  │  │  ├─ Callable/Future
│  │  │  └─ 线程池各参数的意义
│  │  ├─ 并发库
│  │  │  ├─ Lock
│  │  │  ├─ Condition
│  │  │  ├─ Semaphore
│  │  │  ├─ CyclicBarrier
│  │  │  ├─ CountDownLatch
│  │  │  ├─ Exchanger
│  │  │  └─ ArrayBlockingQueue
│  │  └─ Java 并发编程
│  ├─ I/O
│  │  ├─ 字节流
│  │  │  ├─ InputStream
│  │  │  │  ├─ FileInputStream
│  │  │  │  ├─ FilterInputStream
│  │  │  │  │  ├─ BufferedInputStream
│  │  │  │  │  ├─ DataInputStream
│  │  │  │  │  └─ PushbackInputStream
│  │  │  │  ├─ ByteArrayInputStream
│  │  │  │  ├─ ObjectInputStream
│  │  │  │  ├─ SequenceInputStream
│  │  │  │  └─ PipedInputStream
│  │  │  └─ OutputStream
│  │  │  │  ├─ FileOutputStream
│  │  │  │  ├─ FilterOutputStream
│  │  │  │  │  ├─ BufferedOutputStream
│  │  │  │  │  ├─ DataOutputStream
│  │  │  │  │  └─ PrintStream
│  │  │  │  ├─ ByteArrayOutputStream
│  │  │  │  ├─ ObjectOutputStream
│  │  │  │  └─ PipedOutputStream
│  │  ├─ 字符流
│  │  │  ├─ Reader
│  │  │  │  ├─ BufferedReader
│  │  │  │  ├─ InputStreamReader
│  │  │  │  │  └─ FileReader
│  │  │  │  ├─ StringReader
│  │  │  │  ├─ PipedReader
│  │  │  │  ├─ CharArrayReader
│  │  │  │  └─ FilterReader
│  │  │  │  │  └─ PushbackReader
│  │  │  ├─ Writer
│  │  │  │  ├─ BufferedWriter
│  │  │  │  ├─ InputStreamWriter
│  │  │  │  │  └─ FileWriter
│  │  │  │  ├─ PrintWriter
│  │  │  │  ├─ StringWriter
│  │  │  │  ├─ PipedWriter
│  │  │  │  ├─ CharArrayWriter
│  │  │  │  └─ FilterWriter
│  ├─ 集合框架
│  │  ├─ List
│  │  │  ├─ ArrayList
│  │  │  ├─ LinkedList
│  │  │  ├─ Stack
│  │  │  ├─ Vector
│  │  │  └─ TreeList
│  │  ├─ Set
│  │  │  ├─ HashSet
│  │  │  ├─ LinkedHashSet
│  │  │  └─ TreeSet
│  │  ├─ Map
│  │  │  ├─ HashMap
│  │  │  ├─ LinkedHashMap
│  │  │  ├─ WeakHashMap
│  │  │  └─ TreeMap
│  │  ├─ Queue
│  │  │  ├─ PriorityQueue
│  │  │  └─ ArrayDeque
│  │  ├─ 并发包
│  │  │  ├─ ConcurrentHashMap
│  │  │  ├─ CopyOnWriteArrayList
│  │  │  ├─ CopyOnWriteArraySet
│  │  │  ├─ ArrayBlockingQueue
│  │  │  ├─ LinkedBlockingDeque
│  │  │  ├─ LinkedBlockingQueue
│  │  │  ├─ ConcurrentLinkedQueue
│  │  │  └─ DelayQueue
│  ├─ JVM
│  │  ├─ class 字节码
│  │  │  ├─ class 字节码的构成
│  │  │  ├─ class 字节码指令
│  │  │  └─ 学习字节码对开发的指导意义
│  │  ├─ class 字节码的执行
│  │  │  ├─ 方法调用
│  │  │  │  ├─ 方法调用指令
│  │  │  │  ├─ 方法重载解析
│  │  │  │  ├─ 动态分派
│  │  │  │  └─ 动态类型语言的支持
│  │  │  ├─ 方法执行
│  │  │  │  ├─ 局部变量表
│  │  │  │  ├─ 操作数栈
│  │  │  │  ├─ 动态连接
│  │  │  │  └─ 方法返回地址
│  │  │  └─ 对象的创建
│  │  │  │  ├─ 对象的创建过程
│  │  │  │  ├─ 对象的内存布局
│  │  │  │  └─ 对象的访问定位
│  │  ├─ 类加载
│  │  │  ├─ 类的加载时机
│  │  │  ├─ 类的加载过程
│  │  │  └─ 类加载器
│  │  │  │  ├─ 双亲委派机制
│  │  │  │  ├─ 自定义类加载器
│  │  │  │  ├─ 类加载器死锁问题
│  │  │  │  └─ Class.forName VS ClassLoader.loadClass
│  │  ├─ JVM 内存区域
│  │  │  ├─ 程序计数器
│  │  │  ├─ Java 虚拟机栈
│  │  │  ├─ 本地方法栈
│  │  │  ├─ Java 堆
│  │  │  ├─ 方法区
│  │  │  └─ 运行时常量池
│  │  ├─ 垃圾回收
│  │  │  ├─ 什么样的对象可以被回收
│  │  │  ├─ 什么是 GC Root
│  │  │  ├─ 垃圾回收算法
│  │  │  │  ├─ 标记-清除算法
│  │  │  │  ├─ 复制算法
│  │  │  │  ├─ 标记整理算法
│  │  │  │  └─ 分代收集算法
│  │  │  ├─ 常见的垃圾收集器
│  │  │  │  ├─ Serial 收集器
│  │  │  │  ├─ ParNew 收集器
│  │  │  │  ├─ Parallel Scavenge 收集器
│  │  │  │  ├─ Serial Old 收集器
│  │  │  │  ├─ CMS 收集器
│  │  │  │  ├─ Parallel Old 收集器
│  │  │  │  ├─ G1 收集器
│  │  │  │  └─ ZGC
│  │  │  ├─ 垃圾回收相关的内存池
│  │  │  │  ├─ Eden Space
│  │  │  │  ├─ Survivor Space
│  │  │  │  ├─ Old Gen
│  │  │  │  ├─ Metaspace
│  │  │  │  ├─ Compressed Class Space
│  │  │  │  └─ Code Cache
│  │  │  └─ JVM 内存模型
├─ Kotlin
│  ├─ Kotlin数据类型、访问修饰符
│  ├─ Kotlin 中的 Class 和 Interface
│  ├─ Lambda 表达式
│  │  ├─ 定义 Lambda 表达式
│  │  ├─ Member Reference
│  │  ├─ 常用函数 let、with、run、apply 分析
│  │  └─ Lambda 原理分析
│  ├─ 高阶函数
│  │  ├─ 高阶函数的定义
│  │  ├─ 高阶函数的原理分析
│  │  └─ 高阶函数的优化
│  ├─ Kotlin 泛型
│  │  ├─ Java 泛型：不变、协变、逆变
│  │  ├─ Kotlin 中的协变、逆变
│  │  └─ Kotlin 泛型擦除和具体化
│  ├─ Kotlin 集合
│  │  ├─ Kotlin 集合创建方式有哪些
│  │  ├─ Kotlin 集合的常用的函数
│  │  └─ Kotlin 集合 Sequence 原理
│  ├─ Kolint 操作符重载
│  │  ├─ 算术操作符重载
│  │  ├─ 比较操作符重载
│  │  ├─ 集合相关的操作符重载
│  │  ├─ 区间操作符重载
│  │  ├─ 解构操作符重载
│  │  └─ 属性委托
│  └─ Koltin 和 Java 交互的一些问题
├─ Android
│  ├─ Android 基础
│  │  ├─ 四大组件
│  │  │  ├─ Activity
│  │  │  ├─ Service
│  │  │  ├─ ContentProvider
│  │  │  └─ BroadcastReceiver
│  │  ├─ Activity 启动模式
│  │  │  ├─ standard
│  │  │  ├─ singleTop
│  │  │  ├─ singleTask
│  │  │  └─ singleInstance
│  │  ├─ 数据存储
│  │  │  ├─ SharedPreferences
│  │  │  ├─ File
│  │  │  ├─ SQLite
│  │  │  └─ Realm
│  │  ├─ 线程异步
│  │  │  ├─ Thread
│  │  │  ├─ AsyncTask
│  │  │  ├─ IntentService
│  │  │  └─ 线程池
│  │  ├─ IPC
│  │  │  ├─ IPC 方式
│  │  │  │  ├─ Bundle
│  │  │  │  ├─ 文件共享
│  │  │  │  ├─ AIDL
│  │  │  │  ├─ Messager
│  │  │  │  ├─ ContentProvider
│  │  │  │  └─ Socket
│  │  │  └─ 框架
│  │  │  │  ├─ Hermes
│  │  │  │  └─ HermesEventBus
│  │  ├─ 熟悉常用的开源框架
│  │  │  │  ├─ Glide
│  │  │  │  ├─ Retrofit
│  │  │  │  ├─ OkHttp
│  │  │  │  ├─ Dagger2
│  │  │  │  ├─ Router
│  │  │  │  ├─ EventBus
│  │  │  │  ├─ LeakCanary
│  │  │  │  └─ RxJava
│  ├─ Android 进阶
│  │  ├─ UI 原理
│  │  │  ├─ UI绘制流程及原理
│  │  │  ├─ 事件的传递机制
│  │  │  ├─ 自定义 View
│  │  │  └─ 屏幕适配
│  │  ├─ 性能优化
│  │  │  ├─ 安装包大小优化
│  │  │  ├─ 启动优化（冷启动、暖启动、热启动）
│  │  │  ├─ 耗电量优化
│  │  │  ├─ UI性能优化
│  │  │  │  ├─ XML 布局优化
│  │  │  │  └─ 代码优化
│  │  │  ├─ 字节码技术
│  │  │  │  ├─ 全局的 bug 修复
│  │  │  │  └─ 日志打点统计
│  │  │  ├─ 网络优化
│  │  │  └─ 线上、线下全链路性能监控
│  │  ├─ UI 原理
│  │  │  ├─ UI绘制流程及原理
│  │  │  ├─ 事件的传递机制
│  │  │  ├─ 自定义 View
│  │  │  └─ 屏幕适配
│  │  ├─ Android架构
│  │  │  ├─ 程序架构
│  │  │  │  ├─ MVC
│  │  │  │  ├─ MVP
│  │  │  │  ├─ MVVM
│  │  │  │  ├─ Clean
│  │  │  │  └─ Jetpack
│  │  │  ├─ 设计模式
│  │  │  │  ├─ 6大设计原则
│  │  │  │  ├─ 模板模式
│  │  │  │  ├─ 观察者模式
│  │  │  │  ├─ 单例模式
│  │  │  │  ├─ 建造者模式
│  │  │  │  ├─ 工厂模式
│  │  │  │  ├─ 适配器模式
│  │  │  │  └─ 代理模式
│  │  │  ├─ 架构思想
│  │  │  │  ├─ 概念
│  │  │  │  │  ├─ IoC 控制反转
│  │  │  │  │  ├─ DI 依赖注入
│  │  │  │  │  └─ AOP 面向切面
│  │  │  │  ├─ 开源方案
│  │  │  │  │  ├─ ASM
│  │  │  │  │  ├─ Javasisit
│  │  │  │  │  └─ AspectJ
│  │  │  │  └─ 工具
│  │  │  │  │  ├─ APT
│  │  │  │  │  └─ Gradle plugin
│  │  │  ├─ 组件化
│  │  │  │  ├─ 如何处理组件之间的代码边界
│  │  │  │  ├─ 组件之间的 Router 路由
│  │  │  │  ├─ 控制反转和依赖注入
│  │  │  │  └─ 如何管理拆分的 Module
│  │  │  ├─ 插件化
│  │  │  │  ├─ 发展历程
│  │  │  │  │  ├─ 2014 年
│  │  │  │  │  │  └─ Dynamic-load-apk
│  │  │  │  │  ├─ 2015 年
│  │  │  │  │  │  ├─ OpenAltas
│  │  │  │  │  │  ├─ DroidPlugin
│  │  │  │  │  │  └─ Small
│  │  │  │  │  ├─ 2016 年
│  │  │  │  │  │  └─ Zeus
│  │  │  │  │  ├─ 2017 年
│  │  │  │  │  │  ├─ Atlas
│  │  │  │  │  │  ├─ RePlugin
│  │  │  │  │  │  └─ VirtualAPK
│  │  │  │  │  ├─ 2019 年
│  │  │  │  │  │  ├─ Qigsaw
│  │  │  │  │  │  └─ Shadow
│  │  │  ├─ 热修复
│  │  │  │  ├─ Native Hook
│  │  │  │  │  ├─ Dexposed
│  │  │  │  │  ├─ AndFix
│  │  │  │  │  └─ HotFix
│  │  │  │  ├─ Java Multidex
│  │  │  │  │  ├─ QZone
│  │  │  │  │  ├─ QFix
│  │  │  │  │  ├─ Nuwa
│  │  │  │  │  └─ RocooFix
│  │  │  │  ├─ Java Hook
│  │  │  │  │  ├─ Robust
│  │  │  │  │  └─ Aceso
│  │  │  │  ├─ Dex Replace
│  │  │  │  │  ├─ Tinker
│  │  │  │  │  └─ Amigo
│  │  │  │  ├─ 混合/优化(商业收费)
│  │  │  │  │  └─ Sophix
│  │  │  ├─ 知晓常用开源框架的实现原理
├─ Android 虚拟机
│  ├─ Dalvik
│  └─ ART
├─ NDK
│  ├─ C/C++
│  ├─ FFmpeg
│  └─ AndroidStudio NDK
├─ 跨平台
│  ├─ H5
│  ├─ ReactNative
│  ├─ Weex
│  └─ Flutter
├─ 团队
│  ├─ 如何提高整体的团队水平？
│  ├─ 如何提高提高团队内代码质量？
│  └─ 如何提高团队积极性？
```
