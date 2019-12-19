package com.chiclaim.customview.animation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected ILabelInteraction labelInteraction;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILabelInteraction) {
            labelInteraction = (ILabelInteraction) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


    protected void initViews(View view) {

    }

    protected abstract int getLayoutId();

    protected void setLabel(CharSequence title) {
        if (labelInteraction != null) {
            labelInteraction.setLabel(title);
        }

    }

    public final void addFragment(Fragment fragment) {
        String tag = getActivity().getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
    }


    @Override
    public void onClick(View v) {

    }

}