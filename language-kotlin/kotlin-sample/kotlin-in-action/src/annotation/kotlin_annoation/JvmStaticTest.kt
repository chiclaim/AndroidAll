package annotation.kotlin_annoation

class JvmStaticTest {

    companion object {
        fun greeting() {
            println("hello...")
        }

        @JvmStatic
        fun sayHello() {
            println("hello...")
        }

    }

}

fun main() {
    JvmStaticTest.greeting()
    JvmStaticTest.sayHello()
}