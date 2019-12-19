> 欢迎转载，转载请标明出处： 
[http://blog.csdn.net/johnny901114/article/details/58225116](http://blog.csdn.net/johnny901114/article/details/58225116)
本文出自:[【Chiclaim的博客】](http://blog.csdn.net/johnny901114)

Dagger2 是一个 依赖注入框架(DI), 对Java开发有些了解的读者, 肯定熟悉这个概念, 好处就是解耦, 需要某个对象, 不需要自己通过new关键字去创建。

Dagger2 不是通过反射的方式去实现注入的，是通过生成代码的方式来达到注入效果的，在Android的很多开源框架中都是用到了代码生成工具（APT）, 比如ButterKnife. 关于生成代码工具APT的使用可以参考我以前写的文章 : 

> [Android开发之手把手教你写ButterKnife框架（一）](http://blog.csdn.net/johnny901114/article/details/52662376)
[Android开发之手把手教你写ButterKnife框架（二）](http://blog.csdn.net/johnny901114/article/details/52664112)
[Android开发之手把手教你写ButterKnife框架（三）](http://blog.csdn.net/johnny901114/article/details/52672188)


## (一) 如何在项目中加入Dagger2框架

在 `project/build.gradle ` 文件中加入apt插件依赖, 如:

```
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.3'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```

然后在`project/app/build.gradle 应用apt插件, 如:

```
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'
```

最后加上dagger2库依赖 : 

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    apt "com.google.dagger:dagger-compiler:2.2"
    compile "com.google.dagger:dagger:2.2"
}
```


## (二) 如何使用Dagger2框架


### 先来了解下Dagger2的几个注解(Annotation) 

**@Module**: 该注解要用在类上 , 该注解一般和@Provides一起使用, 主要是为@Component提供需要的对象

**@Provides**: 该注解用在方法上, 主要作用是提供依赖@Provides联合使用. 

**@Inject**: 该注解要用在方法,构造方法和字段上. 

	1) 如果是用在字段上,表示需要注入该字段的对象;
	
	2) 如果用在构造方法上, 表示构造当前类对象通过此构造方法;
	
	3) 如果是用在方法上, 表示当前类对象构造完成后, 立马调用该方法.

**@Component**: 该注解主要用在类上. 一般结合@Component的modules/dependencies属性一起使用.  如: 

```
@Component(dependencies = MenuBalanceRepoComponent.class, modules = AddMenuBalancePresenterModule.class)
public interface AddMenuBalanceComponent {

    void inject(AddMenuBalanceFragment fragment);
}
```

`dependencies`属性用于设置提供依赖的组件类(即加上了@Component注解的类)

`modules`属性用于设置依赖的模块类(即加上了@Modules注解的类)

Dagger2会自动生成`Dagger+类名`的Java类, 用于执行依赖注入操作.  如上面的类`AddMenuBalanceComponent`加上了`@Component`注解, Dagger2为我们会生成`DaggerAddMenuBalanceComponent`类.

在生成的类中会有相应的方法以供设置该属性配置的类型, 如生成的`DaggerAddMenuBalanceComponent`类中会有如下两个方法来设置`modules`和`dependencies`配置的类型, `DaggerAddMenuBalanceComponent`通过Builder模式来设置属性 : 

```
    public Builder addMenuBalancePresenterModule(
        AddMenuBalancePresenterModule addMenuBalancePresenterModule) {
      this.addMenuBalancePresenterModule =
          Preconditions.checkNotNull(addMenuBalancePresenterModule);
      return this;
    }

    public Builder menuBalanceRepoComponent(MenuBalanceRepoComponent menuBalanceRepoComponent) {
      this.menuBalanceRepoComponent = Preconditions.checkNotNull(menuBalanceRepoComponent);
      return this;
    }
```

### 使用Dagger2框架来实现注入(MVP模式进行开发)

**需求描述**:

在Activity/Fragment中依赖 Presenter , Presenter依赖 Model层, Model依赖远程数据源层.  也就是说这里有好几层依赖关系 : 

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjI1MTYzODEyOTY1?x-oss-process=image/format,png)

下面贴出具体的代码, 因为是基于MVP分层的模式开发, 先把View层的接口贴出来: 

**IAddMenuBalanceView.java**

```
public interface IAddMenuBalanceView {
    void addMenuBalanceSuccess(MenuBalance menuBalance);

    void addMenuBalanceFailure(String errorMsg);
}
```

Presenter接口*IAddMenuBalancePresenter.java*

public interface IAddMenuBalancePresenter {
    void addMenuBalance();
}


View层的Activity/Fragment : 

**AddMenuBalanceActivity.java**

```
public class AddMenuBalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framelayout);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, new AddMenuBalanceFragment()).commit();
    }


}
```


**AddMenuBalanceFragment.java**
```

public class AddMenuBalanceFragment extends Fragment implements IAddMenuBalanceView {

    private ProgressDialog dialog;
    private TextView tvMenuBalanceList;

    @Inject
    AddMenuBalancePresenter presenter; //请求注入AddMenuBalancePresenter对象


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerAddMenuBalanceComponent.builder()
                .menuBalanceRepoComponent(ComponentManager.getInstance().getMenuBalanceRepoComponent())
                .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule(this))
                .build()
                .inject(this);//完成注入
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_menu_balance_layout, container, false);
        tvMenuBalanceList = (TextView) view.findViewById(R.id.tv_menu_balance_list);
        view.findViewById(R.id.btn_add_menu_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                presenter.addMenuBalance();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void addMenuBalanceSuccess(MenuBalance menuBalance) {
        hideLoading();
        tvMenuBalanceList.append(menuBalance.getMenuId() + "--" + menuBalance.getMenuName() + "\n");
        showToast("Added successfully");
    }

    @Override
    public void addMenuBalanceFailure(String errorMsg) {
        hideLoading();
        showToast(errorMsg);
    }

    void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("请稍后...");
        }
        dialog.show();

    }

    void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

```

**AddMenuBalancePresenter.java**

```
public class AddMenuBalancePresenter implements IAddMenuBalancePresenter {

    private final IAddMenuBalanceView mView;

    private final MenuBalanceRepository mMenuBalanceRepository;

	//如果需要AddMenuBalancePresenter对象的, 通过调用此构造方法
    @Inject
    AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
        this.mView = view;
        this.mMenuBalanceRepository = mMenuBalanceRepository;
    }

    @Override
    public void addMenuBalance() {
        mMenuBalanceRepository.addMenuBalance(new Callback<MenuBalance>() {
            @Override
            public void onSuccess(MenuBalance data) {
                mView.addMenuBalanceSuccess(data);
            }

            @Override
            public void onFailed(String errorMsg) {
                mView.addMenuBalanceFailure(errorMsg);
            }
        });
    }
}

```

上面主要是View和Presenter层的代码, 现在通过Dagger2注解,把他们的依赖关系配置好 : 

**AddMenuBalanceComponent.java**

```
@FragmentScoped
@Component(dependencies = MenuBalanceRepoComponent.class, modules = AddMenuBalancePresenterModule.class)
public interface AddMenuBalanceComponent {

	void inject(AddMenuBalanceFragment fragment);
}

```

**AddMenuBalancePresenterModule.java**
```
@Module
public class AddMenuBalancePresenterModule {

    private final IAddMenuBalanceView mView;

    public AddMenuBalancePresenterModule(IAddMenuBalanceView view) {
        mView = view;
    }

    @Provides
    IAddMenuBalanceView provideAddMenuBalanceView() {
        return mView;
    }
}


```

下面是数据层代码 : 


```
/**
 * 远程数据源 如HTTP请求
 */
public class MenuBalanceRemoteSource implements IMenuBalanceSource {

    @Override
    public void addMenuBalance(final Callback<MenuBalance> callback) {
        //模拟网络请求
        new AsyncTask<Void, Void, MenuBalance>() {

            @Override
            protected MenuBalance doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
                return new MenuBalance("1001", "东坡肉");
            }

            @Override
            protected void onPostExecute(MenuBalance menuBalance) {
                super.onPostExecute(menuBalance);
                callback.onSuccess(menuBalance);
            }
        }.execute();
    }
}

```


**MenuBalanceRepository.java** Presenter需要的Model对象

```
public class MenuBalanceRepository implements IMenuBalanceSource {
    private final IMenuBalanceSource remoteSource;

    @Inject
    MenuBalanceRepository(IMenuBalanceSource remoteSource){
        this.remoteSource = remoteSource;
    }

    @Override
    public void addMenuBalance(Callback<MenuBalance> callback) {
        remoteSource.addMenuBalance(callback);
    }
}

```

现在新建Model层的 Component和Module

**MenuBalanceRepoComponent.java**

```
@Singleton
@Component(modules = {MenuBalanceRepoModule.class, ApplicationModule.class})
public interface MenuBalanceRepoComponent {

    MenuBalanceRepository getMenuBalanceRepository();
}
```

**MenuBalanceRepoModule.java**

```
@Module
public class MenuBalanceRepoModule {

    @Singleton
    @Provides
        //@Remote
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
}
```


具体的项目源码可以到Github上去下载[Dagger2 Sample](https://github.com/chiclaim/dagger2-sample)


---

最终项目的运行效果: 



![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjI3MTk1MjIxMzE0?x-oss-process=image/format,png)



## (三) 构造方法传递多个相同类型参数问题

上面的 AddMenuBalancePresenter 我们有两个参数 如:

```
@Inject
AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
    this.mView = view;
    this.mMenuBalanceRepository = mMenuBalanceRepository;
}
```

如果现在我们需要在传递一个额外的参数怎么办? 如加个`String categoryName`类型的参数:

```
@Inject
AddMenuBalancePresenter(String categoryName, IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
    this.mCategoryName = categoryName;
    this.mView = view;
    this.mMenuBalanceRepository = mMenuBalanceRepository;
}
```

很简单, 只需要在AddMenuBalancePresenterModule构造方法里加个参数`String categoryName`, 然后再提供一个provider就可以了:

```
public AddMenuBalancePresenterModule(String categoryName, IAddMenuBalanceView view) {
    mCategoryName = categoryName;
    mView = view;
}

@Provides
String provideUsername() {
    return mUsername;
}
```

然后我们在注入的时候new AddMenuBalancePresenterModule的时候加个参数即可 :

```
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DaggerAddMenuBalanceComponent.builder()
            .menuBalanceRepoComponent(ComponentManager.getInstance().getMenuBalanceRepoComponent())
            .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule("湘菜", this))
            .build()
            .inject(this);//完成注入
}
```

这样就OK了.


假设我们想在
`AddMenuBalancePresenter(String categoryName, IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository)`
的基础上再加一个参数`String username`呢?
我们按照上面添加参数的套路来做:

```
    @Inject
    AddMenuBalancePresenter(String username, String categoryName, IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
        this.mUsername = username;
        this.mCategoryName = categoryName;
        this.mView = view;
        this.mMenuBalanceRepository = mMenuBalanceRepository;
    }
```

然后在AddMenuBalancePresenterModule构造方法里加个参数(String username):

```
public AddMenuBalancePresenterModule(String username, String categoryName, IAddMenuBalanceView view) {
    mUsername = username;
    mCategoryName = categoryName;
    mView = view;
}

@Provides
String provideUsername() {
    return mUsername;
}

```

然后我们在注入的时候new AddMenuBalancePresenterModule的时候加个参数即可 :

```
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DaggerAddMenuBalanceComponent.builder()
            .menuBalanceRepoComponent(ComponentManager.getInstance().getMenuBalanceRepoComponent())
            .addMenuBalancePresenterModule(new AddMenuBalancePresenterModule("Chiclaim",湘菜", this))
            .build()
            .inject(this);//完成注入
}
```

编译的时候出错了 , 如下所示:

```
D:\Workspace\Android\dagger2-sample\app\src\main\java\com\chiclaim\dagger\sample\presenter\dagger\AddMenuBalanceComponent.java
Error:(13, 10) 错误: java.lang.String is bound multiple times:
@Provides String com.chiclaim.dagger.sample.presenter.dagger.AddMenuBalancePresenterModule.provideCategoryName()
@Provides String com.chiclaim.dagger.sample.presenter.dagger.AddMenuBalancePresenterModule.provideUsername()

```

其实原因很简单, 因为我们添加了两个String类型的参数, Dagger在生成注入代码的时候就不知道是使用下面那个provide, 因为两个provide返回的都是String类型 : 

```
@Provides
String provideCategoryName() {
    return mCategoryName;
}

@Provides
String provideUsername() {
    return mUsername;
}

```

这个时候就需要我们让Dagger区分了. 通过注解来区分, 现在新建两个注解类, 分别给categoryName和username两个参数来用的 : 

```
@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
}


@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
}
```

**需要注意的是一定要加上`@Qualifier`注解**.

然后呢, 在AddMenuBalancePresenter构造方法的两个参数`username`,`categoryName`分别使用`@Username`,`@Category`注解来修饰

```
@Inject
AddMenuBalancePresenter(@Username String username, @Category String categoryName, IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
    this.mUsername = username;
    this.mCategoryName = categoryName;
    this.mView = view;
    this.mMenuBalanceRepository = mMenuBalanceRepository;
}

```

这样就解决了注入相同类型冲突的问题了.



## (四) 进一步分析总结Dagger2注解

自此, 我相信读者对Dagger2的使用应该有了一个大致的了解了. 但是可能还是云里雾里的. 例如在本例子中我们写了两组 **Component** 和 **Module**:

```
AddMenuBalanceComponent.java
AddMenuBalancePresenterModule.java

MenuBalanceRepoComponent.java
MenuBalanceRepoModule.java

```

为什么要提供两组 **Component** 和 **Module**呢? 在真实的项目开发中, 每个人的项目依赖都是不尽相同的, 那我怎么知道我应该写几个**Component** 和 **Module**呢?


OK, 现在我们刚才阅读到的知识全部清空, 假设你是Dagger2, 对于上面的代码依赖你怎么去做? 现在我们来大致模拟一下思路 : 

在 **AddMenuBalanceFragment** 中我们依赖 **AddMenuBalancePresenter** 那么你肯定想着我怎么创建 **AddMenuBalancePresenter**对象, 那么创建对象肯定要调用它的构造方法, 而**AddMenuBalancePresenter**构造方法有两个参数如下所示 : 

```
    @Inject
    AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
        this.mView = view;
        this.mMenuBalanceRepository = mMenuBalanceRepository;
    }
```

Dagger2看到这两个参数又在想, View这个参数我不能创建, 因为这个是系统管理的, 那么谁有这个View(Fragment/Activity), 当然是 Fragment/Activity, 所以View参数只能通过Fragment/Activity传递进来.


除了View参数还有**MenuBalanceRepository**参数 , Dagger2发现**MenuBalanceRepository**的构造方法也需要一个参数, 如下所示:

```
    @Inject
    MenuBalanceRepository(IMenuBalanceSource remoteSource){
        this.remoteSource = remoteSource;
    }
```
而这个参数又是一个接口**IMenuBalanceSource** 那Dagger怎么提供这个对象. 这个时候就需要 Component和Module.

```
//MenuBalanceRepoModule用来提供IMenuBalanceSource参数

@Singleton
@Component(modules = {MenuBalanceRepoModule.class, ApplicationModule.class})
public interface MenuBalanceRepoComponent {
    //提供Presenter构造方法最终需要的对象
    MenuBalanceRepository getMenuBalanceRepository();
}

@Module
public class MenuBalanceRepoModule {

    //提供IMenuBalanceSource参数
    @Singleton
    @Provides
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
}
```

从上面的代码可以知道组件(**Component**)MenuBalanceRepoComponent他来提供MenuBalanceRepository对象, 该对象所需要的IMenuBalanceSource是模块(**Module**)MenuBalanceRepoModule来提供的.


OK, **View和MenuBalanceRepository**两个参数都有了创建方式了, 但是这两个参数还是零散的, Dagger2怎么知道这两个参数给**AddMenuBalancePresenter**呢?  就算知道某种方式来提供这两个参数, 那怎么传递给**AddMenuBalancePresenter**的构造方法呢?  所以Dagger2还需要Component和Module来为**AddMenuBalancePresenter**提供构造方法的参数.

```
//MenuBalanceRepoComponent用于提供MenuBalanceRepository参数的
//AddMenuBalancePresenterModule用于提供View参数的

@FragmentScoped
@Component(dependencies = MenuBalanceRepoComponent.class, modules = AddMenuBalancePresenterModule.class)
public interface AddMenuBalanceComponent {

    void inject(AddMenuBalanceFragment fragment);
}


@Module
public class AddMenuBalancePresenterModule {

    private final IAddMenuBalanceView mView;

    public AddMenuBalancePresenterModule(IAddMenuBalanceView view) {
        mView = view;
    }

    //提供View参数
    @Provides
    IAddMenuBalanceView provideAddMenuBalanceView() {
        return mView;
    }
}

```

---

所以**Component和Module**是用来为**有参数的构造方法**提供参数的. 你所依赖的对象构造方法是有参数的, 那么你就必须为其提供一组**Component和Module**. 在上面的例子中因为**AddMenuBalancePresenter**的构造方法有参数:**AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository)**. 
它的参数**MenuBalanceRepository**的构造方法也有参数: **MenuBalanceRepository(IMenuBalanceSource remoteSource)** 这就是为什么我们需要两组**Component和Module**了.


---

是不是感觉有点繁琐? 为啥Dagger2不为我们自动去创建对象呢? 比如像下面的代码我觉得不需要我们去**new**:

```
    //提供IMenuBalanceSource参数
    @Singleton
    @Provides
    IMenuBalanceSource provideRemoteDataSource() {
        return new MenuBalanceRemoteSource();
    }
```

因为**MenuBalanceRemoteSource**本来只有无参数的构造方法, 那Dagger2完全可以帮我们自动去创建, 不用我们通过上面的代码片段的方式来自己手写的方式new出来. 

那么我们如何在不改动上面依赖的关系的情况下, 有什么方式简化下? 

再来回顾下它们之间的依赖: **Fragment-->Presenter-->MenuBalanceRepository->MenuBalanceRemoteSource**

再看看下面的这段代码 :

```
@Inject
AddMenuBalancePresenter(IAddMenuBalanceView view, MenuBalanceRepository mMenuBalanceRepository) {
    this.mView = view;
    this.mMenuBalanceRepository = mMenuBalanceRepository;
}
```

我们自己其实并没有去new这个AddMenuBalancePresenter, 是Dagger2帮我new的. 那我们可不可以让Dagger2帮我们自动去**new MenuBalanceRepository**和**MenuBalanceRemoteSource** 我们只要不通过构造方法的方式去初始化参数, 这样不就可以省掉一组**Component/Module**了吗? 
```
//老的实现方式,通过构造方法
public class MenuBalanceRepository implements IMenuBalanceSource {
    private final IMenuBalanceSource remoteSource;

    @Inject
    MenuBalanceRepository(IMenuBalanceSource remoteSource){
        this.remoteSource = remoteSource;
    }

    @Override
    public void addMenuBalance(Callback<MenuBalance> callback) {
        remoteSource.addMenuBalance(callback);
    }
}

//改成构造方法不带参数
public class MenuBalanceRepository2 implements IMenuBalanceSource {

    @Inject
    MenuBalanceRemoteSource2 remoteSource;

    @Inject
    MenuBalanceRepository2(){
    }

    @Override
    public void addMenuBalance(Callback<MenuBalance> callback) {
        remoteSource.addMenuBalance(callback);
    }
}

```

因为**MenuBalanceRepository和MenuBalanceRemoteSource2**构造方法都改成无参数的了, 那么就不需要**Component/Module**, 因为构造方法没有参数了, Dagger2就可以自动帮我们去new了.

AddMenuBalancePresenter还是和以前一样:

```
@Inject
AddMenuBalancePresenter2(IAddMenuBalanceView view, MenuBalanceRepository2 mMenuBalanceRepository) {
    this.mView = view;
    this.mMenuBalanceRepository = mMenuBalanceRepository;
}
```

在Fragment中就更加简单了:

```
@Inject
AddMenuBalancePresenter2 presenter;

@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DaggerAddMenuBalanceComponent2.builder()
            //就不需要设置数据层的Component了
            .addMenuBalancePresenterModule2(new AddMenuBalancePresenterModule2(this))
            .build()
            .inject(this);//完成注入
}

```


如果你想了解 Dagger2 是如何实现依赖注入的, 可以查阅我的这篇博客： [Android Dagger2（二）源码分析-对象是如何被注入的](http://blog.csdn.net/johnny901114/article/details/58231038)
