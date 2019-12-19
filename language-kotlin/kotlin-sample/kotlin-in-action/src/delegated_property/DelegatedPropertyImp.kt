package delegated_property

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Desc: Implementing delegated property
 * Created by Chiclaim on 2018/9/30.
 */


open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport("people")

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

class People(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

    var age: Int = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("age", oldValue, newValue)
        }

    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("salary", oldValue, newValue)
        }

}

fun testImp1() {
    val p = People("chiclaim", 18, 3500)
    p.addPropertyChangeListener(PropertyChangeListener { event ->
        println("event source: ${event.source}")
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    })
    p.age += 1
    p.salary += 1000
}

//========上面的setter方法都是重复代码，下面改造一下 starting========
class ObservableProperty(
        private val propName: String,
        private var propValue: Int,
        private val changeSupport: PropertyChangeSupport) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}

class People2(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) {
            _age.setValue(value)
        }
    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) {
            _salary.setValue(value)
        }
}

fun testImp2() {
    val p2 = People2("chiclaim", 28, 13500)
    p2.addPropertyChangeListener(PropertyChangeListener { event ->
        //println("event source: ${event.source}")
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    })
    p2.age += 1
    p2.salary += 1000
}
//================ending==========================


//=========继续进行改造，使用操作符重载和by关键字 starting=======
class ObservableProperty2(
        private var propValue: Int,
        private val changeSupport: PropertyChangeSupport) {
    operator fun getValue(p: People3, pro: KProperty<*>): Int = propValue
    operator fun setValue(p: People3, pro: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(pro.name, oldValue, newValue)
    }
}

class People3(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    //通过by实现属性委托，委托给ObservableProperty2
    var age: Int by ObservableProperty2(age, changeSupport)
    var salary: Int by ObservableProperty2(salary, changeSupport)
}

fun testImp3() {
    val p3 = People3("chiclaim", 38, 74500)
    p3.addPropertyChangeListener(PropertyChangeListener { event ->
        //println("event source: ${event.source}")
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    })
    p3.age += 1
    p3.salary += 1000
}

//=================ending==============================


//========使用Kotlin内置的Delegates.observable，可以不用开发者定义委托类 starting==============================
class People4(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

    private val observer = { prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)
}

fun testImp4() {
    val p4 = People4("chiclaim", 48, 104500)
    p4.addPropertyChangeListener(PropertyChangeListener { event ->
        //println("event source: ${event.source}")
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    })
    p4.age += 1
    p4.salary += 1000
}
//=================ending==============================


fun main(args: Array<String>) {
    testImp1()
    println("====================================================")
    testImp2()
    println("====================================================")
    testImp3()
    println("====================================================")
    testImp4()

}