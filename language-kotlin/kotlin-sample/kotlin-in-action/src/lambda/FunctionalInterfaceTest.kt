package lambda

/**
 * desc:
 *
 * Created by Chiclaim on 2018/12/31
 */

fun main(args: Array<String>) {
    val button = Button()
    var count = 0
    button.setOnClickListener {
        println("click ${++count}")
    }

    button.setOnClickListener {
        println("click ${++count}")
    }

}