package operator_overloading

import java.math.BigDecimal

/**
 * Desc: 一元操作符 重载
 * Created by Chiclaim on 2018/9/28.
 */

//+a          unaryPlus
//-a          unaryMinus
//!a          not
//++a , a++   inc
//--a , a--   dec
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}


operator fun Point.unaryPlus(): Point {
    return Point(+x, +y)
}


operator fun Point.inc(): Point {
    return Point(x + 1, y + 1)
}

operator fun Point.dec(): Point {
    return Point(x - 1, y - 1)
}

operator fun BigDecimal.inc() = this + BigDecimal.ONE


fun main(args: Array<String>) {
    var p1 = Point(10, 30)
    println(+p1)
    println(-p1)
    println(p1++)
    println(++p1)

    var bd = BigDecimal.ZERO
    println(bd++)
    println(++bd)
}



