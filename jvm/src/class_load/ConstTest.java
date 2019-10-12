package class_load;

public class ConstTest {
    static {
        System.out.println("ConstTest init");
    }

    public static final String HELLO_WORLD = "HelloWorld";

}
