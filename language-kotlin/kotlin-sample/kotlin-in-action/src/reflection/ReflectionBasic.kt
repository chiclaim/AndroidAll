package reflection

import kotlin.reflect.full.*

/**
 * Desc:
 * Created by Chiclaim on 2018/10/15.
 */

class Person(val name: String, val age: Int) {
    fun run() {
        println("run...")
    }
}


fun Person.getShowName(): String {
    return "@chiclaim"
}

val <T> List<T>.lastIndex: Int
    get() = size - 1

var Person.grade: String
    get() = ""
    set(value) {

    }

fun main(args: Array<String>) {
    val p = Person("chiclaim", 18)
    //val kClass = p.javaClass.kotlin
    val kClass = Person::class
    println("simpleName = ${kClass.simpleName}")
    kClass.memberProperties.forEach {
        println("memberProperties=${it.name}")
    }

    kClass.memberFunctions.forEach {
        println("memberFunctions=${it.name}")
    }

    kClass.memberExtensionFunctions.forEach {
        println("memberExtensionFunctions=${it.name}")
    }

    kClass.memberExtensionProperties.forEach {
        println("memberExtensionProperties=${it.name}")
    }

    kClass.declaredMemberProperties.forEach {
        println("declaredMemberExtensionProperties=${it.name}")
    }

    kClass.declaredMemberExtensionProperties.forEach {
        println("declaredMemberExtensionProperties=${it.name}")
    }
}