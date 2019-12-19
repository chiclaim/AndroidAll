package class_load.classloader;

import java.io.*;

public class CipherClassLoader extends ClassLoader {

    private final static String DEST_DIR = "D:\\dev\\Workspace\\MyGitHub\\AndroidAll\\jvm\\src\\class_load\\classloader";

    public static void main(String[] args) throws Exception {
        String dir = "D:\\dev\\Workspace\\MyGitHub\\AndroidAll\\jvm\\src\\class_load\\classloader";
        Class clazz = new CipherClassLoader(dir).loadClass("class_load.CipherClass");
        clazz.newInstance();

    }

    private static void doCypher() throws Exception {
        String srcPath = "D:\\dev\\Workspace\\MyGitHub\\AndroidAll\\jvm\\src\\class_load\\CipherClass.class1";
        FileInputStream fis = new FileInputStream(srcPath);
        String destFileName = srcPath.substring(srcPath.lastIndexOf('\\') + 1);
        String destPath = DEST_DIR + "\\" + destFileName;
        FileOutputStream fos = new FileOutputStream(destPath);
        cypher(fis, fos);
        fis.close();
        fos.close();
    }

    /**
     * 加密方法,同时也是解密方法
     *
     * @param ips
     * @param ops
     * @throws Exception
     */
    private static void cypher(InputStream ips, OutputStream ops) throws Exception {
        int b = -1;
        while ((b = ips.read()) != -1) {
            ops.write(b ^ 0xff);//如果是1就变成0,如果是0就变成1
        }
    }

    private String classDir;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classFileName = classDir + "\\" + name.substring(name.lastIndexOf('.') + 1) + ".class1";
        try {
            FileInputStream fis = new FileInputStream(classFileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            cypher(fis, bos);
            fis.close();
            byte[] bytes = bos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CipherClassLoader(String classDir) {
        this.classDir = classDir;
    }
}