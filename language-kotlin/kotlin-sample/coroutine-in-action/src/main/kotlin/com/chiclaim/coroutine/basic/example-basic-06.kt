import kotlinx.coroutines.*

// thread和coroutine对比
fun main(){
//    testCoroutine()
    testThread()
}

private fun testCoroutine() = runBlocking {
    repeat(100_000) {
        // launch a lot of coroutines
        launch {
            delay(1000L)
            println(".")
        }
    }
}

private fun testThread(){
    for (i in 0..100_000) {
        Thread(Runnable {
            Thread.sleep(1000L)
            println(".")
        }).start()
    }

}


