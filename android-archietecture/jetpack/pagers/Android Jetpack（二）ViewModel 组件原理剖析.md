


## ViewModel 简介

上一篇文章我们介绍了 [Jetpack Lifecycle 的使用及原理分析](https://chiclaim.blog.csdn.net/article/details/104189041)，今天我们来看下 `Jetpack ViewModel` 组件的使用及原理分析。


ViewModel 类被设计用于以感知生命周期的方式来管理和存储 UI 界面相关的数据，例如当屏幕发生旋转、App权限被动态修改、系统语言发生改变（这些都属于 Configuration changes）的时候，Activity 重建的时候，ViewModel 可以保留界面的数据。在没有 ViewModel 之前，一般我们是在 `onSaveInstanceState()` 方法中将数据保存在 Bundle 中，然后在 `onCreate()` 方法中判断 Bundle 是否为空，不为空则从中获取保存的数据，如果被保存的数据是复杂类型的话还需要实现 `Serializable` 或 `Parcelable` 接口，所以这种方式保存和恢复数据比较麻烦也不适合保存大量的数据。有了 ViewModel 之后，一切都变得简单，我们无需手动的在某个特定的方法中保存数据，保存的动作系统帮我们自动完成了，而且 ViewModel 可以保留任何对象，如果是复杂类型不需要实现`Serializable` 或 `Parcelable` 接口。

下面我们通过一个案例演示 ViewModel 的用法，为了大家更好的理解 ViewModel，Demo 中不会引入其他的概念如 LiveData。

> 友情提示：本文使用的 ViewModel 是最新的 2.2.0 版本.

## ViewModel 的引入

首先我们要引入 ViewModel 相关的库，如果你的工程是 Kotlin 工程：

```
dependencies {
    def lifecycle_version = "2.2.0"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
}
```

如果你的工程是 Java 工程：

```
dependencies {
    def lifecycle_version = "2.2.0"

    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"    
}
```

## ViewModel 案例

Demo 案例也非常简单，点击按钮，界面的数字不断累加，然后当屏幕旋转 Activity 重建的时候，界面依然能够保留上一个 Activity 实例展示的数据，如下图所示：

![ViewModel-Demo](https://img-blog.csdnimg.cn/20200206154836727.gif)


**ViewModelDemoActivity.kt：**

```
class ViewModelDemoActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewmodel_demo_layout)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        text_number.text = "${myViewModel.number}"

        btn_plus.setOnClickListener {
            text_number.text = "${++myViewModel.number}"
        }
    }
}
```

**MyViewModel.kt：**

```
class MyViewModel : ViewModel() {
    var number: Int = 0
}
```

上面的代码非常简单，首先让 `MyViewModel` 继承自 `ViewModel`，将数据放在 `MyViewModel` 中，然后在 `Activity` 中通过 `ViewModelProvider.get()` 方法获取 `MyViewModel` 对象，获取到的 ViewModel 对象和 Activity 重建之前的 ViewModel 对象是一样的，所以界面旋转后可以恢复到之前的数据。下面我们就来分析下 ViewModel 原理，为什么当配置发生变化的时候 `ViewModelProvider.get()` 获取到 ViewModel 实例是一样的。


## ViewModel 原理分析

上面的 Demo 中最新关键的一行代码就是 ：

```
ViewModelProvider(this).get(MyViewModel::class.java)
```
我们先来看下 ViewModelProvider 构造方法：

```
public class ViewModelProvider {

    public ViewModelProvider(@NonNull ViewModelStoreOwner owner) {
        this(owner.getViewModelStore(), owner instanceof HasDefaultViewModelProviderFactory
                ? ((HasDefaultViewModelProviderFactory) owner).getDefaultViewModelProviderFactory()
                : NewInstanceFactory.getInstance());
    }
    
    public ViewModelProvider(@NonNull ViewModelStore store, @NonNull Factory factory) {
        mFactory = factory;
        mViewModelStore = store;
    }
}
```

我们将 `this` 传递 `ViewModelProvider` 的构造方法，说明 `AppCompatActivity` 肯定是实现了 `ViewModelStoreOwner` 接口，继承关系如下所示：


```
androidx.appcompat.app.AppCompatActivity
    -> androidx.fragment.app.FragmentActivity
        -> androidx.activity.ComponentActivity
            -> androidx.lifecycle.ViewModelStoreOwner
```

所以我们来看下 `ComponentActivity.getViewModelStore`：

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner {
    
    // 省略其他代码...
        
    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (getApplication() == null) {
            throw new IllegalStateException("Your activity is not yet attached to the "
                    + "Application instance. You can't request ViewModel before onCreate call.");
        }
        if (mViewModelStore == null) {
            NonConfigurationInstances nc =
                    (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                // Restore the ViewModelStore from NonConfigurationInstances
                mViewModelStore = nc.viewModelStore;
            }
            if (mViewModelStore == null) {
                mViewModelStore = new ViewModelStore();
            }
        }
        return mViewModelStore;
    }
}
```

关键代码为 `getLastNonConfigurationInstance` 方法，如果返回的 `NonConfigurationInstances` 对象不为空，则从中获取 ViewModelStore 然后返回。

所以我们来看下 `getLastNonConfigurationInstance` 方法 ：

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner {
        
    /**
     * Retrieve the non-configuration instance data that was previously
     * returned by {@link #onRetainNonConfigurationInstance()}.  This will
     * be available from the initial {@link #onCreate} and
     * {@link #onStart} calls to the new instance, allowing you to extract
     * any useful dynamic state from the previous instance.
     *
     * <p>Note that the data you retrieve here should <em>only</em> be used
     * as an optimization for handling configuration changes.  You should always
     * be able to handle getting a null pointer back, and an activity must
     * still be able to restore itself to its previous state (through the
     * normal {@link #onSaveInstanceState(Bundle)} mechanism) even if this
     * function returns null.
     *
     * <p><strong>Note:</strong> For most cases you should use the {@link Fragment} API
     * {@link Fragment#setRetainInstance(boolean)} instead; this is also
     * available on older platforms through the Android support libraries.
     *
     * @return the object previously returned by {@link #onRetainNonConfigurationInstance()}
     */
    @Nullable
    public Object getLastNonConfigurationInstance() {
        return mLastNonConfigurationInstances != null
                ? mLastNonConfigurationInstances.activity : null;
    }
}
```

可以发现 `NonConfigurationInstances` 就是 `mLastNonConfigurationInstances.activity`，通过 `getLastNonConfigurationInstance()` 方法的注释我们知道，该方法用于获取 `onRetainNonConfigurationInstance()` 方法返回的 `“non-configuration”` 对象。

所以我们来看下 `onRetainNonConfigurationInstance()` 方法：

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner {
        
    /**
     * Retain all appropriate non-config state.  You can NOT
     * override this yourself!  Use a {@link androidx.lifecycle.ViewModel} if you want to
     * retain your own non config state.
     */
    @Override
    @Nullable
    public final Object onRetainNonConfigurationInstance() {
        Object custom = onRetainCustomNonConfigurationInstance();
        
        ViewModelStore viewModelStore = mViewModelStore;
        // 说明没有地方调用过 getViewModelStore 方法
        // getViewModelStore 方法中对 mViewModelStore 赋值，只要调用过 getViewModelStore 方法，mViewModelStore 则不会为空
        if (viewModelStore == null) {
            // No one called getViewModelStore(), so see if there was an existing
            // ViewModelStore from our last NonConfigurationInstance
            NonConfigurationInstances nc =
                    (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                viewModelStore = nc.viewModelStore;
            }
        }

        if (viewModelStore == null && custom == null) {
            return null;
        }
        
        // 封装 NonConfigurationInstances
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.custom = custom;
        nci.viewModelStore = viewModelStore;
        return nci;
    }
}
```

`onRetainNonConfigurationInstance` 方法在界面重建前会调用，这是将 viewModelStore 实例进行保存的最后时机。然后在界面重建调用 onCreate 的时候通过 `getLastNonConfigurationInstance` 方法获取之前保留的 `NonConfigurationInstances` 对象。那我们在回到 `getLastNonConfigurationInstance` 方法：

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner {
   
    @Nullable
    public Object getLastNonConfigurationInstance() {
        return mLastNonConfigurationInstances != null
                ? mLastNonConfigurationInstances.activity : null;
    }
}
```

发现 `getLastNonConfigurationInstance` 方法中操作的是 ComponentActivity 的成员变量 mLastNonConfigurationInstances，那么 ComponentActivity 在什么时候对 mLastNonConfigurationInstances 属性赋值过呢？

```
public class Activity extends ContextThemeWrapper
        implements LayoutInflater.Factory2,
        Window.Callback, KeyEvent.Callback,
        OnCreateContextMenuListener, ComponentCallbacks2,
        Window.OnWindowDismissedCallback, WindowControllerCallback,
        AutofillManager.AutofillClient, ContentCaptureManager.ContentCaptureClient {
   
    @UnsupportedAppUsage
    final void attach(Context context, ActivityThread aThread,
            Instrumentation instr, IBinder token, int ident,
            Application application, Intent intent, ActivityInfo info,
            CharSequence title, Activity parent, String id,
            NonConfigurationInstances lastNonConfigurationInstances,
            Configuration config, String referrer, IVoiceInteractor voiceInteractor,
            Window window, ActivityConfigCallback activityConfigCallback, IBinder assistToken) {
        
        // 省略其他代码...
        
        mLastNonConfigurationInstances = lastNonConfigurationInstances;
        
    }

```

原来是在 `Activity.attach` 方法中赋值的，当 Activity 重建的时候会调用 attach 方法，会将上次保存的 `lastNonConfigurationInstances` 对象赋值给 `mLastNonConfigurationInstances` 成员变量。那么谁调用了 `attach` 呢？会在 `ActivityThread.performLaunchActivity` 方法中触发 attach 方法：

```
public final class ActivityThread extends ClientTransactionHandler {

    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        
        // 省略其他代码 ...
        
        activity.attach(appContext, this, getInstrumentation(), r.token,
                r.ident, app, r.intent, r.activityInfo, title, r.parent,
                r.embeddedID, r.lastNonConfigurationInstances, config,
                r.referrer, r.voiceInteractor, window, r.configCallback,
                r.assistToken);

    }

}
```

好，现在我们知道了 `mLastNonConfigurationInstances` 属性什么时候赋值的。那什么时候调用 `onRetainNonConfigurationInstance` 方法呢？

根据 `getLastNonConfigurationInstance` 方法源码的注释我们知道，该方法获取的是 `onRetainNonConfigurationInstance` 方法返回的对象，那么谁会触发 `onRetainNonConfigurationInstance` 方法的调用呢？查看 ActivityThread 源码得知会在 `ActivityThread.performDestroyActivity` 方法里调用：

```
public final class ActivityThread extends ClientTransactionHandler {

    // 省略其他代码...

    ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance, String reason) {
            
        // 省略其他代码...
        
        ActivityClientRecord r = mActivities.get(token);
        
        if (getNonConfigInstance) {
            try {
                r.lastNonConfigurationInstances
                        = r.activity.retainNonConfigurationInstances();
            } catch (Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException(
                            "Unable to retain activity "
                            + r.intent.getComponent().toShortString()
                            + ": " + e.toString(), e);
                }
            }
        }
        
        return r;
    }
}
```

所以在界面销毁的时候会调用 retainNonConfigurationInstances 用于保存数据。

**至此，我们就将整个源码串联起来了，那么 ViewModel 的基本原理就是：在 `configuration changes` 的时候，在界面 `destroy` 的时候（`ActivityThread.performDestroyActivity`）通过 `onRetainNonConfigurationInstance` 方法将 NonConfigurationInstances 对象（里面包含 ViewModel） 保存起来，在重建 Activity 的时候在 `attach` 方法中（`ActivityThread.performLaunchActivity`）将上次保存的 NonConfigurationInstances 对象赋值给 Activity 的成员变量 mLastNonConfigurationInstances，然后在 onCreate 中通过 `getLastNonConfigurationInstance` 方法获取上次保存的 NonConfigurationInstances 对象。**


## 小结

至此我们就将 Jetpack `ViewModel` 组件介绍完毕了，希望对你有帮助 ~。

另外本文涉及到的代码都在我的 **[AndroidAll GitHub](https://github.com/chiclaim/AndroidAll)** 仓库中。该仓库除了 `Jetpack`，还有 Android 程序员需要掌握的技术栈，如：**程序架构、设计模式、性能优化、数据结构算法、Kotlin、Flutter、NDK，以及常用开源框架 Router、RxJava、Glide、LeakCanary、Dagger2、Retrofit、OkHttp、ButterKnife、Router 的原理分析** 等，持续更新，欢迎 star。











