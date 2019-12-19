
系列文章目录导读：

- [Android开发之手把手教你写ButterKnife框架（一）](http://blog.csdn.net/johnny901114/article/details/52662376)
- [Android开发之手把手教你写ButterKnife框架（二）](http://blog.csdn.net/johnny901114/article/details/52664112)
- [Android开发之手把手教你写ButterKnife框架（三）](http://blog.csdn.net/johnny901114/article/details/52672188)


## 一、概述
[JakeWharton](https://github.com/JakeWharton)我想在 Android 界无人不知，无人不晓的吧， [ButterKnife](https://github.com/JakeWharton/butterknife)这个框架就是出自他只手。这个框架我相信很多人都用过，本系列博客就是带大家更加深入的认识这个框架，ButterKnife截至目前已有1w+的star：

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwOTI1MTUyODE0NjE0?x-oss-process=image/format,png)

如果我们对于这个优秀框架还是停留在使用阶段，那就太可惜。


> 本系列文章的主要内容如下：
1，ButterKnife是什么？
2，ButterKnife的作用和功能介绍。
3，ButterKnife的实现原理。
4，自己动手实现个ButterKnife。


## 二、ButterKnife是什么？

ButterKnife是一个编译时的依赖注入框架（compile-time dependency injection framework）用来简化android中类似findViewById、setOnclickListener等的模板代码。
比如在写activity界面的时候常常有如下代码：

```
public class MyActivity extents Activity{
	private EditText etConsultValidDate;
    private TextView tvToolbarCenter;
    private TextView  tvLeftAction;
    private TextView  tvRightAction;
    private TextView tvConsultTip;
    private TextView tvSuggestTime;
    private EditText etConsultTitle;
    private EditText etConsultDesc;
    private EditText etConsultTime;
    private EditText etConsultNumber;
    private RelativeLayout rlContactInfo;
    private LinearLayout llAnswerTime;
    private TextView tvAnswerTime, tvAnswerTimePre;
    private TextView tvToolbarRight;
    private LinearLayout llBottom;
    private int from;
    private RelativeLayout rlOppositeInfo;
    private ImageView ivHeadIcon;
    private TextView tvOppositeUsername, tvOppositeDesc;

	@Override
    protected void initViews() {
        tvExpertIdentify = (TextView) findViewById(R.id.tv_expert_identify);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        rlOppositeInfo = (RelativeLayout) findViewById(R.id.rl_opposite_info);
        ivHeadIcon = (ImageView) findViewById(R.id.iv_head_icon);
        tvOppositeUsername = (TextView) findViewById(R.id.tv_opposite_username);
        tvOppositeDesc = (TextView) findViewById(R.id.tv_opposite_desc);
        rbOppositeScore = (RatingBar) findViewById(R.id.rbar_star);
        tvUserCompany = (TextView) findViewById(R.id.tv_user_company);
        infoArrow = findViewById(R.id.iv_member_info_arrow);
        tvConsultTip = (TextView) findViewById(R.id.tv_consult_tip);
        tvLeftAction = (TextView) findViewById(R.id.tv_left_action);
        tvRightAction = (TextView) findViewById(R.id.tv_right_action);
        llAnswerTime = (LinearLayout) findViewById(R.id.ll_answer_time);
        tvAnswerTimePre = (TextView) findViewById(R.id.tv_answer_time_pre);
        tvAnswerTime = (TextView) findViewById(R.id.tv_answer_time);
        tvLeftAction.setOnClickListener(this);
        tvRightAction.setOnClickListener(this);
        etConsultTitle = (EditText) findViewById(R.id.et_consult_title);
        etConsultDesc = (EditText) findViewById(R.id.et_consult_desc);
        etConsultTime = (EditText) findViewById(R.id.et_contact_time);
        etConsultNumber = (EditText) findViewById(R.id.et_contact_number);
        etConsultValidDate = (EditText) findViewById(R.id.et_consult_valid_day);
        tvSuggestTime = (TextView) findViewById(R.id.tv_contact_time);
    }
}
```


初始化Views大量的没有技术含量的模板代码。如果界面比较复杂的话，这样的代码变得更多。使用ButterKnife可以很好的简化上面冗长的代码。

```
public class MyActivity extents Activity{

	@BindView(R.id.xxx) EditText etConsultValidDate;
    @BindView(R.id.xxx) TextView tvToolbarCenter;
    @BindView(R.id.xxx) TextView tvLeftAction;
    @BindView(R.id.xxx) TextView tvRightAction;
    @BindView(R.id.xxx) TextView tvConsultTip;
    @BindView(R.id.xxx) TextView tvSuggestTime;
    @BindView(R.id.xxx) EditText etConsultTitle;
    @BindView(R.id.xxx) EditText etConsultDesc;
    @BindView(R.id.xxx) EditText etConsultTime;
    @BindView(R.id.xxx) EditText etConsultNumber;
    @BindView(R.id.xxx) RelativeLayout rlContactInfo;
    @BindView(R.id.xxx) LinearLayout llAnswerTime;
    @BindView(R.id.xxx) TextView tvAnswerTime, tvAnswerTimePre;
    @BindView(R.id.xxx) TextView tvToolbarRight;
    @BindView(R.id.xxx) LinearLayout llBottom;
    @BindView(R.id.xxx) RelativeLayout rlOppositeInfo;
    @BindView(R.id.xxx) ImageView ivHeadIcon;
    @BindView(R.id.xxx) TextView tvOppositeUsername;
    @BindView(R.id.xxx) TextView tvOppositeDesc;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.simple_activity);
	    //初始化Views
	    ButterKnife.bind(this); 
    }
}
```

相比之下，极大的简化了View的初始化代码。


##三、ButterKnife的功能介绍（所有的功能）
除了上面的@BindView注解，还其他功能：

### 1. 使用@BindViews初始化多个View
@BindViews({ R.id.first_name, R.id.middle_name, R.id.last_name })
List<EditText> nameViews;

### 2. 使用@OnClick设置监听事件
@OnClick(R.id.submit)
public void submit(View view) {
  // TODO do something...
}

如果不想要submit方法参数可以去掉如：
@OnClick(R.id.submit)
public void submit() {
  // TODO do something...
}

View的参数还可以自动转换，比如给TextView设置点击事件
@OnClick(R.id.submit)
public void submit(TextView textView) {
  // TODO do something...
}

如果是自定义的View可以不指定View Id 如：
public class FancyButton extends Button {
  @OnClick
  public void onClick() {
    // TODO do something!
  }
}

### 3. listView item点击事件
@OnItemSelected(R.id.list_view)
void onItemSelected(int position) {
  // TODO ...
}

### 4. view的onTouchEvent
@OnTouch(R.id.example) boolean onTouch() {
    Toast.makeText(this, "Touched!", Toast.LENGTH_SHORT).show();
    return false;
}

### 5. 监听EditText的addTextChangedListener
@OnTextChanged(R.id.example) void onTextChanged(CharSequence text) {
    Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_SHORT).show();
}

### 6. 设置ViewPager的OnPageChangeListener
@OnPageChange(R.id.example_pager) void onPageSelected(int position) {
   Toast.makeText(this, "Selected " + position + "!", Toast.LENGTH_SHORT).show();
}

### 7. 设置TextView的OnEditorActionListener（该事件主要用来设置软键盘上的按钮）
@OnEditorAction(R.id.example) boolean onEditorAction(KeyEvent key) {
    Toast.makeText(this, "Pressed: " + key, Toast.LENGTH_SHORT).show();
    return true;
}

### 8. 设置View的OnFocusChangeListener事件
@OnFocusChange(R.id.example) void onFocusChanged(boolean focused) {
    Toast.makeText(this, focused ? "Gained focus" : "Lost focus", Toast.LENGTH_SHORT).show();
}

### 9. 设置View的OnLongClickListener长按事件
@OnLongClick(R.id.example) boolean onLongClick() {
    Toast.makeText(this, "Long clicked!", Toast.LENGTH_SHORT).show();
    return true;
}

### 10. 关于资源的绑定
  @BindString(R.string.title) String title;   //字符串
  @BindDrawable(R.drawable.graphic) Drawable graphic; //drawable 
  @BindColor(R.color.red) int red; // int or ColorStateList field
  @BindDimen(R.dimen.spacer) Float spacer; // int (for pixel size) or float (for exact value) field
  @BindArray(R.array.countries) String[] countries; 字符串数组
  @BindArray(R.array.icons) TypedArray icons;
  @BindBool(R.bool.is_tablet) boolean isTablet;


## 四、ButterKnife的实现原理

通过上面的例子我们知道，要使用ButterKnife首先要在目标代码使用注解，然后在onCreate生命周期方法里调用`ButterKnife.bind(this);`方法。使用注解没什么好说的，那只有看看ButterKnife.bind(this);这个方法是怎么实现的：
```
  @NonNull @UiThread
  public static Unbinder bind(@NonNull Activity target) {
    View sourceView = target.getWindow().getDecorView();
    return createBinding(target, sourceView);
  }
```

获取activity的decorView，然后调用createBinding方法：

```
private static Unbinder createBinding(@NonNull Object target, @NonNull View source) {
	//获取target的字节码
    Class<?> targetClass = target.getClass();
    if (debug) Log.d(TAG, "Looking up binding for " + targetClass.getName());
    //搜索构造方法（什么类的构造方法，下面会分析）
    Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);

    if (constructor == null) {
      return Unbinder.EMPTY;
    }

    //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
    try {
      //通过反射构造类实例
      return constructor.newInstance(target, source);
    } catch (IllegalAccessException e) {
    	//Ignore Exceptions
    } 
  }

```

createBinding方法的第一个参数target就是我们的activity实例，source就是decorView。上面的代码也比较简单，我也加上了注释。这个方法我就不多说了。然后看看findBindingConstructorForClass方法是怎么实现的：


```
  @Nullable @CheckResult @UiThread
  private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> cls) {
  	//从容器中查找构造方法，如果找到了直接返回。
    Constructor<? extends Unbinder> bindingCtor = BINDINGS.get(cls);
    if (bindingCtor != null) {
      if (debug) Log.d(TAG, "HIT: Cached in binding map.");
      return bindingCtor;
    }
    String clsName = cls.getName();
    //如果是android framework里的类则直接return
    if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
      if (debug) Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
      return null;
    }
    try {
      //拼接类名，然后获取该类的字节码
      Class<?> bindingClass = Class.forName(clsName + "_ViewBinding");
      //noinspection unchecked
      //获取该类的构造方法
      bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(cls, View.class);
      if (debug) Log.d(TAG, "HIT: Loaded binding class and constructor.");
    } catch (ClassNotFoundException e) {
      if (debug) Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
      bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
    }
    BINDINGS.put(cls, bindingCtor);
    return bindingCtor;
  }
```

`findBindingConstructorForClass`方法核心代码是下面2行代码：

```
Class<?> bindingClass = Class.forName(clsName + "_ViewBinding");
bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(cls, View.class);
```

意思就是target的类型(qualified name)拼接_ViewBinding，然后通过获取拼接后类的构造方法。那么`clsName + _ViewBinding` 这个类是从哪里来的。
我们把butterKnife的源码（8.4.0版本）下载下来，build代码后，查找以`_ViewBinding`为结尾的java类，发现有十个只有，都位于各自所在项目的build->gernerated->source->apt->debug目录下。
以里面的`SimpleActivity_ViewBinding`为例：

```
// Generated code from Butter Knife. Do not modify!
package com.example.butterknife.library;
public class SimpleActivity_ViewBinding<T extends SimpleActivity> implements Unbinder {
  //ignore some code
  @UiThread
  public SimpleActivity_ViewBinding(final T target, View source) {
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.subtitle = Utils.findRequiredViewAsType(source, R.id.subtitle, "field 'subtitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.hello, "field 'hello', method 'sayHello', and method 'sayGetOffMe'");
    target.hello = Utils.castView(view, R.id.hello, "field 'hello'", Button.class);
    //ignore some code
  }
}

```

target其实是我们上面的activity，source就是DecorView。发现所有的View的初始化工作全部放在了`SimpleActivity_ViewBinding`构造方法里。

```
// Generated code from Butter Knife. Do not modify!
```
通过这句话我们知道，`SimpleActivity_ViewBinding`是ButterKnife生成的。那么ButterKnife是怎么生成这个类的呢？

通过一个叫APT（Annotation Processing Tool）工具生成对应的类。

 **总结下：**
1. butterKnife 是一个运行时依赖注入框架，有效地帮我们简化一些重复代码。
2. butterKnife 在 ButterKnife.bind 方法里通过反射调用对应的类构造方法执行初始化工作，所以 ButterKnife 并不是完全没有使用反射，只在这个地方用到了。所以 ButterKnife 的效率也是很高的。对于反射这个技术，不应该持极端态度(彻底不用，或到处滥用)。特别是在 Android 中，到处都是反射，对性能也是有一定的影响的。
3. butterknife 使用 apt 技术来生成 Java 类。

















