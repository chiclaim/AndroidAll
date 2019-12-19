import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch {
        println(this.coroutineContext)
        println("launch " + Thread.currentThread().name)
        //DefaultDispatcher-worker-1
        println("Before Coroutine delay....")

        // launch new coroutine in background and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)

        println("World!") // print after delay

        withContext(Dispatchers.Default){

        }
    }

    //当Coroutine delay的时候，执行main线程代码
    //需要的注意的是，这里的main thread的代码，比launch代码块的代码先执行
    println("Hello,") // main thread continues while coroutine is delayed

    //一定要比上面的delay时间要长，否则jvm关闭了，'World!' 字符串就没有机会执行
    Thread.sleep(1100L) // block main thread for 2 seconds to keep JVM alive
}

/*
Hello,
Before Coroutine delay....
World!



launch 函数调用后立马会return，所以会先输出 hello


 */



