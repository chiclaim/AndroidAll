package instruction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class MethodHandlerTest {

    static class MyPrinter {
        public void println(String msg) {
            System.out.println(msg);
        }
    }

    private static void test(Object obj) throws Throwable {

        // 描述方法的返回值，参数类型
        MethodType methodType = MethodType.methodType(void.class, String.class);

        // 在指定类中查找符合给定的方法名称、方法返回值、参数类型的方法
        // 因为调用的是成员方法，成员方法都有一个隐含参数，也就是方法的调用者，通过 bingTo 传递过去
        MethodHandle methodHandle = MethodHandles.lookup()
                .findVirtual(obj.getClass(), "println", methodType)
                .bindTo(obj);

        // 通过 MethodHandler 调用方法
        methodHandle.invokeExact("chiclaim");
    }

    private static void test2(Object obj) throws Throwable {
        Class clazz = obj.getClass();
        Method method = clazz.getDeclaredMethod("println", String.class);
        method.invoke(obj, "chiclaim");
    }


    public static void main(String[] args) throws Throwable {

        test(System.out);      // PrintStream
        test(new MyPrinter()); // MyPrinter

        System.out.println();

        test2(System.out);      // PrintStream
        test2(new MyPrinter()); // MyPrinter
    }

}
