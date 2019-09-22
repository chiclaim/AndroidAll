#include <jni.h>
#include <string>

#include <android/log.h>

#define TAG "AndroidNativeDemo" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型


// Native 返回一个字符串（实例方法）
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// Native 返回一个字符串（静态方法）
extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI2(
        JNIEnv *env,
        jclass) {
    std::string hello = "Hello from C++ static method...";
    return env->NewStringUTF(hello.c_str());
}

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

// Native 设置/获取 Java 对象的属性
extern "C" JNIEXPORT jint JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_updateObjProperty(
        JNIEnv *env,
        jobject obj) {
    jclass jclazz = env->GetObjectClass(obj);
    jfieldID jfid = env->GetFieldID(jclazz, "number", "I");


    // jclass jIntClass = env->FindClass("java/lang/Integer");
    // jint 不能赋值给 jobject
    // 注意区分，Java 中的 int 和 Integer，JNI 是不会自动装箱和拆箱的
    // valueOf 入参是 int ，返回值为 Integer
    // jmethodID valueOf = env->GetStaticMethodID(jIntClass, "valueOf", "(I)Ljava/lang/Integer;");
    // jobject v = env->CallStaticObjectMethod(jIntClass, valueOf, COUNT);

    // 设置属性的值
    env->SetIntField(obj, jfid, COUNT);

    // 获取属性的值
    jint value = env->GetIntField(obj, jfid);

    env->DeleteLocalRef(jclazz);

    return value;
}




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



extern "C" JNIEXPORT jboolean JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_equals(
        JNIEnv *env, jobject thiz, jobject user1,
        jobject user2) {
    return env->IsSameObject(user1, user2);
}