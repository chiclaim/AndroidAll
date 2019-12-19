package operator_overloading

/**
 * Desc: 解构操作符重载
 * Created by Chiclaim on 2018/9/30.
 */


operator fun Point.component1() = x
operator fun Point.component2() = y


fun getPoint(): Point {
    return Point(1, 2)
}


fun getMaxMin(): Pair<Int, Int> {
    //省略获取最大值最小值的逻辑...

    val min: Int = 5
    val max: Int = 10
    return Pair(min, max)

}

fun main(args: Array<String>) {
    val p1 = Point(10, 19)
    val (x, y) = p1
    println("x=$x , y=y$y")

    val (x1, y1) = getPoint()
    println("x=$x1 , y=y$y1")


    //解构操作符用于 集合遍历
    val map = hashMapOf("name" to "chiclaim", "address" to "hangzhou")
    for ((key, value) in map) {
        println("$key -> $value")
    }


    //集合最多支持声明5个变量
    val (v1, v2, v3, v4, v5) = listOf(1, 2, 3, 4, 5, 6)

    val (min, max) = getMaxMin()

}










