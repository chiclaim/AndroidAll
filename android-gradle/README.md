
```
gradle -q -b hello.gradle hello
```

+ -q 控制 gradle 日志输出级别（Log errors only）
+ -b 指定脚本文件，不指定默认找 build.gradle

加 -q 还不加 -q 的区别：

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