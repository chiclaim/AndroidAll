package com.chiclaim.generic;

/**
 * 有界类型参数
 *
 * @author chiclaim
 */

// 通过 extends 来定义有界类型参数
public class BoundedTypeParameter<T extends Animal> {

    public static void main(String[] args) {
        // argument type 只能是 Animal 或者是 Animal 的派生类
        BoundedTypeParameter<Dog> p = new BoundedTypeParameter<>();
        MultipleBoundTypeParameter<ArgumentP> p2 = new MultipleBoundTypeParameter<>();
    }

    static class ClassBound {
    }

    interface IBound {
    }

    interface IBound2 {
    }

    //多限定类型参数，要求Argument type 是所有限定类型的子类
    static class ArgumentP extends ClassBound implements IBound,IBound2 {
    }

    // 如果某个限定类型是 Class，那么它必须放在类型参数列表的第一个：
    static class MultipleBoundTypeParameter<T extends ClassBound & IBound & IBound2> {
    }

}
