package higher_order_function;

import kotlin.jvm.functions.Function2;

/**
 * Desc: 在Java中使用高阶函数
 * Created by Chiclaim on 2018/10/8.
 */
public class UsingFuncInJava {

    public static void main(String[] args) {

        //使用lambda的方式
        HigherOrderFuncKt.process(11, 2, (i1, i2) -> i1 + i2);

        //使用内部类的方式
        HigherOrderFuncKt.process(11, 2, new Function2<Integer, Integer, Integer>() {

            @Override
            public Integer invoke(Integer i1, Integer i2) {
                return i1 * i2;
            }
        });
    }

}
