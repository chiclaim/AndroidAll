package delegated_property

/**
 * Desc: delegated property
 * Created by Chiclaim on 2018/9/30.
 */
/*
//代理属性实现原理
class Foo {
    private val delegate = Delegate()
    var p: Type
    set(value: Type) = delegate.setValue(..., value)
    get() = delegate.getValue(...)
}

class Delegate {
    operator fun getValue(...) { ... }
    operator fun setValue(..., value: Type) { ... }
}
 */

//Kotlin lazy函数就是使用了delegated property，实现原理大致如下（非线程安全）：
class Person(val name: String) {
    //存储值，不对外暴露，
    private var _emails: List<String>? = null
    //对外暴露，延迟加载
    val emails: List<String>
        get() {
            if (_emails == null) {
                _emails = loadEmail()
            }
            return _emails!!
        }

    private fun loadEmail() = listOf("chiclaim@gmail.com", "chiclaim@163.com")

}

//Kotlin lazy 的使用
class Person2(val name: String) {
    val emails by lazy {
        loadEmail()
    }

    private fun loadEmail() = listOf("chiclaim@gmail.com", "chiclaim@163.com")

}

//Kotlin lazy 的使用
class DelegatedProperty {
    val friends by lazy {
        loadFriendList()
    }

    private fun loadFriendList(): List<String> {
        return listOf("pony", "johnny", "jack")
    }
}

/*

Kotlin lazy 源码分析

private class SynchronizedLazyImpl<out T>(initializer: () -> T, lock: Any? = null) : Lazy<T>, Serializable {
    private var initializer: (() -> T)? = initializer
    //用于存储值。初始值为UNINITIALIZED_VALUE
    @Volatile private var _value: Any? = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    override val value: T
        get() {
            val _v1 = _value
            //如果不是初始值说明已经赋过值了，直接返回
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }
            //下面进入同步代码块
            return synchronized(lock) {
                //再判断一次是否是初始值，因为可能多个线程在同步代码块处等待
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    //执行初始化代码(initializer)，初始化逻辑从外面传进来的
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }
}
 */

fun main(args: Array<String>) {
    println(DelegatedProperty().friends)

}