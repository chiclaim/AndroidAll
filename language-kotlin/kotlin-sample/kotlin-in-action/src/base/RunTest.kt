package base

/**
 * Desc: run函数使用，和LambdaWithReceiver.kt进行对比
 * Created by Chiclaim on 2018/10/17.
 */

//inline fun <R> run(block: () -> R): R
fun runTest() = run {
    println("====")
    "this is run return"
}


//lambda参数是this的，this就是lambda receiver
//lambda receiver
//inline fun <T, R> T.run(block: T.() -> R): R
fun runTest2() = StringBuilder().run {
    for (c in 'A'..'Z') {
        append(c)
    }
    append("\nNow I know the alphabet!")
}

fun main(args: Array<String>) {

    println(runTest())
    println(runTest2())

}

