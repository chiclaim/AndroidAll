package operator_overloading

/**
 * Desc: 比较操作符重载   ==、!=、>、<、>=、<=
 * Created by Chiclaim on 2018/9/28.
 */

//equals()      ==
//!equals()     !=
//compareTo()   >、<、>=、<=

fun main(args: Array<String>) {
    val p1 = Person("chiclaim", 20)
    val p2 = Person("pony", 20)
    println(p1 == p2) //p1.equals(p2)
    println(p1 > p2)  //p1.compareTo(p2) > 0
    println(p1 >= p2)  //p1.compareTo(p2) >= 0
    println(p1 < p2)  //p1.compareTo(p2) < 0
    println(p1 <= p2)  //p1.compareTo(p2) <= 0

}