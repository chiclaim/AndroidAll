package higher_order_function

/**
 * Desc: 高阶函数是以另一个函数作为参数或者返回值是一个函数
 * Created by Chiclaim on 2018/10/8.
 */

//高阶函数是能把lambda作为参数，或者把函数引用作为参数，或者返回一个函数

//======================声明一个函数类型 starting===============

//把lambda赋值给一个变量，编译器会推导出它是一个函数类型(function type)
val sum = { x: Int, y: Int ->
    x + y
}
//可以显式声明成函数类型(function type)
//格式：(函数参数) -> 函数返回值
val sum2: (Int, Int) -> Int = { x, y ->
    x + y
}
//以上 sum 和 sum2 是等价的，底层实现也是一样的
//======================ending=================================


//======================把函数当做参数 starting=================
fun process(x: Int, y: Int, operate: (Int, Int) -> Int) {
    println(operate(x, y))
}

fun String.filter(predicate: (Char) -> Boolean): String {
    val builder = StringBuilder()
    for (c in this) {
        if (predicate(c)) {
            builder.append(c)
        }
    }
    return builder.toString()
}
//====================ending=================================


//============ 高阶函数和Lambda starting==================================
fun Any.hashC(predicate: (Int) -> Boolean): Int {
    return if (predicate(this.hashCode())) hashCode() else 0
}

fun testHashC() {
    val o = Object()
    println(o.hashCode())
    //调用高阶函数的时候，如下面的HashC函数
    //下面lambda的it代表的是什么值？
    //it代表的是在hashC函数里调用predicate函数的参数(this.hashCode())值
    //所以当我们处理集合的时候使用的filter、foreach等高阶函数，lambda中的it含义都是由该高阶函数内部决定的
    val resultCode = o.hashC {
        println("it = $it")
        it % 2 == 0
    }
    println("resultCode = $resultCode")
}

//如果function type没有任何参数呢？那么在使用的时候，在使用的时候就没有it参数
fun Any.hashC2(predicate: () -> Boolean): Int {
    //predicate没有任何参数
    return if (predicate()) hashCode() else 0
}

fun testHashC2() {
    val o = Object()
    val resultCode = o.hashC2 {
        //----没有it参数----
        //println("it = $it")
        true
    }
    println("resultCode = $resultCode")
}

//下面在介绍一种类似apply高阶函数，function type也没有参数，但是把lambda当做参数传递的时候，依然会返回一个receiver的参数
//function type格式如下：T.() -> 返回值
//在使用lambda的时候，receiver参数就是这个T类型
fun Any.hashC3(predicate: Any.() -> Boolean): Int {
    //predicate没有任何参数
    return if (predicate()) hashCode() else 0
}

fun testHashC3() {
    val o = Object()
    //下面的this就是 ‘Any.()‘ 中的Any
    val resultCode = o.hashC3 {
        //----没有it参数，但是有this参数----
        println("this = $this")
        true
    }
    println("resultCode = $resultCode")
}

//下面也可以让传递lambda的时候it是其本身，但是和上面还是有区别的：
//一个是it:Any 我们称之为 lambda parameter
//一个是this:Any 我们称之为 lambda receiver
fun Any.hashC4(predicate: (Any) -> Boolean): Int {
    return if (predicate(this)) hashCode() else 0
}
fun testHashC4() {
    val o = Object()
    val resultCode = o.hashC4 {
        //----没有it参数，但是有this参数----
        println("this = $it")
        true
    }
    println("resultCode = $resultCode")
}
//====================ending=============================================


//============ 高阶函数null问题 starting===================
var canReturnNull: (Int, Int) -> Int? = { _, _ ->
    null
}
var funOrNull: ((Int, Int) -> Int)? = null

fun <T> Collection<T>.joinToString(separator: String = ", ",
                                   prefix: String = "", postfix: String = "",
                                   transform: ((T) -> String)? = null
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0)
            result.append(separator)
        val str = transform?.invoke(element)
                ?: element.toString()
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}
//============ ending====================================


//============ 函数的返回值 starting===================
enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}

//============ ending ================================

fun main(args: Array<String>) {
    val a = 11
    val b = 2
    process(a, b, sum)
    process(a, b, sum2)


    //把lambda当做参数进行传递
    process(a, b) { x, y ->
        x * y
    }

    val reuslt = "123abcd".filter {
        it in 'a'..'z'
    }
    println(reuslt)

    println(sum.javaClass.kotlin.simpleName)
    println(sum2.javaClass.kotlin.simpleName)

    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    println("Shipping costs ${calculator(Order(3))}")

}











