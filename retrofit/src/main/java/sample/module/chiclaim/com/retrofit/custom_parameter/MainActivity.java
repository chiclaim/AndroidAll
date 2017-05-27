package sample.module.chiclaim.com.retrofit.custom_parameter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import sample.module.chiclaim.com.retrofit.R;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_userinfo);
    }


    public void userLogin(View view) {
        ApiFactory.createService(UserApi.class)
                .login("mobile", MD5Util.encode("password"), getDeviceId(this), NetParams.ANDROID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody response) {
                        try {
                            textView.setText(response.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public void userInfo(View view) {
        ApiFactory.createService(UserApi.class).fetchUserInfo2("2", "这是一个VPI用户")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public void menuFilter(View view) {
        //entityId: 99224994
        //userId: c2e0a20016413feb552b94a873d4cc20
        ApiFactory.createService(UserApi.class)
                .menuList("99224994", "c2e0a20016413feb552b94a873d4cc20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody response) {
                        try {
                            textView.setText(response.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static String getDeviceId(Context context) {
        @SuppressLint("HardwareIds") String callDeviceId = Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return callDeviceId;
    }

}
