package operator_overloading

/**
 * Desc: 索引操作符重载
 * Created by Chiclaim on 2018/9/29.
 */


operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

operator fun Point.get(propertyName: String): Int {
    return when (propertyName) {
        "x" -> x
        "y" -> y
        else ->
            throw IllegalArgumentException("Invalid propertyName $propertyName")
    }
}

operator fun Point.set(propertyName: String, value: Int) {
    when (propertyName) {
        "x" -> x = value
        "y" -> y = value
        else ->
            throw IllegalArgumentException("Invalid propertyName $propertyName")
    }
}

operator fun Point.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else ->
            throw IllegalArgumentException("Invalid property index $index")
    }
}

fun main(args: Array<String>) {
    val point = Point(100, 200)
    println("x=${point[0]}, y=${point[1]}")
    point["x"] = 500
    point[1] = 500
    println("x=${point["x"]}, y=${point["y"]}")


    val map = hashMapOf("chiclaim" to 28) //声明一个可变集合
    map["chiclaim"] = 18 //相当于 map.put("chiclaim", 18)
    println(map["chiclaim"])  //相当于 println(map.get("chiclaim"))
}

