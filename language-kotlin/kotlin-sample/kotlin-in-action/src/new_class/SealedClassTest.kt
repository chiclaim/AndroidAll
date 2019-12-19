package new_class

/**
 * desc: sealed class 演示
 *
 * Created by Chiclaim on 2018/09/22
 */


/*
小结：

当我们使用when语句通常需要加else分支，如果when是判断某个复杂类型，如果该类型后面有新的子类，when语句就会走else分支

sealed class就是用来解决这个问题的。

当when判断的是sealed class，那么不需要加else默认分支，如果有新的子类，编译器会通过编译报错的方式提醒开发者添加新分支，从而保证逻辑的完整性和正确性

需要注意的是，当when判断的是sealed class，千万不要添加else分支，否则有新子类编译器也不会提醒

sealed class 在外部不能被继承，私有构造方法是私有的
*/


sealed class Expr {

    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()

}

fun eval(e: Expr): Int =
        when (e) {
            is Expr.Num -> e.value
            is Expr.Sum -> eval(e.left) + eval(e.right)
        }

//=========================================================

open class Expr2;
class Num2(val value: Int) : Expr2()
class Sum2(val left: Expr2, val right: Expr2) : Expr2()

fun eval2(e: Expr2): Int =
        when (e) {
            is Num2 -> e.value
            is Sum2 -> eval2(e.left) + eval2(e.right)
            else -> -1
        }


fun main(args: Array<String>) {
    val sum = eval(Expr.Sum(Expr.Num(1), Expr.Num(2)))
    println("sum = $sum")
}


