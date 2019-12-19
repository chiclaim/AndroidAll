package data_type

/**
 * Desc: Unit和Void
 * Created by Chiclaim on 2018/9/27.
 */

//Kotlin中的Unit和Java的Void一样，一般用于方法没有返回值

//和Java不同的是，如果不需要返回值，Kotlin方法可以省略Unit关键字

fun foo(): Unit {

}

fun foo2() { //省略 Unit

}

//Unit用于类泛型的时候，如果Unit作为该类方法返回值的话，则表示该方法没有返回值，不会像Java一样一定要返回一个Void类型
//Kotlin不需要这样做是因为Kotlin已经在编译的时候已经替我们返回了一个Unit，底层是通过一个桥接方法来实现的，具体可以查看class字节码
//需要注意到是，一个普通返回Unit的方法，不会有额外的桥接方法，只有泛型作为方法返回值是才会，如下面的例子

interface Processor<T> {
    //子类实现该方法，都会在子类中产生一个额外process桥接方法
    fun process(): T
}


class NoResultProcessor : Processor<Unit> {
    //会有桥接方法
    override fun process() {
        println("process")
    }

    //不会产生桥接方法
    fun <T> genericMethod(t: T): T {
        return t
    }
}

class StringProcessor : Processor<String> {
    //会有桥接方法
    override fun process(): String {
        return "this is a String"
    }

}

fun main(args: Array<String>) {
    //对于Kotlin来说任何方法都有返回值的，如果没有指定返回值，那么这个方法的返回值类型就是Unit
    println(foo2().javaClass)
}






