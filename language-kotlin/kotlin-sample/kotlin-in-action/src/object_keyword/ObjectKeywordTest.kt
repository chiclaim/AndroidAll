package object_keyword

/**
 * Desc: companion object里声明变量、常量 演示
 * Created by Chiclaim on 2018/9/20.
 */

class ObjectKeywordTest {
    companion object {
        //公共常量
        const val FEMALE: Int = 0
        const val MALE: Int = 1

        //私有常量
        val GENDER: Int = FEMALE

        //私有静态变量
        var username: String = "chiclaim"

        //静态方法
        fun run() {
            println("run...")
        }
    }

}

/*

   私有常量和私有静态变量的访问统一通过ObjectKeywordTest的静态内部类Companion来访问
   如果是kotlin代码访问私有常量或私有静态变量调用的时候不需要显示的通过Companion，为了方便底层已经为我们做了
   如果是java代码访问私有常量或私有静态变量，则需要使用ObjectKeywordTest的静态内部类Companion

   上面companion object 对应的Java代码：
    class ObjectKeywordTest {
        //公共常量
       public static final int FEMALE = 0;
       public static final int MALE = 1;
       //私有常量
       private static final int gender = 1;
       //静态变量
       @NotNull
       private static String username = "chiclaim";

       public static final ObjectKeywordTest.Companion Companion = new ObjectKeywordTest.Companion((DefaultConstructorMarker)null);

       public static final class Companion {

          public final void run() {
            String var1 = "run...";
            System.out.println(var1);
          }

          public final int getGENDER() {
             return ObjectKeywordTest.GENDER;
          }

          @NotNull
          public final String getUsername() {
             return ObjectKeywordTest.username;
          }

          public final void setUsername(@NotNull String var1) {
             Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
             ObjectKeywordTest.username = var1;
          }

          private Companion() {
          }
       }
    }
 */