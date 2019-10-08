package execute_bytecode;

public class Client {

    private Client(String name) {
        this(name, 18);
    }

    private Client(String name, int age) {

    }

    public static void main(String[] args) {
        Fruit fruit = new Apple();
        fruit.getName();


    }
}
