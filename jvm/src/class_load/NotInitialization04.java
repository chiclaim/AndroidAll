package class_load;

public class NotInitialization04 {
    public static void main(String[] args) {
        // 会加载和初始化 ConstTest 类
        System.out.println(ConstTest2.HELLO_WORLD);
    }

}
