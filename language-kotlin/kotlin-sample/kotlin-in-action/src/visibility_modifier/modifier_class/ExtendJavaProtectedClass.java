package visibility_modifier.modifier_class;

/**
 * Desc:
 * Created by Chiclaim on 2018/9/21.
 */

//Java只能在同包名下可以继承 protected class


public class ExtendJavaProtectedClass extends JavaProtectedClass.ProtectedClassExtend2 {
}


//Java可以访问同包名下的 protected class

class UseProtectedJavaClass {
    private JavaProtectedClass.ProtectedClassExtend2 protectedClass;
}
