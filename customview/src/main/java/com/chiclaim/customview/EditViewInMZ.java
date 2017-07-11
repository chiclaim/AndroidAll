package com.chiclaim.customview;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/5/15.
 */

public class EditViewInMZ extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_mz);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_container);
        MZEditText myEditText = new MZEditText(this);

        myEditText.setExtraWidth(11);
        //myEditText.removeExtraWidth();

        myEditText.setBackgroundResource(0);
        myEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        myEditText.setMaxLines(1);
        myEditText.setSingleLine(true);
        ll.addView(myEditText, 1);

        Log.e("EditViewInMZ", Build.BRAND);
        Log.e("EditViewInMZ", Build.DEVICE);
        Log.e("EditViewInMZ", Build.MODEL);
        Log.e("EditViewInMZ", Build.FINGERPRINT);
        Log.e("EditViewInMZ", Build.HOST);
        Log.e("EditViewInMZ", Build.TYPE);
        Log.e("EditViewInMZ", Build.MANUFACTURER);
        Log.e("EditViewInMZ", Build.USER);
        Build.VERSION.
    }
}
