
分析的源码是基于[Android Dagger2（一） 基本使用](https://blog.csdn.net/johnny901114/article/details/58225116) 的例子来分析对象是如何被注入的. 如果还没看上一篇文章, 可以先去看看.

在分析源码之前, 先理一理类与类之间的依赖关系 : 

```
public class AddMenuBalanceFragment extends BaseFragment{
    @Inject
    AddMenu BalancePresenter presenter;
}
```


从上面的代码可以看出 `AddMenuBalanceFragment`需要注入`AddMenuBalancePresenter`


Dagger2要想注入`AddMenuBalancePresenter`, 必须调用`AddMenuBalancePresenter`的构造方法:

```
public class AddMenuBalancePresenter implements AddMenuBalanceContract.Presenter {

    private final AddMenuBalanceContract.View mView;

    private final MenuBalanceRepository mMenuBalanceRepository;

    @Inject
    AddMenuBalancePresenter(AddMenuBalanceContract.View view, MenuBalanceRepository mMenuBalanceRepository) {
        this.mView = view;
        this.mMenuBalanceRepository = mMenuBalanceRepository;
    }
}
```

而`AddMenuBalancePresenter`的构造方法又依赖`View`和`MenuBalanceRepository`, 这里的View就是Activity或者Fragment, 这个不能是我们new出来的,因为所有的界面都是由Android系统来创建和销毁的. 所以这个参数只能我们通过传递参数的方式传递给Dagger2.

要想提供`MenuBalanceRepository` Dagger2就必须要调用`MenuBalanceRepository`的构造方法 :

```
@Singleton
public class MenuBalanceRepository implements IMenuBalanceSource {

    private final IMenuBalanceSource remoteSource;

    @Inject
    MenuBalanceRepository(@Remote IMenuBalanceSource remoteSource) {
        this.remoteSource = remoteSource;
    }
}

```
发现`MenuBalanceRepository`的构造方法又依赖`IMenuBalanceSource`.

它们的依赖关系可以用下图来表示:

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjI1MTYzODEyOTY1?x-oss-process=image/format,png)


通过面的图得知, 只要把`IMenuBalanceSource`构造出来了, `MenuBalanceRepository`也就可以构造出来了, 如果`MenuBalanceRepository`和`View`都有了就可以把`AddMenuBalancePresenter`构造出来了. 

这样`AddMenuBalanceFragment`所需要的`AddMenuBalancePresenter`也就被注入了. 

下面就分析下`Dagger2`生成的源码, 看看这些依赖到底是怎么被注入的.

---

我们分析源码的不能一下子扎进源码堆里不能自拔, 那么分析源码该如何下手, 我觉得应该从使用它的地方入手. 比如Dagger2, 使用它的地方也就是触发依赖注入的地方:

```
public class AddMenuBalanceFragment extends BaseFragment{
    @Inject
    AddMenuBalancePresenter presenter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
    DaggerAddMenuBalanceComponent.builder()
        .menuBalanceRepoComponent(((CcdApplication) getActivity().getApplication()).getMenuBalanceRepoComponent())
        .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule(this))
        .build()
        .inject(this); //完成注入
    }
}

```

从上面的代码可以直观的看出`DaggerAddMenuBalanceComponent`使用了`Builder`模式, 设置了两个参数 `menuBalanceRepoComponent` 和 `addMenuBalancePresenterModule`, 最后调用`inject()`把当前的Fragment作为参数传递进去了.

接下来看看`inject()`的实现 : 

```
  @Override
  public void inject(AddMenuBalanceFragment fragment) {
    addMenuBalanceFragmentMembersInjector.injectMembers(fragment);
  }
```

然后看看`DaggerAddMenuBalanceComponent`中 `addMenuBalanceFragmentMembersInjector`是怎么产生的: 

```
  private void initialize(final Builder builder) {

    this.provideAddMenuBalanceViewProvider =
        AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory.create(
            builder.addMenuBalancePresenterModule);

    this.getMenuBalanceRepositoryProvider =
        new Factory<MenuBalanceRepository>() {
          private final MenuBalanceRepoComponent menuBalanceRepoComponent =
              builder.menuBalanceRepoComponent;

          @Override
          public MenuBalanceRepository get() {
            return Preconditions.checkNotNull(
                menuBalanceRepoComponent.getMenuBalanceRepository(),
                "Cannot return null from a non-@Nullable component method");
          }
        };

  this.addMenuBalancePresenterProvider =
          AddMenuBalancePresenter_Factory.create(
  provideAddMenuBalanceViewProvider, getMenuBalanceRepositoryProvider);

  this.addMenuBalanceFragmentMembersInjector =
          AddMenuBalanceFragment_MembersInjector.create(addMenuBalancePresenterProvider);
}

```

从`initialize`方法里可以看出
`addMenuBalanceFragmentMembersInjector`依赖`addMenuBalancePresenterProvider`
`addMenuBalancePresenterProvider`又依赖`provideAddMenuBalanceViewProvider`和`getMenuBalanceRepositoryProvider`

所以要想明白`addMenuBalanceFragmentMembersInjector`是怎么创建出来的, 就必须弄清楚`addMenuBalancePresenterProvider`,`provideAddMenuBalanceViewProvider`和`getMenuBalanceRepositoryProvider`.

---

### 1) provideAddMenuBalanceViewProvider属性的分析

```
this.provideAddMenuBalanceViewProvider =
    AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory.create(
        builder.addMenuBalancePresenterModule);
```

从上面的代码可以看出, `provideAddMenuBalanceViewProvider`是通过`AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory.create`构建出来的：

```
  public static Factory<AddMenuBalanceContract.View> create(AddMenuBalancePresenterModule module) {
    return new AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory(module);
  }
  
  
```

然后看看构造方法和`AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory.get()`方法：

```
  public AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory(
      AddMenuBalancePresenterModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public AddMenuBalanceContract.View get() {
    return Preconditions.checkNotNull(
        module.provideAddMenuBalanceView(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

```
如此可见，`AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory.get`方法的返回值是通过传递进去的`module.provideAddMenuBalanceView()`提供的，这个`module`就是在`Fragment`中`DaggerAddMenuBalanceComponent.build`的时候设置进去的，代码如下：

```
public class AddMenuBalanceFragment extends BaseFragment{

    //ignore other code
    
    DaggerAddMenuBalanceComponent.builder()
        .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule(this))
        .build()
        .inject(this); //完成注入
    }
}

```

说白了`AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory`的`module`就是`AddMenuBalancePresenterModule(this)`. 这里的`this`就是`View`了

看看**AddMenuBalancePresenterModule**的代码: 

```
@Module
public class AddMenuBalancePresenterModule {

    private final AddMenuBalanceContract.View mView;

    public AddMenuBalancePresenterModule(AddMenuBalanceContract.View view) {
        mView = view;
    }

    @Provides
    AddMenuBalanceContract.View provideAddMenuBalanceView(){
        return mView;
    }
}
```

所以`provideAddMenuBalanceViewProvider.get`返回的值就是`View`也就是我们的`Fragment`了。

所以`provideAddMenuBalanceViewProvider`的作用就是返回`View` , 至此`provideAddMenuBalanceViewProvider`讲解完毕了。

---

### 2) getMenuBalanceRepositoryProvider 属性的分析

```
    this.getMenuBalanceRepositoryProvider =
        new Factory<MenuBalanceRepository>() {
          private final MenuBalanceRepoComponent menuBalanceRepoComponent =
              builder.menuBalanceRepoComponent;

          @Override
          public MenuBalanceRepository get() {
            return Preconditions.checkNotNull(
                menuBalanceRepoComponent.getMenuBalanceRepository(),
                "Cannot return null from a non-@Nullable component method");
          }
        };
```
从上面的代码可以看出, 它通过一个匿名内部类来实现的。先看看匿名内部类中的`menuBalanceRepoComponent`属性

这里的`menuBalanceRepoComponent`是build.menuBalanceRepoComponent，DaggerAddMenuBalanceComponent.build我们只在`AddMenuBalanceFragment`里设置了参数了，如下：

```
DaggerAddMenuBalanceComponent.builder()
        .menuBalanceRepoComponent(((CcdApplication) getActivity().getApplication()).getMenuBalanceRepoComponent())
        .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule(this))
        .build()
        .inject(this);
```

从代码可以看出`menuBalanceRepoComponent`就是`((CcdApplication) getActivity().getApplication()).getMenuBalanceRepoComponent()` 


Ok, 那我们看看`CcdApplication.getMenuBalanceRepoComponent()` :

```
public void onCreate() {
    //...
    mMenuBalanceRepoComponent = DaggerMenuBalanceRepoComponent.builder()
            .applicationModule(new ApplicationModule(getApplicationContext()))
            .build();
}

public MenuBalanceRepoComponent getMenuBalanceRepoComponent() {
    return mMenuBalanceRepoComponent;
}

```

所以匿名内部类(Factory)中的`getMenuBalanceRepositoryProvider`属性就是`DaggerMenuBalanceRepoComponent`.

然后再看匿名内部类(Factory)的`get()`方法, 返回的是 `DaggerMenuBalanceRepoComponent.getMenuBalanceRepository();`, `getMenuBalanceRepository()`方法具体是怎么实现的: 

```
public final class DaggerMenuBalanceRepoComponent implements MenuBalanceRepoComponent {
  @Override
  public MenuBalanceRepository getMenuBalanceRepository() {
    return menuBalanceRepositoryProvider.get();
  }
}
```

发现该方法返回的是`menuBalanceRepositoryProvider.get();` , 那么`menuBalanceRepositoryProvider`是什么东西?


`menuBalanceRepositoryProvider`是在`DaggerMenuBalanceRepoComponent.initialize(builder)`中初始化：

```
  private void initialize(final Builder builder) {

    this.provideRemoteDataSourceProvider =
        ScopedProvider.create(
            MenuBalanceRepoModule_ProvideRemoteDataSourceFactory.create(
                builder.menuBalanceRepoModule));

    this.menuBalanceRepositoryProvider =
        ScopedProvider.create(
            MenuBalanceRepository_Factory.create(provideRemoteDataSourceProvider));
  }
```

从这段代码可以看出 `menuBalanceRepositoryProvider`初始化依赖于`provideRemoteDataSourceProvider`

所以又得先分析下 `provideRemoteDataSourceProvider`了, `provideRemoteDataSourceProvider`的初始化需要`builder.menuBalanceRepoModule`, 这个`builder.menuBalanceRepoModule`是在我们调用build的时候Dagger2创建的:

```
public MenuBalanceRepoComponent build() {
  if (menuBalanceRepoModule == null) {
    this.menuBalanceRepoModule = new MenuBalanceRepoModule();
  }
  return new DaggerMenuBalanceRepoComponent(this);
}
```

所以`builder.menuBalanceRepoModule = new MenuBalanceRepoModule()` , MenuBalanceRepoModule类的代码如下: 

```
@Module
public class MenuBalanceRepoModule {

    @Singleton
    @Provides
    @Remote
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
}
```

`provideRemoteDataSourceProvider`属性需要`MenuBalanceRepoModule_ProvideRemoteDataSourceFactory.create`创建出来的, 那来看看这个工厂方法 :
 
```
  @Override
  public IMenuBalanceSource get() {
    return Preconditions.checkNotNull(
        module.provideRemoteDataSource(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<IMenuBalanceSource> create(MenuBalanceRepoModule module) {
    return new MenuBalanceRepoModule_ProvideRemoteDataSourceFactory(module);
  }
```
`create`方法的参数`module`就是我们刚刚分析的`MenuBalanceRepoModule`, 工厂方法的get方法返回的就是:

```
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
```

所以`provideRemoteDataSourceProvider`的作用就是提供`new MenuBalanceRemoteSource()`对象的.

`menuBalanceRepositoryProvider`属性依赖的参数`provideRemoteDataSourceProvider`讲完了, 那么就可以分析`menuBalanceRepositoryProvider`属性了:

```
this.menuBalanceRepositoryProvider =
    ScopedProvider.create(
        MenuBalanceRepository_Factory.create(provideRemoteDataSourceProvider));
```

MenuBalanceRepository_Factory类:

```
  @Override
  public MenuBalanceRepository get() {
    return new MenuBalanceRepository(remoteSourceProvider.get());
  }

  public static Factory<MenuBalanceRepository> create(
      Provider<IMenuBalanceSource> remoteSourceProvider) {
    return new MenuBalanceRepository_Factory(remoteSourceProvider);
  }
```

它的get()方法new了一个MenuBalanceRepository, 构造这个MenuBalanceRepository需要MenuBalanceRemoteSource参数,通过传进来的provideRemoteDataSourceProvider提供即可.


所以`menuBalanceRepositoryProvider`的作用就是提供`new MenuBalanceRepository`对象的.

所以`DaggerMenuBalanceRepoComponent.getMenuBalanceRepository()`方法返回的就是`MenuBalanceRepository`

```
  @Override
  public MenuBalanceRepository getMenuBalanceRepository() {
    return menuBalanceRepositoryProvider.get();
  }
```

再来回顾下匿名内部类(Factory) : 

```
    this.getMenuBalanceRepositoryProvider =
        new Factory<MenuBalanceRepository>() {
          private final MenuBalanceRepoComponent menuBalanceRepoComponent =
              builder.menuBalanceRepoComponent;

          @Override
          public MenuBalanceRepository get() {
            return Preconditions.checkNotNull(
                menuBalanceRepoComponent.getMenuBalanceRepository(),
                "Cannot return null from a non-@Nullable component method");
          }
        };
```

到现在为止, 我们就可以解释匿名内部类get()方法返回的是什么了, 返回就是`MenuBalanceRepository`对象.

所以`getMenuBalanceRepositoryProvider`属性的作用就是提供`MenuBalanceRepository`对象的.


---

### 3) addMenuBalancePresenterProvider 属性的分析


`addMenuBalancePresenterProvider`属性初始化所依赖的`provideAddMenuBalanceViewProvider`和`getMenuBalanceRepositoryProvider`都分析完毕了, 现在就可以分析`addMenuBalancePresenterProvider`属性了.

通过上面的分析我们知道:

`provideAddMenuBalanceViewProvider`属性的作用就是返回`View`

`getMenuBalanceRepositoryProvider`属性的作用就是提供`MenuBalanceRepository`对象的


看看`addMenuBalancePresenterProvider`初始化代码如下:

```
  this.addMenuBalancePresenterProvider =
          AddMenuBalancePresenter_Factory.create(
  provideAddMenuBalanceViewProvider, getMenuBalanceRepositoryProvider);

```

`AddMenuBalancePresenter_Factory`类:

```
  public AddMenuBalancePresenter_Factory(
      Provider<AddMenuBalanceContract.View> viewProvider,
      Provider<MenuBalanceRepository> mMenuBalanceRepositoryProvider) {
    assert viewProvider != null;
    this.viewProvider = viewProvider;
    assert mMenuBalanceRepositoryProvider != null;
    this.mMenuBalanceRepositoryProvider = mMenuBalanceRepositoryProvider;
  }

  @Override
  public AddMenuBalancePresenter get() {
    return new AddMenuBalancePresenter(viewProvider.get(), mMenuBalanceRepositoryProvider.get());
  }

  public static Factory<AddMenuBalancePresenter> create(
      Provider<AddMenuBalanceContract.View> viewProvider,
      Provider<MenuBalanceRepository> mMenuBalanceRepositoryProvider) {
    return new AddMenuBalancePresenter_Factory(viewProvider, mMenuBalanceRepositoryProvider);
  }
```

通过它的get()方法就知道该工厂方法返回一个AddMenuBalancePresenter实例, 构造AddMenuBalancePresenter需要的两个参数通过上面讲解了两个属性provideAddMenuBalanceViewProvider和getMenuBalanceRepositoryProvider提供.


所以`addMenuBalancePresenterProvider`属性的最终的作用就是提供`AddMenuBalancePresenter`对象的.

到此为止, 我们就知道了Presenter是怎么被构建出来的了.

---

### 4) addMenuBalanceFragmentMembersInjector 属性的分析

接下来分析`DaggerAddMenuBalanceComponent`最后一个需要分析的属性`addMenuBalanceFragmentMembersInjector`

```
  this.addMenuBalanceFragmentMembersInjector =
          AddMenuBalanceFragment_MembersInjector.create(addMenuBalancePresenterProvider);
```

`AddMenuBalanceFragment_MembersInjector`类代码:

```
  public static MembersInjector<AddMenuBalanceFragment> create(
      Provider<AddMenuBalancePresenter> presenterProvider) {
    return new AddMenuBalanceFragment_MembersInjector(presenterProvider);
  }

  @Override
  public void injectMembers(AddMenuBalanceFragment instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.presenter = presenterProvider.get();
  }
```

核心就是`injectMembers`方法, 该方法完成了Fragment中对Presenter的注入, `injectMembers`方法所需要的参数 `presenterProvider` 就是`addMenuBalancePresenterProvider`属性(提供AddMenuBalancePresenter对象的). 

因为`DaggerAddMenuBalanceComponent.inject()`方法调用了`AddMenuBalanceFragment_MembersInjector.injectMembers`.

所以为什么说调用`DaggerAddMenuBalanceComponent.inject()`方法就完成了依赖注入操作. 

所以这也就是为什么需要在Fragment中调用它了, 如下代码所示:


```
public class AddMenuBalanceFragment extends BaseFragment{
    @Inject
    AddMenuBalancePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    DaggerAddMenuBalanceComponent.builder()
        .menuBalanceRepoComponent(((CcdApplication) getActivity().getApplication()).getMenuBalanceRepoComponent())
        .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule(this))
        .build()
        .inject(this); //完成注入
    }
}
```

上面的源码分析流程可以用如下图来表示 : 

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjI3MTUxMTQ2MDk2?x-oss-process=image/format,png)

---

Dagger2生成类我们写的Component和Module类对应关系

AddMenuBalanceComponent(Interface) --> DaggerAddMenuBalanceComponent implements AddMenuBalanceComponent

AddMenuBalancePresenterModule --> AddMenuBalancePresenterModule_ProvideAddMenuBalanceViewFactory

MenuBalanceRepoComponent(Interface) --> DaggerMenuBalanceRepoComponent implements MenuBalanceRepoComponent

MenuBalanceRepoModule --> MenuBalanceRepoModule_ProvideRemoteDataSourceFactory


从上面的源码分析，Dagger2生成的源码大量使用了`Builder`、`Factory`模式。

