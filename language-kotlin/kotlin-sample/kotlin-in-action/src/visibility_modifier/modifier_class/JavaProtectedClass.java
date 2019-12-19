package visibility_modifier.modifier_class;

/**
 * Desc:
 * Created by Chiclaim on 2018/9/21.
 */

public class JavaProtectedClass {

    protected static class JavaProtected {
        protected void add() {
            System.out.println("add...");
        }
    }

    public static class ProtectedClassExtend extends JavaProtected {

    }

    protected static class ProtectedClassExtend2 extends JavaProtected {
        protected void add() {
            System.out.println("add...");
        }
    }
}
