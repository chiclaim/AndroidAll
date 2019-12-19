package object_keyword


/**
 * Desc: companion object 函数扩展 演示
 * Created by Chiclaim on 2018/9/20.
 */


class ObjectKeywordTest5 {

    companion object {
    }


}

fun ObjectKeywordTest5.Companion.create(): ObjectKeywordTest5 {
    return ObjectKeywordTest5()
}


fun main(args: Array<String>) {
    ObjectKeywordTest5.create()
}
/*

public final class ObjectKeywordTest5 {
   public static final ObjectKeywordTest5.Companion Companion = new ObjectKeywordTest5.Companion((DefaultConstructorMarker)null);

   public static final class Companion {
      private Companion() {
      }
   }
}

public final class ObjectKeywordTest5Kt {
   @NotNull
   public static final ObjectKeywordTest5 create(@NotNull ObjectKeywordTest5.Companion $receiver) {
      Intrinsics.checkParameterIsNotNull($receiver, "$receiver");
      return new ObjectKeywordTest5();
   }

   public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      create(ObjectKeywordTest5.Companion);
   }
}

 */