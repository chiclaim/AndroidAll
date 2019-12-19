package new_class


//枚举类

enum class Color(val r: Int, val g: Int, val b: Int ){ //枚举常量属性

    // 定义枚举常量对象
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0),
    BLUE(0, 0, 255), INDIGO(75, 0, 130),
    VIOLET(238, 130, 238); //最后一个枚举对象需要分号结尾

    // 在枚举类中定义函数
    fun rgb() = (r * 256 + g) * 256 + b
}