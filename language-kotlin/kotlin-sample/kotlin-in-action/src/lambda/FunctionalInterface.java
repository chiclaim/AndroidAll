package lambda;

/**
 * desc:
 * <p>
 * Created by Chiclaim on 2018/12/31
 */

public class FunctionalInterface {

    public static void main(String[]args){
        Button button = new Button();
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void click() {
                System.out.println("click 1");
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void click() {
                System.out.println("click 2");
            }
        });

    }
}
