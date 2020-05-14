package com.chiclaim.gist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

public class IntentHelper {

    public static boolean intentAvailable(Context context, Intent intent) {
        ActivityInfo activityInfo = intent.resolveActivityInfo(context.getPackageManager(), intent.getFlags());
        return activityInfo != null && activityInfo.exported;
    }


    public static boolean start(Context context, String packageName, String targetActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, targetActivity));
        if (intentAvailable(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean start(Context context, Intent intent) {
        if (intentAvailable(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 跳转到拨号界面
     */
    public static boolean startDial(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        return start(context, intent);
    }

    /**
     * 启动默认浏览器
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean startBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        if (intentAvailable(context, intent)) {
            start(context, intent);
            return true;
        }
        return false;
    }


}
