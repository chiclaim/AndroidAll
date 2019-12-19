package test

import visibility_modifier.modifier_member.MemberModifier

/**
 * Desc: 成员修饰符 演示
 * Created by Chiclaim on 2018/9/21.
 */
//在同一个模块，不同包名
class MemberModifierTest {

    val memberModifier: MemberModifier = MemberModifier()

    fun call() {

        //memberModifier.gender    internal
        //memberModifier.username  public

        println("${memberModifier.username}'s gender ${memberModifier.gender}")
    }
}

//继承
class MemberModifierTest2 : MemberModifier() {
    fun call() {
        //gender //internal
        //username //public
        //asset  //protected

        println("$username's gender $gender, He has $asset")
    }

}