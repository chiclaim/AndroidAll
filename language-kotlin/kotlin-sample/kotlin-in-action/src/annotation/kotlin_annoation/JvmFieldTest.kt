package annotation.kotlin_annoation

class Person(
        @JvmField
        var name: String,
        var age: Int
)
/*

name 属性没有生成 getter、setter方法
并且该属性是public的，可以通过private显式指定为private


public final class Person {
   @JvmField
   @NotNull
   public String name;
   private int age;

   public final int getAge() {
      return this.age;
   }

   public final void setAge(int var1) {
      this.age = var1;
   }

   public Person(@NotNull String name, int age) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.name = name;
      this.age = age;
   }
}
 */