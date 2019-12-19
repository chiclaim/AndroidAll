package visibility_modifier

import visibility_modifier.modifier_member.MemberModifier

/**
 * Desc:
 * Created by Chiclaim on 2018/9/21.
 */

//在不同模块测试访问修饰符
class MemberModifierTest {

    private val memberModifier: MemberModifier = MemberModifier()


    fun call() {
        println("${memberModifier.username}")
    }


}

fun main(args: Array<String>) {
    MemberModifierTest().call()
}