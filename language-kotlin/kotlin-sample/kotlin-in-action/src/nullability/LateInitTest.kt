package nullability

/**
 * Desc: Late-initialized properties 属性延迟初始化 演示
 * Created by Chiclaim on 2018/9/18.
 */
class LateInitTest {

    private lateinit var username: String

    fun onCreate(name: String) {
        username = name

        //lateinit 属性不能设置为null，username = null;

        //lateinit 属性可用于依赖注入
    }
}



