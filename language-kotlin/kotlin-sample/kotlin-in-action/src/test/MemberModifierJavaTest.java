package test;

import visibility_modifier.modifier_member.MemberModifierJava;

/**
 * Desc: default只能在同包名下才能访问、protected同包名和子类都可访问
 * Created by Chiclaim on 2018/10/18.
 */
public class MemberModifierJavaTest {

    private MemberModifierJava memberModifier = new MemberModifierJava();

    void call() {

        //无法访问
        //memberModifier.asset    protected
        //memberModifier.username  default
        //System.out.println(memberModifier.username + " has " + memberModifier.asset);
    }


}

class MemberModifierJavaExtends extends MemberModifierJava {
    void call() {
        //memberModifier.asset    protected 子类可以访问
        //memberModifier.username  default 无法访问
        System.out.println("xxx has " + asset);
    }
}
