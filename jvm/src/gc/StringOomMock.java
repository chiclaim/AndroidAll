package gc;
import java.util.ArrayList;
import java.util.List;

public class StringOomMock {

    private static String base = "string";

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            base += base;
            list.add(base.intern());
        }
    }
}