
import kotlinx.coroutines.*


//把所有的代码都放到阻塞代码块里
// start main coroutine
fun main() = runBlocking<Unit> {
    GlobalScope.launch { // launch new coroutine in background and continue
        println("Before Coroutine delay....")
        delay(1000L)
        println("World!")

    }
    println("Hello,") // main coroutine continues here immediately
    delay(2000L)      // delaying for 2 seconds to keep JVM alive
}
