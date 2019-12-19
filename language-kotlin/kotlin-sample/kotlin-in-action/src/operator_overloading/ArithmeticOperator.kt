package operator_overloading

/**
 * Desc: 算术符操作符重载
 * Created by Chiclaim on 2018/9/28.
 */

//算术符重载 函数和操作符的对应关系：
// plus  -> +
// minus -> -
// times -> *
// div   -> /
// rem   -> %



//通过函数扩展进行操作符重载
operator fun Point.minus(other: Point): Point {
    return Point(x - other.x, y - other.y)
}

fun main(args: Array<String>) {
    val p1 = Point(10, 30)
    val p2 = Point(20, 30)
    println("plus: ${p1 + p2}")
    println("minus: ${p1 - p2}")
    println("times: ${p1 * p2}")
    println("div: ${p1 / p2}")

}

/*
   //使用重载的操作符，编译后最终也是调用对应的方法

   public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      Point p1 = new Point(10, 30);
      Point p2 = new Point(20, 30);
      String var3 = "plus " + p1.plus(p2);
      System.out.println(var3);
      var3 = "minus " + minus(p1, p2);
      System.out.println(var3);
      var3 = "times " + p1.times(p2);
      System.out.println(var3);
      var3 = "div " + p1.div(p2);
      System.out.println(var3);
   }


 */
