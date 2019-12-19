package class_load;

public class NotInitialization03 {
    public static void main(String[] args) {
        // 并不会加载和初始化 ConstTest 类
        System.out.println(ConstTest.HELLO_WORLD);
    }

}
