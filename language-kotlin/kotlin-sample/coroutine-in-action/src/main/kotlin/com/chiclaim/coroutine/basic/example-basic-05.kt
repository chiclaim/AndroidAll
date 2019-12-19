
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        /* 将代码抽取到doWorld方法中
        delay(1000L)
        println("World!")*/
        doWorld()
    }
    println("Hello,")
}

// this is your first suspending function
//因为delay是suspend方法，所以doWorld必须是suspend方法
//suspend方法必须执行在coroutine中
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}

/*

Hello,
World!


 */