package class_load;

class Parent {
    static {
        System.out.println("Parent init------");
    }

//    public static String count = "1";
//    public static Object count = new Object();
    public static int count = 1;
}
