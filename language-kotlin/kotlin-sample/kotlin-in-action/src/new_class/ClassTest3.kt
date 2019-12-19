package new_class

/**
 * Desc: 属性的 getter和setter方法 演示
 * Created by Chiclaim on 2018/9/19.
 */
class Person3 {

    //可变属性
    var name: String? = null
    //不可变属性
    val age: Int = 0

    //如果使用private修饰，则不会有getter和setter方法
    //外面就无法访问该属性了
    private var gender: Int = -1
}

/*

public final class Person3 {
   @Nullable
   private String name;
   private final int age;
   private int gender = -1;

   @Nullable
   public final String getName() {
      return this.name;
   }

   public final void setName(@Nullable String var1) {
      this.name = var1;
   }

   public final int getAge() {
      return this.age;
   }
}

 */