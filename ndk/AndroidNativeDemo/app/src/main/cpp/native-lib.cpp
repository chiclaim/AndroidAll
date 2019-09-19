#include <jni.h>
#include <string>

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

// Native 设置/获取 Java 对象的属性
extern "C" JNIEXPORT jint JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_updateObjProperty(
        JNIEnv *env,
        jobject obj) {
    jclass jclazz = env->GetObjectClass(obj);
    jfieldID jfid = env->GetFieldID(jclazz, "number", "I");

    // 设置属性的值
    //jint newValue = COUNT;
    //env->SetObjectField(obj, jfid, (jobject) newValue);

    // 获取属性的值
    jint value = env->GetIntField(obj, jfid);

    env->DeleteLocalRef(jclazz);

    return value;
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
    jclass jclazz = env->FindClass("com/chiclaim/androidnative/jni.User");
    jmethodID jmid = env->GetMethodID(jclazz, "<init>", "(Ljava/lang/String;)V");
    jstring username = env->NewStringUTF("Chiclaim");
    jobject user = env->NewObject(jclazz, jmid, username);
    env->DeleteLocalRef(jclazz);

    // use 是否能被释放
    // env->DeleteLocalRef(user);
    return user;
}



