
import kotlinx.coroutines.*

fun main() {
    // launch new coroutine in background and continue
    GlobalScope.launch {
        println("Before Coroutine delay....")
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main thread continues here immediately

    //底层也是通过while(true)来实现的
    runBlocking {     // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    } 
}
