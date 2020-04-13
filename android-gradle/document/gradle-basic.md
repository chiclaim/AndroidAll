
## gradle 入门

### 执行任务
```
gradle -q -b hello.gradle hello
```

+ -q 控制 gradle 日志输出级别（Log errors only）
+ -b 指定脚本文件，不指定默认找 build.gradle

加 `-q` 还不加 `-q` 的区别：

```
gradle -b hello.gradle hello

> Task :hello
hello world

BUILD SUCCESSFUL in 606ms
1 actionable task: 1 executed
```

```
gradle -b hello.gradle hello

hello world
```

> gradle 和 gradlew 命令的区别，gradlew 是 grade wrapper 的简称， gradlew 用于包装 gradle，让开发者统一使用某一个版本的 gradle，而不是依赖于系统的配置的 gradle 环境变量。

如何执行多任务，直接在后面加上 task 名称即可，空格分隔：

```
gradlew -q hello hello2
```

### 强制刷新依赖

```
/gradlew  --refresh-dependencies  assemble
```

### 通过名字缩写来执行任务

gradle 还支持通过任务的缩写来执行任务，如果某个任务名字很长，这个就是挺实用的。例如任务的名字叫做 connectCheck，执行该任务就可以这样写：`gradlew cc`

例如我们通过 bintray 来上传我们开发好的库，一般会首先执行 clean 任务，然后执行 bintrayUpload 任务，我们就可以这样写：`gradlew clean bU`


## Groovy 语言基础

Groovy 是基于 JVM 的一门动态语言，语法和 Java 语言比较相似，并且可以完全兼容 Java 代码。掌握 Groovy 语言对于编写复杂的 gradle 脚本还是很有裨益的。

### 字符串

gradle 脚本默认是用 Groovy 语言来编写的。在 Groovy 中可以使用单引号和双引号来定义字符串（Java中单引号用于定义字符）。

虽然单引号和双引号都可以用来定义字符串，他们的区别是，如果定一个纯粹的字符串常量使用单引号，如果字符串中有表达式则使用双引号。例如我们在 Android 中在 gradle 中配置依赖：

```
// 如果字符串中有表达式，使用双引号
implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

// 使用单引号定义纯粹的字符串常量
implementation 'androidx.appcompat:appcompat:1.1.0'
```

例如下面一段程序：

```
task testString {
    // 通过 def 关键字定义字符串
    def name = "chiclaim"
    println "my name is $name"
    println 'my name is $name'
}
```

控制台输出结果：

```
my name is chiclaim
my name is $name
```

可见单引号字符串会原样输出，不支持表达式。

### 方法

通过 def 关键字来定义方法。

#### 方法调用时括号可以省略


```
def method1(int a, int b) {
    println a + b
}

task invokeMethod {
    method1(1, 2)
    method1 1, 2 // 调用方法时省略括号
}
```

#### return 关键字可以省略

在定义有返回值方法的时候，可以省略 return，Groovy 会将方法执行过程中的最后一句当做返回值。

```
def method2(int a, int b) {
    if (a > b) {
        a  // 省略 return
    } else {
        b  // 省略 return
    }
}

task invokeMethod2 {
    def max = method2 2, 6
    println "max = $max"
}
```

#### 代码块可以作为参数传递

所谓代码块就是被花括号包围的代码，这就是我们后面要介绍的 `闭包`。例如上面我们遍历 nums 集合时候：

```
// 将代码块作为参数传递给 each 方法
nums.each({
    print it
})

// 如果方法最后一个参数是闭包，则闭包可以放到括号外面
nums.each(){
    print it
}

// 方法的括号是可以省略的
nums.each {
    print it
}
```

### Field 和 Property

Groovy 字段由以下几部分组成：

- 一个强制的访问修饰符 public, protected, or private
- 一个或多个可选修饰符 static, final, synchronized
- 一个可选的字段类型
- 一个强制的字段名称

```
class Data {
    private int id
    protected String description
    public static final boolean DEBUG = false
}
```

属性由以下几部分组成：

- 一个缺省(absent)访问修饰符（不能使用 public, protected, or private）
- 一个或多个可选修饰符 static, final, synchronized
- 一个可选的属性类型
- 一个强制的字段名称

Groovy 会生成对应的 setter/getter 方法，例如：

```
class Person {
    // 会创建 backing private String name 字段, a getName and a setName method
    String name

    // 会创建 backing private int age 字段, a getAge and a setAge method
    int age

    // 会创建 backing private int gender 字段, 只会创建 getter 方法，不会有 setter 方法
    final int gender
}
```

### 闭包

闭包其实就是一个包裹在花括号里的代码块，我们可以将闭包作为一个参数传递给方法，在方法里面可以像执行方法一样执行闭包：

```
// 函数参数为一个闭包
def closureList(closure) {
    for (int i in 1..10) {
        // 执行闭包代码，就像方法调用一样
        closure i
    }
}


task closureTest() {
    // 传递一个闭包给 closureList 方法
    closureList {
        println(it)
    }
}

```

上面的闭包只接受一个参数，如何传递多个参数给闭包呢？

```
def closureMap(closure) {
    def map = ['name': 'chiclaim', 'age': 18]
    map.each {
        // 传递两个参数给闭包
        closure(it.key, it.value)
    }
}


task closureTest() {
    // 通过 -> 符号来分开闭包参数，只有一个参数的时候默认是 it
    closureMap { k, v ->
        println("$k = $v")
    }
}
```

#### 闭包委托

闭包中有 3 个属性：thisObject、owner、delegate，默认情况下 owner 和 delegate 是相等的。delegate 是可以修改，这个功能是非常强大的，gradle 中的闭包很多都是通过修改 delegate 实现的。

我们先来看下在闭包中执行方法是使用 thisObject、owner、delegate 中的哪个？

```
// 闭包委托

def delegateMethod1() {
    println "context this = ${this.getClass()}"
    println "delegateMethod1 in root"
}


class Delegate {
    def delegateMethod1() {
        println "Delegate this = ${this.getClass()}"
        println "delegateMethod1 in Delegate class"
    }

    def test(closure) {
        closure(this)
    }
}

task closureDelegateTest {
    new Delegate().test {
        println "thisObject:${thisObject.getClass()}"
        println "owner:${owner.getClass()}"
        println "delegate:${delegate.getClass()}"


        // 这里的 this 和 thisObject 对象是一样的
        println "this in closure = ${this.getClass()}"

        // 优先使用 thisObject 对象来调用该方法
        delegateMethod1()
        it.delegateMethod1()
    }
}
```

输出结果：

```
thisObject:class build_64uzau9ivvf5jbis7jan38dhj
owner:class build_64uzau9ivvf5jbis7jan38dhj$_run_closure12
delegate:class build_64uzau9ivvf5jbis7jan38dhj$_run_closure12
this in closure = class build_64uzau9ivvf5jbis7jan38dhj
context this = class build_64uzau9ivvf5jbis7jan38dhj
delegateMethod1 in root
Delegate this = class Delegate
delegateMethod1 in Delegate class
```

Delegate 类的外部和内部都定义了 delegateMethod1 方法，但是在闭包内调用 delegateMethod1 方法是会优先调用外部的方法，也就是会先尝试调用 thisObject 对象里的方法。

它们之间的优先级是：thisObject>owner>delegate

下面我们看下如何修改 delegate：

```
class Human {
    def sayHello() {
        println "say hello by human"
    }

}

def sayHello() {
    println "say hello by outer"
}

def human(Closure<Human> closure) {
    Human human = new Human()
    closure.delegate = human
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    // 如果不改变 delegate，那么会优先调用外部的 sayHello
    // 将 delegate 设置为 human，就是调用 Human.sayHello
    closure(human)
}


task delegateChange {
    human {
        sayHello()
    }
}

```

控制台输出结果：

```
say hello by human
```

成功改变了 delegate。我们知道如果将下面的两行代码注释，就会调用外部的 sayHello：

```
closure.delegate = human
closure.setResolveStrategy(Closure.DELEGATE_FIRST)
```

注释后输出结果：

```
say hello by outer
```

## Reference
[《Android Gradle 权威指南》](#)
[http://www.groovy-lang.org/objectorientation.html](http://www.groovy-lang.org/objectorientation.html)











