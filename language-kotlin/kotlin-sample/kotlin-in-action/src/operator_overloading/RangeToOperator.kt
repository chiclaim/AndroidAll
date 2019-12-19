package operator_overloading

import java.time.LocalDate

/**
 * Desc: ..操作符
 * Created by Chiclaim on 2018/9/29.
 */

//rangeTo  -> ..
//任何实现Comparable接口都可以使用 .. 操作符
//in Ranges.kt
//public operator fun <T : Comparable<T>> T.rangeTo(that: T): ClosedRange<T> = ComparableRange(this, that)
fun main(args: Array<String>) {
    val nowDate = LocalDate.now()
    val vacation = nowDate..nowDate.plusDays(10)

    //class kotlin.ranges.ComparableRange
    println(vacation.javaClass)

    println(nowDate.plusWeeks(1) in vacation)


    for (i in (0..10)){
        println(i)
    }


    val a = 0..10
    println(a.javaClass) //class kotlin.ranges.IntRange



    val personRange = Person("chiclaim",28)..Person("steve",56)
    println(personRange.javaClass) //class kotlin.ranges.ComparableRange

    val zucker = Person("zuckerberg",34)

    println(zucker in personRange)


}