
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    //创建一个全局的coroutine
    // launch new coroutine and keep a reference to its Job
    val job = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // wait until child coroutine completes
//sampleEnd    
}
