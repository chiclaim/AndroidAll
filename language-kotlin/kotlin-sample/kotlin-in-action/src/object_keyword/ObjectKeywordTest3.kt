package object_keyword

/**
 * Desc: companion object 自定义名称 演示
 * Created by Chiclaim on 2018/9/20.
 */

class ObjectKeywordTest3 {
    companion object Cache {
        fun save2DB() {
            println("save data to database...")
        }

        fun save2Memory() {
            println("save data to memory...")
        }
    }


}

/*
    companion object对应的静态内部类名字默认是Companion
    也可以通过companion object XXX来自定义自己想要的名字

    public final class ObjectKeywordTest3 {
       public static final ObjectKeywordTest3.Cache Cache = new ObjectKeywordTest3.Cache((DefaultConstructorMarker)null);
       public static final class Cache {
          public final void save2DB() {
             String var1 = "save data to database...";
             System.out.println(var1);
          }

          public final void save2Memory() {
             String var1 = "save data to memory...";
             System.out.println(var1);
          }

          private Cache() {
          }
       }
    }

 */