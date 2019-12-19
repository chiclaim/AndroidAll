package nullability

/**
 * Desc: Null相关的扩展函数
 * Created by Chiclaim on 2018/9/26.
 */

//isNullOrBlank
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) {
        println("Please fill in the required fields")
        return
    }
    println("inputted $input is valid")
    //.....
}

fun verifyUserInput2(input: String?) {
    if (input.isNullOrEmpty()) {
        println("Please fill in the required fields")
        return
    }
    println("inputted $input is valid")
    //.....
}

fun main(args: Array<String>) {
    verifyUserInput(null)
    verifyUserInput("")
    verifyUserInput("  ")
    verifyUserInput("chiclaim")

    println("-------------------------------------")


    verifyUserInput2(null)
    verifyUserInput2("")
    verifyUserInput2("  ")
    verifyUserInput2("chiclaim")
}