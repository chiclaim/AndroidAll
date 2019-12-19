package feature.method_handle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Client {
    public static void main(String[] args) {
        // 方法描述符
        MethodType methodType = MethodType.methodType(int.class, new Class[]{int.class, int.class});

        try {
            // 通过方法名和方法描述符找到方法句柄(method handle)
            MethodHandle methodHandle = MethodHandles.lookup()
                    .findVirtual(MethodHandleTest.class, "sum", methodType);

            // 通过方法句柄调用方法
            int result = (int) methodHandle.invoke(new MethodHandleTest(), 1, 2);

            System.out.println(result);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
