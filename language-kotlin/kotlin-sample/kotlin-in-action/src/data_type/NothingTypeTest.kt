package data_type

/**
 * Desc: Nothing一般用于 Elvis 操作符
 * Created by Chiclaim on 2018/9/27.
 */

class Person(var name: String?)


//fail方法和fail2方法类似，只不过fail2返回Nothing类型

fun fail(message: String) {
    throw IllegalArgumentException(message)
}

fun fail2(message: String): Nothing {
    throw IllegalArgumentException(message)
}


fun test() {
    val p = Person(null)
    //为什么下面的表达式成立呢？虽然fail的没有写返回值，但是它的默认返回值Unit，所以表达式成立
    val name = p.name ?: fail("name is null")
    //编译器报错
    //println(name.length)
}

fun test2() {
    val p = Person(null)
    if (p.name == null) {
        fail2("name is null")
    }
    val name = p.name
    //编译器报错提示name是可空类型，虽然上面进行p.name判空
    //println(name.length)
}

fun test3() {
    val p = Person(null)
    //Nothing一般用于 Elvis 操作符
    val name = p.name ?: fail2("name is null")
    //编译器发现fail2返回Nothing，那么这个方法肯定不会正常退出的， 如果异常退出，name就不能顺利的赋值，所以编译认为name肯定是一个非空类型
    println(name.length)
}


fun main(args: Array<String>) {
    test()

    test2()

    test3()

}
