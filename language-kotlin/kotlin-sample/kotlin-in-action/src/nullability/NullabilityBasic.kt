package nullability

/**
 * Desc: ? 、?. 、:? 、!! 、let 使用演示
 * Created by Chiclaim on 2018/9/18.
 */


// 在Kotlin中声明的变量、属性、方法参数等默认是不能为空，如果允许为空需要在后面加上'?'

//==========参数name不能为空===============
fun isEmpty0(name: String) = name.isEmpty()
//========================================


//==========参数name可以为空===============
fun isEmpty1(name: String?) = name.isNullOrBlank()
//========================================


//===========通过 ?. 操作符 来判空（可用于方法、属性）===============
//相当于 if(value!=null) return value.toUpperCase() return null
fun toUpperCase(value: String?) = value?.toUpperCase()

//和上面等价
fun toUpperCase2(value: String?): String? {
    if (value == null) {
        return null
    }
    return value.toUpperCase()
}
//================================================================


//===============使用多个 ?. 链式调用================================
//底层相当于if判断嵌套
fun getUpperCaseByte(value: String?) = value?.toUpperCase()?.toByte()
//=============================================================


//===========Elvis操作符 ?: 可用作判空的三目运算符，其他情况使用 if 实现三目运算符
//底层就是通过三目运算符实现 age == null ? 0 : age
fun safeAge(age: Int?): Int = age ?: 0
//=================================================================


//不要写这样的代码，因为getName()方法返回值是Any，可能返回Int也可能返回String
fun getName(name: String?) = name ?: 1


//===============非空断言 !! ========================================
//非空断言意思就是说 开发者保证这个变量一定不会为空，如果运行时变量为空，则抛出空指针异常
fun ignoreNulls(str: String?) {
    println(str!!.length)
}
//==================================================================


//===============let 函数判空========================================
fun console(message: String?) {

    message?.let {
        val result = it.substring(1)
        println(result)
    }

    message?.run {
        val result = substring(1)
        println(result)
    }

    message?.apply {
        val result = substring(1)
        println(result)
    }

    with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow I know alphabet!").toString()
    }



    //相当于
    //if (message != null) {
    //    println(it)
    //}

    message.run {
        println("run--------")
        1
    }

    run {
        println("run--------")
        1
    }
}


//==================================================================

fun main(args: Array<String>) {
    println(isEmpty0("chiclaim"))
    println(isEmpty1(null))

    println(toUpperCase(null))
    println(toUpperCase("chiclaim"))

    println("age---" + safeAge(null))
    println("age---" + safeAge(99))


    //getName()返回Integer
    println(getName(null).javaClass)
    //getName()返回String
    println(getName("chiclaim").javaClass)
}

