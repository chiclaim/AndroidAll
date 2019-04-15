package sample.module.chiclaim.com.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/7/6.
 */

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);

        findViewById(R.id.btn_set_result_to_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "this value from ResultActivity");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
