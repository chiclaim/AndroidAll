#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_chiclaim_androidnative_jni_JNIHolder_stringFromJNI2(
        JNIEnv* env,
        jclass) {
    std::string hello = "Hello from C++ static method...";
    return env->NewStringUTF(hello.c_str());
}

