package lambda

data class Person(var name: String, var age: Int) {
    lateinit var children: List<Person>
}

val list = listOf<Person>(Person("chiclaim", 18), Person("yuzhiqiang", 15),
        Person("johnny", 27), Person("clause", 190),
        Person("fin", 190))

