public class Test {


    public static void main(String[] args) {
        printName(String.class);
        printName(String[].class);

        printName(int.class);

        printName(int[].class);

        System.out.println("L" + Deprecated.class.getName().replaceAll("\\.", "\\/") + ";");

    }

    private static void printName(Class clazz) {
        System.out.println();
        if (clazz.isAnnotation())

            System.out.println("getName: " + clazz.getName());
        System.out.println("getSimpleName: " + clazz.getSimpleName());
        System.out.println("getCanonicalName: " + clazz.getCanonicalName());

    }

}

