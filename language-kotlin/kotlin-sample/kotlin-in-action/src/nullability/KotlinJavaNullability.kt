package nullability

/**
 * Desc: Kotlin和Java交互关于Null相关的问题
 * Created by Chiclaim on 2018/9/26.
 */

class UserActivity : UserView {
    var userPresenter = UserPresenter(this)


    override fun showFriendList(list: List<User>) {
        list.forEachIndexed { index, user ->
            println("${index + 1}, $user")
        }
    }


    fun getFriendList() {
        println("-----------get remote friends")
        userPresenter.getRemoteFriendList()
    }

    fun getFriendList2() {
        println("-----------get local friends")
        userPresenter.getLocalFriendList()
    }

}


fun main(args: Array<String>) {
    with(UserActivity()) {
        getFriendList()
        getFriendList2() //It will throw a Exception
    }
}

/*


Kotlin会在用到的非空参数前做非空校验（底层自动为我们加上非空校验代码）：
Intrinsics.checkParameterIsNotNull(list, "list");

public void showFriendList(@NotNull List list) {
  Intrinsics.checkParameterIsNotNull(list, "list");
  Iterable $receiver$iv = (Iterable)list;
  int index$iv = 0;
  Iterator var4 = $receiver$iv.iterator();

  while(var4.hasNext()) {
     Object item$iv = var4.next();
     int var10000 = index$iv++;
     User user = (User)item$iv;
     int index = var10000;
     String var8 = index + 1 + ", " + user;
     System.out.println(var8);
  }

}


 */

