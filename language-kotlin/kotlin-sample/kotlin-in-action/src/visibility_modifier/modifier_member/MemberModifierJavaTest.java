package visibility_modifier.modifier_member;

/**
 * Desc: default只能在同包名下才能访问、protected同包名和子类都可访问
 * Created by Chiclaim on 2018/10/18.
 */
public class MemberModifierJavaTest {

    private MemberModifierJava memberModifier = new MemberModifierJava();

    void call() {

        //memberModifier.asset    protected
        //memberModifier.username  public

        System.out.println(memberModifier.username + " has " + memberModifier.asset);
    }
}
