import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class Test2 {
    private static void fixIteratorMultipleThread2() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
//        java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    list.add(++i);
                    System.out.println("---------add " + i);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int value : list) {
                    list.remove(value);
                    System.out.println("------remove + " + value);
                }
            }
        });
        thread2.start();
    }

    public static void main(String[] args) {
        fixIteratorMultipleThread2();
    }
}
