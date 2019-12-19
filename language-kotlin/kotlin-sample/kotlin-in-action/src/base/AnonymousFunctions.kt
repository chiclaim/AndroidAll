package base

fun main(args: Array<String>) {

    (fun(x: Int, y: Int): Int {
        val result = x + y
        println("sum:$result")
        return result
    })(1, 9)


}