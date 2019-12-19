package higher_order_function

import base.Person

/**
 * Desc:
 * Created by Chiclaim on 2018/10/8.
 */
fun lookForChiclaim(people: List<Person>) {
    for (person in people) {
        if (person.name == "chiclaim") {
            println("Found!")
            return
        }
    }
    println("chiclaim is not found")
}

fun lookForChiclaim2(people: List<Person>) {
    people.forEach {
        if (it.name == "chiclaim") {
            println("Found!")
            return
        }
    }
    println("chiclaim is not found")
}

//这里会隐含一个lambda标签，名字叫做 forEach
fun lookForChiclaim3(people: List<Person>) {
    people.forEach {
        if (it.name == "chiclaim") {
            return@forEach
        }
    }
    println("chiclaim might be somewhere")
}

//也可以自定义lambda标签名字
fun lookForChiclaim4(people: List<Person>) {
    people.forEach label@{
        if (it.name == "chiclaim") {
            return@label
        }
    }
    println("chiclaim might be somewhere")
}

//lambda中使用label来return的方式，在有多个return表达式的时候，不好维护，可以使用匿名函数的方式
//anonymous function
//匿名函数默认是 local return
fun lookForChiclaim5(people: List<Person>) {
    people.forEach(fun(person) {
        if (person.name == "chiclaim") return
        println("${person.name} is not chiclaim")
    })
}

fun main(args: Array<String>) {
    val list = listOf(Person("johnny"), Person("chiclaim"), Person("jack"))
    lookForChiclaim(list)
    lookForChiclaim2(list)
    lookForChiclaim3(list)
    lookForChiclaim4(list)
    lookForChiclaim5(list)

}