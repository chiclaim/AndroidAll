import kotlinx.coroutines.*

fun main() {
    //test1()
    test2()
}

fun test1() {
    runBlocking {
        //Global coroutines are like daemon threads
        //jvm进程关闭后，这个协程也会关闭
        //关于daemon thread 可以查看 ：DaemonThreadTest.java
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        println("delay 1300")
        delay(1300L) // just quit after delay
        println("after 1300")
    }
    /*
    delay 1300
    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    after 1300
     */
}

fun test2() {
    runBlocking {
        launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        println("delay 1300")
        delay(1300L) // just quit after delay
        println("after 1300")
    }
    /*

    delay 1300
    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    after 1300
    I'm sleeping 3 ...
    I'm sleeping 4 ...
    I'm sleeping 5 ...
    ...
    ...
    I'm sleeping 999 ...

     */
}

