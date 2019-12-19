package visibility_modifier.modifier_class

/**
 * Desc: kotlin protected class 演示
 *
 * Created by Chiclaim on 2018/9/21.
 */
open class ProtectedClassTest {

    //protected class 只能在当前包可见 这点和Java是一样的
    //继承 protected class 的类也要声明成 protected class，这是和Java不同点

    protected open class ProtectedClass {
        open fun getName(): String {
            return "chiclaim"
        }
    }

    protected class ProtectedClassExtend : ProtectedClass() {
        override fun getName(): String {
            return "yuzhiqiang"
        }
    }

}
