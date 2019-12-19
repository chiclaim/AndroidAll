package visibility_modifier.modifier_class

/**
 * desc: internal修饰的class只在当前module可见
 *
 * Created by Chiclaim on 2018/09/20
 */

//internal修饰的class只对本module可见，对其他module不可见 是如何实现的？

internal open class InternalClass {
    fun getName(): String {
        return "chiclaim"
    }
}