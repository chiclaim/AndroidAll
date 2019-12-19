package operator_overloading

/**
 * Desc: in操作符
 * Created by Chiclaim on 2018/9/29.
 */

// a in c                       -> c.contains(a)
// c in List/Array/CharSequence -> iterator()

data class Rectangle(val upperLeft: Point, val lowerRight: Point)


//until是一个中缀函数(infix)

//10 until 20 --> 10 to 19   开区间(open range)
//10..20      --> 10 to 20   闭区间(close range)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in (upperLeft.x until lowerRight.x) &&
            (p.y in upperLeft.y until lowerRight.y)
}

fun main(args: Array<String>) {
    val rect = Rectangle(Point(10, 10), Point(60, 60))
    println(Point(20, 20) in rect)
    println(Point(5, 20) in rect)


    val a = 'c' in "chiclaim"  //contains()
    println("----$a")

    for(value in "chiclaim"){//iterator()
        println(value)
    }

    val list = listOf(1,2,3)
    for (i in list){

    }
}