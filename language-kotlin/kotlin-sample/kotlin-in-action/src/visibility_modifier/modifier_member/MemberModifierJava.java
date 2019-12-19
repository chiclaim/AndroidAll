package visibility_modifier.modifier_member;

/**
 * Desc:
 * Created by Chiclaim on 2018/10/18.
 */
public class MemberModifierJava {

    //default, visible in same package
    String username = "chiclaim";

    //visible in file
    private String password = "123456";

    //visible in module
    //internal val gender:Int =0

    //visible in subclass or same package
    protected float asset = 100000f;
}
