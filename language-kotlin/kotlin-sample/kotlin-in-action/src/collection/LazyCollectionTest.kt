package collection

import lambda.Person
import lambda.list

/**
 * desc: lazy collection和非 lazy collection 对比演示
 *
 * Created by Chiclaim on 2018/09/23
 */


fun main(args: Array<String>) {
//    lazyCollectionTest()
//    println("============================")
//    lazyCollectionTest2()

    lazyCollectionTest3()
}

fun collectionTest() {
    list.map(Person::age).filter { age ->
        age > 18
    }

    /*

    通过编译后对应的Java代码可以发现，经过map和filter函数，创建了两个临时集合：

    Iterable $receiver$iv = (Iterable)PersonKt.getList();
      //创建集合
      Collection destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($receiver$iv, 10)));
      Iterator var4 = $receiver$iv.iterator();
      Object element$iv$iv;
      while(var4.hasNext()) {
         element$iv$iv = var4.next();
         Integer var11 = ((Person)element$iv$iv).getAge();
         destination$iv$iv.add(var11);
      }
      $receiver$iv = (Iterable)((List)destination$iv$iv);

      //创建集合
      destination$iv$iv = (Collection)(new ArrayList());
      var4 = $receiver$iv.iterator();

      while(var4.hasNext()) {
         element$iv$iv = var4.next();
         int age = ((Number)element$iv$iv).intValue();
         if (age > 18) {
            destination$iv$iv.add(element$iv$iv);
         }
      }
     */
}

fun lazyCollectionTest() {
    list.asSequence().map { person ->
        println("map ${person.age}")
        person.age
    }.filter { age ->
        println("filter $age")
        age > 20
    }//.toList()
            //或者下面的遍历
            .forEach {
                println("---------forEach $it")
            }

}

fun lazyCollectionTest2() {
    //把filter函数放置前面，可以有效减少map函数的调用次数
    list.asSequence().filter { person ->
        println("filter ${person.age}")
        person.age > 20
    }.map { person ->
        println("map ${person.age}")
        person.age
    }.forEach {
        println("---------forEach $it")
    }

}

//create sequence
fun lazyCollectionTest3() {
    generateSequence(0) {
        it + 1
    }.takeWhile {
        it <= 100
    }.sum().apply {
        println(this)
    }
}

/*
经过分析class字节码、对应的Java代码以及debug跟踪调试，lazy collection 有如下优点：

1，不会创建临时集合
2，用到集合元素的时候，如遍历或转化成新集合(forEach,toList)，才会触发集合的过滤、转化等操作。（某种意义上讲和RxJava有点类似）


 */



 
 