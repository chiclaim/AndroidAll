package annotation

import kotlin.reflect.KClass

/**
 * Desc:
 * Created by Chiclaim on 2018/10/15.
 */
interface Company {
    val name:
            String
}

data class CompanyImpl(override val name: String) : Company
data class Employee(
        val name: String,
        @DeserializeInterface(CompanyImpl::class) val company: Company
)

@Target(AnnotationTarget.PROPERTY)
annotation class DeserializeInterface(val targetClass: KClass<out Any>)