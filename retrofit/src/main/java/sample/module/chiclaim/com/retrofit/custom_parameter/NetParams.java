package sample.module.chiclaim.com.retrofit.custom_parameter;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import sample.module.chiclaim.com.retrofit.BuildConfig;

public class NetParams {
    public static final String ANDROID = "android";
    public static final String MD5 = "md5";
    public static final String JSON = "json";


    public static Map<String, String> commonParams() {
        String callDeviceId = "deviceIdxxxx-0099";//DeviceUtil.getDeviceId(GlobalVars.context);
        if (callDeviceId.length() > 16) {
            callDeviceId = callDeviceId.substring(16, callDeviceId.length());
        }
        Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("s_os", ANDROID);
        apiParamMap.put("s_osv", String.valueOf(Build.VERSION.SDK_INT));
        apiParamMap.put("s_apv", BuildConfig.VERSION_NAME);
        apiParamMap.put("s_net", "2");
        apiParamMap.put("s_sc", "1920*1080");
        apiParamMap.put("s_br", Build.MODEL);
        apiParamMap.put("s_did", MD5Util.encode(callDeviceId));
        apiParamMap.put("format", JSON);
        apiParamMap.put("app_key", "200016");
        apiParamMap.put("v", "1.0");
        apiParamMap.put("timestamp", String.valueOf(System.currentTimeMillis()));    // 时间戳
        apiParamMap.put("sign_method", MD5);                             // 签名算法

        return apiParamMap;
    }

}
