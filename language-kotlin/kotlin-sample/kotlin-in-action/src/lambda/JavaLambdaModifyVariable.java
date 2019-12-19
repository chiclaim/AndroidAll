package lambda;

import java.util.Arrays;
import java.util.List;

/**
 * desc: 在lambda中不能修改lambda外面的变量值
 * <p>
 * Java Lambda底层是使用匿名内部来实现的，所以在Lambda里无法修改外面的变量值
 * <p>
 * Created by Chiclaim on 2018/09/22
 */

public class JavaLambdaModifyVariable {
    private static void printGoods(String prefix, List<String> goods) {
        int count = 0;
        goods.forEach(value -> {
                    //不可修改, Kotlin的lambda可以
                    //count++;
                    System.out.println(prefix + " " + value);
                }
        );
    }

    public static void main(String[] args) {
        printGoods("china", Arrays.asList("telephone", "tv"));
    }
}
