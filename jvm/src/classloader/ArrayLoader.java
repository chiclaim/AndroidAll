package classloader;


import java.io.Serializable;

public class ArrayLoader {
    public static void main(String[] args) throws ClassNotFoundException {

        ClassLoader loader = MyClass.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader);
            loader = loader.getParent();
        }


        System.out.println();

        MyClass[] arr = new MyClass[1];
        System.out.println(arr.getClass().getClassLoader());

        Object[] arr2 = new Object[1];
        System.out.println(arr2.getClass().getClassLoader());

        int[] arrInt = new int[1];
        System.out.println(arrInt.getClass().getClassLoader());

        Class<?> clazz = Class.forName("sun.misc.Launcher$AppClassLoader");
        System.out.println(clazz.getClassLoader());

        System.out.println(Object.class.getClassLoader());

        System.out.println(arrInt.getClass().getComponentType());

    }
}
