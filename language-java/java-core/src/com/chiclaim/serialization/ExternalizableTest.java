package com.chiclaim.serialization;

import java.io.*;

/**
 * @author chiclaim
 */
public class ExternalizableTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        writeObj(new User("chiclaim",19));
        System.out.println("read from local: "+readObj());

    }

    private static void writeObj(Externalizable obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user2.obj"));
        oos.writeObject(obj);
        oos.close();
    }

    private static Externalizable readObj() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("user2.obj")));
        return (Externalizable)ois.readObject();
    }



    private static class User implements Externalizable {

        private String name;
        private int age;

        // 需要显式的声明一个无参的构造方法
        public User(){
            System.out.println("User()");
        }

        public User(String name, int age) {
            System.out.println("User(String name, int age)");
            this.name = name;
            this.age = age;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(name);
            out.writeInt(age);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            name = (String) in.readObject();
            age = in.readInt();
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}






