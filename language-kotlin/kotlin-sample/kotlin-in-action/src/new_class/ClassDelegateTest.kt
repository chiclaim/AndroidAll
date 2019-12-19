package new_class

/**
 * Desc: Kotlin 代理类 演示
 * Created by Chiclaim on 2018/9/20.
 */

interface IAnimal {
    fun eat()
    fun run()
}

class Kangaroo : IAnimal {

    override fun eat() {
        println("eating grass ...")
    }

    override fun run() {
        println("jump...")
    }

}

//注意：如果使用val/var修饰了构造方法参数，记得要设置private，否则就暴露了被代理的对象了
class KangarooDelegate(private val kangaroo: Kangaroo = Kangaroo()) : IAnimal by kangaroo {

    //假如 需要对run方法进行增强改造
    override fun run() {
        kangaroo.run()
        println("shout...")
    }

    //kotlin会替我们生成其他代理方法，如 eat 方法
    /*
       public void eat() {
          this.kangaroo.eat();
       }
     */
}

fun main(args: Array<String>) {
    val k = KangarooDelegate()
    k.run()
}




