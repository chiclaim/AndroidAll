package generic

/**
 * desc:
 *
 * Created by Chiclaim on 2019/01/01
 */

interface MyList<out E> : Collection<E> {
    //泛型不允许当入参
    //public fun indexOf(element: E): Int
}