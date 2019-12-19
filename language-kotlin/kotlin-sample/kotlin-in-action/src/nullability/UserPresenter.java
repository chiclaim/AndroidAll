package nullability;

import java.util.Arrays;
import java.util.List;

/**
 * Desc:
 * Created by Chiclaim on 2018/9/26.
 */
public class UserPresenter {

    //The interface define with kotlin
    private final UserView friendView;

    public UserPresenter(UserView friendView) {
        this.friendView = friendView;
    }

    public void getRemoteFriendList() {
        List<User> friends = requestFriendList();
        friendView.showFriendList(friends);
    }

    public void getLocalFriendList() {
        List<User> friends = getFriendList();
        //因为 showFriendList方法参数定义为非空，但是friends变量可能为空
        //Java的编写的变量，对于Kotlin来说既可以用于nullable和non-nullable，所以可以传给Kotlin定义的参数不能为空的方法
        friendView.showFriendList(friends);
    }


    private List<User> requestFriendList() {
        return Arrays.asList(new User("chiclaim"), new User("johnny"),
                new User("pony"), new User("jack"));
    }

    private List<User> getFriendList() {
        return wrapList();
        //return null; Don't return null directly , avoid showing warning tip by IDE
    }

    private List<User> wrapList() {
        return null;
    }

}
