



## 1. NDK相关术语

### 1.1 什么是 NDK

`NDK` 全称是 `Native Development Kit`，它是一套用于本地代码开发工具集，让开发者能够在 `Android` 应用中使用 `C/C++` 代码，并提供众多平台库。主要用于以下几种常见：

- 在平台之间移植其应用。
- 进一步提升设备性能，以降低延迟，或运行计算密集型应用，如游戏或物理模拟。
- 重复使用您自己或其他开发者的 C/C++ 库。


NDK开发工具集将 C/C++ 代码编译成 `.so`（share object 共享库）文件，然后打包进 APK 文件

### 1.2 什么是 ABIs

不同的 Android 设备使用不同的 CPU，不同的 CPU 支持不同的指令集。CPU 与指令集的每种组合都有专属的应用二进制接口，即 `ABI`(Android Binary Interace)。ABI 可以非常精确地定义应用的机器代码在运行时如何与系统交互。您必须为应用要使用的每个 CPU 架构指定 ABI。（来自Android官方）


所以 NDK 需要为不同的 ABI 生成对应的 `.so` 文件，以便我们的本地代码能够正常运行在不同的 Android 设备上，由于 Android 设备绝大部分都是 ARM 架构的，所以一般只需要提供这一个 ABI 的 `.so` 文件即可。下面是 NDK 支持的 ABI：

![NDK支持的ABI有](https://img-blog.csdnimg.cn/20190920193540252.jpg)



**通过命令查看 APK 支持的 ABI：**

```
aapt dump badging xxx.apk | grep abi

//查看支付宝APK支持的 ABI
$ aapt dump badging alipay.apk | grep abi
native-code: 'armeabi'

```

也可以将 APK 拖到 AndroidStudio 查看 lib 文件夹：

![alipay支持的ABI](https://img-blog.csdnimg.cn/20190921165517862.png)


**在gradle中配置特定的ABI打进APK**

上面我们查看到 `支付宝APK` 只支持 ABI 为 `armeabi` 的设备，armeabi 文件夹下的 `so` 文件有 `36.9M`。如果为每个 ABI 都提供相应的 `.so` 库，那么 APK包的体积 就会变得非常庞大，可以在 gradle 中指定兼容的 ABI 哪怕程序中还存在其他 ABI 对应的 `.so` 文件，也不会将其打包进 APK：

```
defaultConfig {
    ndk {
        // 指定ndk需要兼容的ABI
        // x86,armeabi-v7a等ABI的so不会打包进APK 
        abiFilters 'armeabi'
    }
}
```

> 微信APK 只支持 ABI 为 armeabi-v7a 的设备，armeabi-v7a 是支持 armeabi，市面上的主流的 Android 设备基本上都是 arm 架构的，所以支持这一个 ABI 即可。


**通过命令查看 Android系统 支持的 ABI：**

```
getprop | grep abilist
```

例如查看 `华为Mate20` 支持的 `ABI`：

![华为Mate20](https://img-blog.csdnimg.cn/20190921171339420.jpg)

也可以通过 `API` 来获取系统支持的 `ABI`：

```
// 同样查看 华为Mate20 支持的ABI

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    String[] supportedAIs = Build.SUPPORTED_ABIS;
}

// 输出结果
arm64-v8a
armeabi-v7a
armeabi
```


### 1.3 JNI 和 NDK 的区别

JNI 是 Java Native Interface 的缩写，它允许运行在 JVM 虚拟上的 `Java` 代码和 `C/C++` 或汇编进行交互

JNI 定义了 Java 代码和 Native 代码的交互细节，本文后面会介绍 JNI 一些常用的规范

Android 主要是 Java 编写的，当然 Kotlin 也可以编写 Android 程序，Kotlin 最终编译后也是 Java 字节码，所以他们都是基于 JVM 的语言。

所以 JNI 和 NDK 的区别是：JNI 是定义 Java 代码和 Native 代码的交互规范，NDK 是一个工具集，主要用于编译 Native 代码，生成 ABI 对应的 `.so` 文件，并将其打包进 APK 文件中


## 2. 第一个NDK程序

`AndroidStudio3.5` 版本使得开发 NDK 更加方面了，我们需要在 AndroidStudio 中先安装一下组件：

- **NDK** 

    这套工具使开发中能在 `Android` 应用中使用 `C/C++` 代码

- **CMake**

    一款开源的，跨平台的，用于编译或测试的工具，可与 `Gradle` 搭配使用来编译原生库。如果在使用 `ndk-build` 编译，则不需要此组件。
    
- **LLDB**

    用于在 `AndroidStudio` 中调试 `Native` 代码

![AndroidStudio NDK](https://img-blog.csdnimg.cn/20190921161810835.png)

安装完毕之后，在 AndroidStudio 中新建一个 Native C++ 程序：

![第一步](https://img-blog.csdnimg.cn/20190921162406334.jpg)

![第二步](https://img-blog.csdnimg.cn/20190921164200346.jpg)

![第三步](https://img-blog.csdnimg.cn/20190921164258431.jpg)

运行结果如下图所示：

![第一个NDK程序](https://img-blog.csdnimg.cn/20190921164037635.jpg)


### 1.4 第一个 NDK 程序分析

下面我们来分析下这个 NDK 程序，先来看下 `MainActivity` 界面：

```
public class MainActivity extends AppCompatActivity {

    // 加载 'native-lib' library 
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }
    
    // 通过 native 关键字，映射 JNI 方法
    public native String stringFromJNI();
}

```

下面来看下 `native-lib.cpp` 中的 JNI 方法：

```
// jstring 就是 stringFromJNI 方法的返回类型
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    
    // NewStringUTF() 返回一个 jstring
    return env->NewStringUTF(hello.c_str());
}
```

上面的 JNI 方法非常长：`Java_com_chiclaim_androidnative_MainActivity_stringFromJNI`

它的命名规则为：`Java_类的全路径_方法名`

因为 `MainActivity` 中的 `native` 方法名为 `stringFromJNI` 所以它对应的 JNI 方法名为：

`Java_com_chiclaim_androidnative_MainActivity_stringFromJNI`

这个也不用去记，例如我们在 MainActivity 中创建一个新的 native 方法，可以通过 `alt + Enter` 快捷键帮我们自动生成对应的 JNI 方法。

这样就将 `Java` 方法和 `Native` 方法关联起来了，通过类的全路径来定位对应的方法在哪里。

我们发现上面的例子中 JNI 中的方法返回的是一个 `jstring`，而 Java 中对应的方法返回的是 String，也就是说 Java 中的 `String`，在 JNI 中使用 `jstring` 对应，关于 Java 和 JNI 数据类型对应关系，待会再介绍。

`native-lib.cpp` 文件所在的目录中还有一个名为 `CMakeLists.txt` 的文件，它是 CMake 脚本。例如程序中的 `native-lib.cpp`，程序怎么知道这就是一个需要编译的库呢？所以需要一个脚本来控制 CMake 的编译。我们来看下这个文件：

```
# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

```

上面的注释非常详尽，每个参数都有备注，由于是英文的，所以我在这里在做一下解释：

- **cmake_minimum_required**

    配置 cmake 兼容的最低版本

- **add_library**

    创建一个共享或者静态库，可以定义多个库，cmake 将会替我们编译这些库
    Gradle 会自动将这些共享库打包到 APK 中

- **find_library**

    寻找预编译好的库，将它的路径使用一个变量保存
    因为 Android 系统中已经存在了一些已经编译好的共享库了，我们自己开发库，可能需要使用这些已经存在的库

- **target_link_libraries**

    find_library 是用来定位预编译好的库，定位到了，还需要将其链接到目标库，这样目标库才能使用这个库

可能 **find_library** 和 **target_link_libraries**，不太好理解，我们仍然以上面的程序为例

```
// 这段代码意思就是找到 `log` 库，将 `log` 库的路径保存在 `log-lib` 变量里
find_library(
        log-lib
        log)

// 将 log 这个库链接到我们写的 native-lib
target_link_libraries(
        native-lib
        ${log-lib})
```

> log 库就是 Android 的日志库

经过这两个步骤我们就可以在 native-lib 这个 Native 代码中使用 Android 的日志库：

```
#include <jni.h>
#include <string>

// 添加日志头文件
#include <android/log.h>

#define TAG "AndroidNativeDemo"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    
    LOGD("------------the log from C++-------------------");
    
    return env->NewStringUTF(hello.c_str());
}
```

运行后，控制台成功输出了这行日志。日志格式和在 Android 中输出的一样，因为 Android 中的 Log，底层也是调用了 native 代码。

我们可以尝试将 target_link_libraries 注释，将 log 库链接到目标库这个步骤注释掉，程序则无法编译了。提示我们：

```
undefined reference to '__android_log_print'
```

最后，我们再来看下 `build.gradle` 文件：

```
android {

    // 省略其他配置...

    externalNativeBuild {
        cmake {
            // 指定 CMakeLists 路径
            path "src/main/cpp/CMakeLists.txt"
            // 设置 cmake 版本
            version "3.10.2"
        }
    }
}
```

`externalNativeBuild` 主要用来指定 cmake 的版本以及设置 cmake 脚本文件

> 同理，我们也可以按照上面分析的步骤，在一个已有的 Android 项目中添加 NDK 支持


## 3. JNI 数据类型与描述符

JNI 定义了 Java 代码和 Native 代码的交互规范，既然是两者之间的交互，那么 Java 中的一些概念在 JNI 中也有相应的概念与之对应，比如 数据类型和描述符。

我们知道 Java 有许多的数据类型，JNI 相应的数据类型与之对应，Java 有两种数据类型，一个是基本数据类型，另一个引用类型。

**基本数据类型对应表:**

|Java Type|Native Type|
|---|---|
|boolean|jboolean|
|byte|jbyte|
|char|jchar|
|short|jshort|
|int|jint|
|long|jlong|
|float|jfloat|
|double|jdouble|

**引用类型对应表:**

|Java Type|Native Type|
|---|---|
|Object|jobject|
|Class|jclass|
|String|jstring|
|Object[]|jobjectArray|
|boolean[]|jbooleanArray|
|byte[]|jbyteArray|
|char[]|jcharArray|
|short[]|jshortArray|
|int[]|jintArray|
|long[]|jlongArray|
|float[]|jfloatArray|
|double[]|jdoubleArray|
|void|void|

介绍完了数据类型，下面来看下 `描述符`，这个也是 `NDK` 开发中经常用到的

这里的描述符简单来说就是 `Java` 代码编译成 `class字节码` 对应的描述符

例如下面代码一段 Java 代码：

```
public class Person {
    private String name;

    public String getName(){
        return name;
    }
}
```

代码很简单，一个 `name` 属性和一个 `getName` 方法，他们分别对应的 `class字节码` 为：

```
public String name;
// 该属性对应的部分字节码：
  public java.lang.String name;
    descriptor: Ljava/lang/String;
    flags: ACC_PUBLIC


public String getName(){
    return name;
}
// 该方法对应的部分字节码：
  public java.lang.String getName();
    descriptor: ()Ljava/lang/String;
    flags: ACC_PUBLIC

```

`描述符` 就是字节码里 `descriptor` ，所以 `name` 属性的描述符为 `Ljava/lang/String`，`getName()` 方法的描述符为 `()Ljava/lang/String`，所以描述符忘记了，可以使用 `javap` 命令查看下。

描述符主要分为下面几种：

- 类的描述符

    类描述符就是一个类的完整路径，将包名中的 `.` 换成 `/`
    |例子|描述符|
    |---|---|
    |java.lang.Object|java/lang/Object|
    |java.lang.String|java/lang/String|

- 基本类型的描述符

    |Java Type|描述符|
    |---|---|
    |boolean| Z|
    |byte| B|
    |char |C|
    |short |S|
    |int| I|
    |long |J|
    |float |F|
    |double |D|

- 引用类型的描述符

    引用类型的描述符语法：`L+类描述符+;`，数组类型的描述符语有点不同，
    一维数组由一个`[`开头，二维数组由`[[`开头，以此类推。后面加上数组元素的描述符
    
    |例子|描述符|
    |---|---|
    |java.lang.Object|Ljava/lang/Object;|
    |java.lang.Object[]|[Ljava/lang/Object;|    
    |int[]|[I|
    |long[][]|[[J|

- 方法的描述符

    方法的描述符语法：`(parameterDescriptor)returnTypeDescriptor`
    如果方法没有返回值，`void` 使用 `V` 表示
    |例子|描述符|
    |---|---|
    |int add(int a,int b)|(II)I|
    |void operate()|()V|    
    |boolean equals(Object obj)|(Ljava/lang/Object;)Z|



## 4. 小结

至此，我们详细介绍了 NDK 开发中经常遇见的术语，如 ABI、JNI、NDK。还介绍了如何使用 AndroidStudio 开发一个 NDK 程序，然后分析了这个 NDK程序的代码以及相关配置文件，详细介绍了每个配置的含义是什么。最后介绍了 JNI 的规范中的数据类型和描述符，下一接我们开始介绍常用的 JNI 函数。



## 5. Reference

- [https://developer.android.com/ndk/guides](https://developer.android.com/ndk/guides)
- [https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/jniTOC.html](https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/jniTOC.html)
- [http://allenfeng.com/2016/11/06/what-you-should-know-about-android-abi-and-so/](http://allenfeng.com/2016/11/06/what-you-should-know-about-android-abi-and-so/)
