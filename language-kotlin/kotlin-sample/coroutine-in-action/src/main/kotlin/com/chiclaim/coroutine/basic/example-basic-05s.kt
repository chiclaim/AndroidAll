

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    launchDoWorld()
    println("Hello,")
}

// this is your first suspending function
//基于全新的scope创建coroutine
//所以会先执行基于全新scope创建的coroutine代码
suspend fun launchDoWorld() = coroutineScope {
    launch {
        println("World!")
    }
}

/*

World!
Hello,


 */