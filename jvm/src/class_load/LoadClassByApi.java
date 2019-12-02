package class_load;

public class LoadClassByApi {

    public static void main(String[] args) throws Exception {

        // 会执行初始化操作
        Class clazz1 = Class.forName("class_load.ConstTest");
        // 不会执行初始化操作
        Class clazz2 = new ClassLoader() {}.loadClass("class_load.ConstTest");
        // 依然不会执行初始化操作（static代码块）
        clazz2.newInstance();


        System.out.println(clazz1.getClassLoader());
        System.out.println(clazz2.getClassLoader());



        // 可以用于加载数组类
        Class.forName("[Ljava.lang.String;");
        Class c = Class.forName("[J");

        long[] ls = (long[]) c.newInstance();

        int[] arr = (int[]) java.lang.reflect.Array.newInstance(int.class, 10);
        System.out.println(arr.length);
        System.out.println(ls.length);

        // 抛出异常：java.lang.ClassNotFoundException: [Ljava.lang.String;
        // ClassLoader.getSystemClassLoader().loadClass("[Ljava.lang.String;");
        // ClassLoader.getSystemClassLoader().loadClass("[I");

    }


}
