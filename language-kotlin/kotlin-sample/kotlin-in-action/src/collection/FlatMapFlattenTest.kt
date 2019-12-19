package collection

import lambda.Person

/**
 * desc: flatMap、flatten函数使用演示
 *
 * Created by Chiclaim on 2018/09/22
 */

fun main(args: Array<String>) {

    val people = listOf<Person>(Person("Peter", 35).apply {
        val children = listOf<Person>(Person("Lily", 12), Person("Lucy", 8))
        this.children = children
    })

    people.map(Person::children).apply {
        println(this) //List<List<Person>>
    }

    //flatMap vs map

    //map函数把每个person的children集合放到新集合中，最终返回的记过是集合嵌套
    //flat是扁平的意思，flatMap把每个person的每个child放到集合
    people.flatMap { it.children }.apply {
        println(this) //List<Person>
    }


    //我们也可以把集合嵌套通过flatten函数将其扁平化
    //如上面的map函数返回的List<List<Person>扁平化成List<Person>
    people.map(Person::children).flatten().apply {
        println(this) //List<Person>
    }


}