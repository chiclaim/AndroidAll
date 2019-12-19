package gc;

import javassist.CannotCompileException;
import javassist.ClassPool;

public class MemoryLeakTest {

    public static void main(String[] args) throws CannotCompileException, InterruptedException {
        System.out.println("Starting...");
        ClassPool cp = ClassPool.getDefault();
        for (int i = 0; i < 1_500_000; i++) {
            /*if (i % 50000 == 0) {
                Thread.sleep(2000);
            }*/
            String className = "com.waitingforcode.metaspace.MemoryTroubler" + i;
            Class clazz = cp.makeClass(className).toClass();
            System.out.println(clazz);

        }
    }
}