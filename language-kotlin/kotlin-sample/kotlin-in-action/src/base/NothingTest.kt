package base

fun main() {
    val address: String? = null
    val result = address ?: fail("No address")
    println(result.length)

    val address2: String? = ""
    val result2 = address2 ?: fail2("No address")
    //println(result2.length)
}


fun fail(message: String):Nothing {
    throw IllegalStateException(message)
}
fun fail2(message: String) {
    throw IllegalStateException(message)
}
