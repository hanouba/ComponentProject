package com.hansen.hansensdk.okhttp;

import com.hansen.hansensdk.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author HanN on 2019/12/4 14:05
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 请求的发送,请求参数配置,https支持
 * @updateuser:
 * @updatedata: 2019/12/4 14:05
 * @updateremark:
 * @version: 2.1.67
 */
public class CommonOkHttpClient {
    //超时参数
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    //为我们的client配置参数
    static {
        //保证所有参数一致
        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient.Builder();
        //为构建者填充参数设置超时时间
        okHttpClientBuild.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuild.readTimeout(TIME_OUT,TimeUnit.SECONDS);
        okHttpClientBuild.writeTimeout(TIME_OUT,TimeUnit.SECONDS);
        //容许重定向
        okHttpClientBuild.followRedirects(true);

        //https支持
        okHttpClientBuild.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        okHttpClientBuild.sslSocketFactory(HttpsUtils.initSSLSocketFactory(),HttpsUtils.initTrustManager());

        //生成clinet对象
        mOkHttpClient = okHttpClientBuild.build();




    }

    /**
     * 发送具体的http/https请求
     * static 正确使用的情况下不会导致内存泄露
     *
     * @param request
     * @param commCallBack
     * @return Call
     */
    public static Call sendRequest(Request request, Callback commCallBack) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallBack);
        return call;
    }
}
