package class_bytecode;

public class UnBoxing {

    public static void main(String[] args) {
        int j = 10000;
        Integer k = 10000;

        System.out.println(j == k);  // j == k.intValue()

        test(null);
        test2(null);
    }

    /*
    public static void main(java.lang.String[]);
    Code:
       0: sipush        10000
       3: istore_1
       4: sipush        10000
       7: invokestatic  #2       // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      10: astore_2
      11: getstatic     #3       // Field java/lang/System.out:Ljava/io/PrintStream;
      14: iload_1
      15: aload_2
      16: invokevirtual #4       // Method java/lang/Integer.intValue:()I
      19: if_icmpne     26
      22: iconst_1
      23: goto          27
      26: iconst_0
      27: invokevirtual #5       // Method java/io/PrintStream.println:(Z)V
      30: return
     */


    private static void test(Integer k) {
        // k.intValue() 可能会导致 NullPointerException
        if (k == 10000) {
            // do something...
        }
    }

    private static void test2(Integer k) {
        // k.valueOf() 可能会导致 NullPointerException
        switch (k){
            case 10000:
                // do something...
                break;
        }
    }

}
