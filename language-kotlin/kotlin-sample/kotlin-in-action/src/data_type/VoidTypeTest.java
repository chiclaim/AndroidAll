package data_type;

/**
 * Desc:
 * Created by Chiclaim on 2018/9/27.
 */

//Java Void 和 Kotlin Unit 的对比

interface MyProcessor<T> {
    T process();
}

public class VoidTypeTest implements MyProcessor<Void> {
    @Override
    public Void process() {
        return null;
    }
}

class MyStringProcessor implements MyProcessor<String> {
    @Override
    public String process() {
        return "This is a String";
    }
}
