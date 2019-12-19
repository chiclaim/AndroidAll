package base

val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

val range = 0 until 10

fun test(){
    for (i in 100 downTo 0 step 2) {
        print("$i，")
    }

    println("------")

    for(i in 0 until 100){
        print("$i-")
    }

}

