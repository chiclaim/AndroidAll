package sample.module.chiclaim.com.killbysystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/14.
 */

public class MyFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Kill", "MyFragment onCreate : " + this + " " + System.currentTimeMillis());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Kill", "MyFragment onCreateView : " + System.currentTimeMillis());
        return inflater.inflate(R.layout.my_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e("Kill", "MyFragment onViewCreated : " + System.currentTimeMillis());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.e("Kill", "MyFragment onStart : " + System.currentTimeMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("Kill", "MyFragment onResume : " + System.currentTimeMillis());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Kill", "MyFragment onDetach : " + System.currentTimeMillis());
    }
}
