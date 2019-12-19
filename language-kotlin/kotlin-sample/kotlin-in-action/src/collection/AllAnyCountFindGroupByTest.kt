package collection

import lambda.Person
import lambda.list

/**
 * desc: all、any、count、find、firstOrNull、groupBy使用演示
 *
 * Created by Chiclaim on 2018/09/22
 */


fun main(args: Array<String>) {
    val predicate = { p: Person ->
        p.age > 20
    }

    //是否所有人的年龄大于20
    list.all(predicate).apply {
        println(this)
    }

    //是否有一个年龄大于20的人
    list.any(predicate).apply {
        println(this)
    }

    //尽量少用 !  （下面两句是等价的，少用!，避免增加理解难度）
    println(!list.all { person -> person.age == 27 })
    println(list.any { person -> person.age != 27 })

    //年龄大于20的总人数
    list.count(predicate).apply {
        println(this)
    }

    //获取第一个年龄大于20的人
    //find 等价 firstOrNull()
    list.find(predicate).apply {
        println(this)
    }


    //按年龄分组
    list.groupBy(Person::age).apply {
        println(this)
    }

}