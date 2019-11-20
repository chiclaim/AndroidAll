package gc.jol;

import gc.objsize.MyObject1;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class JavaObjectLayout {

    public static void main(String[] args) {
        int size = 10;
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        //虚拟机信息
        //out.println(VMSupport.vmDetails());
        //打印类内部的占用
        //out.println(ClassLayout.parseClass(ArrayList.class).toPrintable());
        //打印实例内部的占用
        //out.println(ClassLayout.parseClass(ArrayList.class).toPrintable(list));
        //打印实例外部的占用
        //out.println(GraphLayout.parseInstance(list).toPrintable());
        //打印实例各个依赖的占用,并汇总
        //out.println(GraphLayout.parseInstance(list).toFootprint());

        System.out.println(VM.current().details());
//        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        System.out.println(ClassLayout.parseInstance(new MyObject1()).toPrintable());
//        System.out.println(ClassLayout.parseClass(MyObject1.class).toPrintable());
//
//        System.out.println(ClassLayout.parseInstance(new byte[0]).toPrintable());
//        System.out.println(ClassLayout.parseInstance(new byte[7]).toPrintable());
//        System.out.println(ClassLayout.parseInstance(new byte[9]).toPrintable());
//        System.out.println(ClassLayout.parseInstance(new byte[1024 * 1024]).toPrintable());


    }


}
