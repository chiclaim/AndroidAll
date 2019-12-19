package new_class

/**
 * Desc: 自动生成 hashCode、equals、toString方法
 * Created by Chiclaim on 2018/9/20.
 */

data class Human(var id: String) {
    var name: String? = null
}

/*

会根据构造方法参数：id生成 hashCode、equals、toString、copy、componentN方法

此处省略对应的Java代码

 */