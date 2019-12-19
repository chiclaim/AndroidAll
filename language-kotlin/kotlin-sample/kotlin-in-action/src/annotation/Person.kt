package annotation

/**
 * Desc:
 * Created by Chiclaim on 2018/10/15.
 */
class Person {

    @JsonName
    var firstName: String? = null

    @JsonName2("lastName")
    var lastName: String? = null

    @JsonName2(name = "email")
    var email: String? = null

    @JsonName3("sex", "male")
    var gender: String? = null

    @PropertyOnly
    private var adress: String? = null

    @MethodOnly
    fun run() {
        println("run...")
    }


}