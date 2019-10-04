package class_load;

public class ConstClass {
    static {
        System.out.println("ConstClass init");
    }

    public static final String HELLO_WORLD = "HelloWorld";

}
