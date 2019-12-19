package new_class

/**
 * Desc: 构造方法和getter、setter演示
 * Created by Chiclaim on 2018/9/19.
 */
class Person2(var name: String)

/*

public final class Person2 {
   @NotNull
   private String name;

   @NotNull
   public final String getName() {
      return this.name;
   }

   public final void setName(@NotNull String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.name = var1;
   }

   public Person2(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.name = name;
   }
}

 */