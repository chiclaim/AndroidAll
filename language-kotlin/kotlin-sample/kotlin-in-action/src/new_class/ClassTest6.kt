package new_class

/**
 * Desc: 调用父类或者自身构造方法 演示
 *
 * Created by Chiclaim on 2018/9/19.
 */


class Person6 : Person5 {

    var height: Float = 0f

    constructor(name: String) : super(name) //使用super调用父类的构造方法

    constructor(height: Float) : this("anonymity") { //使用this调用本类的构造方法
        this.height = height
    }

}

fun main(args: Array<String>) {
    val p = Person6(190f)
    System.out.println("name = ${p.name}, height = ${p.height}")
}
