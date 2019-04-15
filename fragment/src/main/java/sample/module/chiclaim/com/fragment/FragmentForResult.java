package sample.module.chiclaim.com.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/7/6.
 */

public class FragmentForResult extends Fragment {


    private static final int REQUEST_CODE_1 = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start_activity_for_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ResultActivity.class), REQUEST_CODE_1);
                //getActivity().startActivityForResult(new Intent(getActivity(), ResultActivity.class), REQUEST_CODE_1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_1:
                if (data != null) {
                    String result = data.getStringExtra("result");
                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
