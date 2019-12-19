package com.chiclaim.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }


    public void appChooser(View view) {
        startActivity(new Intent(this, AppChooserActivity.class));
    }

    public void customChooser(View view) {
        startActivity(new Intent(this, CustomChooserActivity.class));
    }
}
