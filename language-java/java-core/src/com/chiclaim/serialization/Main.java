package com.chiclaim.serialization;

import java.io.*;

/**
 * @author chiclaim
 */
public class Main {

    public static void main(String[] args) throws Exception {
        writeObj(new User("chiclaim",18));
        // 不会调用User的构造方法
        User user = readObj();
        System.out.println(user);
    }

    private static void writeObj(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.obj"));
        oos.writeObject(user);
        oos.close();
    }
    
    private static User readObj() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("user.obj")));
        return (User)ois.readObject();
    }
}






