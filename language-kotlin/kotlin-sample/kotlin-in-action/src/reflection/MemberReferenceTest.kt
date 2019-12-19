package reflection

import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1


class Book(val name: String, var author: String) {
    fun present() = "book's name = $name,  author = $author "
}

fun <T> printProperty(instance: T, prop: KProperty1<T, *>) {
    println("${prop.name} = ${prop.get(instance)}")
}

fun <T> changeProperty(instance: T, prop: KMutableProperty1<T, String>) {
    val value = prop.get(instance)
    prop.set(instance, "$value, Johnny")
}

fun main() {

    //property reference ====== start
    val prop = Book::name
    println(prop.javaClass)


    val book = Book("Kotlin从入门到放弃", "Chiclaim")

    printProperty(book, Book::name)
    printProperty(book, Book::author)

    changeProperty(book, Book::author)
    println("book's author is [${book.author}]")
    //property reference ====== end


    //function reference ====== start
    val fk = Book::present
    println("function name is ${fk.name}")
    println(fk.call(book))
    //function reference ====== end

}


/*


Book::name 会生成实现了 KProperty 接口的内部类

val prop = Book::name

KProperty1 prop = MemberReferenceTestKt$main$prop$1.INSTANCE;

final class MemberReferenceTestKt$main$prop$1 extends PropertyReference1 {
   public static final KProperty1 INSTANCE = new MemberReferenceTestKt$main$prop$1();

   public String getName() {
      return "name";
   }

   public String getSignature() {
      return "getName()Ljava/lang/String;";
   }

   public KDeclarationContainer getOwner() {
      return Reflection.getOrCreateKotlinClass(Book.class);
   }

   @Nullable
   public Object get(@Nullable Object receiver) {
      return ((Book)receiver).getName();
   }
}
 */