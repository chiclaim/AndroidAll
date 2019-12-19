package visibility_modifier

import base.Person

/**
 * desc: 演示调用另一个模块的 internal 修饰的类
 *
 * Created by Chiclaim on 2018/09/20
 */

fun getInternalClassFromOtherModule() {
    //Person class is in another module
    val p1 = Person("yuzhiqiang")

    //----Can't resolve InternalClass，It's internal class in another module
    //val p2 = InternalClass()

    println(p1.name)
}