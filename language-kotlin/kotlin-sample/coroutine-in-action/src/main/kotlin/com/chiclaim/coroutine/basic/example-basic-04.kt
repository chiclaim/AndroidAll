

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(200L)
        println("Task from runBlocking")
    }

    // Creates a new coroutine scope
    coroutineScope {

        //基于该scope的coroutine
        launch {
            delay(500L)
            println("Task from nested launch1")
        }

        //基于该scope的coroutine
        launch {
            delay(500L)
            println("Task from nested launch2")
        }
    
        delay(100L)
        //称之为scope域代码
        println("Task from coroutine scope") // This line will be printed before nested launch

    }
    
    println("Coroutine scope is over") // This line is not printed until nested launch completes
}
/*
Task from coroutine scope
Task from runBlocking
Task from nested launch
Coroutine scope is over

 */


/*
从example-basic-03s.kt的例子我们知道，如果内部的coroutine是基于外部的scope创建的，那么执行顺序为：
    1, 先执行外部的scope域代码
    2, 然后执行基于外部scope创建的coroutine代码

在本例子中是在example-basic-03s.kt的基础上，添加了 基于一个全新的scope创建coroutine，它的执行顺序为：
    1, 先执行基于全新scope域代码
    2, 然后执行基于外部scope创建的coroutine代码(如果有多个coroutine，则依次执行)
    3, 在然后执行基于全新scope的coroutine代码
    4, 最后执行外部scope域代码

 */