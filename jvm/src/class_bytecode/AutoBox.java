package class_bytecode;

public class AutoBox {

    public static void main(String[] args) {
        Integer j = 10000; // Integer j = Integer.valueOf(10000)
        Integer k = 10000;

        System.out.println(j==k);

        /**
         *
         * public static void main(java.lang.String[]);
         *     Code:
         *        0: sipush        10000
         *        3: invokestatic  #2      // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
         *        6: astore_1
         *        7: sipush        10000
         *       10: invokestatic  #2      // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
         *       13: astore_2
         *       14: getstatic     #3      // Field java/lang/System.out:Ljava/io/PrintStream;
         *       17: aload_1
         *       18: aload_2
         *       19: if_acmpne     26
         *       22: iconst_1
         *       23: goto          27
         *       26: iconst_0
         *       27: invokevirtual #4     // Method java/io/PrintStream.println:(Z)V
         *       30: return
         *        }
         * 	```
         */
    }

}
