
系列文章目录导读：

> [Android开发之手把手教你写ButterKnife框架（一）](http://blog.csdn.net/johnny901114/article/details/52662376)
[Android开发之手把手教你写ButterKnife框架（二）](http://blog.csdn.net/johnny901114/article/details/52664112)
[Android开发之手把手教你写ButterKnife框架（三）](http://blog.csdn.net/johnny901114/article/details/52672188)


上一篇博客[Android开发之手把手教你写ButterKnife框架（一）](http://blog.csdn.net/johnny901114/article/details/52662376)我们讲了ButterKnife是什么、ButterKnife的作用和功能介绍以及ButterKnife的实现原理。

本篇博客主要讲在android studio中如何使用apt。



### 一、新建个项目, 然后创建一个module名叫processor

新建module的时候一定要选择 `Java Library` 否则在后面会找不到AbstractProcessor。

分别在app和processor 的文件夹下的build.gralde添加如下配置：

```
compileOptions {
   sourceCompatibility JavaVersion.VERSION_1_7
   targetCompatibility JavaVersion.VERSION_1_7
}
```


### 二、然后在建一个module名叫annotation，主要用来保存项目用到的annotation.

```
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS) @Target(FIELD)
public @interface BindView {
    int value();
}
```

### 三、新建MainActivity 字段上加上注解，如下：
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
    }
}

```



### 三、在processor module下新建一个ButterKnifeProcessor 继承`AbstractProcessor`. 

```
@SupportedAnnotationTypes("com.chiclaim.processor.annotation.BindView")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ButterKnifeProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		StringBuilder builder = new StringBuilder()
                .append("package com.chiclaim.processor.generated;\n\n")
                .append("public class GeneratedClass {\n\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \"");


        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            String objectType = element.getSimpleName().toString();
            // this is appending to the return statement
            builder.append(objectType).append(" says hello!\\n");
        }

        builder.append("\";\n") // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class

        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.chiclaim.processor.generated.GeneratedClass");
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
        return true;
    }
}
```

@SupportedAnnotationTypes(...) 里面的参数是我们需要处理的注解


### 四、在processor module主目录下`resources`目录
然后新建`META-INF`目录，然后在`META-INF`下新建`services` 然后新建一个文件名为 `javax.annotation.processing.Processor`, 里面的内容就是刚刚新建的ButterKnifeProcessor的qualified name ：

```
com.chiclaim.butterknife.processor.ButterKnifeProcessor
```

当然也可以用新建这么多文件夹，只需要加入google AutoService，这样就会自动完成上面的操作。

```
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
//AutoService自动生成文件(in processor.jar): META-INF/services/javax.annotation.processing.Processor
public class ButterKnifeProcessor extends AbstractProcessor
```



### 五、添加android-apt支持
在全局的build.gradle添加添加apt支持，`com.neenbedankt.gradle.plugins:android-apt:1.8`,如下所示：
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

ext {
    sourceCompatibilityVersion = JavaVersion.VERSION_1_7
    targetCompatibilityVersion = JavaVersion.VERSION_1_7
}

allprojects {
    repositories {
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

分别在app module的build.gradle添加 apply plugin 如下所示：
```
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'
```

### 六、添加module之间的依赖

```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:24.2.1'
    testCompile 'junit:junit:4.12'
    compile project(':annotation')
    compile project(':processor')
    compile project(':butterknife')
}
//把processor module生成的jar拷贝到app libs目录
//task processorTask(type: Exec) {
//    commandLine 'cp', '../processor/build/libs/processor.jar', 'libs/'
//}
task processorTask(type: Copy) {
	  from '../processor/build/libs/processor.jar' into 'libs/'
}

//build processor 生成processor.jar
processorTask.dependsOn(':processor:build')
preBuild.dependsOn(processorTask)

```

如下图所示：
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwOTI1MjAwMDMwNDM0?x-oss-process=image/format,png)


会在app module的build的目录下生成代码，如：

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYwOTI1MjAyNzE2MTg1?x-oss-process=image/format,png)

```
public class GeneratedClass {
	public String getMessage() {
		return "button says hello!\nimageView says hello!\ntextView says hello!\nview says hello!\n";
	}
}
```


据此，在android studio 使用apt就介绍完毕了。



下一篇将介绍如何实现ButterKnife注入初始化View功能。


## 参考文档
[http://blog.stablekernel.com/the-10-step-guide-to-annotation-processing-in-android-studio](http://blog.stablekernel.com/the-10-step-guide-to-annotation-processing-in-android-studio)

