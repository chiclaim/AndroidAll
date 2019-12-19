import kotlinx.coroutines.*

fun main() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // will get cancelled before it produces this result
    }

    println("Result is $result")
}
/*
I'm sleeping 0 ...
I'm sleeping 1 ...
I'm sleeping 2 ...
Result is null

 */