package collection

/**
 * Desc: Kotlin集合 为空性演示
 * Created by Chiclaim on 2018/9/18.
 */

fun main(args: Array<String>) {
    test2()
}

//演示 MutableList<Int?> 和 MutableList<Int>? 的区别
fun test() {
    val arr: MutableList<Int?> = mutableListOf()
    var arr2: MutableList<Int> = mutableListOf()

    arr.add(null) //legal
    //arr = null //illegal

    //arr2.add(null) //illegal

    var arr3: MutableList<Int>? = mutableListOf()
    arr3 = null //legal


}

//演示 Kotlin使用Java集合数据，可能产生的问题
fun test2() {
    //首先声明一个集合，且集合里的元素不能为空
    val arr1: MutableList<Int> = mutableListOf()
    //然后调用一个Java写的ListJava获取一个集合（集合元素可能为null），把该集合的里的元素一个个放进arr1
    ListJava.getList().forEach { value ->
        //编译时会在add插入前进行判断：Intrinsics.checkExpressionValueIsNotNull(value, "value");
        arr1.add(value)
    }
}
