package operator_overloading

/**
 * Desc: compound assignment operator 混合赋值操作符
 * Created by Chiclaim on 2018/9/28.
 */

//混合赋值操作符 函数和操作符的对应关系：
// plusAssign  -> +=
// minusAssign -> -=
// timesAssign -> *=
// divAssign   -> /=
// remAssign   -> %=


// compound assignment operator 首选保证备操作的属性是可以被修改的（mutable）
//如下面的x，y属性是var修饰的

operator fun Point.plusAssign(other: Point) {
    x += other.x
    y += other.y
}

operator fun Point.minusAssign(other: Point) {
    x -= other.x
    y -= other.y
}

operator fun Point.timesAssign(other: Point) {
    x *= other.x
    y *= other.y
}

operator fun Point.divAssign(other: Point) {
    x /= other.x
    y /= other.y
}

operator fun Point.remAssign(other: Point) {
    x %= other.x
    y %= other.y
}


fun main(args: Array<String>) {
    val p1 = Point(10, 30)
    val p2 = Point(20, 30)

    //没有定义plusAssign 则会调用 plus方法 相当于 p1 = p1 + p2
    p1 += p2
    println("plusAssign: $p1")

    p1 -= p2
    println("minusAssign: $p1")

    p1 *= p2
    println("timesAssign: $p1")

    p1 /= p2
    println("divAssign: $p1")

    p1 %= p2
    println("remAssign: $p1")


    //p1 = p1 + p2
    //p1 += p2


}
