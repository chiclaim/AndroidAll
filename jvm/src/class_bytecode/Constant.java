package class_bytecode;


public class Constant {

    //复杂类型“常量”    本类会初始化

    //public static final Object OBJ = new Object();

    //基本类型及string常量     本类不会初始化

    public static final int OBJ = 1;

    static {
        //System.out.println("Constant class init------");
    }


}

