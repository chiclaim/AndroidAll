package generic.java_generic;

import generic.java_generic.bean.*;

/**
 * Desc: 关于Java 泛型中的通配符：上界通配符(extends、协变)、下界通配符(super、逆变)
 * <p></p>
 * PECS(Producer Extends, Consumer Super)
 *
 * <p></p>
 *
 * <b>上界通配符</b>
 * <p></p>
 * <img src='res/generic_extends.png' width='400'/>
 * <p></p>
 * <b>下界通配符</b>
 * <p></p>
 * <img src='res/generic_super.png' width='400'/>
 * <p></p>
 * Created by Chiclaim on 2018/10/11.
 */
public class JavaGenericWildcardTest {

    //关于泛型通配符的两个问题：
    //1，泛型通配符的变量可能包含哪些类型？[上界、下界]，
    //2，我们能对这个泛型通配符变量做什么？[协变、逆变]

    //测试泛型通配符
    private static void testGenericWildcards() {

        Plate<Food> foodPlate = new Plate<>(new Food());
        Plate<Fruit> fruitPlate = new Plate<>(new Fruit());
        Plate<Apple> applePlate = new Plate<>(new Apple());
        Plate<Pear> pearPlate = new Plate<>(new Pear());
        //take2方法参数是上界通配符：意思就是最高到Fruit这个类(Fruit类及其子类)
        //takeExtendsFruit(foodPlate); 编译报错
        takeExtendsFruit(fruitPlate);
        takeExtendsFruit(applePlate);
        takeExtendsFruit(pearPlate);

        //----------------------------------

        //方法参数是下界通配符：意思就是最低是Apple这个类(Apple及其以上的类)
        takeSuperApple(foodPlate);
        takeSuperApple(fruitPlate);
        //takeSuperApple(applePlate); 编译报错
        //takeSuperApple(pearPlate);  编译报错

    }

    //测试泛型协变covariant/逆变contravariant
    private static void testGenericCovariation() {

        //1，定义一个能装水果的盘子，按道理应该也可以装苹果，但是编译器会提示错误：
        //      Incompatible types.
        //      Required:Plate<generic.java_generic.bean.Fruit>
        //      Found:Plate<generic.java_generic.bean.Apple>
        //Plate<Fruit> plate = new Plate<Apple>(new Apple());

        //可能有有人奇怪，我们可以使用下面的方式防止报错(new Plate<>)：
        //Plate<Fruit> plate = new Plate<>(new Apple());
        //这个就相当于下面的语句：
        //Explicit type argument Fruit can be replaced with <>
        //Plate<Fruit> plate2 = new Plate<Fruit>(new Apple());


        //takeFruit(new Plate<Apple>(new Apple())); //泛型之不变
        takeFruit(new Plate<>(new Apple()));//相当于test(new Plate<Fruit>(new Apple()));

        takeExtendsFruit(new Plate<Apple>(new Apple()));//泛型协变

        Plate<Apple> plate3 = new Plate<>(new Apple());
        //take方法需要一个装水果的盘子，但是把装苹果的盘子无法作为参数传递进去
        //take(plate3); //编译报错


        //2，泛型协变covariant(? extends T)，也叫[上界通配符]
        // 为了能把苹果盘子传递给take方法，需要用到泛型协变
        takeExtendsFruit(plate3);
        //使用了泛型协变，所有的水果盘子都能传递给take2方法，下面看下装梨的盘子传递给take2方法
        Plate<Pear> plate4 = new Plate<>(new Pear());
        takeExtendsFruit(plate4);


        //3，泛型逆变 contravariant 也叫做下界通配符（Lower Bounds Wildcards）
        Plate<? extends Fruit> plate5 = new Plate<>(new Apple());
        //plate5.set(new Fruit()); 编译报错
        //plate5.set(new Apple()); 编译报错
        Fruit fruit = plate5.get();            //编译正常
        //可以看出，泛型协变导致Fruit类的set方法失效，get方法正常
        //也就是说泛型T不能传递进来，只能返回出去给别人用，换句话说就是只能作为生产者
        //如果要想泛型能当做参数传递进来，就不能使用泛型协变了，使用泛型逆变
        Plate<? super Fruit> plate6 = new Plate<>(new Apple());
        plate6.set(new Pear());     //编译正常
        //Apple apple = plate6.get(); 编译报错
        //Pear pear = plate6.get();   编译报错
        //可以看出通过泛型逆变，泛型可以当做参数进行传递，set方法生效了
        //但是get方法失效，任何`水果`都不能接收get方法的返回值
        //也就是说泛型逆变可以传递泛型参数，不能返回泛型
        //换句话说就是泛型逆变只能作为消费者来消费传递进来的泛型参数，不能作为生产者把泛型返回出去

        //extends 协变：泛型只能当做方法返回值，不能当做方法参数
        //super   逆变：泛型只能当做方法参数，不能当做返回值

        //4，Java Collections工具类源码，关于协变和逆变的应用案例
        //List拷贝，因为要修改dest，所以把dest声明成逆变(super)，因为需要获取数据源src，所以把src声明成协变(extends)
         /*
            public static <T> void copy(List<? super T> dest, List<? extends T> src) {

            }
         */

        //5，PECS(Producer Extends, Consumer Super)
        //总结：什么实用extends、super
        //例如上面的Java Collections.copy方法：
        //当集合不需要修改的时候，只需要获取的时候，用extends，防止集合被修改
        //当集合不需要获取元素，只需要修改集合，用super，防止集合元素被修改

    }


    //如果一个泛型参数没有加上extends或super，那么他就是invariant：List<T> dest


    private static void takeFruit(Plate<Fruit> plate) {
    }

    //泛型协变. plate里可能装着Fruit或者它的子类(Fruit、Apple、Pear)
    private static void takeExtendsFruit(Plate<? extends Fruit> plate) {
        //plate.set(new Fruit()); 编译报错
        //plate.set(new Apple()); 编译报错
        Fruit fruit = plate.get(); //编译正常
    }

    //泛型逆变. plate里装着只有可能是Fruit及其父类(Fruit、Food)
    private static void takeSuperApple(Plate<? super Fruit> plate) {
        plate.set(new Apple());     //编译正常
        //Fruit fruit = plate.get(); //编译报错
        //Fruit pear = plate.get();   //编译报错
    }

}
