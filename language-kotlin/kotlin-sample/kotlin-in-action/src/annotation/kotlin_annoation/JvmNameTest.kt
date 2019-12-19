
@file:JvmName("JavaClassName")

package annotation.kotlin_annoation


@JvmName("java_method_name")
fun javaMethodName(){
    println("java method name")
}


fun main() {
    //kotlin invoke
    javaMethodName()
}