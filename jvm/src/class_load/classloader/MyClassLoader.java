package class_load.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        // 自定义的 ClassLoader
        Class klass1 = new MyClassLoader().loadClass("SimpleClass");

        //AppClassLoader
        Class klass2 = ClassLoader.getSystemClassLoader().loadClass("class_bytecode.SimpleClass");

        System.out.println(klass1 == klass2);

        // instanceof
        System.out.println(klass1.newInstance() instanceof class_bytecode.SimpleClass); //false
        // 因为都是由 AppClassLoader 加载的
        System.out.println(klass2.newInstance() instanceof class_bytecode.SimpleClass); // true
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //System.out.println("findClass: " + name);
        byte[] bytes = loadClassData(name);
        String packagename = "class_bytecode.";
        return defineClass(packagename + name, bytes, 0, bytes.length);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //System.out.println("loadClass: " + name);
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //System.out.println("loadClass: " + name + ", resolve: " + resolve);

        return super.loadClass(name, resolve);
    }

    public byte[] loadClassData(String name) {
        String path = "D:\\dev\\Workspace\\MyGitHub\\AndroidAll\\jvm\\src\\class_bytecode\\" + name + ".class";
        FileInputStream fis;
        byte[] data = null;
        try {
            File file = new File(path);
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("loadClassData-IOException");
        }
        return data;
    }
}
