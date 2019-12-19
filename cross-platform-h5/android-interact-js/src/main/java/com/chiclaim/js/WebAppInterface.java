package com.chiclaim.js;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebAppInterface(Context c) {
        mContext = c;
    }


    /**
     * Caution: If you've set your targetSdkVersion to 17 or higher,
     * you must add the @JavascriptInterface annotation to any method that you want available to your JavaScript (the method must also be public).
     * If you do not provide the annotation, the method is not accessible by your web page when running on Android 4.2 or higher.
     */

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}