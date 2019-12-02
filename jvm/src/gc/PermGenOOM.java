package gc;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PermGenOOM {

    //C:\Program Files (x86)\Java\jdk1.6.0_45\bin>
    //java -XX:PermSize=1m -XX:MaxPermSize=9m -Xmx16m PermGenOOM

    //C:\Program Files\Java\jdk1.8.0_201
    //C:\Program Files (x86)\Java\jdk1.6.0_45


    //-XX:MetaspaceSize=1m -XX:MaxMetaspaceSize=9m -Xmx16m
    //-XX:PermSize=1m -XX:MaxPermSize=9m -Xmx16m

    private static Class klass;

    public static void main(String[] args) throws Exception {

        List<Object> list = new ArrayList<>();
        URL url = new File(".").toURI().toURL();
        URL[] urls = {url};

        int count = 0;

        while (true) {
            try {
                //System.out.println(++count);
                ClassLoader loader = new URLClassLoader(urls);
                //classLoaderList.add(loader);


                list.add(loader.loadClass("gc.GcTest"));
                list.add(loader.loadClass("gc.PermGenOOM"));

                //loader.loadClass("gc.StringOomMock");
                //Class clazz = loader.loadClass("gc.BigClass");

                //loader.loadClass("feature.method_handle.MethodHandleTest");
                //loader.loadClass("feature.method_handle.Client");
//                System.out.println(clazz);
               // System.out.println(klass == clazz);
                //klass = clazz;
                list.add(loader);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
