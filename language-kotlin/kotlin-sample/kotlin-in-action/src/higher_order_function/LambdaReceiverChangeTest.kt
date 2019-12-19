package higher_order_function

fun main() {

    val any = Any()
    println(any)
    // 一般来说 lambda receiver 是调用该高阶函数对象
    any.apply {
        println(this)
    }

    // lambda receiver 并不是 `any` 对象，lambda receiver 已经被改变(apply3)
    val result = any.apply2 {
        println(this)  // The obj of this  has changed
    }
    println(result)


}


fun Any.apply2(block: Any.() -> Unit): String {
    return apply3(Any(), block)
}

// change lambda receiver
fun <R> apply3(r: R, block: R.() -> Unit): String {
    block(r)
    return r.toString()
}