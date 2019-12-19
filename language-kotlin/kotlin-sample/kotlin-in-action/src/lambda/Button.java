package lambda;

/**
 * desc:
 * <p>
 * Created by Chiclaim on 2018/12/31
 */

public class Button {
    public void setOnClickListener(OnClickListener listener) {
        listener.click();
    }

    public void setCallback(KotlinInterface callback) {
        callback.call();
    }

    public interface OnClickListener {
        void click();
    }
}
