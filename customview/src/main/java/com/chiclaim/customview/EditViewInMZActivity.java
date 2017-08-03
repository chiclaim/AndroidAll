package com.chiclaim.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.LinearLayout;

import com.chiclaim.android.base.BaseActivity;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/5/15.
 */

public class EditViewInMZActivity extends BaseActivity {

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

    }

}
