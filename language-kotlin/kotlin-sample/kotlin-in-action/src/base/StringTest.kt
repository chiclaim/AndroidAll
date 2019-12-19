package base

fun main() {
    val arr = "www.chiclaim.com".split(".")
    println(arr.size) // size = 3
    val path = "/Users/chiclaim/kotlin-book/kotlin-in-action.doc"

    parsePathRegexp(path)
    parsePath(path)

    val kotlinLogo = """
        | //
        .|//
        .|/ \"""

    println(kotlinLogo)
}

fun parsePathRegexp(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
        println("Dir: $directory, name: $filename, ext: $extension")
    }
}

fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
    println("Dir: $directory, name: $fileName, ext: $extension")
}