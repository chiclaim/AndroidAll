

## Jetpack 介绍


`Jetpack` 是一系列组件的集合，用于帮助开发者更加容易开发出高质量的 APP，这些组件帮助我们统一遵循最佳实践，这些最佳实践统一由组件实现，开发只需要使用这些组件即可，这样的话开发高质量的 App 就更加简单了。这些组件还可以帮助我们减少模板代码，简化复杂任务，让开发者更加专注于业务代码。

在 [Android 开发者官网](https://developer.android.com/) 中已经将 `Jetpack、Kotlin、AndroidStudio` 并列为一级分类，可见 `Jetpack` 的重要性。

`Jetpack` 主要由 `Foundation、Architecture、Behavior、UI` 4 部分组成：

|Foundation | Architecture |  Behavior  | UI |
|-|-|-|-|
|Android KTX | DataBinding | CameraX | Animation & transitions|
|AppCompat | Lifecycles | Media & playback |Emoji |
|Car | LiveData | Notifications | Fragment|
|Benchmark | Navigation | Permissions | Layout|
|MultiDex | Paging | Preferences | Palette |
|Security | Room |  Sharing| ViewPager2 |
|Test |ViewModel | Slices | WebView |
|TV |WorkManager | - | - |
|Wear OS by Google | - | - | - |

## Lifecycle 的引入

Lifecycle 相关的组件库非常多，如果你的工程是 **Java 工程**，则依赖的配置有：

```
dependencies {
    def lifecycle_version = "2.2.0"

    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
    // Lifecycles only (without ViewModel or LiveData)
    implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"

    // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    // Annotation processor
    annotationProcessor "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    // optional - ReactiveStreams support for LiveData
    implementation "androidx.lifecycle:lifecycle-reactivestreams:$lifecycle_version"

    // optional - Test helpers for LiveData
    testImplementation "androidx.arch.core:core-testing:$lifecycle_version"
}
```

如果你的工程是 **Kotlin 工程**，则依赖的配置有：
```
dependencies {
    def lifecycle_version = "2.2.0"
    def savedstate_version = "1.0.0"
    def arch_version = "2.1.0"

    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    // Lifecycles only (without ViewModel or LiveData)
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"

    // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$savedstate_version"

    // Annotation processor
    kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    // optional - ReactiveStreams support for LiveData
    implementation "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version"

    // optional - Test helpers for LiveData
    testImplementation "androidx.arch.core:core-testing:$arch_version"
}
```

大家可以根据需要添加依赖，关于 Lifecycle 依赖大家可以参考官网：[Declaring dependencies](https://developer.android.com/jetpack/androidx/releases/lifecycle)

## Lifecycle 介绍

`Lifecycle` 是 `Jetpack` 中的一个架构组件，用于感知 Activity、Fragment 组件的生命周期。

例如执行一个耗时任务，成功之后修改 UI，但是在执行耗时任务的时候，用户把界面关闭了，等到成功之后修改UI，肯定会 Crash。

所以组件对生命周期的感知是非常重要的。再比如，如果我们使用的是 MVP 架构来开发 APP，那么在 Presenter 中如果界面销毁了，我们会把 Presenter 中持有的 View，也就是界面的引用设置为 null，在 Presenter 中执行的网络请求成功后判断 View 是否为空，如果为空则不做任何处理，不为空则修改界面UI。

这里主要有两个问题：

- 需要在 Activity、Fragment 的 onDestroy 中手动调用 Presenter 的方法（如 destroy）将 View 置为 null
- 无法处理更细粒度的处理生命周期方法。例如我们只能在 Presenter 回调中根据 View 是否为空来决定渲染 View（生命周期状态是否为 Destroy），如果我们想在 Pause、Stop 状态也不渲染 View 呢？此时我们做不到这一点，因为 Presenter 感知不到组件的生命周期状态。

Lifecycle 组件可以轻松优雅的解决这些问题。下面我们就来看看 Lifecycle 的使用和原理分析吧。


## Lifecylce 案例分析


我们以 [官网的定位功能](https://developer.android.com/topic/libraries/architecture/lifecycle) 为案例进行分析。官网只给出了伪代码，我将其改成了可以运行的小 demo。

先简单的描述下该案例的基本逻辑：界面需要定位功能，但是在定位之前需要判断用户的状态（耗时任务），然后根据状态来决定是否开启定位。

如下面的伪代码：

```
class MyActivity : AppCompatActivity() {
    private lateinit var myLocationListener: MyLocationListener

    override fun onCreate(...) {
        myLocationListener = MyLocationListener(this) { location ->
            // update UI
        }
    }

    public override fun onStart() {
        super.onStart()
        Util.checkUserStatus { result ->
            // what if this callback is invoked AFTER activity is stopped?
            if (result) {
                myLocationListener.start()
            }
        }
    }

    public override fun onStop() {
        super.onStop()
        myLocationListener.stop()
    }

}
```

`checkUserStatus()` 方法就是用来检查用户状态的，如果该方法还在执行的时候，界面执行了 onStop，等到 checkUserStatus 执行回调的时候依然会启动定位；如果 checkUserStatus 还在执行的时候，如果界面被销毁(onDestroy)，程序可能会闪退。

接下来我们看下如何使用 `Lifecycle` 组件来解决这个问题：

```
class LifecycleDemoActivity : AppCompatActivity() {

    private lateinit var myLocationListener: MyLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        myLocationListener = MyLocationListener(this, lifecycle) { location ->
            // update UI
        }

        Util.checkUserStatus { result ->
            if (result) {
                myLocationListener.enable()
            }
        }

        lifecycle.addObserver(myLocationListener)
    }
}

internal class MyLocationListener(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: (Location) -> Unit
) : LifecycleObserver {

    private var enabled = false

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // connect if not connected
            start()
        }
    }
    
    // Activity、Fragment onStart 的时候调用
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            // connect
            Log.e("MyLocationListener", "start connect")
        }
    }
    
    // Activity、Fragment onStop 的时候调用
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // disconnect if connected
        Log.e("MyLocationListener", "disconnect")

    }
}

internal class Util {
    companion object {
        fun checkUserStatus(callback: (Boolean) -> Unit) {
            Handler().postDelayed({
                callback(true)
            }, 3000)
        }
    }
}
```

对上面使用 `Lifecycle` 组件做一个简单的描述：

- 上面的 `androidx.appcompat.app.AppCompatActivity` 不是 support 包下，而是 androidx 包下。
- 让 MyLocationListener 实现 `LifecycleObserver`，该接口是一个空接口
- 在 MyLocationListener 中的方法上使用 `@OnLifecycleEvent` 注解，在触发相应的生命周期的时候自动调用该方法。
- 最后将 MyLocationListener 交给 Lifecycle 管理：`lifecycle.addObserver(myLocationListener)`

然后运行程序，3s 之后关闭页面，输入如下所示：

```
E/MyLocationListener: start connect
E/MyLocationListener: disconnect
```
如果 3s 之内就关闭了页面，则不会启动定位：

```
E/MyLocationListener: disconnect
```

可见，开发者无需在 `Activity、Fragment` 生命周期方法中手动调用我们的业务方法。也可以在业务类中查询到当前的生命周期状态。

上面的案例中，我们在 start、stop 方法上使用了 `@OnLifecycleEvent` 注解，Lifecycle 组件会通过 APT 技术生成对应的类，例如我们是在 `MyLocationListener` 中使用了注解，那么就会在 `app/build/generated/source/kapt/debug` 路劲下生成 `MyLocationListener_LifecycleAdapter`：

```
public class MyLocationListener_LifecycleAdapter implements GeneratedAdapter {
  final MyLocationListener mReceiver;

  MyLocationListener_LifecycleAdapter(MyLocationListener receiver) {
    this.mReceiver = receiver;
  }

  @Override
  public void callMethods(LifecycleOwner owner, Lifecycle.Event event, boolean onAny,
      MethodCallsLogger logger) {
    boolean hasLogger = logger != null;
    if (onAny) {
      return;
    }
    if (event == Lifecycle.Event.ON_START) {
      if (!hasLogger || logger.approveCall("start", 1)) {
        mReceiver.start();
      }
      return;
    }
    if (event == Lifecycle.Event.ON_STOP) {
      if (!hasLogger || logger.approveCall("stop", 1)) {
        mReceiver.stop();
      }
      return;
    }
  }
}
```

其中 `mReceiver` 就是 `MyLocationListener` 的实例对象。在触发生命周期方法的时候，会调用 `callMethods` 方法，从而就调用了对应的注解方法。

但是如果类有继承关系会怎么样？比如我们在文章开头提到的，在 `Activity/Fragment.onDestroy` 的时候自动调用 Presenter 的 destroy 从而断开 Presenter 对 View 的引用。那么这个断开操作适合放在 Presenter 的基类中，因为每个 Presenter 都需要这样做。(在没有 Lifecycle 之前一般会在 BaseActivity、BaseFragment 的 onDestroy 中调用 presenter.destroy)

下面我们来看下在有继承关系的情况下使用 `@OnLifecycleEvent` 注解会出现什么情况。我们继续在上面的定位程序中将 MyLocationListener 改造下。

我们创建一个抽象类 `LocationListener`：

```
internal abstract class LocationListener(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: (Location) -> Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.e("MyLocationListener", "onDestroy...")
    }

}
```

然后让 `MyLocationListener` 继承抽象类 `LocationListener`：

```
internal class MyLocationListener(
        context: Context,
        private val lifecycle: Lifecycle,
        callback: (Location) -> Unit) : LocationListener(context, lifecycle, callback) {

    private var enabled = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            // connect
            Log.e("MyLocationListener", "start connect")
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // disconnect if connected
        Log.e("MyLocationListener", "disconnect")

    }

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // connect if not connected
            start()
        }
    }
}
```

运行程序输出如下所示：

```
E/MyLocationListener: start connect
E/MyLocationListener: disconnect
E/MyLocationListener: onDestroy...
```

可以发现有了继承关系也会正确的执行生命周期方法，因为 LocationListener、MyLocationListener 都使用了 @OnLifecycleEvent 注解，所以 APT 会生成 2 个类：

- LocationListener_LifecycleAdapter
- MyLocationListener_LifecycleAdapter

我们来看下 MyLocationListener_LifecycleAdapter ：

```
public class MyLocationListener_LifecycleAdapter implements GeneratedAdapter {
  final MyLocationListener mReceiver;

  MyLocationListener_LifecycleAdapter(MyLocationListener receiver) {
    this.mReceiver = receiver;
  }

  @Override
  public void callMethods(LifecycleOwner owner, Lifecycle.Event event, boolean onAny,
      MethodCallsLogger logger) {
    boolean hasLogger = logger != null;
    if (onAny) {
      return;
    }
    if (event == Lifecycle.Event.ON_DESTROY) {
      if (!hasLogger || logger.approveCall("destroy", 1)) {
        mReceiver.destroy();
      }
      return;
    }
    if (event == Lifecycle.Event.ON_START) {
      if (!hasLogger || logger.approveCall("start", 1)) {
        mReceiver.start();
      }
      return;
    }
    if (event == Lifecycle.Event.ON_STOP) {
      if (!hasLogger || logger.approveCall("stop", 1)) {
        mReceiver.stop();
      }
      return;
    }
  }
}
```

从中可以发现 `MyLocationListener_LifecycleAdapter.callMethods` 生成的方法包含了 MyLocationListener 注解方法，还有父类 LocationListener 的注解方法的调用。

`Lifecycle` 生成的类是可以很好的支持继承关系的。所以有了 Lifecycle 之后，`BaseActivity、BaseFragment` 都不需要持有 BasePresenter 了，直接在 `BasePresenter` 使用 `@OnLifecycleEvent` 即可，更加利于解耦。

那么 `Lifecycle` 组件是如何使用 `XXX_LifecycleAdapter` 的呢？下面我们就详细分析下 Lifecycle 的实现原理。

## Lifecycle 原理分析

> 友情提示：本文是基于最新的 Lifecycle 2.2.0 版本源码。程序 demo 是基于 compileSdkVersion = 29，也就是 Android 10，Android模拟器系统版本也是 Android 10，便于调试。

我们先从案例中的 LifecycleDemoActivity.onCreate() 看起：

```
class LifecycleDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        // 省略其他代码...
    
        lifecycle.addObserver(myLocationListener)
    }
}
```

`AppCompatActivity` 继承关系：

```
androidx.appcompat.app.AppCompatActivity
    -> androidx.fragment.app.FragmentActivity
        -> androidx.activity.ComponentActivity
            -> androidx.core.app.ComponentActivity
                -> android.app.Activity
```

lifecycle 就是在 `androidx.activity.ComponentActivity` 定义的：

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner {
        
    // 省略其他代码...
        
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }        
}
```

也就是说 `lifecycle` 就是 `LifecycleRegistry`，那我们就来看看 `LifecycleRegistry.addObserver` 方法：

```
public class LifecycleRegistry extends Lifecycle {

    // 省略其他代码...
    
    @Override
    public void addObserver(@NonNull LifecycleObserver observer) {
        // 初始状态
        State initialState = mState == DESTROYED ? DESTROYED : INITIALIZED;
        // 封装 observer 和 initialState
        ObserverWithState statefulObserver = new ObserverWithState(observer, initialState);
        // 将封装后的对象放进 Map 当中
        ObserverWithState previous = mObserverMap.putIfAbsent(observer, statefulObserver);

        //...

        if (!isReentrance) {
            // 核心方法
            sync();
        }
    }
}
```

我们看看 `ObserverWithState` 是如何封装 observer 和 initialState 的：

```
static class ObserverWithState {
    State mState;
    LifecycleEventObserver mLifecycleObserver;

    ObserverWithState(LifecycleObserver observer, State initialState) {
        mLifecycleObserver = Lifecycling.lifecycleEventObserver(observer);
        mState = initialState;
    }

    void dispatchEvent(LifecycleOwner owner, Event event) {
        State newState = getStateAfter(event);
        mState = min(mState, newState);
        mLifecycleObserver.onStateChanged(owner, event);
        mState = newState;
    }
}
```

在构造方法中调用了 `Lifecycling.lifecycleEventObserver`：

```
@NonNull
static LifecycleEventObserver lifecycleEventObserver(Object object) {
    // LifecycleEventObserver 是一个接口，它继承了 LifecycleObserver
    // 我们上面的 LocationListener 就是实现了 LifecycleObserver 接口
    // 如果 Observer 是 LifecycleEventObserver
    boolean isLifecycleEventObserver = object instanceof LifecycleEventObserver;
    
    boolean isFullLifecycleObserver = object instanceof FullLifecycleObserver;
    if (isLifecycleEventObserver && isFullLifecycleObserver) {
        return new FullLifecycleObserverAdapter((FullLifecycleObserver) object,
                (LifecycleEventObserver) object);
    }
    if (isFullLifecycleObserver) {
        return new FullLifecycleObserverAdapter((FullLifecycleObserver) object, null);
    }

    if (isLifecycleEventObserver) {
        return (LifecycleEventObserver) object;
    }
    
    // 我们案例中的 LocationListener 是直接实现了 LifecycleObserver 接口的
    // 所以会执行到下面的流程...

    final Class<?> klass = object.getClass();
    // 判断 sClassToAdapters Map 中是否存在 XXX_LifecycleAdapter
    // 如果不能存在生生成该类的构造方法放进List集合中，然后再放进 Map 中
    int type = getObserverConstructorType(klass);
    
    if (type == GENERATED_CALLBACK) {
        // 从 Map 中获取 klass 相关集合
        List<Constructor<? extends GeneratedAdapter>> constructors =
                sClassToAdapters.get(klass);
        if (constructors.size() == 1) {
            // 根据 XXX_LifecycleAdapter 构造方法创建对象
            GeneratedAdapter generatedAdapter = createGeneratedAdapter(
                    constructors.get(0), object);
            // 然后将创建的对象封装在 SingleGeneratedAdapterObserver 对象中
            return new SingleGeneratedAdapterObserver(generatedAdapter);
        }
        GeneratedAdapter[] adapters = new GeneratedAdapter[constructors.size()];
        for (int i = 0; i < constructors.size(); i++) {
            adapters[i] = createGeneratedAdapter(constructors.get(i), object);
        }
        return new CompositeGeneratedAdaptersObserver(adapters);
    }
    return new ReflectiveGenericLifecycleObserver(object);
}
```

**由此可见，Lifecycle 创建的 XXX_LifecycleAdapter 会包装成 SingleGeneratedAdapterObserver 或 CompositeGeneratedAdaptersObserver 对象，然后赋值给它的成员变量 mLifecycleObserver 以便在 dispatchEvent 方法中通知相关的观察者**

好，分析完了 `Lifecycling.lifecycleEventObserver` 方法，然后我们再回过头来看下 `addObserver` 方法中的 `sync` 方法：

```
private void sync() {
    LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
    if (lifecycleOwner == null) {
        throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is already"
                + "garbage collected. It is too late to change lifecycle state.");
    }
    while (!isSynced()) {
        mNewEventOccurred = false;
        // no need to check eldest for nullability, because isSynced does it for us.
        if (mState.compareTo(mObserverMap.eldest().getValue().mState) < 0) {
            backwardPass(lifecycleOwner);
        }
        Entry<LifecycleObserver, ObserverWithState> newest = mObserverMap.newest();
        if (!mNewEventOccurred && newest != null
                && mState.compareTo(newest.getValue().mState) > 0) {
            forwardPass(lifecycleOwner);
        }
    }
    mNewEventOccurred = false;
}
```

比较难理解的可能是 `backwardPass` 和 `forwardPass` 两个方法。我们先来看下官方关于 `Lifecycle` 对生命周期 `Event` 和 `State` 的描述：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200205154434120.png)

可以看出 init 和 destroy 状态是在 create、start、resume 状态之前的。

`backwardPass` 方法用于 `“向后传递”`，例如屏幕旋转或关闭页面的情况，在关闭页面之前的状态是 resume，那么关闭页面后，就要依次执行 onPause、onStop、onDestroy，对照上面的图来看，有一种 `“往回走”` 的感觉，所以叫做 `backwardPass`

`forwardPass` 方法用于 `“向前传递”`，比如打开一个新页面，会执行 onCreate、onStart、onResume，对照上面的图来看，就是一直往前走的感觉，所以叫做 `forwardPass`

我们先来看下 `backwardPass` 方法：

```
private void backwardPass(LifecycleOwner lifecycleOwner) {
    Iterator<Entry<LifecycleObserver, ObserverWithState>> descendingIterator =
            mObserverMap.descendingIterator();
    // 遍历 Map 里的 Observer 观察者
    while (descendingIterator.hasNext() && !mNewEventOccurred) {
        Entry<LifecycleObserver, ObserverWithState> entry = descendingIterator.next();
        ObserverWithState observer = entry.getValue();
        while ((observer.mState.compareTo(mState) > 0 && !mNewEventOccurred
                && mObserverMap.contains(entry.getKey()))) {
            // downEvent 方法用于获取 state 的上一个 event
            // 如果 observer.mState 为 resume 状态，那么 downEvent 返回的则为 ON_PAUSE 事件
            // 可以结合上面的图来理解
            Event event = downEvent(observer.mState);
            pushParentState(getStateAfter(event));
            // 真正触发生命周期回调的方法
            observer.dispatchEvent(lifecycleOwner, event);
            popParentState();
        }
    }
}
```

再来看下 `forwardPass` 方法：

```
private void forwardPass(LifecycleOwner lifecycleOwner) {
    Iterator<Entry<LifecycleObserver, ObserverWithState>> ascendingIterator =
            mObserverMap.iteratorWithAdditions();
    while (ascendingIterator.hasNext() && !mNewEventOccurred) {
        Entry<LifecycleObserver, ObserverWithState> entry = ascendingIterator.next();
        ObserverWithState observer = entry.getValue();
        while ((observer.mState.compareTo(mState) < 0 && !mNewEventOccurred
                && mObserverMap.contains(entry.getKey()))) {
            pushParentState(observer.mState);
            // upEvent 方法和 downEvent 正好相反
            // 如果 state 为 create，那么返回的就是 ON_START 事件
            // 可以结合上面的图来理解
            observer.dispatchEvent(lifecycleOwner, upEvent(observer.mState));
            popParentState();
        }
    }
}
```
 至此，我们就把 `sync` 方法分析完毕了，那么也就将 `lifecycle.addObserver` 方法分析完毕了。
 
 但是当 `Activity、Fragment` 调用生命周期方法的时候，是怎么通知 Map 里的观察者的呢？
 
 通过 debug 发现，`Lifecycle` 是通过 `ReportFragment.injectIfNeededIn` 类来实现生命周期的 `“注入”` 的：
 
 ```
 public class ReportFragment extends Fragment {

    public static void injectIfNeededIn(Activity activity) {
    
        // 如果是 Android 10 版本或者以上
        // 则通过 registerActivityLifecycleCallbacks 的方式来实现生命周期的回调
        if (Build.VERSION.SDK_INT >= 29) {
            // On API 29+, we can register for the correct Lifecycle callbacks directly
            activity.registerActivityLifecycleCallbacks(
                    new LifecycleCallbacks());
        }
        
        // 往页面添加一个空的 Fragment，在 Fragment 的生命周期方法中分发事件
        android.app.FragmentManager manager = activity.getFragmentManager();
        if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
            manager.beginTransaction().add(new ReportFragment(), REPORT_FRAGMENT_TAG).commit();
            manager.executePendingTransactions();
        }
    }
}
```

我们先来看下如果是 `Android 10` 或以上的版本是怎么通过 `registerActivityLifecycleCallbacks` 是实现事件分发的：

```
public class ReportFragment extends Fragment {
    // 省略其他代码...
    
    static void dispatch(@NonNull Activity activity, @NonNull Lifecycle.Event event) {
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner) activity).getLifecycle().handleLifecycleEvent(event);
            return;
        }

        if (activity instanceof LifecycleOwner) {
            Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
            }
        }
    }
    
    static class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity,
                @Nullable Bundle bundle) {
        }

        @Override
        public void onActivityPostCreated(@NonNull Activity activity,
                @Nullable Bundle savedInstanceState) {
            dispatch(activity, Lifecycle.Event.ON_CREATE);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPostStarted(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_START);
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPostResumed(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_RESUME);
        }

        @Override
        public void onActivityPrePaused(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_PAUSE);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPreStopped(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_STOP);
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity,
                @NonNull Bundle bundle) {
        }

        @Override
        public void onActivityPreDestroyed(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_DESTROY);
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }
}
```

也就是在各个生命周期方法调用 `dispatch` 方法，然后在该方法中调用 `LifecycleRegistry.handleLifecycleEvent` ：

```
public class LifecycleRegistry extends Lifecycle {

    // 省略其他代码...

    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        State next = getStateAfter(event);
        moveToState(next);
    }

    private void moveToState(State next) {
        if (mState == next) {
            return;
        }
        mState = next;
        if (mHandlingEvent || mAddingObserverCounter != 0) {
            mNewEventOccurred = true;
            // we will figure out what to do on upper level.
            return;
        }
        mHandlingEvent = true;
        sync();
        mHandlingEvent = false;
    }
}
```

最终就会调用我们上面分析过的 `sync` 方法，最终会在里面调用 Observer 的回调方法。

这就是 `ReportFragment` 中 `Android 10` 或以上的版本的回调逻辑。`Android 10` 以下通过 Fragment 的生命周期进行回调的方式：

```
public class ReportFragment extends Fragment {

    // 省略其他代码...

    private ActivityInitializationListener mProcessListener;

    private void dispatchCreate(ActivityInitializationListener listener) {
        if (listener != null) {
            listener.onCreate();
        }
    }

    private void dispatchStart(ActivityInitializationListener listener) {
        if (listener != null) {
            listener.onStart();
        }
    }

    private void dispatchResume(ActivityInitializationListener listener) {
        if (listener != null) {
            listener.onResume();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dispatchCreate(mProcessListener);
        dispatch(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        dispatchStart(mProcessListener);
        dispatch(Lifecycle.Event.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        dispatchResume(mProcessListener);
        dispatch(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        dispatch(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        dispatch(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatch(Lifecycle.Event.ON_DESTROY);
        // just want to be sure that we won't leak reference to an activity
        mProcessListener = null;
    }
    
    void setProcessListener(ActivityInitializationListener processListener) {
        mProcessListener = processListener;
    }

}
```

同时我们发现 `onActivityCreated、onStart、onResume` 方法中分别调用了 `dispatchCreate、dispatchStart、dispatchResume`，这些方法均接受一个 `mProcessListener` 参数，`mProcessListener` 类似一个全局的回调，我们看下 Lifecycle 在哪里设置该了属性呢？发现在 `ProcessLifecycleOwner.attach` 方法中有设置该属性的逻辑，然后在静态 init 方法中调用了 attach 方法：

```
public class ProcessLifecycleOwner implements LifecycleOwner {

    // 省略其他代码...
    
    static void init(Context context) {
        sInstance.attach(context);
    }

    void attach(Context context) {
        mHandler = new Handler();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        Application app = (Application) context.getApplicationContext();
        app.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
            @Override
            public void onActivityPreCreated(@NonNull Activity activity,
                    @Nullable Bundle savedInstanceState) {
                activity.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityPostStarted(@NonNull Activity activity) {
                        activityStarted();
                    }

                    @Override
                    public void onActivityPostResumed(@NonNull Activity activity) {
                        activityResumed();
                    }
                });
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                // Android 10 版本以下
                if (Build.VERSION.SDK_INT < 29) {
                    ReportFragment.get(activity).setProcessListener(mInitializationListener);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                activityPaused();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityStopped();
            }
        });
    }

}
```

然后又在 `ProcessLifecycleOwnerInitializer.onCreate` 方法中调用了 `ProcessLifecycleOwner.init` 方法：

```
public class ProcessLifecycleOwnerInitializer extends ContentProvider {

    @Override
    public boolean onCreate() {
        LifecycleDispatcher.init(getContext());
        ProcessLifecycleOwner.init(getContext());
        return true;
    }
    
    // 省略其他方法...
}
```

`ProcessLifecycleOwnerInitializer` 继承自 `ContentProvider`，它的 `onCreate` 方法比 `Application.onCreate` 方法执行的更早。

这就是 ReportFragment 里完整的逻辑了，主要是根据 Android 版本（Android 10）来实现不同的事件分发逻辑。

## 小结

至此我们就将 Jetpack Lifecycle 组件介绍完毕了，希望对你有帮助 ~。

另外本文涉及到的代码都在我的 **[AndroidAll GitHub](https://github.com/chiclaim/AndroidAll)** 仓库中。该仓库除了 `Jetpack`，还有 Android 程序员需要掌握的技术栈，如：**程序架构、设计模式、性能优化、数据结构算法、Kotlin、Flutter、NDK，以及常用开源框架 Router、RxJava、Glide、LeakCanary、Dagger2、Retrofit、OkHttp、ButterKnife、Router 的原理分析** 等，持续更新，欢迎 star。




## Reference

- [https://developer.android.com/topic/libraries/architecture/lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [https://developer.android.com/jetpack/androidx/releases/lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)














