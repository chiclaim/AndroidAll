
通过上一篇对 [Retrofit 源码的分析](https://chiclaim.blog.csdn.net/article/details/103934516)，我们知道 CallAdapter 在 Retrofit 中扮演者非常重要的角色。

今天我们就来看看如何自定义 CallAdapter，来统一封装对错误信息的处理。然后，我们将通过源码来分析自定义的 CallAdapter 的整个执行流程。

## 自定义 CallAdapter

在自定义 CallAdapter 之前，我们先搞清楚需要哪些角色，在 Retrofit 中 CallAdapter 是通过工厂类来创建的，所以我们需要定义一个 CallAdapter 的工厂类；

为了和已有的 Call 区分，我们还需要自定义自己的 MyCall 接口，意思就是如果接口函数的返回类型是 MyCall，才会使用的我们自定义的 CallAdapter；有了 MyCall 接口，那么就需要一个对应的实现类来统一处理请求的结果（成功和失败），然后将成功或失败给我们自定义的 Callback。

所以自定义 CallAdapter 达到统一封装错误信息的目的，主要如下几个类：

- MyCallAdapterFactory
- MyCallAdapter
- MyCall
- MyCallImpl
- Callback
- ApiException


**Callback.java:**

```
interface Callback<T> {
	// 成功回调
    fun onSuccess(data: T?)
    // 失败回调
    fun onError(error: ApiException)
}
```

**MyCall.java:**

```
interface MyCall<T> {
	// 取消请求
    fun cancel()
    // 发起请求
    fun request(callback: Callback<T>)
    fun clone(): MyCall<T>
}
```

**MyCallImpl.java:**

```
private class MyCallImpl<T>(private val call: Call<T>, private val callbackExecutor: Executor?) : MyCall<T> {

    override fun cancel() {
        call.cancel()
    }
    
    override fun request(callback: Callback<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                // 局部函数
                fun processFailure() {
                    // 通过 transformException 函数统一处理异常
                    callback.onError(ExceptionHelper.transformException(t))
                }
                // 失败回调所执行的线程
                if (callbackExecutor != null) {
                    callbackExecutor.execute {
                        processFailure()
                    }
                } else {
                    processFailure()
                }
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                // 局部函数
                fun processResponse() {
                    val code = response.code()
                    if (code in 200..299) {
                        callback.onSuccess(response.body())
                    } else {
                        callback.onError(ApiException("---", "$code ${response.message()}"))
                    }
                }
                // 成功回调所执行的线程
                if (callbackExecutor != null) {
                    callbackExecutor.execute {
                        processResponse()
                    }
                } else {
                    processResponse()
                }
            }
        })
    }

    override fun clone(): MyCall<T> {
        return MyCallImpl(call.clone(), callbackExecutor)
    }
}

```

**MyCallAdapter.java:**

```
private class MyCallAdapter<R>(private val responseType: Type, private val executor: Executor?) : CallAdapter<R, MyCall<R>> {
    // 通过适配器模式将 Call 装成我们要的 MyCall
    override fun adapt(call: Call<R>): MyCall<R> {
        return MyCallImpl(call, executor)
    }

    override fun responseType(): Type {
        return responseType
    }
}
```

**MyCallAdapterFactory.java:**

```
class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        
        // 如果接口函数的返回类型不是 MyCall，则不使用 MyCallAdapter
        if (getRawType(returnType) != MyCall::class.java) return null
        // 返回类型的泛型校验
        check(returnType is ParameterizedType) { "MyCall must have generic type (e.g., MyCall<ResponseBody>)" }
        val responseType = getParameterUpperBound(0, returnType)
        // 实际上是 MainThreadExecutor
        val executor = retrofit.callbackExecutor()
		// 返回我们自定义的 MyCallAdapter
        return MyCallAdapter<Any>(responseType, executor)
    }
}
```

自定义的异常类 **ApiException.java**：

```
class ApiException(val errorCode: String?, errorMessage: String?) : Exception(errorMessage)
```

对异常统一处理，封装成我们自定义的异常 `ApiException`：

```
class ExceptionHelper {
    companion object {
        private const val ERROR_CODE = "error_code_001"
        @JvmStatic
        fun transformException(t: Throwable): ApiException {
            t.printStackTrace()
            return when (t) {
                is SocketTimeoutException -> ApiException(
                    ERROR_CODE,
                    "网络访问超时"
                )
                is ConnectException -> ApiException(
                    ERROR_CODE,
                    "网络连接异常"
                )
                is UnknownHostException -> ApiException(
                    ERROR_CODE,
                    "网络访问超时"
                )
                is JsonParseException -> ApiException(
                    ERROR_CODE,
                    "数据解析异常"
                )
                else -> ApiException(
                    ERROR_CODE,
                    t.message
                )
            }
        }

    }
}
```

然后把我们自定义的 `MyCallAdapterFactory` 应用到 Retrofit 当中去：

```
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(API_URL)
    // 添加 MyCallAdapterFactory 工厂
    .addCallAdapterFactory(MyCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

最后，定义业务接口函数的返回 `MyCall` 类型：

```
interface UserService {
    @POST("register")
    @FormUrlEncoded
    fun register2(
        @Field("username") username: String,
        @Field("mobile") mobile: String
    ): MyCall<ResponseModel<User>>
}

private fun customCall() {
    // MyCall
    val call = userService.register2("chiclaim", "110")
    call.request(object : Callback<ResponseModel<User>> {
        override fun onSuccess(data: ResponseModel<User>?) {
            // todo something...
        }

        override fun onError(error: ApiException) {
            // todo something...
        }
    })
}

```

至此，我们就完成了通过自定义 `CallAdapter` 来统一封装对网络错误的处理。


## 源码解读 CallAdapter

在上一篇文章中[《Android Retrofit 源码系列（一）~ 原理剖析》](https://chiclaim.blog.csdn.net/article/details/103934516) 我们也讲到了 CallAdapter，但是没有重点去分析 CallAdapter。

既然我们是通过 addCallAdapterFactory 函数来添加我们自定义的 CallAdapterFactory 的，那么我们看看里面是怎么实现的：

```
public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
  callAdapterFactories.add(Objects.requireNonNull(factory, "factory == null"));
  return this;
}
```
其实就是将其放进一个集合而已，那什么时候使用到了呢？

如果记得上一篇对 Retrofit 执行流程分析有印象的话，我们应该知道 Retrofit 执行流程为：

```
Proxy.newProxyInstance()
    -> InvocationHandler.invoke()
        -> loadServiceMethod()
            -> ServiceMethod.parseAnnotations()
                -> HttpServiceMethod.parseAnnotations()
```

`callAdapterFactories` 是在 `HttpServiceMethod.parseAnnotations` 函数中使用到了：

```
static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
  Retrofit retrofit, Method method, RequestFactory requestFactory) {

	// 省略其他代码...
	CallAdapter<ResponseT, ReturnT> callAdapter =  createCallAdapter(retrofit, method, adapterType, annotations);
}
```

我们发现是通过 `createCallAdapter` 方法来创建 `CallAdapter`：

```
private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
	  Retrofit retrofit, Method method, Type returnType, Annotation[] annotations) {
	try {
	  //noinspection unchecked
	  return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
	} catch (RuntimeException e) {
	  throw methodError(method, e, "Unable to create call adapter for %s", returnType);
	}
}
```

里面是通过 `retrofit.callAdapter` 来获取 `CallAdapter`：

```
public final class Retrofit {
    public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
        return nextCallAdapter(null, returnType, annotations);
    }

    public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory skipPast, Type returnType,
      Annotation[] annotations) {
        
        // 因为 skipPast = null，所以 start = 0
        // 也就是获取集合里面第一个 CallAdapterFactory，也就是我们自定义的 MyCallAdapterFactory
        int start = callAdapterFactories.indexOf(skipPast) + 1;
        for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
          // 也就是我们自定义的 MyCallAdapter
          CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
          if (adapter != null) {
            return adapter;
          }
        }
        // 省略其他代码...
    }
}

```

好，现在我们知道了 `CallAdapter` 是怎么创建了。在 `HttpServiceMethod.parseAnnotations()` 方法中创建完 `CallAdapter` ，然后将其传给了 `HttpServiceMethod` 的子类构造方法：

- CallAdapted
- SuspendForResponse
- SuspendForBody

这些类重写了 `HttpServiceMethod.adapt` 方法，里面实际上就是调用了传进来的 `CallAdapter.adapt` 方法。

我们知道在动态代理里调用了 `HttpServiceMethod.invoke` 方法：

```
@Override final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
}
```

`CallAdapter.adapt` 方法的 `call` 参数，其实就是 `OkHttpCall`，真正的请求操作都是封装在 `OkHttpCall` 里。

`invoke` 方法里面调用了 `HttpServiceMethod.adapt` 方法，其实也就是调用了我们自定义 `CallAdapter` 的 `adapt` 方法。

这个时候终于关联到我们自定义的 `MyCallAdapter` 了，在 `MyCallAdapter` 中调用 `call.enenque` 方法来执行网络请求，其实这里的 call 就是 `OkHttpCall`

然后将网络请求的返回结果（包括异常）交给我们自定义的 Callback.

至此，整个自定义的 CallAdapter 的执行过程我们就介绍完毕了。

后面我还将为大家介绍：
- 如何整个 RxJava、Coroutine？
- 如何使用 Retrofit 文件上传
- Retrofit 文件上传遇到问题，如何通过分析源码的方式寻找答案

**所有关于 Retrofit 的使用案例都在我的 [AndroidAll GitHub](https://github.com/chiclaim/AndroidAll)  仓库中。该仓库除了 Retrofit，还有其他 Android 其他常用的开源库源码分析，如「RxJava」「Glide」「LeakCanary」「Dagger2」「Retrofit」「OkHttp」「ButterKnife」「Router」等。除此之外，还有完整的 Android 程序员所需要的技术栈思维导图，欢迎享用。**