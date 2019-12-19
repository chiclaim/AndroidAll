package lambda

/**
 * desc: 把Lambda赋值给一个变量
 *
 * Created by Chiclaim on 2018/09/22
 */

//最多支持22参数 对应的处理类是 Function22

// N  -> FunctionN

class LambdaToVariableTest {

    private val base = 10

    val sum = { x: Int, y: Int, z: Int ->
        x + y + z + base
    }
}

fun main(args: Array<String>) {
    val test = LambdaToVariableTest()
    println(test.sum(12, 10, 15))
}

/*
//除了要看编译后对应的Java代码，还需要分析其编译后生成的class字节码文件
//如下面的这句代码： sum = (Function3)null.INSTANCE; 就问题了，所以Kotlin插件反编译出来的Java代码不是很完全

public final class LambdaToVariableTestKt {
   @NotNull
   private static final Function3 sum;

   @NotNull
   public static final Function3 getSum() {
      return sum;
   }

   public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      int var1 = ((Number)sum.invoke(12, 10, 15)).intValue();
      System.out.println(var1);
   }

   static {
      sum = (Function3)null.INSTANCE;
   }
}



 */