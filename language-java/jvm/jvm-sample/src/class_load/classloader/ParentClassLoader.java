package class_load.classloader;

public class ParentClassLoader {
    public static void main(String[] args) {
        ClassLoader loader = ParentClassLoader.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader);
            loader = loader.getParent();
        }
        System.out.println(loader);
    }
}
