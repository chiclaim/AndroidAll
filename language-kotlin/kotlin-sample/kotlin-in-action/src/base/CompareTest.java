package base;
// 如果是 Java 则会抛出空指针异常，但是 Kotlin 不会

// 看下 他们的 字节码区别：

/*
Kotlin----
  public final static main()V
   L0
    LINENUMBER 25 L0
    GETSTATIC base/CompareTestKt.num : Ljava/lang/Integer;
    BIPUSH 42
    INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
    IF_ACMPNE L1
   L2
    LINENUMBER 26 L2
    LDC "success"
    ASTORE 0


Java-----

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2                  // Field i:Ljava/lang/Integer;
       3: invokevirtual #3                  // Method java/lang/Integer.intValue:()I
       6: bipush        42
       8: if_icmpne     19
      11: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
      14: ldc           #5                  // String success
      16: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      19: return



Kotlin 和 Java 的比较机制不一样：

Kotlin 是比较两个对象
Java 是比较两个Integer的intValue


 */
public class CompareTest {

    private static Integer i;

    public static void main(String[] args) {
        if (i == 42) {
            System.out.println("success");
        }
    }
}
