import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val channel = Channel<Int>()
    launch {
        // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
        for (x in 1..5) {
            println("---begin to send ..." + x * x)
            channel.send(x * x)
        }
    }
    // here we print five received integers:
    repeat(5) {
        println("---try to take...")
        println(channel.receive())
    }
    println("Done!")
}

/*

根据前面我们队coroutine的分析，会先执行scope域代码，然后执行coroutine代码

所以会先去receive，然后send

channel和BlockingQueue非常相像，使用suspend send代替put方法；suspend receive替代take方法

---try to take...
---begin to send ...1
---begin to send ...4
1
---try to take...
4
---try to take...
---begin to send ...9
---begin to send ...16
9
---try to take...
16
---try to take...
---begin to send ...25
25
Done!

 */