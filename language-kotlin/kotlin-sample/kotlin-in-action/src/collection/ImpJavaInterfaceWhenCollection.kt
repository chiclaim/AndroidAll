package collection

import base.Person
import java.io.File

/**
 * Desc: 由于Java和Kotlin在集合的可修改的差异性，Kotlin在实现Java接口（接口方法有集合参数）时需要注意：
 * 1，集合是否可为null
 * 2，集合里的元素是否可为null
 * 3，集合是否可修改
 *
 * Created by Chiclaim on 2018/9/28.
 */

//下面是Kotlin实现Java接口关于Nullability上的处理 的例子（from kotlin in action）

class FileIndexer : ListJava.FileContentProcessor {
    override fun processContents(path: File,
                                 binaryContents: ByteArray?,
                                 textContents: List<String>?) {
        // ...
        println()
    }
}


class PersonParser : ListJava.DataParser<Person> {
    override fun parseData(input: String,
                           output: MutableList<Person>,
                           errors: MutableList<String?>) {
        // ...
    }
}