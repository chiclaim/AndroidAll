package base


fun main() {

    for (i in 0..100) {
        print("$i-")
    }

    println("------")

    for (i in 100 downTo 0 step 2) {
        print("$i，")
    }

    println("------")

    for(i in 0 until 100){
        print("$i-")
    }
}