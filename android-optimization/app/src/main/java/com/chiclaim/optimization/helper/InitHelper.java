package com.chiclaim.optimization.helper;

import android.app.Application;
import android.content.Context;

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

public class InitHelper {

    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context.getApplicationContext());
    }

    public static void initWeex(Context context) {
        InitConfig config = new InitConfig.Builder().build();
        WXSDKEngine.initialize((Application) context.getApplicationContext(), config);
    }

    public static void initJPush(Context context) {
        JPushInterface.init(context.getApplicationContext());
        JPushInterface.setAlias(context.getApplicationContext(), 0, "deviceidxxxx000xxx");
    }

    public static void initFresco(Context context) {
        Fresco.initialize(context.getApplicationContext());
    }

    public static void initAMap(Context context) {
        AMapLocationClient mLocationClient = new AMapLocationClient(context.getApplicationContext());
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

    public static void initUmeng(Context context) {
        UMConfigure.init(context.getApplicationContext(), "58edcfeb310c93091c000be2", "umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
    }

    public static void initBugly(Context context) {
        CrashReport.initCrashReport(context.getApplicationContext(), "注册时申请的APPID", false);
    }
    
}
