package lambda

/**
 * desc:
 *
 * Created by Chiclaim on 2018/12/31
 */

fun main(args: Array<String>) {
    val button = Button()
    val listener = Button.OnClickListener {
        println("click event")
    }
    button.setOnClickListener(listener)
    button.setOnClickListener(listener)

}