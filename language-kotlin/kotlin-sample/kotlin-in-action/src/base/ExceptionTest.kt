package base

import java.io.BufferedReader
import java.io.FileReader

fun readNumber(reader: BufferedReader): Int? {
    try {
        val line = reader.readLine() //throws IOException
        return Integer.parseInt(line)
    } catch (e: NumberFormatException) {
        return null
    } finally {
        reader.close()   //throws IOException
    }
}

fun readNumber2(reader: BufferedReader): Int? {
    reader.use {
        val line = reader.readLine()
        try {
            return Integer.parseInt(line)
        } catch (e: NumberFormatException) {
            return null
        }
        // 省略 reader.close()
    }
}


fun checkNumber(number: Int) {
    val percentage = if (number in 0..100)
        number
    else
        throw IllegalArgumentException(
                "A percentage value must be between 0 and 100: $number")
}

fun readFirstLineFromFile(path: String): String {
    BufferedReader(FileReader(path)).use { br ->
        return br.readLine()
    }
}

fun main() {

    println(checkNumber(1000))

    readNumber(BufferedReader(FileReader("xx.txt")))

    println("this is after readNumber")
}