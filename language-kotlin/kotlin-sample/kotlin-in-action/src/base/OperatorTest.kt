package base

/**
 * desc: Kotlin 操作符 演示
 *
 * ==、===、is、as
 *
 * Created by Chiclaim on 2018/09/19
 */

class Person(var name: String)

fun getPerson(): Person? = null

// == 操作符 相当于Java里的equals()方法
// === 操作符 相当于Java里的 ==
fun equalsTest() {
    val p1 = Person("chiclaim")
    val p2 = getPerson()
    println(p2 == p1)

    /*
        ==  底层调用 Intrinsics.areEqual(p2, p1); 方法
        虽然 kotlin == 相当于 java equals，kotlin避免了空指针异常

        //这样的判断逻辑非常精妙
        public static boolean areEqual(Object first, Object second) {
            return first == null ? second == null : first.equals(second);
        }
     */

    println(p2 === p1)

    /*
        === 编译后代码如下：

        var2 = p2 == p1;
        System.out.println(var2);
     */
}

// is 操作符 相当于Java里的 instanceof
fun isTest(other: Any?) {
    if (other is Person) { // !is
        println("${other.name} is a person")
    }
/*
    public static final void isTest(@Nullable Object other) {
      if (other instanceof Person) {
         String var1 = ((Person)other).getName() + " is a person";
         System.out.println(var1);
      }
   }

 */
}

// as as? 操作符
fun asTest(other: Any?) {
    val p = other as Person
    /*
      val p = other as Person编译后对应的Java代码：
      if (other == null) {
         throw new TypeCastException("null cannot be cast to non-null type base.Person");
      } else {
         Person p = (Person)other;
      }
      据此可以得出 as 操作符可能抛出两种异常：
      1, java.lang.ClassCastException
      2, kotlin.TypeCastException

      但是如果是在Java中，把null进行强制类型转换并不会抛出异常：
      Object other = null;
      System.out.println((String)other);
     */


    //val p1 = other as? Person
    /*
      as? 不管other是null，还是other是不是Person类型，都不会抛出异常，如果不能强转则最终的结果为null

      编译class字节码后对应的Java代码如下：
      Object var10000 = other;
      if (!(other instanceof Person)) {
         var10000 = null;
      }

      Person p1 = (Person)var10000;
     */
}

fun main(args: Array<String>) {
    isTest("")
    isTest(null)
    isTest(Person("chiclaim"))

    //asTest(null) //throw kotlin.TypeCastException
    asTest("")
}
