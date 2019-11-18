package gc.objsize;

import java.lang.instrument.Instrumentation;

public class ObjectSize {

    private static volatile Instrumentation globalInstr;

    public static void premain(String args, Instrumentation inst) {
        globalInstr = inst;
    }

    public static long getObjectSize(Object obj) {
        if (globalInstr == null)
            throw new IllegalStateException("Agent not initialed");
        return globalInstr.getObjectSize(obj);
    }

    public static void main(String[] args) {

        /*
            java -javaagent:agent.jar gc.objsize.ObjectSize

            https://www.zhihu.com/question/39019401

            于是空对象（java.lang.Object）的大小是16：
            mark word：8字节
            压缩的Klass*：4字节
            padding：4字节

            而空数组的大小也是16：
            mark word：8字节
            压缩的Klass*：4字节
            length：4字节

            java -XX:-UseCompressedOops -javaagent:agent.jar gc.objsize.ObjectSize
            // 注意 +/-
            java -XX:+UseCompressedOops -javaagent:agent.jar gc.objsize.ObjectSize

            16
            24
            32
            1048600


            java -XX:-UseCompressedOops XX:UseCompressedClassPointers -javaagent:agent.jar gc.objsize.ObjectSize
         */

        System.out.println("empty object " + getObjectSize(new Object())); //16
        System.out.println("empty myObject " + getObjectSize(new MyObject())); //16
        System.out.println("empty myObject2 " + getObjectSize(new MyObject2()));
        System.out.println("byte[0] " + getObjectSize(new byte[0])); //16
        System.out.println("byte[7] " + getObjectSize(new byte[7])); //24 padding补齐
        System.out.println("byte[1024 * 1024] " + getObjectSize(new byte[1024 * 1024])); //1024 * 1024 + 16 = 1048592
    }


}
