package annotation

/**
 * Desc:
 * Created by Chiclaim on 2018/10/15.
 */

//1，@PropertyOnly Kotlin定义的Property不能使用到Java属性上，可以使用AnnotationTarget.FIELD
//2，在Java中注解默认保留到class字节码级别，Kotlin默认是Runtime运行时
//3，Kotlin注解不能包含函数


@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) {
}


fun main(args: Array<String>) {
    remove(0)
}