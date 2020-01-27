


前面我们介绍了 Retrofit 的基本原来以及如何自定义 CallAdapter。今天我们来看看 Retrofit 是如何整合 RxJava、Kotlin Coroutine 的。


## 整合 RxJava 

Retrofit 结合 RxJava 使用非常简单，只需要将接口方法的返回类型改成 `Observable` 即可：

```
private interface UserService {

    // Observable<Data>
    @POST("register")
    @FormUrlEncoded
    fun registerByRxJava(
        @Field("username") username: String?,
        @Field("mobile") mobile: String
    ): Observable<ResponseModel<User>?>

    // Observable<Response<Data>>
    @POST("register")
    @FormUrlEncoded
    fun registerByRxJava2(
        @Field("username") username: String?,
        @Field("mobile") mobile: String
    ): Observable<Response<ResponseModel<User>?>>
}
```

用法也非常简单：

```
private val userService by lazy {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(FileUploadActivity.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        //.addCallAdapterFactory(MyCallAdapterFactory())
        // RxJava2CallAdapter
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    retrofit.create(UserService::class.java)
}

// 发起网络请求
userService.registerByRxJava("chiclaim", "110")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ responseModel: ResponseModel<User>? ->
            // todo ...
        }, {
            // todo ...
        })
```

可以看出 Observable 的泛型参数有两种，一种直接是接口返回的数据对应的 bean；还有一种是 Response。

经过前面文章的分析，我们知道，如果服务器返回的 http code 为 204 或 205，Retrofit 将 body 设置为 null：

```
if (code == 204 || code == 205) {
  rawBody.close();
  return Response.success(null, rawResponse);
}
```
如果此时，我们的方法的返回值是 `Observable<Data>`，Retrofit 肯定会抛出异常的。

```
java.lang.NullPointerException: Null is not a valid element
```

至于为什么会抛出这个异常，我们后面再来统一分析。

从上可以看出，Retrofit 整合 RxJava 非常简单，我们在实际开发中，需要对异常进行统一处理，指定被观察者的执行的线程等。

好，我们可以通过自定义 CallAdapter 来指定被观察者执行的线程以及对异常进行统一封装处理。

前一篇文章我们已经介绍了如何自定义 CallAdapter，所以我们直接看代码：

```

// CallAdapterFactory 工厂
internal class SubscribeOnCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        // 如何方法的返回值不是 Observable，则跳过 SubscribeOnCallAdapterFactory
        if (getRawType(returnType) != Observable::class.java) {
            return null 
        }
        
        // 我们自定义的 SubscribeOnCallAdapterFactory 也需要加到 Retrofit 中去才能生效，而且要加到 RxJava2CallAdapterFactory 之前
        // nextCallAdapter 实际上是 RxJava2CallAdapterFactory
        // 本质上我们还是依赖 RxJava2CallAdapterFactory 这一桥梁，然后对其适配(adapt) 出来的 Observable 添加我们功能
        
        val delegate = retrofit.nextCallAdapter(this, returnType, annotations) as CallAdapter<Any, Observable<ResponseModel<*>>>

        return object : CallAdapter<Any, Any> {
            override fun adapt(call: Call<Any>): Any {
                val o: Observable<ResponseModel<*>> = delegate.adapt(call)
                return o.subscribeOn(Schedulers.io()) // 统一指定被观察者执行的线程
                    .onErrorResumeNext(ErrorFunction()) // 统一封装异常处理
            }

            override fun responseType(): Type {
                return delegate.responseType()
            }
        }
    }
}

// 统一封装异常处理
public class ErrorFunction implements Function<Throwable, ObservableSource<ResponseModel<?>>> {

    @Override
    public ObservableSource<ResponseModel<?>> apply(Throwable throwable) {
        return Observable.error(ExceptionHelper.transformException(throwable));
    }

}

// 统一封装异常处理
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

使用的时候我们就不用再指定被观察者的所在的线程了：

```
userService.registerByRxJava("chiclaim", "110")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseModel: ResponseModel<User>? ->
                // todo...
            }, {
                // 异常处理，这里的异常实际上我们封装的 ApiException
            })
```

我们还可以对其进一步封装，因为我们要获取的是 ResponseModel 里面的 User 对象，但是我们服务器通常会返回模板结构，里面包含了业务数据，如：

```
class ResponseModel<T> {

    var status: Int = 0

    var data: T? = null

    var errorCode: String? = null

    var message: String? = null

}
```

我们可以通过 `compose` 操作符对其进行转化：

```
userService.registerByRxJava("chiclaim", "110")
    .observeOn(AndroidSchedulers.mainThread())
    .compose(ResponseTransformerHelper.transformResult())
    .subscribe({ user: User? ->
        // todo ...
    }, {
        // hanlde ApiException
    })


class ResponseTransformerHelper {
    companion object {
        fun <T> transformResult(): ObservableTransformer<ResponseModel<T>?, T> {
            return ObservableTransformer { upstream ->
                upstream.flatMap { responseModel ->
                    if (responseModel.status != 0) {
                        Observable.error(ApiException(responseModel.errorCode, responseModel.message))
                    } else {
                        Observable.just(responseModel.data)
                    }
                }
            }
        }
    }
}

```

## 整合 RxJava 源码分析

对于上面的通过自定义 CallAdapter 来实现统一封装错误处理以及统一指定被观察者所在的线程,我们就不进行源码分析了，因为一方面上面的注释已经很清楚了，另一方面关于自定义 CallAdapter 已经在 [Android Retrofit 源码系列（二）~ 自定义 CallAdapter](https://chiclaim.blog.csdn.net/article/details/103944895) 详细介绍过了。

好，那我们来分析两个问题：

- Retrofit 是怎么通过 RxJava2CallAdapterFactory 将 RxJava 集成进来的？
- 当 HTTP code 为 204 或 205，方法的返回类型为 `Observable<Data>` 时为什么会抛出异常？


为了大家更好的理解，我们先对 Retrofit 关于 CallAdapter 的执行流程做一个简单的回顾，首先来看下 Retrofit 执行流程：

```
Proxy.newProxyInstance()
    -> InvocationHandler.invoke()
        -> loadServiceMethod()
            -> ServiceMethod.parseAnnotations()
                -> HttpServiceMethod.parseAnnotations()
```
`HttpServiceMethod.parseAnnotations` 方法会返回以下 HttpServiceMethod 三个子类的其中一个：

- CallAdapted
- SuspendForResponse
- SuspendForBody

然后从 CallAdapterFactory 集合中获取 CallAdapter 将其传递给上面三个子类的构造方法，三个子类的 adapt 核心方法实际上调用的就是 CallAdapter 的 adapt 方法。

所以我们重点来看 RxJava2CallAdapterFactory 生产的 `RxJava2CallAdapter` 即可。

```
public final class RxJava2CallAdapterFactory extends CallAdapter.Factory {
  
  // 省略其他代码...

  @Override public @Nullable CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
    Class<?> rawType = getRawType(returnType);

	// RxJava Completable
    if (rawType == Completable.class) {
      return new RxJava2CallAdapter(Void.class, scheduler, isAsync, false, true, false, false,
          false, true);
    }

    boolean isFlowable = rawType == Flowable.class;
    boolean isSingle = rawType == Single.class;
    boolean isMaybe = rawType == Maybe.class;
    if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
      return null;
    }

    boolean isResult = false;
    boolean isBody = false;
    Type responseType;
	
    // 省略类型检查...

    Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
    Class<?> rawObservableType = getRawType(observableType);
    if (rawObservableType == Response.class) {
      // 省略类型检查...
      responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
    } else if (rawObservableType == Result.class) {
      // 省略类型检查...
      responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
      isResult = true;
    } else {
      responseType = observableType;
      isBody = true;
    }
    return new RxJava2CallAdapter(responseType, scheduler, isAsync, isResult, isBody, isFlowable,
        isSingle, isMaybe, false);
  }
}

// RxJava2CallAdapter

final class RxJava2CallAdapter<R> implements CallAdapter<R, Object> {
  
  // 省略构造方法和成员变量...

  @Override public Type responseType() {
    return responseType;
  }

  @Override public Object adapt(Call<R> call) {
  
    // 将 Call 转化成 Observable
    // 如果是异步的则使用 CallEnqueueObservable
    Observable<Response<R>> responseObservable = isAsync
        ? new CallEnqueueObservable<>(call)
        : new CallExecuteObservable<>(call);

    Observable<?> observable;
    if (isResult) {
      // 如果方法的返回类型是 Observable<Result<XXX>>
      observable = new ResultObservable<>(responseObservable);
    } else if (isBody) {
      // 如果方法的返回类型是 Observable<XXX>
      observable = new BodyObservable<>(responseObservable);
    } else {
      // 如果方法的返回类型是 Observable<Response<XXX>>
      observable = responseObservable;
    }

    if (scheduler != null) {
      observable = observable.subscribeOn(scheduler);
    }

    if (isFlowable) {
      return observable.toFlowable(BackpressureStrategy.LATEST);
    }
    if (isSingle) {
      return observable.singleOrError();
    }
    if (isMaybe) {
      return observable.singleElement();
    }
    if (isCompletable) {
      return observable.ignoreElements();
    }
    return RxJavaPlugins.onAssembly(observable);
  }
}
```

所以，上面最核心的代码为：

```
Observable<Response<R>> responseObservable = isAsync
        ? new CallEnqueueObservable<>(call)
        : new CallExecuteObservable<>(call);
```
是这段代码将 Call 转化成 Observable 了。我们来看下 `CallEnqueueObservable`：

```
final class CallEnqueueObservable<T> extends Observable<Response<T>> {
  private final Call<T> originalCall;

  CallEnqueueObservable(Call<T> originalCall) {
    this.originalCall = originalCall;
  }

  // subscribeActual 方法是在我们调用 subscribe 的时候触发调用的
  // 在这里面会触发网络请求操作
  @Override protected void subscribeActual(Observer<? super Response<T>> observer) {
    Call<T> call = originalCall.clone();
    CallCallback<T> callback = new CallCallback<>(call, observer);
    observer.onSubscribe(callback);
    if (!callback.isDisposed()) {
      // 执行网络请求...
      call.enqueue(callback);
    }
  }

  private static final class CallCallback<T> implements Disposable, Callback<T> {
    private final Call<?> call;
    private final Observer<? super Response<T>> observer;
    private volatile boolean disposed;
    boolean terminated = false;

    CallCallback(Call<?> call, Observer<? super Response<T>> observer) {
      this.call = call;
      this.observer = observer;
    }

    @Override public void onResponse(Call<T> call, Response<T> response) {
      if (disposed) return;

      try {
        // 当网络返回后，将 Response 发送给观察者(observer)
        observer.onNext(response);

        if (!disposed) {
          terminated = true;
          observer.onComplete();
        }
      } catch (Throwable t) {
        // 省略异常处理代码...
      }
    }

    @Override public void onFailure(Call<T> call, Throwable t) {
      if (call.isCanceled()) return;

      try {
        observer.onError(t);
      } catch (Throwable inner) {
        Exceptions.throwIfFatal(inner);
        RxJavaPlugins.onError(new CompositeException(t, inner));
      }
    }
  }
}

```

这是异步情况下使用的 CallEnqueueObservable，同步情况下的 CallExecuteObservable 原理也是类似的，就不赘述了。（默认情况我们使用的是同步情况下的 CallExecuteObservable）

至此，我们就解答了第一个问题：**Retrofit 是怎么通过 RxJava2CallAdapterFactory 将 RxJava 集成进来的？**

解决了第一个问题，我们就明白了 RxJava 和 Retrofit 是怎么结合的了。第二个问题就简单了，之所以会抛异常，是因为 response body 为 null 时，将 null 发送给观察者的时候，`RxJava` 抛出异常：

```
CallExecuteObservable.subscribeActual
	-> observer.onNext(response);
		-> BodyObservable.onNext
			-> observer.onNext(response.body());
				-> SubscribeOnObserver.onNext(T t)
					-> actual.onNext(t)
						-> OnErrorNextObserver.onNext(T t)
							-> actual.onNext(t)
								-> ObserveOnObserver.onNext(T t)
									-> queue.offer(t)
										-> SpscLinkedArrayQueue.offer(final T e)

// SpscLinkedArrayQueue
@Override
public boolean offer(final T e) {
    if (null == e) {
        throw new NullPointerException("Null is not a valid element");
    }
    // 省略其他代码...
}
```


## 整合 Kotlin Coroutine

Retrofit 整合 Kotlin 协程更加简单，甚至不需要设置任何特殊的 CallAdapter：

```
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(FileUploadActivity.API_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

声明接口方法的时候将其声明为 suspend 函数即可，方法的返回类型直接就是你要的返回类型如：`Response<ResponseModel<XXX>>`、`ResponseModel<XXX>` 等：

```
interface UserService {
        @POST("register")
        @FormUrlEncoded
        suspend fun register(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): ResponseModel<User>?


        @POST("register")
        @FormUrlEncoded
        suspend fun register2(
            @Field("username") username: String,
            @Field("mobile") mobile: String
        ): Response<ResponseModel<User>>
    }
}
```

使用起来也非常简单：

```
private fun request1() {
    launch {
        try {
            
            val repsModel = userService.register("chiclaim", "110")
            
            // do something on UI thread...

        } catch (e: Exception) {
            
            // do something on UI thread...
        }
    }
}
```

### 整合 Kotlin Coroutine 源码分析

通过前面的分析我们知道，`HttpServiceMethod.parseAnnotations` 方法会返回以下 HttpServiceMethod 三个子类的其中一个：

- CallAdapted
- SuspendForResponse
- SuspendForBody

其中 `SuspendForResponse 和 SuspendForBody` 便是为协程准备的。

如果是我们的接口函数是 kotlin suspend 函数，且函数的返回类型是 Response，例如：

```
suspend fun register(
	   @Field("username") username: String,
	   @Field("mobile") mobile: String): Response<User>
```

`HttpServiceMethod.parseAnnotations` 将会返回 `SuspendForResponse` ：

```
static final class SuspendForResponse<ResponseT> extends HttpServiceMethod<ResponseT, Object> {
	private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;
	// 省略构造方法...
	@Override protected Object adapt(Call<ResponseT> call, Object[] args) {
	  call = callAdapter.adapt(call);
	  Continuation<Response<ResponseT>> continuation =
		  (Continuation<Response<ResponseT>>) args[args.length - 1];

	  try {
		return KotlinExtensions.awaitResponse(call, continuation);
	  } catch (Exception e) {
		return KotlinExtensions.suspendAndThrow(e, continuation);
	  }
	}
}

suspend fun <T : Any> Call<T>.awaitResponse(): Response<T> {
  return suspendCancellableCoroutine { continuation ->
	// 协程取消掉的时候，调用 Call.cancel()
    continuation.invokeOnCancellation {
      cancel()
    }
	
	// 调用 Call.enenque，真正开始执行网络请求任务
    enqueue(object : Callback<T> {
      override fun onResponse(call: Call<T>, response: Response<T>) {
		// 继续执行相应的协程，将 response 作为最后一个挂起点的返回值
        continuation.resume(response)
      }

      override fun onFailure(call: Call<T>, t: Throwable) {
        continuation.resumeWithException(t)
      }
    })
  }
} 
```
如果接口函数是 kotlin suspend 函数，但是函数的返回值不是 Response，如：

```
suspend fun register(
          @Field("username") username: String,
          @Field("mobile") mobile: String): User
```

`HttpServiceMethod.parseAnnotations` 将会返回 `SuspendForBody ` ，它和 SuspendForResponse 差别在于 SuspendForBody 将 `Response.body` 作为协程挂起点的返回值：

```
static final class SuspendForBody<ResponseT> extends HttpServiceMethod<ResponseT, Object> {
    private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;
    private final boolean isNullable;
	// 省略构造方法...
    @Override protected Object adapt(Call<ResponseT> call, Object[] args) {
      call = callAdapter.adapt(call);
      Continuation<ResponseT> continuation = (Continuation<ResponseT>) args[args.length - 1];

      try {
        return isNullable
            ? KotlinExtensions.awaitNullable(call, continuation)
            : KotlinExtensions.await(call, continuation);
      } catch (Exception e) {
        return KotlinExtensions.suspendAndThrow(e, continuation);
      }
    }
}

// awaitNullable 方法
@JvmName("awaitNullable")
suspend fun <T : Any> Call<T?>.await(): T? {
  return suspendCancellableCoroutine { continuation ->
	// 协程取消掉的时候，调用 Call.cancel()
    continuation.invokeOnCancellation {
      cancel()
    }
	// 调用 Call.enenque，真正开始执行网络请求任务
    enqueue(object : Callback<T?> {
      override fun onResponse(call: Call<T?>, response: Response<T?>) {
        if (response.isSuccessful) {
		  // 继续执行相应的协程，将 response.body 作为最后一个挂起点的返回值
          continuation.resume(response.body())
        } else {
          continuation.resumeWithException(HttpException(response))
        }
      }

      override fun onFailure(call: Call<T?>, t: Throwable) {
        continuation.resumeWithException(t)
      }
    })
  }
}
```

至此，我们就将 Retrofit 如何集成 RxJava、Kotlin Coroutine 分析完毕了。

**所有关于 Retrofit 的使用案例都在我的 [AndroidAll GitHub](https://github.com/chiclaim/AndroidAll)  仓库中。该仓库除了 Retrofit，还有其他 Android 其他常用的开源库源码分析，如「RxJava」「Glide」「LeakCanary」「Dagger2」「Retrofit」「OkHttp」「ButterKnife」「Router」等。除此之外，还有完整的 Android 程序员所需要的技术栈思维导图，欢迎享用。**

