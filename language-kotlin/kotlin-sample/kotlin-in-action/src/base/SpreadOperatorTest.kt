package base

import java.util.*

fun main() {
    val intArr: Array<Int> = arrayOf(1, 2, 3, 4)
    Arrays.asList(intArr).run {
        println("size = ${this.size}")
    }
    /*
    上面反编译出来的代码：

        Integer[] intArr = new Integer[]{1, 2, 3, 4};
        List var1 = Arrays.asList(intArr);
        System.out.println(var1.size());

        这样的Java代码 运行结果是 size=4


        但是上面的Kotlin代码运行结果是 size=1 ？？？？
     */




    val intArr2: Array<Int> = arrayOf(1, 2, 3, 4)
    Arrays.asList(0, *intArr2).run {
        println("size = $size")
    }
    /*
    反编译对应的Java代码：

    Integer[] intArr2 = new Integer[]{1, 2, 3, 4};
    SpreadBuilder var10000 = new SpreadBuilder(2);
    var10000.add(0);             //第1个元素
    var10000.addSpread(intArr2); //数组里的4个元素
    List var2 = Arrays.asList((Integer[])var10000.toArray(new Integer[var10000.size()]));
    int var7 = false;
    String var5 = "size = " + var2.size();
    System.out.println(var5);
     */


}