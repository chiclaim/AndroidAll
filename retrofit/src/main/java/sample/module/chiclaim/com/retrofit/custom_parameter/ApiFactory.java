package sample.module.chiclaim.com.retrofit.custom_parameter;

import android.util.Log;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 1，为Retrofit添加一些公共参数
 * 2，对参数进行加密，然后传递给后端
 * 3，本例子主要是验证添加公共参数和对参数加密的。可能还有一些不完善的地方，可根据需要做一些调整
 */
public class ApiFactory {
    private static final String KEY_SIGN = "sign";

    private static final String API_BASE_URL = "http://10.1.67.34:8080/android_mvvm_server/";

    private static final String Authorization = "Authorization";


    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());


    public static String generateSign(Map<String, String> paramsMap, String secret) {
        String[] keys = paramsMap.keySet().toArray(new String[0]);

        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key.equals(KEY_SIGN)) {
                continue;
            }
            Object v = paramsMap.get(key);
            if (null == v) {
                continue;
            }
            sb.append(key).append(v);
        }
        sb.append(secret);
        return MD5Util.encode(sb.toString());
    }

    public static <S> S createService(Class<S> serviceClass) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new ParameterInterceptor());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = clientBuilder
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = retrofitBuilder
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }


    private static class ParameterInterceptor implements Interceptor {

        Map<String, String> getQueryParameters(Request request) {
            Map<String, String> signParams = new HashMap<>();
            for (int i = 0; i < request.url().querySize(); i++) {
                signParams.put(request.url().queryParameterName(i), request.url().queryParameterValue(i));
            }
            return signParams;
        }

        Map<String, String> getBodyParameters(Request request) throws IOException {
            Map<String, String> requestBodyParams = new HashMap<>();
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String requestBody = buffer.readString(Charset.forName("UTF-8"));
            requestBody = URLDecoder.decode(requestBody, "UTF-8");
            String[] ps = requestBody.split("&");

            for (String p : ps) {
                String[] kv = p.split("=");
                requestBodyParams.put(kv[0], kv[1]);
            }
            return requestBodyParams;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Log.d("ApiFactory", original.method());

            Request.Builder requestBuilder = original.newBuilder();
            switch (original.method()) {
                case "HEAD":
                case "GET":
                    Map<String, String> commonParameters = NetParams.commonParams();
                    Map<String, String> queryParameters = getQueryParameters(original);

                    HttpUrl.Builder urlBuilder = original.url().newBuilder();
                    for (Map.Entry<String, String> entry : commonParameters.entrySet()) {
                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                    }
                    commonParameters.putAll(queryParameters);
                    String sign = generateSign(commonParameters, "2b8e48e608d349cf9e236ecd9c677a83");
                    HttpUrl url = urlBuilder.addQueryParameter("sign", sign).build();
                    requestBuilder.url(url);
                    break;
                case "POST":
                case "PUT":
                case "PATCH":
                case "PROPPATCH":
                case "REPORT":

                    //获取查询参数
                    Map<String, String> signParams = getQueryParameters(original);

                    //通用参数放在QueryParameter
                    Map<String, String> commonParams = NetParams.commonParams();
                    HttpUrl.Builder uBuilder = original.url().newBuilder();
                    for (Map.Entry<String, String> entry : commonParams.entrySet()) {
                        uBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                    }

                    Map<String, String> requestBodyParams = getBodyParameters(original);
                    signParams.putAll(commonParams);
                    signParams.putAll(requestBodyParams);
                    String parametersSign = generateSign(signParams, "2b8e48e608d349cf9e236ecd9c677a83");
                    requestBodyParams.put("sign", parametersSign);
                    //Log.e("ApiFactory", "sign2: " + parametersSign);

                    String requestBody = generateBody(requestBodyParams);
                    requestBuilder.url(uBuilder.build());
                    requestBuilder.method(original.method(), RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestBody));

                    break;
                case "DELETE":
                    //有可能有 requestBody，也可能没有
                    break;
            }
            requestBuilder.header("token", "40%2FdMMC2znAUDGB3PdOQFQO15qwmNQ1jYT8L4%2BPhe9WGGo6Ml99z3gZAokyW8pGgkVomH%2FwPwnXGC%2BElw6vX7w%3D%3D");
            requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
            requestBuilder.addHeader("env", "e0c2f6218a064551baf82e39a6fb40cd");
            requestBuilder.addHeader("lang", (Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry()));
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

    private static String generateBody(Map<String, String> cp) {
        if (cp == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : cp.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}