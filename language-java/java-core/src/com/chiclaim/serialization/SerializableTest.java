package com.chiclaim.serialization;

import java.io.*;

/**
 * @author chiclaim
 */
public class SerializableTest {

    public static void main(String[] args) throws Exception {
        writeObj(new User("chiclaim", 18));
        // 不会调用User的构造方法
        System.out.println("read from local: " + readObj());
    }

    private static void writeObj(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.obj"));
        oos.writeObject(user);
        oos.close();
    }

    private static User readObj() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("user.obj")));
        return (User) ois.readObject();
    }


    private static class User implements Serializable {

        public static final long serialVersionUID = -1;

        private String name;
        private int age;

        public User() {
            System.out.println("User()");
        }

        public User(String name, int age) {
            System.out.println("User(String name, int age)");
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
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








