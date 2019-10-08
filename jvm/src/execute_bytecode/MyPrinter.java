package execute_bytecode;

import java.io.Serializable;

public class MyPrinter {

    /*public void println(char x) {
        System.out.println("char");
    }*/

    /*public void println(int x) {
        System.out.println("int:" + x);
    }*/

   /* public void println(short x) {
        System.out.println("short:" + x);
    }

    public void println(long x) {
        System.out.println("long:" + x);
    }

    public void println(float x) {
        System.out.println("float:" + x);
    }

    public void println(double x) {
        System.out.println("double:" + x);

    }*/

    /*public void println(Character x) {
        System.out.println("character:" + x);
    }
*/

    public void println(Serializable x) {
        System.out.println("serializable:" + x);
    }

    public void println(char... x) {
        System.out.println("char...:" + x);
    }

    public static void main(String[] args) {
        MyPrinter printer = new MyPrinter();

        printer.println('a');

    }


}
