package execute_bytecode;

public class MethodOverloadResolution {

    public void sell(Fruit fruit) {
        System.out.println("fruit...");

    }

    public void sell(Apple apple) {
        System.out.println("apple...");
    }

    public void sell(Banana banana) {
        System.out.println("banana...");
    }

    public static void main(String[] args) {
        Fruit apple = new Apple();
        Fruit banana = new Banana();

        MethodOverloadResolution dispatch = new MethodOverloadResolution();
        dispatch.sell(apple);
        dispatch.sell(banana);

    }
}
