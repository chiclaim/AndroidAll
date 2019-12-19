
系列文章目录导读：

> [Android开发之手把手教你写ButterKnife框架（一）](http://blog.csdn.net/johnny901114/article/details/52662376)
> [Android开发之手把手教你写ButterKnife框架（二）](http://blog.csdn.net/johnny901114/article/details/52664112)
>[Android开发之手把手教你写ButterKnife框架（三）](http://blog.csdn.net/johnny901114/article/details/52672188)


## 一、概述

上一篇博客讲了，如何在android studio使用apt [《 Android开发之手把手教你写ButterKnife框架（二）》](http://blog.csdn.net/johnny901114/article/details/52664112)

然后在Processor里生成自己的代码，把要输出的类，通过StringBuilder拼接字符串，然后输出。

```
	try { // write the file
	    JavaFileObject source = processingEnv.getFiler().createSourceFile("com.chiclaim.processor.generated.GeneratedClass");
	    Writer writer = source.openWriter();
	    writer.write(builder.toString());
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
```

输出简单的类这种方法还是挺好的，简单明了，如果要输出复杂点的java文件，这个就不是很方便了，接下来介绍一个square公司开源的框架[javapoet](https://github.com/square/javapoet)来帮助我们构建java文件。



## 二、 JavaPoet 简单使用

### 通过 MethodSpec 类来构建 Java 方法，如：

```
MethodSpec main = MethodSpec.methodBuilder("main") 						 //方法名
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC) 					 //方法修饰符
    .returns(void.class)                            					 //方法返回类型
    .addParameter(String[].class, "args")           					 //方法参数
    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")//方法体语句
    .build();
```


### 通过 TypeSpec 来构建 Java 类的修饰符
```
TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")  //类名
    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)         //类的修饰符
    .addMethod(main)                                       添加类方法(MethodSpec main)
    .build();
```

### 通过 JavaFile 输出类
```
JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
    .build();
javaFile.writeTo(System.out);
```

### 通过 CodeBlock 来构建方法体
上面通过 MethodSpec.addaddParameter(String[].class, "args") 方法构建方法体，对于比较复杂的可以通过CodeBlock来构建：
```
codeBlock.addStatement("$L.inject(this, this)", mViewInjectorName);
```

javapoet就先介绍到这里。更多具体的使用可以查看官方文档或者其他资料。


## 三、通过 javapoet 生成 Bind 类

### 在 ButterKnifeProcessor process 方法中获取基本信息

我们要想达到在生成的类中初始化activity Views，那么肯定需要如下类似下面的代码(伪代码)：
```
public class MainActivity_Binding {

    public MainActivity_Binding(MainActivity target,View view) {
        target.text1 = (TextView)view.findViewById(id);
        target.text2 = (TextView)view.findViewById(id);
        target.text3 = (TextView)view.findViewById(id);
        target.text4 = (TextView)view.findViewById(id);
    }
}
```

据此，我们需要在`ButterKnifeProcessor process`方法里获取三个基本信息：
1、`注解所在的类`，用于生成类名，比如MainActivity使用了注解，那么生成的类就是 MainActivity_ViewBinding

2、`注解的值`，用于findViewById，如：
```
    @BindView(R.id.title)
    TextView title;
```
那么我们要获取的值就是R.id.title

3、`注解所在字段的类型`，用于强转。如：
```
    @BindView(R.id.title)
    TextView title;
```
那么我们要获取的类型就是TextView

通过下面的方法可以获取上面的信息
【element.getEnclosingElement()】 				  //注解所在的类
【element.getAnnotation(BindView.class).value()】 //注解上的值, 用于findViewById
【element.asType()】								 //注解字段的类型,用于强转


通过上一篇博客知道，我们是在process方法里生成代码的：

```
@Override
public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
	//TODO do something
    return true;
}
```

### 对RoundEnvironment里的信息进行分组处理 

所有关于类上的注解信息，全部在 RoundEnvironment roundEnv里，而且可能的多个类用到了注解， 所以我们要对RoundEnvironment的信息进行分组处理。

我通过Map来保存分组的信息，
Map<TypeElement, BindClass> map = new LinkedHashMap<>();

key用来保存注解所在的类，value保存用来需要生成的代码。最后在遍历mao，逐一生成代码。



下面是我处理的方法，每行都有注释：

```
//roundEnv里的信息进行分组
private void parseRoundEnvironment(RoundEnvironment roundEnv) {
		// 保存分组信息
        Map<TypeElement, BindClass> map = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            //注解的值
            int annotationValue = element.getAnnotation(BindView.class).value();
            //如果不存在创建BindClass，要创建的代码都存在BindClass里
            BindClass bindClass = map.get(enclosingElement);
            if (bindClass == null) {
                bindClass = BindClass.createBindClass(enclosingElement);
                map.put(enclosingElement, bindClass);
            }
            String name = element.getSimpleName().toString();
            TypeName type = TypeName.get(element.asType());
            //ViewBinding用于保存每个注解的相关信息（比如注解所在字段的名称、注解所在字段的类型、注解上的值，）
            ViewBinding viewBinding = ViewBinding.createViewBind(name, type, annotationValue);
            //因为一个类上可能多处用了注解，所以用一个集合保存
            bindClass.addAnnotationField(viewBinding);
        }

        //迭代分组后的信息，主义生成对应的类
        for (Map.Entry<TypeElement, BindClass> entry : map.entrySet()) {
            printValue("==========" + entry.getValue().getBindingClassName());
            try {
                entry.getValue().preJavaFile().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```


`ViewBinding`用于保存每个注解的相关信息，代码也很简单：

```
class ViewBinding {

    private final String name;
    private final TypeName type;
    private final int value;

    private ViewBinding(String name, TypeName type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    static ViewBinding createViewBind(String name, TypeName type, int value) {
        return new ViewBinding(name, type, value);
    }
}
```


BindClass用于保存需要生成的代码，里面封装了javapoet相关处理，所有具有生成代码的功能.
先来看看创建BindClass构造方法：

```
    private BindClass(TypeElement enclosingElement) {
        //asType 表示注解所在字段是什么类型(eg. Button TextView)
        TypeName targetType = TypeName.get(enclosingElement.asType());
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }
        //注解所在类名(包括包名)
        String packageName = enclosingElement.getQualifiedName().toString();
        packageName = packageName.substring(0, packageName.lastIndexOf("."));
        String className = enclosingElement.getSimpleName().toString();
        //我们要生成的类的类名
        ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");
        boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
        //注解所在类，在生成的类中，用于调用findViewById
        this.targetTypeName = targetType;
        this.bindingClassName = bindingClassName;
        //生成的类是否是final
        this.isFinal = isFinal;
        //用于保存多个注解的信息
        fields = new ArrayList<>();
    }

```

添加注解信息实体
```
    void addAnnotationField(ViewBinding viewBinding) {
        fields.add(viewBinding);
    }
```

生成类的修饰符，方法：
```
    private TypeSpec createTypeSpec() {
        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC);
        if (isFinal) {
            result.addModifiers(FINAL);
        }

        result.addMethod(createConstructor(targetTypeName));


        return result.build();
    }
```

创建构造方法，在构造方法里生成初始化View的代码：
```
    private MethodSpec createConstructor(TypeName targetType) {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);
        //构造方法有两个参数，target和source，在本例子中，Target就是activity，source就是activity的DecorView
        constructor.addParameter(targetType, "target", FINAL);
        constructor.addParameter(VIEW, "source");
        //可能有多个View需要初始化，也就是说activity中多个字段用到了注解
        for (ViewBinding bindings : fields) {
        	//生成方法里的语句，也就是方法体
            addViewBinding(constructor, bindings);
        }

        return constructor.build();
    }
```

下面看看如何为activity中每个用到注解的View在构造方法中生成初始化代码：
```
    private void addViewBinding(MethodSpec.Builder result, ViewBinding binding) {
    	//通过CodeBlock生成语句，因为生成的语句比较复杂。
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("target.$L = ", binding.getName());
        //判断是否需要强制类型转换，如果目标View本来就是View，那就不需要强转了
        boolean requiresCast = requiresCast(binding.getType());
        if (!requiresCast) {
            builder.add("source.findViewById($L)", binding.getValue());
        } else {
        	//我们使用ProcessorUtils重点工具方法findViewByCast进行强转 $T就是一个占位符，UTILS就是ClassName包含了UTILS的包名和类名
        	//用ProcessorUtils替换成$T CodeBlock还支持很多占位符，需要了解更多可以去看看文档.
            builder.add("$T.findViewByCast", UTILS);
            //ProcessorUtils.findViewByCast需要的参数source就是DecorView
            builder.add("(source, $L", binding.getValue());
            //ProcessorUtils.findViewByCast需要的参数$T.class，就是目标View需要强转的类型
            builder.add(", $T.class", binding.getRawType());
            builder.add(")");
        }
        result.addStatement("$L", builder.build());

    }
```


下面就是强转用到的工具类：
```
public class ProcessorUtils {


    public static <T> T findViewByCast(View source, @IdRes int id, Class<T> cls) {
        View view = source.findViewById(id);
        return castView(view, id, cls);
    }

    private static <T> T castView(View view, @IdRes int id, Class<T> cls) {
        try {
            return cls.cast(view);
        } catch (ClassCastException e) {
            //提示使用者类型转换异常
            throw new IllegalStateException(view.getClass().getName() + "不能强转成" + cls.getName());
        }
    }

}

```

> 注意, 如果你需要调试public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)，只能输出如System.out.println(message)，不能debug，也可以通过ProcessingEnvironment..getMessager().printMessage()方法来输出， 他们的输出的信息只在`gradle console`看得到，不要到logcat等其他窗口看。

至此，对RoundEnvironment分组处理就完成了，我们Build项目，可以在app build文件夹看到我们生成的类：
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwOTI2MTcxOTI1MjAx?x-oss-process=image/format,png)



MainActivity_ViewBinding:
```
// Generated code from My Butter Knife. Do not modify!!!
package com.chiclaim.sample;

import android.view.View;
import android.widget.TextView;
import com.chiclaim.butterknife.ProcessorUtils;

public class MainActivity_ViewBinding {
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    target.textView = ProcessorUtils.findViewByCast(source, 2131427414, TextView.class);
    target.view = ProcessorUtils.findViewByCast(source, 2131427415, TextView.class);
  }
}


```

接下来就简单了，在MainActivity中调用MainActivity_ViewBinding的构造方法就可以了。因为我们生成的类是有规律的，包名就是使用者的包名，类名是使用者类名加ViewBinding。然后通过反射调用下就可以了：

```
public class MyButterKnife {
    public static void bind(Activity activity) {
        //获取activity的decorView
        View view = activity.getWindow().getDecorView();
        String qualifiedName = activity.getClass().getName();

        //找到该activity对应的Bind类
        String generateClass = qualifiedName + "_ViewBinding";
        try {
            //然后调用Bind类的构造方法,从而完成activity里view的初始化
            Class.forName(generateClass)
                    .getConstructor(activity.getClass(), View.class).newInstance(activity, view);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

```


所以只需要在跟butterknife一样在activity的onCreate声明周期方法里调用bind方法即可,如下所示：

```
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.view)
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //完成初始化操作
        MyButterKnife.bind(this);

        Toast.makeText(this, textView + "--textView", Toast.LENGTH_LONG).show();
        Log.d("MainActivity", textView + "," + view);

        textView.setText("initialed by my butter knife");
    }
}
```

## 四、总结
1> butterknife 是一个运行时依赖祝框架，简化android的大量模板代码，使用apt来生成代码
2> 像很多框架都是跟butterKnife的机制太不多的，比如下面几款流行的框架：
greendao 流行的sqlite框架 
dagger2 依赖注入框架 
PermissionsDispatcher 处理Android6.0权限的框架 
所以利用这个技术，也可以整个自己的框架。

更多实现信息，可以查看github上的源码： [https://github.com/chiclaim/study-butterknife](https://github.com/chiclaim/study-butterknife)


---
# 如果你觉得本文帮助到你，给我个关注和赞呗！
**另外，我为 Android 程序员编写了一份：超详细的 Android 程序员所需要的技术栈思维导图**。

**如果有需要可以移步我的 [GitHub -> AndroidAll](https://github.com/chiclaim/AndroidAll)，里面包含了最全的目录和对应知识点链接，帮你扫除 Android 知识点盲区。** 由于篇幅原因只展示了 Android 思维导图：
![超详细的Android技术栈](https://img-blog.csdnimg.cn/20191213105038316.jpg)