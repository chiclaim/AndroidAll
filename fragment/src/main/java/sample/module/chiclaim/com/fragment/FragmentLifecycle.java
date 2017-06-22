package sample.module.chiclaim.com.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/15.
 */

public class FragmentLifecycle extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onCreate Bundle=" + savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onCreate = " + this);
        if (savedInstanceState != null) {
            String save = savedInstanceState.getString("save");
            ((FragmentLifecycleActivity) getActivity()).log("Fragment savedInstanceState save value = " + save);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onCreateView Bundle=" + savedInstanceState);
        return inflater.inflate(sample.module.chiclaim.com.fragment.R.layout.fragment_lifecycle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onViewCreated");
        TextView textView = (TextView) view.findViewById(R.id.tv_lifecycle);
        textView.setText("add new fragment");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FragmentLifecycleActivity) {
                    FragmentLifecycleActivity activity = (FragmentLifecycleActivity) getActivity();
                    activity.showFragment(new NewFragment());
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onDetach");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("save", "value in fragment");
        ((FragmentLifecycleActivity) getActivity()).log("Fragment onSaveInstanceState outState=" + outState);
    }

    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("add:").append(isAdded()).append(",");
        sb.append("visible:").append(isVisible()).append(",");
        sb.append("resume:").append(isResumed());
        return sb.toString();
    }

}
