package new_class

/**
 * desc: Kotlin接口中包含变量和方法 演示
 *
 * Created by Chiclaim on 2018/09/20
 */

interface InterfaceTest {

    var count: Int

    fun plus(num: Int) {
        count += num
    }

}

class Impl : InterfaceTest {
    override var count: Int = 0

    override fun plus(num: Int) {
        super.plus(num)
        println("invoke plus")
    }
}

/*

Java中的接口是不允许有变量和方法的，那么Kotlin是如何实现的呢？

Kotlin 接口中的变量底层只是setter和setter方法
Kotlin 接口中的方法并不是真的有方法体，底层也只有声明。只是在接口的内部新建一个静态内部类
       内部类的里静态方法名就是我们在接口里声明有方法体的方法，静态方法有个额外的接口类型的参数，但是对我们开发者是透明的
       接口里有方法体的方法在实现类里是不强制我们实现的，不是不需要，而是Kotlin已经替我们做了

    public interface InterfaceTest {
       int getCount();
       void setCount(int var1);
       void plus(int var1);
       void test();

       public static final class DefaultImpls {
          public static void plus(InterfaceTest $this, int num) {
             $this.setCount($this.getCount() + num);
          }

          public static void test(InterfaceTest $this) {
             String var1 = "";
             System.out.print(var1);
          }
       }
    }

    public final class Impl implements InterfaceTest {
       private int count;

       public int getCount() {
          return this.count;
       }

       public void setCount(int var1) {
          this.count = var1;
       }

       public void plus(int num) {
          InterfaceTest.DefaultImpls.plus(this, num);
       }

       public void test() {
          InterfaceTest.DefaultImpls.test(this);
       }
    }


 */