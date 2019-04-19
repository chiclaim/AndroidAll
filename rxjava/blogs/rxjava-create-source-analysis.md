
# 1 create操作符的基本使用

顾名思义，Create操作符是用来创建一个Observable的。下面来看一个简单的代码段：

```
Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            //Emit Data
        }
})
```

create方法接收一个参数`Observable.OnSubscribe`  来看下它的源码：

```
    /**
     * Invoked when Observable.subscribe is called.
     */
    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
        // cover for generics insanity
    }

```
﻿`Observable.OnSubscribe` 说白了就是一个继承了Action1接口的接口：

```
public interface Action1<T> extends Action {
    void call(T t);
}

```

```
/**
 * All Action interfaces extend from this.
 * <p>
 * Marker interface to allow instanceof checks.
 */
public interface Action extends Function {

}
```

```
/**
 * All Func and Action interfaces extend from this.
 * <p>
 * Marker interface to allow instanceof checks.
 */
public interface Function {

}
```



它们的继承关系如下：
Observable.OnSubscribe <- Action1 <- Action <- Function

create()方法也就是个工厂方法：

```
public static <T> Observable<T> create(OnSubscribe<T> f) {
    return new Observable<T>(hook.onCreate(f));
}
```


通过OnSubscribe的源码的注释
`Invoked when Observable.subscribe is called.` 意思是 当Observable被订阅(subscribe)
OnSubscribe接口的call方法会被执行。

知道如何创建(create)Observable, 接下来我们看下如何订阅它：

```
Observable.create(new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> subscriber) {
        for (int i = 0; i < 5; i++) {
            printLog(tvLogs, "Emit Data:", i + "");
            subscriber.onNext("" + i);
        }
    }
})
.subscribe(new Action1<String>() {
    @Override
    public void call(String s) {
        //showToast(s);
        printLog(tvLogs, "Consume Data:", s);
    }
});
```
当调用了subscribe方法 Observable.OnSubscribe的call方法就会被执行，在Observable.OnSubscribe的call方法中循环了调用了5次subscriber.onNext，在subscribe的Action1回调就会接受5次回调。

```
Emit Data:'0' , Thread Name:RxCachedThreadScheduler-1
Emit Data:'1' , Thread Name:RxCachedThreadScheduler-1
Emit Data:'2' , Thread Name:RxCachedThreadScheduler-1
Emit Data:'3' , Thread Name:RxCachedThreadScheduler-1
Emit Data:'4' , Thread Name:RxCachedThreadScheduler-1
Consume Data:'0' , Thread Name:main
Consume Data:'1' , Thread Name:main
Consume Data:'2' , Thread Name:main
Consume Data:'3' , Thread Name:main
Consume Data:'4' , Thread Name:main
```

从输出的日志可以看到，我们还打印了Thread Name线程的名称，我们可以控制发送数据、消费数据所在的线程。

```
.observeOn(AndroidSchedulers.mainThread())
.subscribeOn(Schedulers.io())
```
`subscribeOn` 设置Observable的call方法所在的线程 【生产数据】

`observeOn`   设置subscribe的call方法所在的线程【消费数据】

# 2 从源码角度分析create()和subscribe()如何协同工作的

从上面的分析我们知道，create方法就是一个简单的工厂方法：

```
public static <T> Observable<T> create(OnSubscribe<T> f) {
    return new Observable<T>(hook.onCreate(f));
}
```

直接new一个Observable 接收的参数由hook.onCreate方法返回（该方法也很简单）：

```
public <T> OnSubscribe<T> onCreate(OnSubscribe<T> f) {
    return f;
}
```

```
protected Observable(OnSubscribe<T> f) {
    this.onSubscribe = f;
}
```

> 总结下来一句话：create操作符创建Observable，Observable通过构造方法 保存了我们传进来的`OnSubscribe` 说白了就是`Action1`.

下面来看看Observable的subscribe方法的源代码：

```
public final Subscription subscribe(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                // do nothing
            }

            @Override
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }
        });
    }
```

从源码可以看出subscribe方法并没有直接调用传进来参数的方法（没有直接调用onNext.call()）。
而是通过`subscribe(Subscriber)`方法, subscribe(Subscriber)方法又是调用了Observable的私有静态方法：`private static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable)` 。下面是该方法的源码片段：

```
private static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        
		//remove some code ....

        
        try {
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.unsubscribed();
        }
    }
```

我们看关键部分就可以了：hook.`onSubscribeStart`(observable, observable.onSubscribe).`call`(subscriber);
看看hook.onSubscribeStart源码：

```
  public <T> OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance, final OnSubscribe<T> onSubscribe) {
      // pass-thru by default
      return onSubscribe;
  }
```

很简单，直接返回传递过来的参数(onSubscribe)。 这个OnSubscribe就是我们Observable.create（OnSubscribe）传递进去的OnSubscribe，然后调用OnSubscribe的call。
所以上面的代码可以简化为(便于理解)：observable.onSubscribe.call(subscriber).

> 至此，验证了那句话，只有当Observable被订阅OnSubscribe的call(subscriber)方法才会被执行。

我们知道了OnSubscribe的call(subscriber)执行的时机，那么是如何把生产的数据传递了Observable.subscribe方法的回调的呢？
我们通过Observable.subscribe源码得知，传递进来的回调(Action1),是通过Subscriber来执行Action1的回调，Subscriber又是Observable.create()参数的回调。

```
Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            //Emit Data
        }
})
```
所以Subscriber是Observable.OnSubscribe的回调和Observable.subscribe(Action1..)的Action1之间通信的桥梁。

Subscriber有三个方法：

+  onCompleted();
+  void onError(Throwable e);
+  void onNext(T t);

既然Subscriber是Observable.create(params)参数的回调和Observable.subscribe()参数回调的通信桥梁，Subscriber有三个方法，那么Observable.subscribe肯定也有三个与之对应回调，通过源码知道Observable.subscribe有很多重载方法：

+ public final Subscription subscribe(final Action1<? super T> onNext)
+ public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError)
+ public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete)

第三个重载方法的三个参数分别对应Subscriber的三个方法。

```
总结：Subscriber是Observable.create(Observable.OnSubscribe)参数回调和Observable.subscribe(Action1,[Action1,Action0])参数回调的通信桥梁.

需要你注意的是：如果调用了void onError(Throwable e)方法，那么onNext和onCompleted都不会执行。
```

下面用代码来表示他们之间的关系：

```
Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            for (int i = 0; i < 5; i++) {
                printLog(tvLogs, "Emit Data:", i + "");
                subscriber.onNext("" + i);//对应subscribe方法的第一个参数
                if (condition) {
                    subscriber.onError(Throwable);//对应subscribe方法的第二个参数
                }
            }
            subscriber.onCompleted(); //对应subscribe方法的第三个参数
        }
    })
    .observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
    .subscribe(new Action1<String>() {
        @Override
        public void call(String s) {
            //showToast(s);
            printLog(tvLogs, "Consume Data:", s);
            //onNext
        }
    }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
        	//onError
        }
    }, new Action0() {
        @Override
        public void call() {
        	//onCompleted
        }
    });
```



```
总结：
1. 只有当Observable被订阅OnSubscribe的call(subscriber)方法才会被执行
2. onCompleted方法里会把Subscription取消订阅（unsubscribe）
3. 如果调用了void onError(Throwable e)方法，那么onNext和onCompleted都不会执行。会在onError调用之前，把Subscription取消注册。
4. 整个事件流不管是正常结束（onComplete）还是出现了异常（onError），Subscription都会被取消注册（unsubscribe）。
   但是，由于我们可能执行一些耗时操作，界面又被关闭了，所以还需要把subscription取消注册
5. Subscriber是Observable.create(Observable.OnSubscribe)参数回调和Observable.subscribe(Action1,[Action1,Action0])参数回调的通信桥梁.

```

