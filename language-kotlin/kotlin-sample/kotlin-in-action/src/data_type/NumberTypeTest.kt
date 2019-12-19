package data_type

/**
 * Desc: 在Java中数据类型有基本数据类型和与其对应的包装类型
 *
 * 在Kotlin不用区分基本类型和包装类型，Kotlin重新定义了数据类型的关键字如：Char、Float、Int 等
 *
 * 但是在编译后也会把能编译成基本类型编译成基本类型
 *
 * 下面会列举出各种使用数据类型的情况，及其编译后对应是基本了类型还是包装类型
 *
 * 编译后是基本类型还是包装类型总结：
 *
 * 1，如果使用了泛型则编译后是包装类型，如集合泛型、数组泛型
 * 2，如果想要声明的数组编译是基本类型的数组，需要使用xxxArrayOf(...)，如intArrayOf
 * 2，如果变量可以为null(使用操作符`?`)，则编译后也是包装类型
 *
 * Created by Chiclaim on 2018/9/18.
 */


//为什么Kotlin不用Java里的基本数据类型，因为基本数据类型就无法调用一些有用的方法了
//下面使用Int就可以使用它的一些扩展方法，在编译后依然会编译成基本数据类型int
fun showProgress(progress: Int) {
    val percent = progress.coerceIn(0, 100)
    println("We're $percent% done!")
}


//会编译成基本类型int，同时会生成getter和setter方法
var width1: Int = 10

//会编译成Integer，同时会生成getter和setter方法
//因为可以为null，所以编译后不能是int基本类型
var width2: Int? = 10

//也会编译成Integer
val width3: Int? = 10

//会编译成一个Integer类型
var width4: Int? = null

//private static Object autoBox = Integer.valueOf(1);
var autoBox: Any = 1

//返回值Int编译后变成基本类型int
fun getAge(): Int {
    return 0
}

//返回值Int编译后变成Integer
fun getAge2(): Int? {
    return 0
}

//集合里的元素都是Integer类型
fun getAge3(): List<Int> {
    return listOf(22, 90, 50)
}

//会编译成一个Integer[]
fun getAge4(): Array<Int> {
    return arrayOf(170, 180, 190)
}

//会编译成一个int[]
fun getAge5(): IntArray {
    return intArrayOf(170, 180, 190)
}

fun main(args: Array<String>) {
    showProgress(111)

    //println(getAge() > getAge2())
    //println(getAge() > getAge2() ?: 0)
}

