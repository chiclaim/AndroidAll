package higher_order_function

import java.util.concurrent.locks.Lock

/**
 * Desc: 如果函数参数是lambda，通过inline修饰符来提高性能
 *
 * 在之前我们介绍到了lambda表达式一般会编译成内部类的形式， 意味着每使用一次lambda表达式都会产生一个额外的内部类
 *
 * 如果lambda表达式还使用了外面的变量，那么每次调用都会新建一个内部类对象，具体的可以查看：@see lambda/LambdaAsParameter
 *
 * 为了提高性能，避免上面提到的在使用lambda过程中产生的问题，通过inline修饰符来提高性能
 *
 * 在使用inline 函数的同时也会增加代码体积，我们可以把inline函数体的代码量通过抽取的方式使得inline函数变小
 *
 * Created by Chiclaim on 2018/10/8.
 */

//==============内联函数 starting==============================
//内联函数在被调用的时候直接直接把方法体的逻辑拷贝到调用者里
//需要注意两点：
// 1，内联函数的参数lambda不能用遍历保存；
// 2，内联函数的参数lambda只能传递给内联函数
//否则编译器会提示：Illegal usage of inline-parameter
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        //return wrap(action)
        return action()
    } finally {
        lock.unlock()
    }
}

/*inline*/ fun <T> wrap(action: () -> T): T {
    return action()
}

fun test(lock: Lock) {
    println("Before sync")
    synchronized(lock) {
        println("Action")
    }
    println("After sync")
}
//==============ending===============================


//==============非内联函数对比 starting=============================
//下面是非内联函数的调用，下面是通过内部类来实现的
fun <T> synchronized2(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

//内部类：higher_order_function/InlineFunctionKt$test2$1
fun test2(lock: Lock) {
    println("Before sync")
    synchronized2(lock) {
        println("Action")
    }
    println("After sync")
}

//内部类：higher_order_function/InlineFunctionKt$test3$1
fun test3(lock: Lock) {
    println("Before sync")
    synchronized2(lock) {
        println("Action")
    }
    println("After sync")
}
//通过分析生成的class字节码，每调用一次非内联的以lambda为参数的函数都会生成一个内部类
//上面我们在test2和test3方法里分别调用了一次非内联函数synchronized2，然后生成了两个内部类：
//内部类：higher_order_function/InlineFunctionKt$test2$1
//内部类：higher_order_function/InlineFunctionKt$test3$1

//==============ending=============================
