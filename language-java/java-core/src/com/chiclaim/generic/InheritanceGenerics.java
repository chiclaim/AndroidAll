package com.chiclaim.generic;

/**
 * 泛型在继承中的使用（泛型类的子类）
 * @author chiclaim
 */
public class InheritanceGenerics {

    private static void test(Box<Number> box){

    }

    public static void main(String[] args) {
        Box<Number> box = new Box<>();
        box.add(1);   // 因为 Integer extends Number
        box.add(1.1); // 因为 Double extends Number

        Box<Integer> boxInteger = new Box<>();
        // test(boxInteger); 编译报错，因为 Box<Number> 不是 Box<Integer> 的 super type

        BoxSubtype<Number> boxSubtype = new BoxSubtype<>();
        test(boxSubtype); // 因为 Box<Number> 是 BoxSubtype<Number> 的 super type

        BoxSubtype<Integer> boxSubtype2 = new BoxSubtype<>();
        //test(boxSubtype2);

    }



}


class Box<T> {
    public void add(T t) { }
}

class BoxSubtype<T> extends Box<T>{

}