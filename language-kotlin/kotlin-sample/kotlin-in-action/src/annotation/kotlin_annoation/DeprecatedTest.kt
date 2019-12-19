package annotation.kotlin_annoation

class DeprecatedTest{
    @Deprecated("use greeting", ReplaceWith("greeting()",
            "annotation.kotlin_annoation.DeprecatedTest.greeting()"))
    fun sayHello(){
        println("hello...")
    }

    fun greeting(){
        println("hello...")
    }
}

fun main() {
    val d = DeprecatedTest()
    d.sayHello()
}

