
## 概述

我们知道 Retrofit 网络框架在 Android 开发中的使用是非常普遍的，而且可以很方便的和 RxJava、Kotlin Coroutine 结合使用。

最近花了一些时间研究了一下 Retrofit 框架，从框架的使用到其实现原理研究了一遍，也有一些心得体会。所以决定通过博客的方式将其分享出来，算是对这段时间研究 Retrofit 的归纳和总结，同时希望对大家有帮助。

关于 Retrofit 框架的文章预计会有好几篇，文章的特点如下：

- 文章的组织方式不会一头扎进源码分析一通，而是先从使用案例入手，然后分析其实现原理；

- Retrofit 是一个网络框架，那么最好是有一个服务端与之对应，这样才能更好的对网络请求进行调试。所以我搭建了一个 Tomcat + Servlet 服务器用来接收网络请求，在调试 Retrofit 框架的时候产生的问题，我将会通过分析源码的方式来解答。

- 分析源码的一个直接好处是知道其实现原理。除此以外呢？开源框架这么多，我们能不能提取、总结一些通用的东西出来，提高我们的代码水平。我将在本系列文章的最后一篇文章中汇总一下对 Retrofit 源代码分析的体会。


## 从简单的案例入手

Retrofit 的使用是非常简单的，看一下官方文档基本上就能上手了。所以本文不会介绍 Retrofit 每个注解的使用，如果对 Retrofit 不熟的，可以花几分钟的时间在 [retrofit官网](https://square.github.io/retrofit/) 上了解下。

下面我们来看一个简单的案例：
```
// 定义网络接口类
interface UserService {
	@POST("register")
	@FormUrlEncoded
	fun register(
		@Field("username") username: String,
		@Field("mobile") mobile: String
	): Call<ResponseModel<User>>
}

private val userService by lazy {
	// 构建 Retrofit 实例
	val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(API_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	
	// 创建网络接口的实现类
	retrofit.create(UserService::class.java)
}


private fun request() {

	// 调用业务方法，返回一个 Call 对象
	val call = userService.register("chiclaim", "110")
	showLoading()
	
	// 执行网络请求
	call.enqueue(object : retrofit2.Callback<ResponseModel<User>> {
		
		// 网络请求失败回调
		override fun onFailure(call: Call<ResponseModel<User>>, t: Throwable) {
			dismissLoading()
			ToastHelper.showToast(applicationContext, t.message)
			text_content.text = t.message
		}

		// 网络请求成功回调
		override fun onResponse(call: Call<ResponseModel<User>>, response: Response<ResponseModel<User>>) {
			dismissLoading()
			val repsModel = response.body()
			val code = response.code()
			if (code in 200..299) {
				text_content.text = (repsModel?.message)
				text_content.append("\n")
				text_content.append(repsModel?.data.toString())
			} else {
				text_content.text = "response code ${response.code()}\n${repsModel?.message}"
			}
		}

	})
}
```

上面的案例非常简单，和官网的介绍大同小异，只不过是自己写了一个简单的后端程序来对接这个请求而已。

但是我们不能仅仅满足于 API 调用，我们还应该去了解其背后的实现原理。下面我将对该案例，抛出几个问题，然后通过分析源码的方式找到问题的答案。


## 抛出问题

针对上面的案例，我抛出了如下几个问题：

- 我们只是定义了接口（UserService），并没有定义实现类，Retrofit 如何产生实现类的？

- 在没有使用 Retrofit 之前，发送网络请求，我们需要自己组装参数，Retrofit 如何通过注解组装请求参数的？

- ConverterFactory 有什么用？它在哪个环节起作用的？



## 从源码中找到答案


上面的案例中的 retrofit.create() 方法就是用来创建业务实现类的：

```
retrofit.create(UserService::class.java)
```

然后我们看下 create 方法的具体实现：

```
public <T> T create(final Class<T> service) {
    validateServiceInterface(service);
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();
          private final Object[] emptyArgs = new Object[0];

          @Override public @Nullable Object invoke(Object proxy, Method method,
              @Nullable Object[] args) throws Throwable {
			  
            // 如果调用的方法是 Object 方法，则普通调用即可
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
			
			// 如果调用的方法是 interface default 方法（JDK1.8）
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
			
			// 如果调用的是我们定义的业务方法
            return loadServiceMethod(method).invoke(args != null ? args : emptyArgs);
          }
        });
}
```

发现它是使用动态代理实现的，所以第一步是要理解动态代理的基本原理。关于动态代理的作用和基本原理，我以前分析代理模式的时候阐述过，需要的可以移步[《设计模式 ~ 深入理解代理模式》](https://chiclaim.blog.csdn.net/article/details/100901769)

**所以对于第一个问题：“我们只是定义了接口（UserService），并没有定义实现类，Retrofit 如何产生实现类的？” 就很好回答了，Retrofit 通过动态代理的方式来实现的。** 

接下来就要分析 Retrofit 动态代理的具体实现了。在调用具体的业务方法的时候，如：

```
val call = userService.register("chiclaim", "110")
```

最终会调用到 InvocationHandler.invoke 方法，上面我已经在 invoke 方法中主要逻辑加上了注释说明。核心的代码逻辑就是：

```
loadServiceMethod(method).invoke(args != null ? args : emptyArgs);
```

那来看下 loadServiceMethod 方法 ：

```
ServiceMethod<?> loadServiceMethod(Method method) {
    ServiceMethod<?> result = serviceMethodCache.get(method);
    if (result != null) return result;

    synchronized (serviceMethodCache) {
      result = serviceMethodCache.get(method);
      if (result == null) {
        result = ServiceMethod.parseAnnotations(this, method);
        serviceMethodCache.put(method, result);
      }
    }
    return result;
}
```

里面主要逻辑是解析方法注解，封装成 ServiceMethod，然后放在 serviceMethodCache 缓存容器中。loadServiceMethod 方法的主要逻辑在 parseAnnotations 方法里：

```
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);

    Type returnType = method.getGenericReturnType();
    if (Utils.hasUnresolvableType(returnType)) {
      throw methodError(method,
          "Method return type must not include a type variable or wildcard: %s", returnType);
    }
    if (returnType == void.class) {
      throw methodError(method, "Service methods cannot return void.");
    }

    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

```

ServiceMethod.parseAnnotations 方法里主要是两个方法：

- RequestFactory.parseAnnotations
- HttpServiceMethod.parseAnnotations


我们先来看下 RequestFactory.parseAnnotations 方法：

```
static RequestFactory parseAnnotations(Retrofit retrofit, Method method) {
  return new Builder(retrofit, method).build();
}
  
RequestFactory build() {

  // 解析方法上的注解，如 @GET、@POST、@Multipart、@FormUrlEncoded
  for (Annotation annotation : methodAnnotations) {
	parseMethodAnnotation(annotation);
  }

  // 省略参数的校验...

  
  int parameterCount = parameterAnnotationsArray.length;
  parameterHandlers = new ParameterHandler<?>[parameterCount];
  // 将参数注解转化成 ParameterHandler
  // 所有的参数注解都有一个类与之对应，并且该类继承了 ParameterHandler
  // 这些类都是 ParameterHandler 的内部类
  // ParameterHandler 核心方法是 apply，用于将方法上的参数值添加到 RequestBuilder 当中
  for (int p = 0, lastParameter = parameterCount - 1; p < parameterCount; p++) {
	parameterHandlers[p] =
		parseParameter(p, parameterTypes[p], parameterAnnotationsArray[p], p == lastParameter);
  }

  // 省略参数的校验...

  return new RequestFactory(this);
}
```

`RequestFactory.parseAnnotations` 方法主要是将解析接口方法参数注解，封装在 RequestFactory 对象中，然后将其返回。

这样我们就回答了第二个问题：Retrofit 如何通过注解组装请求参数的？ 主要是通过 RequestFactory.parseAnnotations 解析方法上的注解。

RequestFactory 主要用于在后面创建 OkHttp 发起请求所需要的 Request 对象，这个后面会分析到。


**HttpServiceMethod.parseAnnotations**：

```
  static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
      Retrofit retrofit, Method method, RequestFactory requestFactory) {
    boolean isKotlinSuspendFunction = requestFactory.isKotlinSuspendFunction;
    boolean continuationWantsResponse = false;
    boolean continuationBodyNullable = false;

    Annotation[] annotations = method.getAnnotations();
    Type adapterType;
    if (isKotlinSuspendFunction) {
      Type[] parameterTypes = method.getGenericParameterTypes();
      Type responseType = Utils.getParameterLowerBound(0,
          (ParameterizedType) parameterTypes[parameterTypes.length - 1]);
      if (getRawType(responseType) == Response.class && responseType instanceof ParameterizedType) {
        // Unwrap the actual body type from Response<T>.
        responseType = Utils.getParameterUpperBound(0, (ParameterizedType) responseType);
        continuationWantsResponse = true;
      } else {
        // 省略注释...
      }

      adapterType = new Utils.ParameterizedTypeImpl(null, Call.class, responseType);
      annotations = SkipCallbackExecutorImpl.ensurePresent(annotations);
    } else {
      adapterType = method.getGenericReturnType();
    }

	// 创建 CallAdapter，后面会详细分析 CallAdapter 类
    CallAdapter<ResponseT, ReturnT> callAdapter =
        createCallAdapter(retrofit, method, adapterType, annotations);
    Type responseType = callAdapter.responseType();
    
	// 省略参数校验...

	// 创建 responseConverter 用于将请求的返回结果，转换成需要的对象
    Converter<ResponseBody, ResponseT> responseConverter =
        createResponseConverter(retrofit, method, responseType);

	// callFactory 是在 Retrofit.build 的时候默认设置进去的 OkHttpClient，用于执行网络请求
    okhttp3.Call.Factory callFactory = retrofit.callFactory;
	
	// 如果不是 kotlin suspend 函数
    if (!isKotlinSuspendFunction) {
      return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
    }
	
	// 如果是 kotlin suspend 函数，且函数的返回类型是 Response，例如：
	// suspend fun register(
    //        @Field("username") username: String,
    //        @Field("mobile") mobile: String): Response<User>
	// 这里的 register 方法就会本逻辑
	else if (continuationWantsResponse) {
      return (HttpServiceMethod<ResponseT, ReturnT>) new SuspendForResponse<>(requestFactory,
          callFactory, responseConverter, (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter);
    }
	
	// 其他情况。例如 suspend 函数，返回结果是 Body Data，而不是 Response，例如：
    // suspend fun register(
    //        @Field("username") username: String,
    //        @Field("mobile") mobile: String): User
	else {
      return (HttpServiceMethod<ResponseT, ReturnT>) new SuspendForBody<>(requestFactory,
          callFactory, responseConverter, (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter,
          continuationBodyNullable);
    }
  }

```


这样我们就将 ServiceMethod.parseAnnotations 方法里的：RequestFactory.parseAnnotations 和 HttpServiceMethod.parseAnnotations 分析完毕了。

由于 loadServiceMethod 的主要逻辑是 ServiceMethod.parseAnnotations，所以 loadServiceMethod 也着分析完毕了。所以接下来就要分析下 invoke 方法：

```
// 动态代理里的核心逻辑（这里在贴一下）
loadServiceMethod(method).invoke(args != null ? args : emptyArgs);


abstract class HttpServiceMethod {

	@Override final @Nullable ReturnT invoke(Object[] args) {
		Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
		return adapt(call, args);
	}

	// adapt 方法是一个抽象方法，具体使用哪个子类的实现取决于接口函数的使用方式，我们已经在 HttpServiceMethod.parseAnnotations 分析过了
	// 如果接口函数不是 kotlin suspend 则使用 CallAdapted 类
	// 如果是 kotlin suspend 函数返回类型是 Response，则使用 SuspendForResponse 类
	// 接口函数为其他情况如：kotlin suspend 函数返回类型不是 Response，则使用 SuspendForBody 类
	// 以上 3 个类都是 HttpServiceMethod 的子类
	protected abstract @Nullable ReturnT adapt(Call<ResponseT> call, Object[] args);
}
```

下面我们就来分析下 HttpServiceMethod 的三个子类：

- CallAdapted
- SuspendForResponse
- SuspendForBody

不管是哪个子类，它们的构造方法都依赖 ResponseConverter、CallAdapter（注意不是 CallAdapted）

所以我们先来分析下 ResponseConverter 的创建过程：

```
val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
```

我们是通过 addConverterFactory 方法将 GsonConverterFactory 转化器工厂加进去的， GsonConverterFactory 创建的是 `GsonResponseBodyConverter` :

```
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final Gson gson;
  private final TypeAdapter<T> adapter;

  GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
    this.gson = gson;
    this.adapter = adapter;
  }

  @Override public T convert(ResponseBody value) throws IOException {
    JsonReader jsonReader = gson.newJsonReader(value.charStream());
    try {
      T result = adapter.read(jsonReader);
      if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
        throw new JsonIOException("JSON document was not fully consumed.");
      }
      return result;
    } finally {
      value.close();
    }
  }
}
```

**GsonResponseBodyConverter 简而言之就是将响应体里的数据通过 GSON 转化成我们所需要的对象**


然后我们在来看 CallAdapter，实际上 CallAdapter 对象是在 HttpServiceMethod.parseAnnotations 方法创建的：

```
class HttpServiceMethod {

	static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
		  Retrofit retrofit, Method method, RequestFactory requestFactory) {
		// 创建 CallAdapter
		CallAdapter<ResponseT, ReturnT> callAdapter = createCallAdapter(retrofit, method, adapterType, annotations);
		// 省略其他代码...
	}
	
	// createCallAdapter
	private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
	  Retrofit retrofit, Method method, Type returnType, Annotation[] annotations) {
		try {
		  // 调用了 Retrofit.callAdapter
		  return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
		} catch (RuntimeException e) { // Wide exception range because factories are user code.
		  throw methodError(method, e, "Unable to create call adapter for %s", returnType);
		}
	}
}


public final class Retrofit {
	public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
		return nextCallAdapter(null, returnType, annotations);
	}
  
    // 我们再来看下 nextCallAdapter
	public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory skipPast, Type returnType,
	  Annotation[] annotations) {
	  
		// 省略参数校验...
	  
		// 由于上传传递进来的参数 skipPast 是 null，所以 start 就等于 0
		int start = callAdapterFactories.indexOf(skipPast) + 1;
		for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
		  // 从 callAdapterFactories 工厂集合中获取工厂，并且创建 CallAdapter
		  CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
		  if (adapter != null) {
			return adapter;
		  }
		}

		// 省略组装错误信息...
		throw new IllegalArgumentException(builder.toString());
	}
}
```

从 callAdapterFactories 工厂集合中获取工厂，并且创建 CallAdapter。但是我们文章开头的案例中并没有添加 CallAdapterFactory，我们只是添加了 ConverterFactory：

```
val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
```

其实是在 build 方法中添加默认的 CallAdapterFactory：

```
public Retrofit build() {
  
  // 省略其他逻辑...
  // 创建 CallAdapter 工厂集合
  List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>(this.callAdapterFactories);
  // 添加默认的 CallAdapterFactory
  callAdapterFactories.addAll(platform.defaultCallAdapterFactories(callbackExecutor));

  return new Retrofit(callFactory, baseUrl, unmodifiableList(converterFactories),
	  unmodifiableList(callAdapterFactories), callbackExecutor, validateEagerly);
}
```

我们在来看下 Platform.defaultCallAdapterFactories ：

```
class Platform {

  List<? extends CallAdapter.Factory> defaultCallAdapterFactories(
      @Nullable Executor callbackExecutor) {
	// 创建 DefaultCallAdapterFactory
    DefaultCallAdapterFactory executorFactory = new DefaultCallAdapterFactory(callbackExecutor);
    return hasJava8Types
        ? asList(CompletableFutureCallAdapterFactory.INSTANCE, executorFactory)
        : singletonList(executorFactory);
  }
  
}
```

然后我们看下 DefaultCallAdapterFactory 工厂是怎么创建 CallAdapter 的：

```
final class DefaultCallAdapterFactory extends CallAdapter.Factory {
  private final @Nullable Executor callbackExecutor;

  DefaultCallAdapterFactory(@Nullable Executor callbackExecutor) {
    this.callbackExecutor = callbackExecutor;
  }

  // 创建 CallAdapter
  @Override public @Nullable CallAdapter<?, ?> get(
      Type returnType, Annotation[] annotations, Retrofit retrofit) {
    
	// 省略参数校验...
	
    final Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);

	// 如果接口方法使用了 @SkipCallbackExecutor 注解，则不使用 callbackExecutor
    final Executor executor = Utils.isAnnotationPresent(annotations, SkipCallbackExecutor.class)
        ? null
        : callbackExecutor;

	// 返回 CallAdapter 匿名内部类对象
    return new CallAdapter<Object, Call<?>>() {
      @Override public Type responseType() {
        return responseType;
      }

      @Override public Call<Object> adapt(Call<Object> call) {
        return executor == null
            ? call
            : new ExecutorCallbackCall<>(executor, call);
      }
    };
  }
}


static final class ExecutorCallbackCall<T> implements Call<T> {

	// 省略其他逻辑...

	@Override public void enqueue(final Callback<T> callback) {
	  
	  // 调用 Call.enenque，真正开始执行任务
	  // delegate 实际上是 OkHttpCall
	  delegate.enqueue(new Callback<T>() {
		@Override public void onResponse(Call<T> call, final Response<T> response) {
		  // 将成功回调放在 callbackExecutor 中执行
		  callbackExecutor.execute(() -> {
			if (delegate.isCanceled()) {
			  callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
			} else {
			  callback.onResponse(ExecutorCallbackCall.this, response);
			}
		  });
		}

		@Override public void onFailure(Call<T> call, final Throwable t) {
		  // 将失败回调放在 callbackExecutor 中执行
		  callbackExecutor.execute(() -> callback.onFailure(ExecutorCallbackCall.this, t));
		}
	  });
	}

	// 省略其他逻辑...
}
```

至此，怎么创建 AdapterFactory 就搞清楚了。

这个时候可以来介绍 HttpServiceMethod 的三个子类了（CallAdapted、SuspendForResponse、SuspendForBody）

经过上面的分析我们知道，当我们接口方法不是 suspend 方法时，如：

```
@POST("users/new") fun createUser(@Body user: User): Call<User>
```

它会使用 CallAdapted：

```
  static final class CallAdapted<ResponseT, ReturnT> extends HttpServiceMethod<ResponseT, ReturnT> {
    private final CallAdapter<ResponseT, ReturnT> callAdapter;
    // 省略构造方法...
    @Override protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
      return callAdapter.adapt(call);
    }
  }
```

发现里面非常简单，就是直接调用 callAdapter.adapt 方法而已。

如果是我们的接口函数是 kotlin suspend 函数，且函数的返回类型是 Response，例如：

```
suspend fun register(
	   @Field("username") username: String,
	   @Field("mobile") mobile: String): Response<User>
```

Retrofit 会使用 SuspendForResponsem，SuspendForResponse 要比 CallAdapted，稍微复杂一点：

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

Retrofit 会使用 SuspendForBody，和 SuspendForResponsem 差别在于 SuspendForBody 将 Response.body 作为协程挂起点的返回值：

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

不管是 CallAdapted、SuspendForResponse、SuspendForBody，它们的 adapt 方法都依赖 OkHttpCall：

```
@Override final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
}
```

从名字上可以看出最终的请求是通过 OkHttp 来执行最终的网络请求，我们来看下 OkHttpCall：

```

final class OkHttpCall<T> implements Call<T> {

  // 省略成员变量...
  
  // 由于篇幅原因，只保留同步请求和异步请求...
  
  // 异步请求
  @Override public void enqueue(final Callback<T> callback) {

    okhttp3.Call call;
    Throwable failure;

    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;

      call = rawCall;
      failure = creationFailure;
      if (call == null && failure == null) {
        try {
		  // 里面的实现相当于 OkHttpClient.newCall
          call = rawCall = createRawCall();
        } catch (Throwable t) {
          throwIfFatal(t);
          failure = creationFailure = t;
        }
      }
    }

	// 省略其他代码...
	
	// 通过 OkHttp 发起异步网络请求...
    call.enqueue(new okhttp3.Callback() {
	
	  // 处理请求结果
      @Override public void onResponse(okhttp3.Call call, okhttp3.Response rawResponse) {
        Response<T> response;
        try {
		  // 将 OkHttp Response 转化成 Retrofit Response
          response = parseResponse(rawResponse);
        } catch (Throwable e) {
          throwIfFatal(e);
          callFailure(e);
          return;
        }

        try {
		  // 成功回调
          callback.onResponse(OkHttpCall.this, response);
        } catch (Throwable t) {
          throwIfFatal(t);
          t.printStackTrace();
        }
      }

	  // 处理网络失败情况
      @Override public void onFailure(okhttp3.Call call, IOException e) {
        callFailure(e);
      }

      private void callFailure(Throwable e) {
        try {
		  // 失败回调
          callback.onFailure(OkHttpCall.this, e);
        } catch (Throwable t) {
          throwIfFatal(t);
          t.printStackTrace();
        }
      }
    });
  }

  // 同步请求
  @Override public Response<T> execute() throws IOException {
    okhttp3.Call call;

    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;

      // 省略异常抛出...

      call = rawCall;
      if (call == null) {
        try {
		  // 里面的实现相当于 OkHttpClient.newCall
          call = rawCall = createRawCall();
        } catch (IOException | RuntimeException | Error e) {
          throwIfFatal(e);
          creationFailure = e;
          throw e;
        }
      }
    }

    if (canceled) {
      call.cancel();
    }
	// 将 OkHttp Response 转化成 Retrofit Response
    return parseResponse(call.execute());
  }

  private okhttp3.Call createRawCall() throws IOException {
	// 这里的 callFactory 默认是 OkHttpClient
	// requestFactory.create 用于创建 OkHttp 发起网络请求所需要的 Request 对象
    okhttp3.Call call = callFactory.newCall(requestFactory.create(args));
    if (call == null) {
      throw new NullPointerException("Call.Factory returned null.");
    }
    return call;
  }

  // 解析 OkHttp Response，将其转化成 Retrofit Response
  Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
    ResponseBody rawBody = rawResponse.body();

    // Remove the body's source (the only stateful object) so we can pass the response along.
    rawResponse = rawResponse.newBuilder()
        .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
        .build();

    int code = rawResponse.code();
	
	// HTTP code 非成功状态
    if (code < 200 || code >= 300) {
      try {
        ResponseBody bufferedBody = Utils.buffer(rawBody);
        return Response.error(bufferedBody, rawResponse);
      } finally {
        rawBody.close();
      }
    }

	// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status/204
	// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status/205
	// HTTP 204 表示该请求已经成功了，但是客户端客户不需要离开当前页面。默认情况下 204 响应是可缓存的。一个 ETag 标头包含在此类响应中
	// HTTP 205 表示该请求已经成功了，用来通知客户端重置文档视图
	// 204、205 Response.body 设置为 null，所以上层需要注意 body 为 null 的情况
    if (code == 204 || code == 205) {
      rawBody.close();
      return Response.success(null, rawResponse);
    }

    ExceptionCatchingResponseBody catchingBody = new ExceptionCatchingResponseBody(rawBody);
    try {
	  // 这里的 responseConverter 实际上就是 GsonResponseBodyConverter
	  // 这是在 build Retrofit 对象的时候设置的：
	  // val retrofit: Retrofit = Retrofit.Builder()
      //      .baseUrl(API_URL)
      //      .addConverterFactory(GsonConverterFactory.create())
      //      .build()
	  // 将会返回结果转成需要的类型
      T body = responseConverter.convert(catchingBody);
      return Response.success(body, rawResponse);
    } catch (RuntimeException e) {
      catchingBody.throwIfCaught();
      throw e;
    }
  }
}
```

介绍完 OkHttpCall ，我们也就能回答第三个问题了：“ConverterFactory 有什么用？它在哪个环节起作用的？”

`ConverterFactory` 主要用于对象的序列化和反序列化，比如说将请求的对象转成 JSON ，然后传递给服务器，获取将服务器返回的 JSON 转成我们需要的对象。

例如 `OkHttpCall.parseResponse` 用到的 Converter 将服务器返回的 JSON 转成我们需要的对象。

除此以外，在 OkHttpCall.parseResponse 方法中我们还发现，如果 HTTP code 如果是 204 或者 205，Response 的 body 会是 null，所以在使用的 Retrofit 的时候就一定要注意 body 空的情况。比如：

```
@POST("register")
@FormUrlEncoded
fun registerByRxJava(
	@Field("username") username: String?,
	@Field("mobile") mobile: String
): Observable<ResponseModel<User>?>
```

如果此时 HTTP code 是 204 或 205 ，虽然网络请求成功，但是会抛出空指针异常：

```
java.lang.NullPointerException: Null is not a valid element
```

这个我们将在介绍 Retrofit 结合 RxJava 使用的时候继续分析。


**至此，我们就以一个简单的案例为引子，将 Retrofit 是如何发起网络请求的整个过程介绍完毕了。**

后面我还将为大家介绍：
- Retrofit 如何自定义 CallAdapter？
- 如何整个 RxJava、Coroutine？
- 如何使用 Retrofit 文件上传
- Retrofit 文件上传遇到问题，如何通过分析源码的方式寻找答案

所有关于 Retrofit 的使用案例都在我的 [AndroidAll](https://github.com/chiclaim/AndroidAll)  GitHub 仓库中

该仓库除了 Retrofit，还有其他 Android 其他常用的开源库源码分析。而且还有完整的 Android 程序员所需要的技术栈思维导图，欢迎享用。


