package sample2.module.chiclaim.com.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * android:launchMode="singleInstance"和singleTask
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
