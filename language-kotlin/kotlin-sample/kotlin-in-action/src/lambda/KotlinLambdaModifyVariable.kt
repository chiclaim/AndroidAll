package lambda

/**
 * desc: 修改Lambda外部的变量 演示
 *
 * Created by Chiclaim on 2018/09/22
 */

//----forEach底层是通过iterator来遍历的，并不是通过内部类来实现的，所以可以修改lambda外面的变量
fun printGoods(prefix: String, goods: List<String>) {
    var count = 0
    goods.forEach { goodName ->
        count++
        println("$prefix  $goodName")
    }
    println("goods count: $count")
}

//---- 下面的例子把Lambda赋值给变量inc，由于在lambda中没有使用额外的参数，底层是使用Function0来实现的
//在下面的lambda底层是通过匿名内部类来实现的，我们知道内部类是修改不了外面的变量的，那么它是如何修改外面的counter变量呢？
//通过把外面的变量包装一层，修改的时候，通过包装对象来修改，然后返回修改后的变量值
//在Kotlin中通过Ref包装类，如果是整型则是IntRef，如果是复杂类型则是ObjectRef
fun printCount() {

    var counter = 0

    //把Lambda赋值给变量
    val inc = {
        ++counter
    }

    println(inc())

}
/*
   public static final void printCount() {
      final IntRef counter = new IntRef();
      counter.element = 0;
      Function0 inc = (Function0)(new Function0() {
         public Object invoke() {
            return this.invoke();
         }

         public final int invoke() {
            IntRef var10000 = counter;
            ++counter.element;
            return var10000.element;
         }
      });
      int var2 = ((Number)inc.invoke()).intValue();
      System.out.println(var2);
   }

 */

fun main(args: Array<String>) {
    printGoods("china", listOf("telephone", "tv"))

    printCount()
}