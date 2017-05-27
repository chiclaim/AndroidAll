package com.chiclaim.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.chiclaim.customview.EditItemView.CLICK_LEFT;
import static com.chiclaim.customview.EditItemView.CLICK_RIGHT;

public class MainActivity extends AppCompatActivity {

    EditItemView editItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editItemView = (EditItemView) findViewById(R.id.edit_item_view);
        editItemView.setOnEditViewClick(new EditItemView.OnEditViewClick() {
            @Override
            public void onClick(int which) {
                switch (which) {
                    case CLICK_LEFT:
                        editItemView.minus();
                        break;
                    case CLICK_RIGHT:
                        editItemView.plus();
                        break;
                }
            }
        });


        startActivity(new Intent(this, RecyclerViewInRecyclerView.class));
    }
}
