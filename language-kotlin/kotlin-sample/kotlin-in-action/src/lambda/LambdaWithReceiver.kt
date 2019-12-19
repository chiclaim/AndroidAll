package lambda

/**
 * Desc: with、apply、run函数 Lambda receiver 演示
 * Created by Chiclaim on 2018/9/25.
 */


//with函数有两个参数，一个是receiver 一个是lambda表达式。传递lambda参数，一般放在外面
//此时lambda receiver就是with的第一个参数
//使用with函数，调用receiver对象的方法时，可以很的省略这个对象，直接调用。如下面的append方法
fun alphabet() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know alphabet!").toString()
}


//apply函数是一个扩展函数
//此时lambda receiver是调用apply函数的对象
//使用apply函数可以创建对象后很方便地进行初始化操作
fun alphabet2() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}

fun alphabet3() = StringBuilder().run {
    for (c in 'A'..'Z') {
        append(c)
    }
    append("\nNow I know the alphabet!")
}


fun main(args: Array<String>) {
    println(alphabet())
    println(alphabet2())
    println(alphabet3())
}