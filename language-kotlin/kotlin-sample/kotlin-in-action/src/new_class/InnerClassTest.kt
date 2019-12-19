package new_class

/**
 * desc: Kotlin内部类演示
 *
 * Created by Chiclaim on 2018/09/22
 */

class OuterClass {

    //和Java不同，在Kotlin中内部类默认是静态的，不持有外部类的引用
    class InnerStaticClass{

    }

    //如果要声明非静态的内部类，需要加上inner关键字
    inner class InnerClass{

    }
}

/*
   public static final class InnerStaticClass {
   }

   public final class InnerClass {
   }

 */