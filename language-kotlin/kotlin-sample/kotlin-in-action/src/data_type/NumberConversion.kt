package data_type

/**
 * Desc: Java会自动类型转换，Kotlin大部分需要显式类型转换
 * Created by Chiclaim on 2018/9/27.
 */


fun main(args: Array<String>) {
    val age = 30
    //val longAge: Long = age
    //需要显式进行类型转换
    val longAge: Long = age.toLong()

    //TODO 为什么类型转换函数没有方法体


    val longMoney: Long = 45000000
    //大范围类型转小范围类型
    val intMoney = longMoney.toInt()


    val x = 10
    //集合元素会自动转成最大数据类型：Long
    val list = listOf(10, 20, 30L)
    for (v in list) {
        println("${v.javaClass} $v")
        //Operator '==' cannot be applied to 'Int' and 'Long'
        //if (x == v) { }
    }

    //类型不兼容
    //println(x in list )
    println(x.toLong() in list)


    val y: Byte = 1
    //y 向上转成 Long
    val z = y + 1L
    println(z)

    val intValue = 10
    //传递参数的时Int变量不能当做参数传递给需要Long参数的方法
    //printLong(intValue)
    //字面量Int可以传递给需要Long参数的方法(自动类型转换)
    printLong(10)

}

fun printLong(value: Long) {
    println(value)
}