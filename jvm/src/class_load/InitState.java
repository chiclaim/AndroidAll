package class_load;

public class InitState {
    static {
        i = 10;                // 可以赋值
        //System.out.println(i); //illegal forward reference
    }


    static int i = 0;
}
