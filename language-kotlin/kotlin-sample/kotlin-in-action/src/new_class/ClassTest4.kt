package new_class

/**
 * Desc: 构造方法属性加上 var/val 修饰 和不加 var/val 修饰的区别 演示
 * Created by Chiclaim on 2018/9/19.
 */
class Person4 constructor(var name: String)
//和上面等价 class Person4 (var name: String)

class Person4_ constructor(name: String) {
    var name:String?=null

    init {
        this.name = name
    }
}
//和上面等价 class Person4_ (name: String)


/*
name加上var/val修饰，才会作为类的属性

public final class Person4 {
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

   public Person4(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.name = name;
   }
}

name没有加上var/val修饰，name只是构造方法参数名
public final class Person4_ {
   public Person4_(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
   }
}

 */