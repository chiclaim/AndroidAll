package base

/**
 * Desc: also函数使用演示
 * Created by Chiclaim on 2018/10/17.
 */

fun main(args: Array<String>) {
    //public inline fun <T> T.also(block: (T) -> Unit): T
    val result = "chiclaim".also {
        println(it)
    }
    println(result)
}
