package collection

/**
 * Desc: Kotlin中创建集合演示
 * Created by Chiclaim on 2018/9/28.
 */

//Kotlin中分为只读集合和可修改的集合


fun readOnlyCollection() {
    val list = listOf("Java", "Kotlin", "Python")
    //listOf函数底层通过Java的Arrays$ArrayList来实现的 class java.util.Arrays$ArrayList
    //Arrays.asList(...)
    println(list.javaClass)
    println(list)

    val set = setOf("Sun", "Moon", "Earth")
    //class java.util.LinkedHashSet
    println(set.javaClass)
    println(set)
    set.size


    val map = mapOf("name" to "chiclaim", "gender" to "male", "address" to "hangzhou")
    //class java.util.LinkedHashMap
    println(map.javaClass)
    println(map)

    //在Kotlin中通过自定义了一套只读的集合接口：List、Set、Map，不会提供修改集合的方法
    //在编译后会转成Java的 List、Set、Map接口


}

fun modifyCollection() {
    //java.util.ArrayList
    val list = mutableListOf("chiclaim")
    list.add("johnny")
    println(list.javaClass)
    println(list)

    //java.util.LinkedHashSet
    val set = mutableSetOf("Java")
    set.add("Kotlin")
    println(set.javaClass)
    println(set)

    //java.util.LinkedHashMap
    val map = mutableMapOf("name" to "johnny")
    map["address"] = "hangzhou"
    println(map.javaClass)
    println(map)

    //对于可修改的结合，Kotlin定义一套接口：MutableList、MutableSet、MutableMap
    //在编译后会转成Java的 List、Set、Map接口


    //除了上面的 MutableXXX，还可以通过下面的方式来创建可修改结合,这个没用MutableXXX接口，直接使用Java的集合

    //java.util.ArrayList
    arrayListOf("chiclaim")

    //java.util.HashSet
    hashSetOf("chiclaim")
    //java.util.LinkedHashSet
    linkedSetOf("chiclaim")
    //java.util.TreeSet
    sortedSetOf("chiclaim")

    //java.util.HashMap
    hashMapOf("name" to "chiclaim")
    //java.util.LinkedHashMap
    linkedMapOf("name" to "chiclaim")
    //java.util.TreeHashMap
    sortedMapOf("name" to "chiclaim")

}

//Kotlin中只读集合被Java代码修改的问题
fun processWithJava() {
    //自定义一个只读集合
    val list = listOf(1, 2, 3, 4, 5, 6)
    //把集合传递给Java方法，只读集合可能就被修改了
    ListJava.updateList(list)
    println(list)
}


fun main(args: Array<String>) {
    readOnlyCollection()
    println()
    modifyCollection()
    println()
    processWithJava()
}











