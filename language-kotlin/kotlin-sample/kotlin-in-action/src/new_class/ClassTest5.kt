package new_class

/**
 * Desc: 多个 Secondary Constructor 构造方法以及初始化代码块的执行实际有 演示
 *
 * Primary Constructor和Secondary Constructor对比
 *
 * Created by Chiclaim on 2018/9/19.
 */

//构造方法和class声明在同一行时，该构造方法被称之为 Primary Constructor
//如果构造方法在类的内部声明，该构造方法被称之为 Secondary Constructor

//Primary Constructor和 Secondary Constructor区别：
//Primary Constructor 构造方法的参数如果有val/var修饰，那么该类会生成和该参数一样的属性，以及与之对应setter和getter（如果是var修饰会有getter和setter，val修饰只有getter）
//Secondary Constructor 构造方法的参数不能用val/var属性

//Primary Constructor和 Secondary Constructor不可共存

open class Person5 {
    var name: String? = null
    var id: Int = 0


    constructor(name: String) {
        this.name = name
    }

    constructor(id: Int) : this("chiclaim") {
        this.id = id
    }

    //构造对象的时候，init代码块只会被执行一次
    init {
        System.out.println("init----------")
    }

}


/*
public class Person5 {
   @Nullable
   private String name;
   private int id;

   @Nullable
   public final String getName() {
      return this.name;
   }

   public final void setName(@Nullable String var1) {
      this.name = var1;
   }

   public final int getId() {
      return this.id;
   }

   public final void setId(int var1) {
      this.id = var1;
   }

   public Person5(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      System.out.println("init----------");
      this.name = name;
   }

   public Person5(int id) {
      this("chiclaim");
      this.id = id;
   }
}
 */