package test

import visibility_modifier.modifier_class.InternalClass

/**
 * desc: 调用当前module中internal class
 *
 * 继承一个internal class
 *
 * Created by Chiclaim on 2018/09/20
 */

fun getInternalClassFromThisModule() {

    //InternalClass is a internal class in another package
    val p2 = InternalClass()
    p2.getName()

}

//如果想要继承internal class 首先把internal class设置为open
//还需要把子类也要设置成internal class，为什么要这么做呢？
//因为子类不设置成internal class，那就间接了暴露了原来的internal class了
//这和internal class的设计初衷是相违背的
internal class A : InternalClass()