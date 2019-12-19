package visibility_modifier.modifier_member

/**
 * Desc:
 * Created by Chiclaim on 2018/9/21.
 */
open class MemberModifier {

    //default is public
    public val username: String? = "chiclaim"

    //visible in file
    private var password: String? = "123456"

    //visible in module
    internal val gender: Int = 0

    //visible in subclass
    protected var asset: Float = 100000f



    //方法修饰符也是一样的
}