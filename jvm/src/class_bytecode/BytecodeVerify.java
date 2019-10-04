package class_bytecode;

public class BytecodeVerify {
    public static void main(String[]args){
        long b = 10;
        System.out.println(b);// 通过修改常量池中，将 println 改成 printll，

        /*

        Exception in thread "main" java.lang.NoSuchMethodError: java.io.PrintStream.printll(J)V
        at class_bytecode.BytecodeVerify.main(BytecodeVerify.java:6)

         */
    }
}
