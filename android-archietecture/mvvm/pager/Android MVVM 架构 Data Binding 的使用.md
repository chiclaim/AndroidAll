
# 项目整体效果：
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwMzA1MTU1NjE2NTUz?x-oss-process=image/format,png) 



## 什么是MVVM , 为什么需要MVVM？

MVVM是Model-View-ViewModel的简写. 它是有三个部分组成：Model、View、ViewModel。

Model：数据模型层。包含业务逻辑和校验逻辑。

View：屏幕上显示的UI界面（layout、views）。

ViewModel：View和Model之间的链接桥梁，处理视图逻辑。

MVVM架构图如下：

[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-dBGmf8lk-1575810898135)(http://img.blog.csdn.net/20170211170915345 "MVVM Image")]

MVVM架构通过ViewModel隔离了UI层和业务逻辑层，降低程序的耦合度。通过DataBinding实现View和ViewModel之间的绑定。

### Android App 中MVC的不足
一般来说，我们开发Android App是基于MVC，由于MVC的普及和快速开发的特点，一个app从0开发一般都是基于MVC的。

Activity、Fragment相当于C (Controller), 布局相当于V（View）, 数据层相当于M（Model）

随着业务的增长，Controller里的代码会越来越臃肿，因为它不只要负责业务逻辑，还要控制View的展示。也就是说Activity、Fragment杂糅了Controller和View，耦合变大。并不能算作真正意义上的MVC。

编写代码基本的过程是这样的，在Activity、Fragment中初始化Views，然后拉取数据，成功后把数据填充到View里。

`假如有如下场景`：

> 我们基于MVC开发完第一版本，然后企业需要迭代2.0版本，并且UI界面变化比较大，业务变动较小，怎么办呢？
当2.0的所有东西都已经评审过后。这个时候，新建布局，然后开始按照新的效果图，进行UI布局。然后还要新建Activity、Fragment把相关逻辑和数据填充到新的View上。
如果业务逻辑比较复杂，需要从Activity、Fragment中提取上个版本的所有逻辑，这个时候自己可能就要晕倒了，因为一个复杂的业务，一个Activity几千行代码也是很常见的。千辛万苦做完提取完，可能还会出现很多bug。

MVP架构图如下：

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMTI5MjMyMzI2NzQ2?x-oss-process=image/format,png)


MVP把视图层抽象到View接口，逻辑层抽象到Presenter接口，提到了代码的可读性。降低了视图逻辑和业务逻辑的耦合。

但是有MVP的不足:

1. 接口过多，一定程度影响了编码效率。其实这也不算是不足，为了更好的分层解耦，这也是必须的。
2. 导致Presenter的代码量过大。


这个时候MVVM就闪亮登场了。从上面的MVVM功能图我们知道:

1. 可重用性。你可以把一些视图逻辑放在一个ViewModel里面，让很多view重用这段视图逻辑。
   在Android中，布局里可以进行一个视图逻辑，并且Model发生变化，View也随着发生变化。
2. 低耦合。以前Activity、Fragment中需要把数据填充到View，还要进行一些视图逻辑。现在这些都可在布局中完成（具体代码请看后面）
甚至都不需要再Activity、Fragment去findViewById。这时候Activity、Fragment只需要做好的逻辑处理就可以了。


现在我们回到上面从app1.0到app2.0迭代的问题，如果用MVVM去实现那就比较简单，这个时候不需要动Activity、Fragment，
只需要把布局按照2.0版本的效果实现一遍即可。因为视图逻辑和数据填充已经在布局里了，这就是上面提到的可重用性。


##Android中如何实现DataBinding？
Google在2015年的已经为我们DataBinding技术。下面就详细讲解如何使用DataBinding。

### 环境准备
在工程根目录build.gradle文件加入如下配置，把Android Gradle 插件升级到最新：

	dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
    }
   


在app里的build.gradle文件加入如下配置，启用data binding 功能：


    dataBinding {
        enabled true
    }


### 来个简单的例子
实现上面效果的“Data Binding Simple Sample”

####data binding 布局格式和以往的有些区别：

```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   
   //normal layout
   <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
</layout>
```


- 布局的根节点为<layout/>

- 布局里使用的model 通过<data>中的<variable>指定：

  ```
     <variable name="user" type="com.example.User"/>
  
  ```

- 设置空间属性的值，通过@{}语法来设置：

  ```
     android:text="@{user.firstName}"

  ```

下面是完整的布局实现：

```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable
            name="user"
            type="com.mvvm.model.User"/>
    </data>

    <LinearLayout
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="16dp"
        tools:context=".ui.MainActivity">


        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.realName}"
            android:textSize="14dp"/>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="@{user.mobile}"
            android:textSize="14dp"/>


        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{String.valueOf(user.age)}"
            android:textSize="14dp"/>

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="15dp"
            android:layout_marginBottom="40dp"
            android:layout_marginTop="40dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <View
                android:layout_width="match_parent"
                android:layout_height="1dp"

                android:layout_weight="1"
                android:background="@android:color/darker_gray"/>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="With String Format"
                android:textSize="10dp"
                android:textStyle="bold"/>

            <View
                android:layout_width="match_parent"
                android:layout_height="1dp"
                android:layout_marginBottom="20dp"
                android:layout_marginTop="20dp"
                android:layout_weight="1"
                android:background="@android:color/darker_gray"/>


        </LinearLayout>

        <TextView
            android:id="@+id/tv_realName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{@string/name_format(user.realName)}"
            android:textSize="14dp"/>

        <TextView
            android:id="@+id/tv_phone"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="@{@string/mobile_format(user.mobile)}"
            android:textSize="14dp"/>

    </LinearLayout>
</layout>



```



####接下来实现数据模型类User：

```
public class User {

    private String userName;
    private String realName;
    private String mobile;
    private int age;

    public User(String realName, String mobile) {
        this.realName = realName;
        this.mobile = mobile;
    }

    public User() {
    }
    
    //ignore getter and setter. see code for detail.

}

```

####在Activity中 绑定数据

```
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple);
        fetchData();
    }
    
    //模拟获取数据
    private void fetchData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoadingDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideLoadingDialog();
                User user = new User("Chiclaim", "13512341234");
                binding.setUser(user);
                //binding.setVariable(com.mvvm.BR.user, user);
            }
        }.execute();
    }
}

```


> 通过DataBindingUtil.setContentView设置布局，通过binding类设置数据模型:

```
binding.setUser(user);
```


### 布局详解

#### import导入
- 通过<import>标签导入：

	```
	<data>
    	<import type="android.view.View"/>
    	<import type="com.mvvm.model.User"/>
    	<variable name="user" type="User">
	</data>
	android:visibility="@{user.isAdult ? View.VISIBLE : View.GONE}"
	```

- 如果产生了冲突可以使用别名的方式：

	```
	<import type="com.example.User"/>
	<import type="com.mvvm.model.User" alias="MyUser"/>
	<variable name="user" type="User">
	<variable name="user" type="MyUser">
	```

- 集合泛型左尖括号需要使用转译：

	```
	<import type="com.example.User"/>
    <import type="java.util.List"/>
    <variable name="user" type="User"/>
    <variable name="userList" type="List&lt;User>"/>
	
	```

- 使用导入类的静态字段和方法：

	```
	<data>
    	<import type="com.example.MyStringUtils"/>
    	<variable name="user" type="com.example.User"/>
	</data>
	…
	<TextView
   		android:text="@{MyStringUtils.capitalize(user.lastName)}"
   		android:layout_width="wrap_content"
   		android:layout_height="wrap_content"/>
	
	```

> 像JAVA一样，java.lang.*是自动导入的。



#### Variables
在<data>节点中使用<varibale>来设置。

```
<import type="android.graphics.drawable.Drawable"/>
<variable name="user"  type="com.example.User"/>
<variable name="image" type="Drawable"/>
<variable name="note"  type="String"/>

```

- Binding类里将会包含通过variable设置name的getter和setter方法。如上面的setUser，getUser等。

- 如果控件设置了id，那么该控件也可以在binding类中找到，这样就不需要findViewById来获取View了。

#### 自定义Binding类名(Custom Binding Class Names)

以<layout/>为根节点布局，android studio默认会自动产生一个Binding类。类名为根据布局名产生，如一个名为activity_simple的布局，它的Binding类为ActivitySimpleBinding，所在包为app_package/databinding。
当然也可以自定义Binding类的名称和包名：

  1. `<data class="CustomBinding"></data>` 在app_package/databinding下生成CustomBinding；
  
  2. `<data class=".CustomBinding"></data>` 在app_package下生成CustomBinding；
  
  3. `<data class="com.example.CustomBinding"></data>` 明确指定包名和类名。



#### Includes
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </LinearLayout>
</layout>

```

name.xml 和 contact.xml都必须包含  ` <variable name="user" ../>`

### DataBinding Obervable

在上面的一个例子上，数据是不变，随着用户的与app的交互，数据发生了变化，如何更新某个控件的值呢？

有如下几种方案(具体实现下载代码，运行，点击DataBinding Observable 按钮)：

1. BaseObservable的方式

使User继承BaseObservable，在get方法上加上注解@Bindable，会在BR(BR类自动生成的)生成该字段标识(int)
set方法里notifyPropertyChanged(BR.field);

```
public class User extends BaseObservable{

    private String userName;
    private String realName;

    /**
     * 注意: 在BR里对应的常量为follow
     */
    private boolean isFollow;


    public User(String realName, String mobile) {
        this.realName = realName;
        this.mobile = mobile;
    }

    public User() {
    }

    @Bindable
    public boolean isFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
        notifyPropertyChanged(BR.follow);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }
```

> 如果数据发生变化通过set方法，view的值会自动更新，是不是很方便。


2. 通过ObserableField来实现

```
public class UserField {
    public final ObservableField<String> realName = new ObservableField<>();
    public final ObservableField<String> mobile = new ObservableField<>();

}

```

布局中使用：

```
   <variable name="fields" type="com.mvvm.model.UserField"/>
   
   <TextView
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:background="@null"
       android:text="@{fields.realName}"
       android:textSize="14dp"/>

```

代码中设置/改变数据：

```
userField.realName.set("Chiclaim");

```

3. Observable Collections方式：

```
private ObservableArrayMap<String, Object> map = new ObservableArrayMap();

//设置数据
map.put("realName", "Chiclaim");
map.put("mobile", "110");


```

布局中使用：

```
   <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:text="@{collection[`mobile`]}"
        android:textSize="14dp"
        android:textStyle="bold"/>

```

### 下面通过DataBinding来实现列表

获取square公司retrofit代码贡献者数据列表，通过RecyclerView来实现。

RecyclerView的Adapter实现的核心方法为两个onCreateViewHolder、onBindViewHolder方法和Item的ViewHolder。

```
    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContributorBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_contributor, parent, false);
        ContributorViewHolder viewHolder = new ContributorViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ContributorViewHolder contributorViewHolder = (ContributorViewHolder) viewHolder;
        Contributor contributor = getModel(position);
        contributorViewHolder.getBinding().setVariable(com.mvvm.BR.contributor, contributor);
        contributorViewHolder.getBinding().executePendingBindings();
        Picasso.with(mContext).load(contributor.getAvatar_url()).
                into(contributorViewHolder.binding.ivAvatar);
    }


```

通过setVariable方法来关联数据。
getBinding().setVariable(com.mvvm.BR.contributor, contributor)
大家看到BR.contributor的contributor常量是怎么产生的？布局里的<variable name="">中的name属性值。如：<variable name="book"> 那么就会自动生成BR.book。`有点类似以前的R里面的id`。 有人会问了如果别的实体（model）也有相同的book属性怎么办？那他到底使用哪个呢？其实这是不会冲突，因为在不用的地方用，他的上下文(Binging)不一样，所以不会冲突。也是和以前的R里面的常量是一回事情。只是把它放到BR里面去了。所以我猜想BR的全称应该是（`Binding R`(R就是以前我们用的常量类)）虽然官方没有说明。

通过executePendingBindings强制执行绑定数据。

Item对应的VIewHolder

```
    public class ContributorViewHolder extends RecyclerView.ViewHolder {

        ItemContributorBinding binding;

        public void setBinding(ItemContributorBinding binding) {
            this.binding = binding;
        }

        public ItemContributorBinding getBinding() {
            return binding;
        }

        public ContributorViewHolder(View itemView) {
            super(itemView);
        }
    }


```

### EL表达式(Expression Language)

####DataBinding支持的表达式有：

数学表达式： + - / * %

字符串拼接 +

逻辑表达式 && ||

位操作符 & | ^

一元操作符 + - ! ~

位移操作符 >> >>> <<

比较操作符 == > < >= <=

instanceof

分组操作符 ()

字面量 - character, String, numeric, null

强转、方法调用

字段访问

数组访问 []

三元操作符 ?:

#### 聚合判断（Null Coalescing Operator）语法 ‘??’
      <TextView
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:padding="5dp"
         android:text="@{user.userName ?? user.realName}"
         android:textSize="12dp"/>

上面的意思是如果userName为null，则显示realName。

#### Resource（资源相关）
在DataBinding语法中，可以吧resource作为其中的一部分。如：

```
android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"

```

除了支持dimen，还支持color、string、drawable、anim等。

注意，对mipmap图片资源支持还是有问题，目前只支持drawable。

#### Event Binding (事件绑定)

事件处理器：

```
public interface UserFollowEvent {
    void follow(View view);
    void unFollow(View view);
}

```

布局中使用：

```
<variable
     name="event"
     type="com.mvvm.event.UserFollowEvent"/>
     
android:onClick="@{user.isFollow ? event.unFollow : event.follow}"
```

在Activity实现该接口UserFollowEvent：

```
    @Override
    public void follow(View view) {
        user.setIsFollow(true);
    }

    @Override
    public void unFollow(View view) {
        user.setIsFollow(false);
    }
```

效果如下所示：
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwMjI3MTEyMjU4NjQ3?x-oss-process=image/format,png)

点击按钮后：
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwMjI3MTEyMzU3MDEy?x-oss-process=image/format,png)


### Custom Setter（自定义Setter方法）
有些时候我们需要自定义binding逻辑,如:在一个TextView上设置大小不一样的文字,这个时候就需要我们自定义binding逻辑了.

在比如我们为ImageView加载图片，通过总是通过类似这样的的代码来实现：

```
Picasso.with(view.getContext()).load(url).into(view);
```
如果我们自定Setter方法，那么这些都可以是自动的。怎么实现呢？

```
@BindingAdapter({"imageUrl"})
public static void loadImage(ImageView view, String url) {
      Log.d("BindingAdapter", "loadImage(ImageView view, String url)");
      Log.d("BindingAdapter", url + "");
      Picasso.with(view.getContext()).load(url).into(view);
}
```
@BindingAdapter({"imageUrl"}) 这句话意味着我们自顶一个imageUrl属性，可以在布局文件中使用。当在布局文件中设置该属性的值发生改变，会自动
调用loadImage(ImageView view, String url)方法。

布局中使用：

```
<ImageView
      android:layout_width="50dp"
      android:layout_height="50dp"
      android:background="#f0f0f0"
      app:imageUrl="@{avatar}"/>
```

再来看下如何实现：在一个TextView上设置大小不一样的文字(其实是一样的)

```
@BindingAdapter("spanText")
public static void setText(TextView textView, String value) {
    Log.d("BindingAdapter", "setText(TextView textView,String value)");
    SpannableString styledText = new SpannableString(value);
    styledText.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.style0),
            0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    styledText.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.style1),
            5, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    styledText.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.style0),
            12, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    textView.setText(styledText, TextView.BufferType.SPANNABLE);
}
```

```
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:spanText="@{`Hello Custom Setter`}"/>
```

注意：使用自定义Setter，需要使用dataBinding语法。以下用法是不对的：

```
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:spanText="Hello Custom Setter"/>
```


其他的例子就不一一在这里介绍了，详情可以查看github上的代码。
