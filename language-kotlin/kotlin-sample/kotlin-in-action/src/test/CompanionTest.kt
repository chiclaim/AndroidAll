package test

import object_keyword.ObjectKeywordTest
import object_keyword.ObjectKeywordTest2

/**
 * Desc: 访问companion属性、方法 演示
 * Created by Chiclaim on 2018/9/20.
 */
fun main(args: Array<String>) {

    println("flag=${ObjectKeywordTest.GENDER}, username=${ObjectKeywordTest.username}")


    ObjectKeywordTest2.run()
}
