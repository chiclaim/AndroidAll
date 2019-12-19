package new_class

/**
 * Desc: 自动生成构造方法和getter演示
 * Created by Chiclaim on 2018/9/19.
 */
class Person(val name: String) //只会生成name的getter方法

/*

-由于name属性不可修改，所以不提供name的setter方法

-Kotlin声明的class默认是public final的

public final class Person {
   @NotNull
   private final String name;

   @NotNull
   public final String getName() {
      return this.name;
   }

   public Person(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.name = name;
   }
}


 */