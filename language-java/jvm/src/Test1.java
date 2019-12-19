 public class Test1 {

    public static void main(String[] args) {


        String s = "'sds gdasda" + "\r" + "edaeafd'";

        System.out.println("转换前："+s);


        s = s.replaceAll("\r|\n", "");


        System.out.println("转换后："+s);
    }
}