package class_bytecode;

public class InvokeDynamic {

    public static void main(String[] args) {
        //System.setProperty("jdk.internal.lambda.dumpProxyClasses", ".");
        Runnable x = () -> System.out.println(args.length);
    }
}
