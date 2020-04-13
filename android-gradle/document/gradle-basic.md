
## gradle 入门

### 执行任务
```
gradle -q -b hello.gradle hello
```

+ -q 控制 gradle 日志输出级别（Log errors only）
+ -b 指定脚本文件，不指定默认找 build.gradle

加 `-q` 还不加 `-q` 的区别：

```
gradle -b hello.gradle hello

> Task :hello
hello world

BUILD SUCCESSFUL in 606ms
1 actionable task: 1 executed
```

```
gradle -b hello.gradle hello

hello world
```

> gradle 和 gradlew 命令的区别，gradlew 是 grade wrapper 的简称， gradlew 用于包装 gradle，让开发者统一使用某一个版本的 gradle，而不是依赖于系统的配置的 gradle 环境变量。

如何执行多任务，直接在后面加上 task 名称即可，空格分隔：

```
gradlew -q hello hello2
```

### 强制刷新依赖

```
/gradlew  --refresh-dependencies  assemble
```