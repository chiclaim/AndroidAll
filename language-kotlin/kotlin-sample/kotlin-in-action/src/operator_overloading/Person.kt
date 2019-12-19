package operator_overloading

/**
 * Desc:
 * Created by Chiclaim on 2018/9/29.
 */
class Person(val name: String, val age: Int) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        //先比较age，如果age相等，在比较name
        //比较字段是可变参数，可以输入多个
        return compareValuesBy(this, other, Person::age, Person::name)
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? Person
        return name == o?.name
    }
}
