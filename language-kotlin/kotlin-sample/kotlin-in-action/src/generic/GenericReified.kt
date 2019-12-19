package generic

/**
 * Desc:
 * Created by Chiclaim on 2018/10/9.
 */

//starting===========关于泛型为空 ================================
class Processor<T> {
    //value是可以为null，尽管没有使用‘?‘做标记
    fun process(value: T) {
        value?.hashCode()
    }
}

//使用泛型约束，则T不能为空
class Processor2<T : Any> {
    fun process(value: T) {
        value.hashCode()
    }
}

class Processor3<T : Any?> {
    fun process(value: T) {
        value?.hashCode()
    }
}
//ending=======================================================


//starting===========泛型擦除 type erasure ================================
//和Java一样，在运行时Kotlin的泛型也会被擦除，所以说泛型只是在编译时有效
fun isList(obj: Any): Boolean {
    //Cannot check for instance of erased type: List<String>
    //return obj is List<String>
    return obj is List<*>
}

fun isList2(obj: Any): Boolean {
    //Unchecked cast: Any to List<Int>
    //return obj as? List<Int> != null

    return obj as? List<*> != null
}
//ending===========泛型擦除 type erasure ================================


//starting===========reified type ================================

//我们知道泛型在运行时会擦除，但是在inline函数中我们可以指定泛型不被擦除，因为inline函数在编译期会copy到调用它的方法里
//所以编译器会知道当前的方法中泛型对应的具体类型是什么，然后把具体类型替换泛型，从而达到不被擦除的目的
//在inline函数中我们可以通过reified关键字来标记这个泛型不被擦除

//Error: Cannot check for instance of erased type: T
//fun <T> isType(value: Any) = value is T

inline fun <reified T> isType(value: Any) = value is T

//这个是很有用的特性，因为我们在封装的时候，为了尽可能的复用代码，通常把泛型封装到最底层，但是受到泛型运行时擦除的限制
//比如下面的的逻辑：请求网络，成功后把返回的json解析成对应的bean，所有的网络请求都有这样的逻辑，所以把网络请求和json解析成bean的逻辑通过泛型抽取出来
//但是不同的逻辑对应不同的bean，由于泛型参数在运行时被擦除所以拿不到上层传下的bean类型，从而json解析成bean的时候失败
//通过inline和reified就可以很好的解决这个问题，达到代码重用最大化

/*
//第一版本网络请求逻辑
@Override
public Observable<List<Menu>> getMenuList(final MenuListRequest request) {
    return RxUtils.wrapCallable(new Callable<HttpResult<HttpBean<List<Menu>>>>() {
        @Override
        public HttpResult<HttpBean<List<Menu>>> call() throws Exception {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MenuHttpConstant.MenuList.ENTITY_ID, request.getEntityId());
            paramMap.put(MenuHttpConstant.MenuList.SEAT_CODE, request.getSeatCode());
            Type listType = new TypeToken<HttpResult<HttpBean<List<Menu>>>>() {
            }.getType();
            RequestModel requestModel = HttpHelper.getDefaultRequestModel(paramMap, MenuHttpConstant.MenuList.METHOD)
                    .newBuilder().responseType(listType).build();
            ResponseModel<HttpResult<HttpBean<List<Menu>>>> responseModel = NetworkService.getDefault().request(requestModel);
            return responseModel.data();
        }
    });
}


//第二版本：抽取网络请求逻辑
fun <T> requestRemoteSource(paramMap: Map<String, Any?>, method: String, responseType: Type): Observable<T> {
    return RxUtils.wrapCallable<T> {
        val requestModel = HttpHelper.getDefaultRequestModel(paramMap, method)
                .newBuilder()
                .responseType(responseType)
                .build()
        val responseModel = NetworkService.getDefault().request<HttpResult<HttpBean<T>>>(requestModel)
        responseModel.data()
    }
}

//第三版本：通过inline和reified重构代码
inline fun <reified T> requestRemoteSource(paramMap: Map<String, Any?>, method: String): Observable<T> {
    val responseType = object : TypeToken<HttpResult<HttpBean<T>>>() {}.type
    return requestRemoteSource(paramMap, method, responseType)
}

 */

//--------reified type很好用，它有以下限制：
//1, 只能用于类型检查和转换 ( is , !is , as , as? )
//2, 用于Kotlin反射
//3, 获取泛型对应的class(java.lang.Class(::class.java))
//4, 调用其他函数时作为类型参数

//--------reified type很好用，不能用于：
//1, Create new instances of the class specified as a type parameter
//2, Call methods on the companion object of the type parameter class
//3, Use a non-reified type parameter as a type argument when calling a function with a reified type parameter
//4, Mark type parameters of classes, properties, or non-inline functions as reified


//--------需要注意的是，inline reified type的函数不能被Java方法调用，只能被Kotlin函数调用
//反编译后的class可以知道， inline reified type的函数是private的

//ending===========================================















