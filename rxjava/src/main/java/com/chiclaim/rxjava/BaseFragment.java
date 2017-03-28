package com.chiclaim.rxjava;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chiclaim on 2016/01/27
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    protected void printLog(TextView textView, String prefix, String s) {
        String content = prefix + "'" + s + "'" + "\nMain Thread:" + (getActivity().getMainLooper() == Looper.myLooper()) +
                ", Thread Name:" + Thread.currentThread().getName();
        Log.d("OnSubscribe", content);
        //if (!TextUtils.isEmpty(s)) {
        appendText(textView, content);
        //}
    }

    protected void printErrorLog(TextView textView, String prefix, String s) {
        String content = prefix + " Main Thread:" + (getActivity().getMainLooper() == Looper.myLooper()) +
                " thread name:" + Thread.currentThread().getName() + ",data:" + s;
        Log.e("OnSubscribe", content);
        //if (!TextUtils.isEmpty(s)) {
        appendText(textView, content);
        // }
    }

    protected void appendText(final TextView textView, final String content) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                textView.append("\n\n");
                textView.append(content);
            }
        });
    }


    public final void addFragment(Fragment fragment) {
        String tag = getActivity().getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
    }


    protected boolean isMain() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    protected String getMainText(String flag) {
        return flag + " is main thread : " + (Looper.myLooper() == Looper.getMainLooper());

    }

    @Override
    public void onClick(View v) {

    }
}
