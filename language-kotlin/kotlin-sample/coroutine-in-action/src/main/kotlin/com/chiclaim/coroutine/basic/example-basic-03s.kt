import kotlinx.coroutines.*

fun main() = runBlocking {
    println("----" + this)
    //基于runBlocking的Scope创建一个新的coroutine
    launch {
        println("----" + this)
        println("Before Coroutine delay....")
        //delay(1000L)
        println("World!")
    }
    println("Hello,")

    //1, coroutine后面的代码，要先于coroutine代码块里的代码先执行
    //2, 如果内部的coroutine是基于外部的coroutine的scope创建的，那么执行完外部的scope域代码，会自动执行内部的coroutine代码

}
/*
Hello,
Before Coroutine delay....
World!
 */