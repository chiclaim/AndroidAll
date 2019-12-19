package object_keyword


/**
 * Desc: companion object 实现接口 演示
 * Created by Chiclaim on 2018/9/20.
 */
interface IAnimal {
    fun eat()
}

class ObjectKeywordTest4 {

    companion object : IAnimal {
        override fun eat() {
            println("eating apple")
        }
    }
}

fun feed(animal: IAnimal) {
    animal.eat()
}

fun main(args: Array<String>) {
    feed(ObjectKeywordTest4)//实际传递的是静态对象 ObjectKeywordTest4.Companion
}

/*
   public static final void main(@NotNull String[] args) {
      feed((IAnimal)ObjectKeywordTest4.Companion);
   }

 */