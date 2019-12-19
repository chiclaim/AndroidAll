package collection

import java.math.BigDecimal

fun main() {
    println(trimPointIfZero(15.892343))
}

fun trimPointIfZero(d: Double): String {
    var d = d
    if (d - d.toInt() == 0.0) {
        return d.toInt().toString()
    }
    var bigDecimal = BigDecimal(d)
    bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)//设置精度
    d = bigDecimal.toDouble()
    println("---$d")
    if (d - d.toInt() == 0.0) {
        return d.toInt().toString()
    } else {
        return d.toString()
    }
}
