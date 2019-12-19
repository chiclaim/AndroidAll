package operator_overloading

/**
 * 操作符重载需要的类
 */
class Point(var x: Int, var y: Int) {

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun times(other: Point): Point {
        return Point(x * other.x, y * other.y)
    }

    operator fun div(other: Point): Point {
        return Point(x / other.x, y / other.y)
    }

    operator fun rem(other: Point): Point {
        return Point(x % other.x, y % other.y)
    }

    override fun toString(): String {
        return "Point[$x, $y]"
    }

}