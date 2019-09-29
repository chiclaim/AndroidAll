package class_bytecode;

public class MaxLocalsTest {

    static {
        System.out.println("test");
    }

    //Slot1 = this
    //Slot2 = num
    //Slot3 = count
    //Slot4 = i



    public void test(int num) {
        if (num > 10) {
            int count = 0;
            for (int i = 0; i < num; i++) {
                count += i;
            }
            System.out.println(count);
        }
    }

    public int test2() {
        return 0;
    }


}
