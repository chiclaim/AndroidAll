package com.chiclaim.js;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by chiclaim on 2016/05/31
 */
public class BasicSampleActivity extends AppCompatActivity {


    private WebView webView;
    private View btnInvokeJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_layout);

        btnInvokeJs = findViewById(R.id.java_menu);
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.loadUrl("file:///android_asset/hello.html");


        //如果没有设置webView.setWebChromeClient(new WebChromeClient() {}); 将会报如下错:
        //I/chromium: [INFO:CONSOLE(1)] "Uncaught ReferenceError: showJsToast is not defined", source:  (1)
        // [重载的方法可选]
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //页面开始加载
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                btnInvokeJs.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
                //每次加载url都会走这个方法. 比如用户点击一个下载url,我们可以让他跳到浏览器app进行下载.
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载url失败. 在这种情况可以加载一个本地错误页面:error.html
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                //加载url失败. 走的协议是https

                //加载https url需要证书, 如果因为证书问题加载失败,页面会出现空白,因为系统默认调用handler.cancel();
                //需要把super.onReceivedSslError(view, handler, error);代码注释
                //然后调用handler.proceed();让webView继续加载
                // handler.proceed();
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
                //在子线程中执行
                //加载任何资源都会走这个方法
            }

        });

        //如果不设置该方法,js的alert无法弹出框 [重载的方法可选]
        webView.setWebChromeClient(new WebChromeClient() {

            boolean useCustom = true;

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //use custom dialog
                if (useCustom) {
                    new AlertDialog.Builder(BasicSampleActivity.this)
                            .setTitle("提示(自定Dialog)").setMessage(message).show();
                    result.confirm();
                    //result.cancel();
                    return true;
                } else {
                    return super.onJsAlert(view, url, message, result);
                }
            }


            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                if (useCustom) {
                    final AlertDialog dialog = new AlertDialog.Builder(BasicSampleActivity.this)
                            .setMessage(message)
                            .setView(R.layout.dialog_prompt_layout)
                            .show();

                    final EditText editText = (EditText) dialog.findViewById(R.id.et_username);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    if (editText != null && !TextUtils.isEmpty(defaultValue)) {
                        editText.setText(defaultValue);
                        editText.setSelection(defaultValue.length());
                    }

                    if (tvConfirm != null)
                        tvConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                result.confirm(editText.getText().toString());
                                dialog.dismiss();
                            }
                        });

                    if (tvCancel != null)
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                result.cancel();
                                dialog.dismiss();
                            }
                        });

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //用户既没有点击"确定"也没有点击"取消"
                            result.cancel();
                        }
                    });
                    //result.confirm();
                    return true;
                } else {
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                //you can custom dialog like above onJsPrompt()
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                //log
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                //权限请求
                super.onPermissionRequest(request);
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //加载网页的进度
            }
        });
    }

    public void invokeJs(View view) {
        webView.loadUrl("javascript:showJsToast()");
    }

    public void invokeAlert(View view) {
        webView.loadUrl("javascript:showPrompt()");
    }

    public void invokeConfirm(View view) {
        webView.loadUrl("javascript:showConfirm()");
    }

}
