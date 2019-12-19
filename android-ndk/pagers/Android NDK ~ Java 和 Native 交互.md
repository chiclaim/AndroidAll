
### 前言

**本文主要内容：**

- 什么是 JNIEnv
- JNI 实例方法和静态方法
- Native 操作 Java 对象的属性
- Native 调用 Java 对象的方法
- Native 创建 Java 对象并返回
- Native 返回一个int数组
- Native 修改 Java 传递进来的数组
- 判断两个对象的地址是否相同
- JNI 与 垃圾回收

前面我们已经在[《Android NDK ~ 基础入门指南》](https://chiclaim.blog.csdn.net/article/details/101112607) 介绍了 NDK 开发的基础知识，今天我们开始介绍下 JNI 的常用函数

### 什么是 JNIEnv

在我们的第一个 NDK 程序中的 `stringFromJNI` 方法的第一个参数就是 `JNIEnv`

```
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
```

那么什么是 JNIEnv 呢？ 

`JNIEnv` 是一个与线程相关的，代表 JNI 环境的结构体。JNIEnv 里面封装了一些 JNI 系统函数，用于操作对象和调用函数。

`JNIEnv` 是线程相关的，所以不能跨线程使用，各自的线程只能使用各自的 JNIEnv。例如我们上面的 `stringFromJNI` 就可以直接使用它的第一个参数 JNIEnv，但有的时候我们不能直接拿到 JNIEnv，比如在 Native 层的后台线程收到了某个消息，需调用 Java 层的函数时，这个时候没有 JNIEnv，怎么办呢？我们可以使用 `JavaVM`，它是虚拟机在 JNI 层的代表，不管有多少个线程只有一个 JavaVM，通过 JavaVM 的 `AttachCurrentThread` 函数来获取当前线程的 JNIEnv 结构体，当后台线程退出时需要通过 JavaVM 的 `DetachCurrentThread` 函数来释放资源。

下面我们来看下 JNIEnv 常用的函数。

### JNI 实例方法

还是以我们的第一个 NDK 程序中的 `stringFromJNI` 方法为例：

```
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
```
该方法的第二个参数是 `jobject`，意思就是说该方法是一个实例方法，Java 层与之对应就是：

```
public native String stringFromJNI();
```


### JNI 实例静态方法

上面我们提到第二个参数是 jobject ，表明它是实例方法，那么我们将第二个参数改成 jclass 那么该方法就是静态方法了：

```
// Native 返回一个字符串（静态方法）
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI2(
        JNIEnv *env,
        jclass) {
    std::string hello = "Hello from C++ static method...";
    return env->NewStringUTF(hello.c_str());
}
```

对应 Java 层方法：

```
public native static String stringFromJNI2();
```

### Native 操作 Java 对象的属性

主要通过 `SetIntField` 和 `GetIntField` 方法操作对象的属性

```
// Native 设置/获取 Java 对象的属性
extern "C" JNIEXPORT jint JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_updateObjProperty(
        JNIEnv *env,
        jobject obj) {
        
    // 获取对象的 class
    jclass jclazz = env->GetObjectClass(obj);
    
    // 获取字段id
    jfieldID jfid = env->GetFieldID(jclazz, "number", "I");

    // 设置属性的值
    env->SetIntField(obj, jfid, COUNT);

    // 获取属性的值
    jint value = env->GetIntField(obj, jfid);

    env->DeleteLocalRef(jclazz);

    return value;
}
```


对应 Java 层方法：

```
public native int updateObjProperty();
```

> 如果对象的属性为private，Native 层也能操作

### Native 调用 Java 对象的方法

主要通过 Call[Type]Method 方法来调用 Java 对象实例方法，如果是静态方法则使用：CallStatic[Type]Method，其中的 type 就是方法的返回值类型：

```
// Native 调用 Java 对象的方法
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_invokeObjMethod(
        JNIEnv *env,
        jobject obj) {
    jclass jclazz = env->GetObjectClass(obj);
    jmethodID jmid = env->GetMethodID(jclazz, "methodForJNI", "(I)Ljava/lang/String;");
    env->DeleteLocalRef(jclazz);
    return (jstring) env->CallObjectMethod(obj, jmid, COUNT);
}
```

对应的 Java 层代码：

```
public native String invokeObjMethod();
```

> 注意：调用方法的时候需要注意参数是基本数据类型还是引用类型，如果参数为 Integer，不能传递 jint，JNI 是不会自动装箱的

### Native 创建 Java 对象并返回

创建对象主要是通过 `NewObject` 来实现：

```
// Native 创建 Java 对象并返回
extern "C" JNIEXPORT jobject JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_createObj(
        JNIEnv *env,
        jobject) {
    jclass jclazz = env->FindClass("com/chiclaim/androidnative/jni/User");
    jmethodID jmid = env->GetMethodID(jclazz, "<init>", "(Ljava/lang/String;)V");
    jstring username = env->NewStringUTF("Chiclaim");
    jobject user = env->NewObject(jclazz, jmid, username);
    env->DeleteLocalRef(jclazz);
    return user;
}
```

对应的 Java 层代码：

```
public native User createObj();
```

> 注意：构造方法的名字为 `<init>`

### Native 返回一个int数组

主要是通过 `NewIntArray` 函数创建数组，`SetIntArrayRegion` 函数为数组设置值：

```
const jint COUNT = 10;
// Native 返回一个int数组
extern "C" JNIEXPORT jintArray JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_getIntArray(
        JNIEnv *env,
        jobject) {
    jintArray _intArray = env->NewIntArray(COUNT);
    jint tmpArray[COUNT];
    for (jint i = 0; i < COUNT; i++) {
        tmpArray[i] = i;
    }
    env->SetIntArrayRegion(_intArray, 0, COUNT, tmpArray);
    return _intArray;
}
```

对应的 Java 代码：

```
public native int[] getIntArray();
```

### Native 修改 Java 传递进来的数组


通过 `GetIntArrayElements` 获取数组的值，然后修改响应的值，最后通过 `SetIntArrayRegion` 方法将元素重新设置到原来的数组中去：

```
const int VALUE = 100;
// Native 修改 Java 传递进来的数组
extern "C" JNIEXPORT void JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_updateIntArray(
        JNIEnv *env,
        jobject,
        jintArray intArray) {
    jboolean isCopy = static_cast<jboolean>(false);
    jint *arr = env->GetIntArrayElements(intArray, &isCopy);
    jint length = env->GetArrayLength(intArray);
    arr[0] = VALUE;

    env->SetIntArrayRegion(intArray, 0, length, arr);
    
    env->ReleaseIntArrayElements(intArray, arr, JNI_ABORT);
}
```

对应的 Java 代码：

```
public native void updateIntArray(int[] array);
```


### 判断两个对象的地址是否相同

判断两个对象的地址是否相同不能使用 `==`，要是用 `IsSameObject` 函数：

```
extern "C" JNIEXPORT jboolean JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_equals(
        JNIEnv *env, 
        jobject thiz, 
        jobject user1,
        jobject user2) {
    return env->IsSameObject(user1, user2);
}
```
对应的 Java 代码：

```
public native boolean equals(User user1,User user2);
```


### 垃圾回收

在 Java 中有两种数据类型，一个是基本数据类型(Primitive Type)，一种是引用类型。对于基本数据类型，Java 和 native code 之间是通过拷贝的方式进行传递，而引用类型传递的是引用。

Java 对象传递给 native code，虚拟机需要对该对象进行跟踪，以便将来被垃圾回收器回收掉。所以在 native code 中需要有一种方法通知虚拟机我不再需要这个对象了。

JNI 将对象引用分为两大类，一个是局部引用，一个是全局引用。

局部引用会在 Native 方法执行完毕后自动释放。传递给 Native 方法的参数就是局部引用，Native 方法返回的 Java 对象也属于局部引用。

而全局引用会一直保留着这个对象的引用，直到显式的调用 JNI 方法释放该引用。可以通过一个局部引用创建一个全局引用。JNI 通过 `NewGlobalRef` 和 `DeleteGlobalRef` 方法来创建全局引用和删除全局引用：

```
jobject holdObj = NULL;

extern "C" JNIEXPORT  void JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_holdUser(
        JNIEnv *env, jobject thiz, jobject user) {
    // 通过局部引用 user 创建全局引用
    holdObj = env->NewGlobalRef(user);
}
```

需要注意，不能将局部引用直接赋值给全局引用：

```
jobject holdObj = NULL;

extern "C" JNIEXPORT  void JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_holdUser(
        JNIEnv *env, jobject thiz, jobject user) {
    holdObj = user; 
}
```

否则虚拟机会报错：

```
JNI DETECTED ERROR IN APPLICATION: use of invalid jobject 0x7fefe45918
```

不用了这个全局引用需要删除它：

```
env->DeleteGlobalRef(holdObj);
```

既然局部引用 native 方法执行完毕后会自动释放，是不是就代码不用处理局部引用呢？大部分情况是这样的，但是如果遇到类似如下情况还是需要手动释放局部引用：

- native 方法需要访问一个大的 Java 对象，从而需要创建一个局部引用，在该方法里需要做一些计算任务，那么这个大对象不会被释放，哪怕计算任务不再需要这个对象了，因为 native 方法还没有还行完毕。这个时候在执行计算任务之前，可以通过 `DeleteLocalRef` 方法将大对象的引用删除。

- native 方法创建非常多的局部引用，但同一时间又不会使用这么多的引用，虚拟机为了跟踪这些局部引用需要花费很多空间，可能会导致内存溢出。例如，在 native 方法中遍历一个很大的对象数组，遍历的时候需要获取每个元素，每个元素就是一个局部引用，在遍历的时候，用完一个局部引用就应该删除它，因为遍历下一个元素时，上一个元素的引用就不需要了。


> 为了实现局部引用，虚拟机会创建一个“登记中心”用于控制 Java 对象过渡到 native 方法。这个“登记中心”会映射一个局部引用到Java对象，这样就可以阻止垃圾回收器将该 Java 对象。所有传递给 native 方法的 Java 对象，包括 native 方法返回的 Java 对象都会自动注册到“登记中心”，native 方法 return 后，“登记中心”也会自动移除该对象，以便被垃圾回收器回收它。“登记中心” 可以是 hash table 或 linked list。





### 小结

本文只介绍了最常用的一些 JNI 函数使用方法和注意事项，方便日后查阅和完善，更多的 JNI 函数可以查阅官方文档：[https://docs.oracle.com/javase/9/docs/specs/jni/index.html](https://docs.oracle.com/javase/9/docs/specs/jni/index.html)


### Reference 

- 《深入理解 Android 卷I》
- [https://docs.oracle.com/en/java/javase/11/docs/specs/jni/design.html#referencing-java-objects](https://docs.oracle.com/en/java/javase/11/docs/specs/jni/design.html#referencing-java-objects)
- [https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/jniTOC.html](https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/jniTOC.html)
- [https://blog.csdn.net/qinjuning/article/details/7607214](https://blog.csdn.net/qinjuning/article/details/7607214)
- [https://blog.csdn.net/shulianghan/article/details/38012515](https://blog.csdn.net/shulianghan/article/details/38012515)
