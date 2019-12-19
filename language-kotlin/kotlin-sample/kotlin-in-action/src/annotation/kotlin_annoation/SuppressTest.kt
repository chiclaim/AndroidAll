package annotation.kotlin_annoation

@Suppress("UNCHECKED_CAST")
fun test(source: Any) {
    if (source is List<*>) {
        val list = source as List<String>
        val str: String = list[0]
        println(str)
    }
}