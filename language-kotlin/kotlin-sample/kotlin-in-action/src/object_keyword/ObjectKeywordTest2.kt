package object_keyword

/**
 * Desc: companion object里声明方法 演示
 * Created by Chiclaim on 2018/9/20.
 */

class ObjectKeywordTest2 {
    companion object {
        fun run() {
            println("run...")
        }
    }

}

/*
    需要注意的是上面的run方法并不是一个static静态方法，只是让我们在调用的时候像静态方法(ObjectKeywordTest2.run())
    底层调用依然是通过ObjectKeywordTest2的静态内部类来访问的：ObjectKeywordTest2.Companion.run();

    对应的Java代码如下所示：
    public final class ObjectKeywordTest2 {
       public static final ObjectKeywordTest2.Companion Companion = new ObjectKeywordTest2.Companion((DefaultConstructorMarker)null);

       public static final class Companion {
          public final void run() {
             String var1 = "run...";
             System.out.println(var1);
          }

          private Companion() {
          }
       }
    }

    据此可得：在companion object中声明的方法、静态变量、私有常量都只能通过Companion访问
    在companion object中声明的方法会放到静态内部类Companion中

 */