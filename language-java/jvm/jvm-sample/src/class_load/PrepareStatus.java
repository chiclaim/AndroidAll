package class_load;

public class PrepareStatus {
    public static int count = 10;
}
/*

类变量会在静态代码块中赋值，静态代码块对应的就是 **<clinit>** 方法。将 `PrepareStatus` 反编译：


  // 静态变量
  public static int count;
    descriptor: I
    flags: ACC_PUBLIC, ACC_STATIC

  // 静态代码块 <clinit>
  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: bipush        10
         // 设置变量的值
         2: putstatic     #2                  // Field count:I
         5: return
      LineNumberTable:
        line 4: 0

 */