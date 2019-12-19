package class_load;

public class ConstTest2 {
    static {
        System.out.println("ConstTest2 init");
    }

    public static final String HELLO_WORLD = "HelloWorld";
//    public static final Object HELLO_WORLD = 1;

}
