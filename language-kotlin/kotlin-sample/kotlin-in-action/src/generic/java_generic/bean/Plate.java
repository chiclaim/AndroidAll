package generic.java_generic.bean;

/**
 * Desc:
 *
 * <p>
 * <a href='https://www.zhihu.com/question/20400700'>Java Generic Wildcards</a>
 *
 * <p>
 * Created by Chiclaim on 2018/10/11.
 */

//定义一个`盘子`类
public class Plate<T extends Food> {

    private T item;

    public Plate(T t) {
        item = t;
    }

    public void set(T t) {
        item = t;
    }

    public T get() {
        return item;
    }

}












