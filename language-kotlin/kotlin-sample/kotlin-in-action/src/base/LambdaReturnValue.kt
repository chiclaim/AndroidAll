package base

fun main(args: Array<String>) {

    listOf<Int>().filter {
        if (it in 1..9) {
            return@filter true //显式return
        }
        if (it in 10..19) {
            return@filter false //显式return
        }
        if (it in 20..29) {
            return@filter true //显式return
        }
        false //隐式return
    }

}