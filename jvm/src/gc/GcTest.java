package gc;

public class GcTest {

    // 通过 DisableExplicitGC 禁用调用 gc API
    // -verbose:gc -XX:+DisableExplicitGC

    public static void main(String[] args) {
        {
            // 分配 64M 空间
            byte[] buff = new byte[1024 * 1024 * 64];
        }
        System.gc();
    }

}
