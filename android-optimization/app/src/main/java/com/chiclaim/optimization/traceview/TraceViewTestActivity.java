package com.chiclaim.optimization.traceview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

public class TraceViewTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStetho();
        initWeex();
        initJPush();
        initFresco();
        initAMap();
        initUmeng();
        initBugly();
    }



    private void initStetho() {
        Handler handler = new Handler(Looper.getMainLooper());
        Stetho.initializeWithDefaults(this);
    }

    private void initWeex() {
        InitConfig config = new InitConfig.Builder().build();
        WXSDKEngine.initialize(getApplication(), config);
    }

    private void initJPush() {
        JPushInterface.init(this);
        JPushInterface.setAlias(this, 0, "deviceidxxxx000xxx");
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    private void initAMap() {
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                // 一些处理
            }
        });
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private void initUmeng() {
        UMConfigure.init(this, "58edcfeb310c93091c000be2", "umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);
    }
}
