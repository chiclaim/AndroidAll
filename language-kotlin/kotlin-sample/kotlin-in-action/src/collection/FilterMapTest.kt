package collection

import lambda.Person
import lambda.list

/**
 * desc: filter、map函数 使用演示和原理分析
 *
 * Created by Chiclaim on 2018/09/22
 */


//filter函数的底层也是通过新建一个集合，然后遍历数据源，把符合条件的元素放进新的集合中
fun filterTest() {
    list.filter { person ->
        person.age > 18
    }.apply {
        println(this)
    }

    //找出年龄最大的人（可能存在多个）
    //我们在filter里面使用了maxBy函数，底层通过嵌套循环实现的，效率低(O(n^2)))
    list.filter { person ->
        person.age == list.maxBy(Person::age)!!.age
    }.apply {
        println(this)
    }

    //上面问题的改良版，把嵌套循环扁平化(O(n))
    val maxAge = list.maxBy(Person::age)?.age
    list.filter { person ->
        person.age == maxAge
    }.apply {
        println(this)
    }

}

//上面filter可以过滤元素，map可以修改集合的元素
fun mapTest() {

    //收集用户的名字
    list.map { person ->
        person.name
    }.apply {
        println(this)
    }

    //通过member reference替代上面的实现方式，需要把大括号改成小括号
    list.map(Person::name).apply {
        println(this)
    }

    //为每个人加1岁. 需要注意的是此时并不会新建新集合，操作的是原来数据源的元素
    list.map { person ->
        person.age += 1
    }
    println(list)// print original list


    //filter、map组合使用
    //底层是通过两个循环实现的
    //第一个循环是filter出年龄大于20
    //第二个循环是遍历上面的新集合，取出他们的名字
    list.filter { person ->
        person.age > 20
    }.map(Person::name).apply {
        println("age > 20 : $this")
    }
}

fun main(args: Array<String>) {
    filterTest()
    //mapTest()

}