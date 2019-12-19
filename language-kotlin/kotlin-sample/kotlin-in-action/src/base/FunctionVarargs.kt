package base

import java.util.*

/*fun <T> listOf(vararg items: T): List<T> {
    return Arrays.asList(*items) // * spread operator
}*/

fun main(args: Array<String>) {
    //listOf(1, 2, 3, 4, 5)

    val intArr: Array<Int> = arrayOf(1, 2, 3, 4)

    Arrays.asList(0, *intArr).run {
        println("size = $size")
    }

    Arrays.asList(0, intArr).run {
        println("size = $size")
    }

}