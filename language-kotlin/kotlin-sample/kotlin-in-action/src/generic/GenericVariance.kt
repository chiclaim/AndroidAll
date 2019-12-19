package generic

import generic.java_generic.bean.Apple
import generic.java_generic.bean.Food
import generic.java_generic.bean.Fruit
import generic.java_generic.bean.Pear

/**
 * Desc: Kotlin泛型协变(out)、逆变(in)
 *
 * <p>
 *
 * 关于Java泛型的协变、逆变可以查看JavaGenericWildcardTest里面有详细的介绍
 *
 * Kotlin同样也有泛型的协变、逆变，他们之间的概念是一致的，但是在声明上有些差异：
 *
 * Java是在声明变量的时候声明泛型协变、逆变的，不能再声明类的时候声明泛型协变、逆变(如Collections.copy函数源码)
 *
 * Kotlin是在声明类的时候声明泛型协变和逆变的，然后在类里使用的泛型的时候就不用声明泛型协变了；也可以在声明变量的时候声明泛型协变
 *
 * <p>
 *
 *
 *
 * Created by Chiclaim on 2018/10/10.
 */


fun takeFruit(fruits: List<Fruit>) {
}


fun testGenericNumber2(numbers: MutableList<Number>) {
}

fun main(args: Array<String>) {
    val foods: List<Food> = listOf(Food(), Food())
    val fruits: List<Fruit> = listOf(Fruit(), Fruit())
    val apples: List<Apple> = listOf(Apple(), Apple())
    val pears: List<Pear> = listOf(Pear(), Pear())
    //public interface List<out E>
    //out修饰的泛型是 泛型协变 covariant
    //像这样的类或接口如List，称之为协变(covariant)类(接口)
    //和Java一样，协变泛型不能传递父类类型，只能传递Fruit或者它的子类
    //takeFruit(foods) 编译报错
    takeFruit(fruits)
    takeFruit(apples)
    takeFruit(pears)

    //-------------------------------------


    // 根据上面的介绍发现List是协变类
    // 我们在来看下MutableList是否是协变类
    val ints2: MutableList<Int> = mutableListOf(1, 3, 4)
    //并不能成功传递参数，所以MutableList并不是一个协变类(invariant)
    //testGenericNumber2(ints2)

    //-------------------------------------

    //我们分别来看下协变类List和非协变类MutableList的源码声明
    //在声明类的时候使用协变、逆变
    //Kotlin List是一个泛型协变
    //public interface List<out E>
    //MutableList是一个invariant
    //public interface MutableList<E> : List<E>


    //-------------------------------------
    val foodComparator = Comparator<Food> { e1, e2 ->
        e1.hashCode() - e2.hashCode()
    }
    val fruitComparator = Comparator<Fruit> { e1, e2 ->
        e1.hashCode() - e2.hashCode()
    }
    val appleComparator = Comparator<Apple> { e1, e2 ->
        e1.hashCode() - e2.hashCode()
    }

    val list = listOf(Fruit(), Fruit(), Fruit(), Fruit())
    //来看下sortedWith方法的声明sortedWith(comparator: Comparator<in T>)
    //Comparator声明成了逆变(contravariant)，这和Java的泛型通配符super一样的
    //所以只能传递Fruit以及Fruit父类的Comparator
    list.sortedWith(foodComparator)
    list.sortedWith(fruitComparator)
    //list.sortedWith(appleComparator) 编译报错

    //掌握了Java泛型通配符，也会很快掌握Kotlin的泛型协变和逆变
    //不同的是：Java只能在声明变量用到泛型的地方使用泛型变异，称之为use-site variance
    //Kotlin不仅支持use-site variance还支持 declaration-site variance
    //declaration-site variance 就是在声明类的时候声明泛型变异，如上面使用的Kotlin List就是在定义类的时候声明泛型变异
    //在下面的copyData和sortedWith都是use-site variance，即在用到的时候定义泛型变异



}

//也可以在声明泛型变量的时候使用协变、逆变
fun <T> copyData(source: MutableList<out T>,
                 destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}

public fun <T> sortedWith(comparator: Comparator<in T>) {

}
