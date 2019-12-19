package com.module.chiclaim.com.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/23.
 */

public class NewFragment extends Fragment {

    private static final int REQUEST_CODE_1 = 1;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onCreateView");
        return inflater.inflate(com.module.chiclaim.com.fragment.R.layout.fragment_lifecycle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.tv_lifecycle);
        textView.setText("This is new fragment");
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onViewCreated");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onDetach");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((FragmentLifecycleActivity) getActivity()).log("New Fragment onSaveInstanceState");
    }
}
