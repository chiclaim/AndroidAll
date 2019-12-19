
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // we're done sending
    }
    // here we print received values using `for` loop (until the channel is closed)
    for (y in channel) println(y)
    println("Done!")
}

/*

如果没有 channel.close() ，则会一直阻塞在取数据的方法。channel的操作符重载后面在介绍

1
4
9
16
25
Done!

 */